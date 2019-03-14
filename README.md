# "Students' Why" Server API

## Введение
Данный файл содержит API для сервера "Почемучник студента" - описание HTTP-запросов, используемых для получения информации или совершения действий с данным сервисом.  
**Важно!** Каждый HTTP-запрос имеет двойной перенос \n\n в качестве разделителя шапки и тела сообщения. Он должен быть даже при отсутствии тела.

## Подключение
Для запуска сервера необходимо в качестве первого аргумента передать ему **ваш local IP/(DNS) (_Получить его можно по [следующей ссылке](https://www.whatismyip.com/what-is-my-public-ip-address/))**. Для запуска тестового клиента надо сделать то же самое.
Для подключения к серверу необходимо подключиться по сокету по адресу **{local IP сервера}** к порту **80** и отправить ему соответствующий строковый запрос образца **"Запрос"** в виде массива байт. Сервер обработает ваш запрос и отправит соответствующий ответ образца **"Ответ"**.
**Важно!** Не рекомендуется использовать подключение через URL, т.к. соответствующие реализации могут не поддерживать Unicode, который использует сервер для кодирования русских символов.  
**Важно!** Пока что есть проблемы с запуском сервера, поэтому для отладки необходимо скачать проект и запустить. Когда сервер получится запустить в сети Интернет, будут опубликованы актуальные IP и номер порта.

### Переменные
Переменные - это значения, вложенные в скобки "{}", которые при отправке сообщения надо заменить необходимыми значениями.
* **{ip}** - ip-адрес сервера;
* **{login}** - логин пользователя в системе Outlook без @hse.ru или @edu.hse.ru;
* **{password}** - пароль пользователя, если он - администратор;
* **{fromDate}, {toDate}** - даты в формате YYYY.MM.DD;
* **{action}** - заголовок, указывающий на действие, которое требуется от сервера;
* **{result}** - заголовок, принимающий значения **Success**, **Fail**, **Already-Done** или **No-Rights**;
* **{programID}** - уникальный идентификатор образовательной программы;
* **{name}** - ФИО в формате "Ф-И-О";
* **{token}** - Токен авторизации. Используется для совершения запросов вместо логина и пароля.

_Прим. Заголовки, помеченные курсивом, являются необязательными_

# JSON-спецификации классов
### Дерево
В деревьях хранятся пользователи, дисциплины, преподаватели, предметы, а также почемучник.  
Дерево является односвязным. Для поиска по нему либо используйте доступные запросы, либо пишите свои методы для частных реализаций.  
**Узел root** - корень дерева, является узлом.  

  ### Узел дерева
  **String id** - Идентификатор узла.  
  **Узел[] nodes** - Потомки.  
  **Лист[] leaves** -  Листья.  

  ### Лист дерева
  **Object value** - Хранимое значение.  
  **String id** - Уникальный идентификатор для листа.  

#### Дополнительная информация по дереву
В приложении всего 2 дерева: дерево почемучника, у которого в листьях лежат только вопросы, и дерево образовательных программ, у которого в листьях могут лежать пользователи, учебные дисциплины или преподаватели  
UML-диаграмма классов:  
![Диаграмма дерева UML](http://images.vfl.ru/ii/1551302805/1a82afb0/25573792_m.png)  
Пример дерева:  
![Пример дерева](http://images.vfl.ru/ii/1551302805/1a82afb0/25573792_m.png)  



### Список
Все списковые структуры данных на сервере представлены либо массивом, либо объектом с единственным полем-массивом.

## User
Пользователь системы.
**String name** - ФИО пользователя    
**String login** - Логин пользователя  

## Tutor
Преподаватель.
**String name** - ФИО  
**String[] emails** - Почты  
**String phone** - Телефон  
**String[] disciplines** - Дисциплины _Прим. Названия дисциплин должны соответствовать названиям_  
**String[] post** - Должности  
**String adress** - Кабинет с адресом  
**String attendTime** - Время присутствия  
**String url** - Ссылка на личную страницу  
**String imgURL** - Ссылка на иконку  

## Discipline
Учебная дисципилна (предмет).
**String name** - Название дисциплины  
**String type** - Тип дисциплины  
**String[] tutors** - Преподаватели  
**String schedule** - Время преподавания: формат {начало}-{конец} с номерами соотв. модулей    
**String url** - Ссылка на страницу дисциплины  

## Item 
**String header** - Заголовок новости.  
**String content** - Контент новости.  
**String publishDate** - Дата публикации в формате DD.MM.YYYY HH:MM. 

## Event
**String header** - Заголовок новости.  
**String content** - Контент новости.  
**double latitude** - Широта.  
**double longitude** - Долгота.  
**String place** - Дополнительная информация о месте проведения.

# Авторизация в системе

## Регистрация в системе
Регистрирует вас в системе. Возвращает токен авторизации.
### Запрос
> POST / HTTP/1.1  
> Action: Register  
> Login: {login}  
> Password: {password}  
> Name: {name}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
>
> token={token}


## Вход в систему
Проверяет ваш логин и пароль. Возвращает логин, имя и токен.
### Запрос
> POST / HTTP/1.1  
> Action: Log-In  
> Login: {login}  
> Password: {password} 

или

> POST / HTTP/1.1  
> Action: Log-In  
> Token: {token}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
> User-Type: {User/Admin}  
>
> login={login}&name={name}&token={token}


## Смена персональных данных
Меняет логин, пароль и имя. В случае, если не хотите менять всё сразу, отправьте старые данные.
### Запрос
> POST / HTTP/1.1  
> Action: Change-Personal-Data  
> Token: {token}  
> New-Login: {Новый логин}  
> New-Password: {Новый пароль}  
> New-Name: {Новые ФИО}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
> User-Type: {user/admin}
>
> token={token}


## Удаление пользователя
Удаляет вас из системы.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-User  
> Token: {token}   

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  

---
# Управление пользователями

## Получение пользователя
Получает пользователя по его логину. Администратор может получить любого пользователя.
### Запрос
> GET res/virtual/programs/users/{login} HTTP/1.1  
> Token: {token}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
>
> {JSON-сериализованный пользователь}

## Получение дерева пользователей (ADMIN)
Возвращает JSON-дерево зарегистрированных пользователей.
### Запрос
> GET res/virtual/programs/users HTTP/1.1  
> Token: {token}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}
>
> {JSON-дерево зарегистрированных пользователей}


## Назначение нового администратора (ADMIN)
Наделяет существующего пользователя правами администратора.
### Запрос
> PUT / HTTP/1.1  
> Action: Add-Admin  
> Token: {token}  
> Login: {Логин нового администратора}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  

## Удаление администратора (ADMIN)
Убирает у администратора права.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-Admin  
> Token: {token}  
> Login: {Логин нового администратора}


## Перемещение пользователя (ADMIN)
Назначает пользователю другую образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Rebase-User  
> Token: {token}  
> Login: {Логин Пользователя}  
> Program: {Образовательная программа}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Добавление пользователя в образовательную программу (ADMIN)
Добавляет пользователя в образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Add-User-To-Program  
> Token: {token}  
> Login: {Логин пользователя}  
> Program: {Образовательная программа}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Удаление пользователя из образовательной программы (ADMIN)
Удаляет пользователя из образовательной программы.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-User-From-Program  
> Token: {token}  
> Login: {Логин пользователя}  
> Program: {Образовательная программа}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}

---
# Управление образовательными программами

## Получение JSON-дерева образовательных программ.
Получает JSON-дерева образовательных программp.
### Запрос
> GET res/virtual/programs/programs HTTP/1.1

### Ответ
> HTTP/1.1 200 OK
>
> {JSON-дерево программ}


## Получение образовательной программы
Получение образовательной программы по названию.
### Запрос
> GET res/virtual/programs/programs/{program} HTTP/1.1  

### Ответ
> HTTP/1.1 200 OK
>
> {JSON-сериализованная программа}


## Получение списка образовательных программ пользователя
Получает JSON-список образовательных программ пользователя (Администраторы могут получать списки любого пользователя).
### Запрос
> GET res/virtual/programs/users/{login}/programs HTTP/1.1   
> Token: {token}  

### Ответ
> HTTP/1.1 200 OK
> Result: {result}  
>
> {JSON-список образовательных программ}


## Добавление образовательной программы (ADMIN)
Добавляет новую образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Add-Program  
> Token: {token}  
> Program: {program}   

или  

> PUT / HTTP/1.1  
> Action: Add-Program  
> Token: {token}  
> Program: {program}  
> Base-Program: {Название родительской образовательной программы}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  


## Удаление образовательной программы (ADMIN)
Удаляет образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-Program  
> Token: {token}  
> Program: {program}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  


## Перемещение образовательной программы (ADMIN)
Перемещает образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Rebase-Program  
> Token: {token}  
> Program: {program}  
> Base-Program: {Новая родительская программа}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  

---
# Управление преподавателями

## Получение JSON-дерева преподавателей.
Получает JSON-дерева преподавателей.
### Запрос
> GET res/virtual/programs/tutors HTTP/1.1

### Ответ
> HTTP/1.1 200 OK
>
> {JSON-дерево преподавателей}


## Получение преподавателя
Получает преподавателя по его логину.
### Запрос
> GET res/virtual/programs/tutors/{tutor} HTTP/1.1

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
>
> {JSON-сериализованный преподаватель}


## Получение дерева преподавателей пользователя
Получает JSON-дерево преподавателей пользователя (Администраторы могут получать деревья любого пользователя).
### Запрос
> GET res/virtual/programs/users/{login}/tutors HTTP/1.1  
> Token: {token}  

### Ответ
> HTTP/1.1 200 OK
> Result: {result}  
>
> {JSON-дерево образовательных преподавателей}


## Добавление преподавателя (ADMIN)
Добавляет нового преподавателя в указанную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Add-Tutor  
> Token: {token}  
> Program: {program}  
>
> {JSON-сериализванный преподаватель}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Изменение преподавателя (ADMIN)
Изменяет преподавателя.
### Запрос
> PUT / HTTP/1.1  
> Action: Change-Tutor  
> Token: {token}  
> Tutor: {tutor}  
>
> {JSON-сериализванный преподаватель}

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Перемещение преподавателя (ADMIN)
Перемещает преподавателя в другую образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Rebase-Tutor  
> Token: {token}  
> Tutor: {tutor}  
> Program: {program}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Добавление преподавателя в образовательную программу (ADMIN)
Добавляет существующего преподавателя в другую образовательную программу.
### Запрос
> PUT / HTTP/1.1  
> Action: Add-Tutor-To-Program  
> Token: {token}  
> Tutor: {tutor}  
> Program: {program}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Удаление преподавателя из образовательной программы (ADMIN)
Удаляет существующего преподавателя из образовательной программы.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-Tutor-From-Program  
> Token: {token}  
> Tutor: {tutor}  
> Program: {program}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}


## Удаление преподавателя (ADMIN)
Удаляет существующего преподавателя.
### Запрос
> PUT / HTTP/1.1  
> Action: Remove-Tutor  
> Token: {token}  
> Tutor: {tutor}  

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}

---
# Управление дисциплинами

## Получение JSON-дерева дисциплин.
Получает JSON-дерева дисциплин.
### Запрос
> GET res/virtual/programs/disciplines HTTP/1.1

### Ответ
> HTTP/1.1 200 OK
>
> {JSON-дерево дисциплин}


## Получение дисциплины
Получает дисциплину по её названию.
### Запрос
> GET res/virtual/programs/disciplines/{discipline} HTTP/1.1 

### Ответ
> HTTP/1.1 200 OK  
> Result: {result}  
>
> {JSON-сериализованная дисциплина}