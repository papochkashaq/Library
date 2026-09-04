# Library

Веб-приложение для управления библиотекой: учёт книг, читателей и выдачи книг на руки. Реализовано на Spring Boot с серверным рендерингом страниц через Thymeleaf.

## Возможности

- **Каталог книг**: просмотр списка книг с постраничной навигацией и опциональной сортировкой по году издания.
- **Карточка книги**: просмотр детальной информации о книге и её текущем владельце.
- **Добавление / редактирование / удаление книг** с валидацией полей (название, автор, год издания).
- **Поиск книги по названию** (частичное совпадение).
- **Выдача и возврат книги**: назначение читателя владельцем книги с фиксацией даты выдачи и снятие книги с читателя.
- **Контроль просрочки**: книга считается просроченной, если прошло более 10 дней с даты выдачи (см. [`Book.isOverdue()`](src/main/java/com/alderson/library/model/Book.java:39)).
- **Учёт читателей**: просмотр списка, добавление, редактирование, удаление читателей с валидацией (уникальное ФИО, корректный год рождения).
- **Автоматическое освобождение книг** при удалении читателя (см. [`Person.releaseBooks()`](src/main/java/com/alderson/library/model/Person.java:35)).

## Технологический стек

- **Java 25**
- **Spring Boot 4.0.5**
  - Spring Web / Spring MVC
  - Spring Data JPA
  - Spring Boot Validation
  - Thymeleaf (шаблонизатор представлений)
- **PostgreSQL** — основная база данных
- **Lombok** — сокращение шаблонного кода моделей
- **Maven** — сборка проекта, упаковка в `war`

## Требования

- JDK 25+
- Maven 3.9+ (либо использовать входящий в проект Maven Wrapper — [`mvnw`](mvnw) / [`mvnw.cmd`](mvnw.cmd))
- PostgreSQL 12+ (запущенный локально или доступный по сети)

## Структура проекта

```
src/main/java/com/alderson/library/
├── LibraryApplication.java      # Точка входа Spring Boot
├── ServletInitializer.java      # Инициализация для деплоя в виде WAR
├── controller/
│   ├── IndexController.java     # Главная страница
│   ├── BookController.java      # CRUD и операции над книгами
│   └── PersonController.java    # CRUD над читателями
├── model/
│   ├── Book.java                # Сущность книги
│   └── Person.java              # Сущность читателя
├── repository/
│   ├── BookRepository.java      # JPA-репозиторий книг
│   └── PersonRepository.java    # JPA-репозиторий читателей
└── service/
    ├── BookService.java         # Бизнес-логика по книгам
    └── PersonService.java       # Бизнес-логика по читателям

src/main/resources/
├── application.properties       # Конфигурация приложения
└── templates/                   # Thymeleaf-шаблоны (index, books/*, people/*)
```

## Настройка базы данных

Перед запуском создайте базу данных PostgreSQL и при необходимости скорректируйте параметры подключения в [`application.properties`](src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=0451
```

Схема базы данных создаётся и обновляется автоматически благодаря настройке `spring.jpa.hibernate.ddl-auto=update`, отдельных SQL-миграций выполнять не требуется.

> ⚠️ По умолчанию в файле конфигурации указаны логин и пароль для локальной разработки. Для рабочего окружения обязательно замените их на актуальные учётные данные (например, через переменные окружения или профили Spring).

## Запуск проекта

### Через Maven Wrapper

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### Через установленный Maven

```bash
mvn spring-boot:run
```

После запуска приложение будет доступно по адресу:

```
http://localhost:8081
```

Порт можно изменить в [`application.properties`](src/main/resources/application.properties:2) (параметр `server.port`).

## Сборка

Проект собирается в артефакт `war`:

```bash
mvnw.cmd clean package
```

Полученный файл будет находиться в каталоге `target/`.

## Основные маршруты

| Метод | Путь | Описание |
|---|---|---|
| `GET` | `/` | Главная страница |
| `GET` | `/books` | Список книг (параметры `page`, `books_per_page`, `sort_by_year`) |
| `GET` | `/books/{id}` | Карточка книги |
| `GET` | `/books/new` | Форма добавления книги |
| `POST` | `/books/new` | Сохранение новой книги |
| `GET` | `/books/edit/{id}` | Форма редактирования книги |
| `PATCH` | `/books/{id}` | Обновление книги |
| `PATCH` | `/books/{id}/add-owner` | Назначить читателя владельцем книги |
| `PATCH` | `/books/{id}/delete-owner` | Освободить книгу от владельца |
| `DELETE` | `/books/{id}` | Удаление книги |
| `GET` / `POST` | `/books/search` | Поиск книги по названию |
| `GET` | `/people` | Список читателей |
| `GET` | `/people/new` | Форма добавления читателя |
| `POST` | `/people/new` | Сохранение нового читателя |
| `GET` | `/people/{id}` | Карточка читателя |
| `GET` | `/people/edit/{id}` | Форма редактирования читателя |
| `PATCH` | `/people/{id}` | Обновление читателя |
| `DELETE` | `/people/{id}` | Удаление читателя |

> Методы `PATCH` и `DELETE` из HTML-форм передаются через скрытое поле `_method` благодаря настройке `spring.mvc.hiddenmethod.filter.enabled=true`.

## Тестирование

```bash
mvnw.cmd test
```
