Добрейшего вечерочка.
Вот вкратце об особенностях и процессе работы программы:
Изначально программа будет запрашивать путь к корневой директории. 
Именно в этой директории буду найдены все файлы и зависимости для них
и выстроена очередь их объединения.

Далее в консоль будет выведен порядок объединения всех файлов.
Будем считать, что файлы являются вершинами графа, также вершины
могут объединяться в компоненты связности, тогда согласно условию,
компоненты, состоящие из одного элемента могут быть в любом порядке,
а для тех компонент, у которых более одной вершины,
то есть у файлов этой компоненты есть какие-то зависимости,
файлы будут записываться в порядке согласно правилу: 
A идет после B, если B зависит от A.

Далее запрашивается путь к директории,
где будет создан файл output.txt, в который будут выведены
объеденные данные файлов, в требуемом порядке.
Файл создастся, если его еще нет и перезапишется, если он есть.

Для написания директивы зависимости в файле должна присутствовать
строка состоящая из ключевого слова require и пути до нужного файла.
Пример:

require Chad folder/Gigachad.txt

В файле cringe.txt директива стоит с пробелом.
Если его убрать, то возникнет цикл.

Приятной проверки <3