--liquibase formatted sql
--changeset saida:001-create-book-table
CREATE TABLE book (
    id               BIGSERIAL PRIMARY KEY,
    public_id        VARCHAR(36)    NOT NULL UNIQUE,
    title            VARCHAR(255)   NOT NULL,
    author           VARCHAR(255)   NOT NULL,
    isbn             VARCHAR(20)    NOT NULL UNIQUE,
    price            DECIMAL(10, 2) NOT NULL,
    publication_year INTEGER        NOT NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

--rollback DROP TABLE book;
