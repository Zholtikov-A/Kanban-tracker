# ТРЕКЕР ЗАДАЧ

Данная программа выполняет функцию системы контроля над выполнением поставленных задач.

Этот код является бэкэндом для приложения трекера.

### Функционал программы:

В программе реализовано хранение трех типов задач:

* Task - одиночная задача, не связанная с другими задачами.
* Epic - комплексная задача, включающая в себя подзадачи.
* Subtask - подзадача, включенная в Epic. Одиночная задача, связанная с другими в комплекс.
* Также программа хранит историю обращений к задачам.

<img src="assets/Kanban.png" align="center"></img>

Все задачи имеют однотипные свойства:

* Название
* Описание
* Уникальный идентификационный номер задачи
* Статус:
    * NEW
    * IN_PROGRESS
    * DONE

Методы для каждого из типа задач(Task/Epic/Subtask):

* Создание.
* Обновление.
* Получение списка всех задач.
* Получение по идентификатору.
* Удаление по идентификатору.
* Удаление всех задач.

Другие функции трекера задач:

* Получить список всех подзадач определённого эпика.
* Вычислить продолжительность задачи.
* Вывести задачи в порядке приоритета.
* Вывести историю обращений к задачам

Принцип управления статусами задач:

* При создании задач устанавливается статус NEW.
* При обновлении задач (Task) или подзадач (Subtask) можно установить статус IN_PROGRESS или DONE.
* Статус комплексной задачи (Epic) не устанавливается вручную, а рассчитывается по статусу вложенных подзадач:
    * NEW - если нет вложенных подзадач или все подзадачи этого "эпика" имеют статус NEW.
    * DONE - если все подзадачи этого "эпика" имеют статус DONE.
    * IN_PROGRESS - в остальных случаях.

### 🛠 Tech & Tools

<div>
      <img src="https://github.com/Salaia/icons/blob/main/green/Java.png?raw=true" title="Java" alt="Java" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/Gson.png?raw=true" title="Gson" alt="Gson" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/JUnit%205.png?raw=true" title="JUnit 5" alt="JUnit 5" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/CSV.png?raw=true" title="*.csv" alt="csv" height="40"/>
      <img src="https://github.com/Salaia/icons/blob/main/green/KVServer.png?raw=true" title="Key-Value server" alt="key value server" height="40"/>
</div>

* Альтернативные методы хранения:
    * в файле формата *.csv
    * хранение на Key-Value server через связку HttpServer, KV-Server + KV-Client.
* История обращений сохраняется в самодельном LinkedList

### Инструкция по развёртыванию ▶️

1) Склонируйте репозиторий и перейдите в него
   https://github.com/Zholtikov-A/Kanban.git

2) Запустите проект в выбранной IDE: src/main/java/Main.java

3) Проект работает по адресу:

http://localhost:8080/tasks

### API

Примеры использования программы можно увидеть в приложенных Postman тестах: postman/PmTests.json

* http://localhost:8080/tasks/task - POST - создать/обновить задачу
* http://localhost:8080/tasks/epic - POST - создать/обновить задачу-эпик
* http://localhost:8080/tasks/subtask - POST - создать/обновить подзадачу (к созданному ранее эпику)

* http://localhost:8080/tasks - GET - получить список задач по приоритету
* http://localhost:8080/tasks/task - GET - получить список всех простых задач
* http://localhost:8080/tasks/task?id= - GET - получить простую задачу по ее id
* http://localhost:8080/tasks/history - GET - получить историю обращений к задачам
* http://localhost:8080/tasks/task - DELETE - удаление всех простых задач
* http://localhost:8080/tasks/task?id= - DELETE - удаление простой задачи по ее id

* http://localhost:8080/tasks/subtask - GET - получить все подзадачи
* http://localhost:8080/tasks/subtask?id= - GET - получить подзадачу по ее id
* http://localhost:8080/tasks/subtask/epic?id= - GET - получить все подзадачи эпика по его id
* http://localhost:8080/tasks/subtask - DELETE - удаление всех подзадач
* http://localhost:8080/tasks/subtask?id= - DELETE - удаление подзадачи по ее id

* http://localhost:8080/tasks/epic - GET - получение всех эпиков
* http://localhost:8080/tasks/epic?id= - GET - получение эпика по его id
* http://localhost:8080/tasks/epic - DELETE - удаление всех эпиков
* http://localhost:8080/tasks/epic?id= - DELETE - удаление эпика по его id

### Testing

* Unit-тесты: src/test/java
* Postman-тесты: postman/KanbanPostmanCollection.json

### Статус и планы по доработке проекта

Kanban - финальный проект модуля Java Core курса Java-разработчик от Яндекс.Практикума. На данный момент проект проверен и зачтен ревьюером. Планов по дальнейшему развитию проекта нет.
