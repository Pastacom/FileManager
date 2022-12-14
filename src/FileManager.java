import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.exit;

public class FileManager {
    // Путь до корневой директории.
    private final Path rootPath;
    // Последовательность объединения файлов.
    private final List<Path> fileSequence;
    // Посещенные файлы. Используем Set, для проверки на наличие за O(1).
    private final Set<Path> visitedFiles;

    /*
     * Конструктор класса.
     * Необходим для считывания корневой директории и
     * инициализации нужных компонентов.
     */
    public FileManager() {
        fileSequence = new ArrayList<>();
        visitedFiles = new HashSet<>();
        Scanner scan = new Scanner(System.in);
        Path path;
        do {
            System.out.print("Enter root directory path: ");
            path = Path.of(scan.nextLine());
            if (!Files.exists(path)) {
                System.out.println("Such directory doesn't exist.");
            }
        } while (!Files.exists(path));
        rootPath = path;
    }
    /*
     * Метод для вывода в консоль последовательности соединения файлов.
     */
    public void outputFileSequence() {
        for (Path path : fileSequence) {
            System.out.println(path);
        }
    }
    /*
     * Getter к приватному полю rootPath.
     */
    public Path getRootPath() {
        return rootPath;
    }

    /*
     * Метод для объединения содержания всех файлов в один выходной.
     */
    public void ConcatFiles() {
        Path outputPath;
        Scanner scan = new Scanner(System.in);
        // Считываем, пока не получим корректный путь до директории.
        do {
            // Считываем директорию, в которой будет храниться выходной файл.
            System.out.print("Enter directory for output file path: ");
            outputPath = Path.of(scan.nextLine());
            // Проверяем, что такая директория существует.
            if (!Files.exists(outputPath)) {
                System.out.println("Such directory doesn't exist.");
            }
        } while (!Files.exists(outputPath));
        // Добавляем к пути стандартное имя файла для вывода.
        outputPath = outputPath.resolve("output.txt");
        /* Используем FileWriter для записи в файл.
         * Обертка BufferedWriter необходима, чтобы быстрее писать в файл
         * при многочисленной дозаписи, не используя долгие механизмы обращения к диску.
         * Обертка printWriter необходима, чтобы использовать println при записи в файл.
         */
        try (FileWriter fileWriter = new FileWriter(outputPath.toString(), false);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            // Обходим все пути файлов и построчно переписываем их в наш файл.
            for (Path path : fileSequence) {
                File file = new File(path.toUri());
                Scanner fileReader = new Scanner(file);
                while (fileReader.hasNextLine()) {
                    String line = fileReader.nextLine();
                    printWriter.println(line);
                }
                fileReader.close();
            }
        } catch (IOException e) {
            System.out.println("IO exception was encountered.");
        }
    }
    /*
     * Метод необходимый для рекурсивного обхода зависимостей файлов.
     */
    private void DFS(File file) {
        // Если файл с таким путем не посещен.
        if (!visitedFiles.contains(file.toPath())) {
            // Проверяем существует ли вообще такой файл.
            if (Files.exists(file.toPath())) {
                // Добавляем файл в посещенные.
                visitedFiles.add(file.toPath());
                try {
                    Scanner fileReader = new Scanner(file);
                    while (fileReader.hasNextLine()) {
                        String line = fileReader.nextLine();
                        // Проверяем является ли строка директивой.
                        if (line.length() > 8 && "require ".equals(line.substring(0, 8))) {
                            // Если строка - директива, рекурсивно запускаемя от файла-зависимости.
                            DFS(new File(rootPath.resolve(line.substring(8)).toUri()));
                        }
                    }
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFound exception was encountered.");
                }
                // Только обойдя все зависимости, добавляем файл в последовательность вывода.
                fileSequence.add(file.toPath());
            } else {
                System.out.printf("File with path %s doesn't exist.\n", file.toPath());
            }
        } else if (!fileSequence.contains(file.toPath())) {
            /* Случай, когда файл посещен, но еще не записан
             * в последовательность для вывода свидетельствует о цикле.
             */
            System.out.println("Found loop in file dependencies, terminating program.");
            exit(0);
        }
    }
    /*
     * Метод для обхода всех файлов и директорий.
     * Для каждой директории рекурсивно обходим все элементы внутри нее.
     */
    public void DirectoryTraversal(Path currentPath) {
        // Проверяем, что текущая директория существует.
        if (Files.exists(currentPath)) {
            File directory = new File(currentPath.toUri());
            // Просматриваем все файлы и директории в этой директории.
            File[] files = directory.listFiles();
            if (files == null) {
                return;
            }
            // Сначала обходим все файлы, прежде чем идти в поддиректории.
            for (File file : files) {
                if (file.isFile()) {
                    // Запускаем чтение файла и рекурсивный обход его зависимостей.
                    DFS(file);
                }
            }
            // Обходим все директории.
            for (File file : files) {
                if (file.isDirectory()) {
                    // Запускаемся от поддиректории
                    DirectoryTraversal(file.toPath());
                }
            }
        } else {
            System.out.println("File not found");
        }
    }
}
