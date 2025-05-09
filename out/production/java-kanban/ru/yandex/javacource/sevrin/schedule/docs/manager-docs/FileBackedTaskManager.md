# Класс FileBackedTaskManager

Класс `FileBackedTaskManager` реализует менеджер задач с автосохранением состояния в файл. Наследует функциональность `InMemoryTaskManager`, добавляя механизмы:
- **Сохранения данных** в файл при каждом изменении (добавление задач/подзадач/эпиков).
- **Восстановления состояния** из файла при создании.

## Наследование
- Родительский класс: `InMemoryTaskManager`.

---

## Поля

| Поле    | Тип       | Описание                     |
|---------|-----------|------------------------------|
| `file`  | `File`    | Файл для хранения данных.    |

---

## Конструкторы

### `public FileBackedTaskManager(File file)`
Инициализирует менеджер с указанным файлом для сохранения.  
**Параметры**:
- `file` - файл, в который будут записываться данные.

---

## Методы

### 1. Методы добавления задач (переопределенные)
Все методы автоматически вызывают `save()` после добавления задачи.

### `addSubtask(Subtask subtask)`
```java
@Override
public Integer addSubtask(Subtask subtask);
```
Сохраняет подзадачу и обновляет файл.

Возвращает: ID созданной подзадачи.

### `addTask(Task task)`
```java
@Override
public Integer addTask(Task task);
```
Сохраняет задачу и обновляет файл.

Возвращает: ID созданной задачи.

### `addEpic(Epic epic)`
```java
@Override
public Integer addEpic(Epic epic)
```
Сохраняет эпик и обновляет файл.

Возвращает: ID созданного эпика.

### `public static FileBackedTaskManager loadFromFile(File file)`
Статический метод. Создает менеджер и восстанавливает его состояние из файла.
Параметры:

file - файл с сохраненными данными.

Логика работы:

Пропускает заголовочную строку.

Читает задачи из файла, используя Formatter.fromString().

Восстанавливает:

Эпики и их списки подзадач.

Связи подзадач с эпиками через epicId.

Обновляет счетчик ID (idCounter).

## Исключения:

`ManagerSaveException` - при ошибках чтения файла.
```java
public void save()
```
Сохраняет текущее состояние менеджера в файл.
Формат данных:

Первая строка: заголовок id,type,name,status,description,epic.

Последующие строки: задачи в формате CSV, сгенерированные Formatter.toString().

`ManagerSaveException` - при ошибках записи.

#### Пример использования
```java
// Создание менеджера с привязкой к файлу
File file = new File("tasks.csv");
FileBackedTaskManager manager = new FileBackedTaskManager(file);

// Добавление задач (автосохранение в файл)
Epic epic = new Epic("Релиз", "Подготовка к выпуску");
manager.addEpic(epic); 

Subtask subtask = new Subtask("Тесты", "Написать юнит-тесты", epic.getId());
manager.addSubtask(subtask);

// Восстановление из файла
FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(file);
```
### Взаимодействие с другими классами
#### 1. `Formatter`
Сериализация: `Formatter.toString(task)` преобразует задачу в CSV-строку.

Десериализация: Formatter.fromString(line) создает объект задачи из строки.

#### 2. `ManagerSaveException`
Обрабатывает ошибки ввода-вывода при работе с файлом.

## Примечания
...