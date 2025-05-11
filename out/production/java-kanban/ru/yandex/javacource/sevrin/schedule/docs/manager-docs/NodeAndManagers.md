# Классы Managers, Node, ManagerSaveException

## 1. Класс Managers

### Назначение
Класс `Managers` реализует шаблон **Фабрика** для создания стандартных реализаций менеджеров. Упрощает получение экземпляров `TaskManager` и `HistoryManager` без явной привязки к конкретным классам.

### Методы
| Метод                          | Описание                                                                 |
|--------------------------------|-------------------------------------------------------------------------|
| `public static TaskManager getDefault()`       | Возвращает реализацию `TaskManager` для работы с задачами в памяти.     |
| `public static HistoryManager getDefaultHistory()` | Возвращает реализацию `HistoryManager` для хранения истории просмотров. |

### Пример использования
```java
// Создание менеджера задач
TaskManager taskManager = Managers.getDefault();

// Создание менеджера истории
HistoryManager historyManager = Managers.getDefaultHistory();
```
## 2. Класс Node (внутренний)
Назначение
Вспомогательный класс для реализации двусвязного списка в InMemoryHistoryManager. Хранит задачу и ссылки на соседние узлы.

## Поля
task ссылка на объект задачи.
prev ссылка на предыдущий узел списка.
next ссылка на следующий узел списка.

## Конструктор
```java
public Node(Task task, Node prev, Node next)
```
Инициализирует узел с задачей и связями.

## 3. Класс ManagerSaveException
#### Назначение
Пользовательское исключение для обработки ошибок сохранения/загрузки данных в FileBackedTaskManager.

## Конструктор
```java
public ManagerSaveException(String message, Throwable cause)
```
message — описание ошибки.
cause — исключение, вызвавшее ошибку (например, IOException).
## Пример использования
```java
try {
    manager.save();
} catch (ManagerSaveException e) {
    System.err.println("Ошибка сохранения: " + e.getMessage());
}
```

## Связь между классами
*Managers ↔ InMemoryTaskManager/InMemoryHistoryManager*
Managers создает экземпляры этих классов через фабричные методы.

Клиентский код использует Managers, чтобы избежать зависимости от конкретных реализаций.

*Node ↔ InMemoryHistoryManager*
Node используется внутри InMemoryHistoryManager для организации списка истории.

Внешние классы не имеют доступа к Node.

*ManagerSaveException ↔ FileBackedTaskManager*
Исключение выбрасывается при ошибках ввода-вывода в методах save() и loadFromFile().