# Bookstore API

REST API для управления каталогом книг с использованием Spring Boot и PostgreSQL.

## Технологии

- **Java 17+**
- **Spring Boot 3.x**
- **PostgreSQL**
- **Liquibase** (миграции базы данных)
- **MapStruct** (маппинг объектов)
- **Maven**

## Функциональность

- Создание, чтение, обновление, удаление книг
- Валидация данных (ISBN, цена, год издания)
- Глобальная обработка исключений
- Уникальные идентификаторы (UUID publicId)
- Полная документация API

## Запуск приложения

```shell
  mvn clean install
  mvn spring-boot:run
```
