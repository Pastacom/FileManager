

public class Main {
    public static void main(String[] args) {
        FileManager manager = new FileManager();
        // Обход всех директорий рекурсивным способом.
        manager.DirectoryTraversal(manager.getRootPath());
        // Вывод последовательности объединения файлов.
        manager.outputFileSequence();
        // Объединить файлы в один выходной текстовый файл.
        manager.ConcatFiles();
    }
}