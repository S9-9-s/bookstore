--liquibase formatted sql
--changeset saida:002-add-indexes
CREATE INDEX idx_book_public_id ON book (public_id);
CREATE INDEX idx_book_author ON book (author);
CREATE INDEX idx_book_publication_year ON book (publication_year);
CREATE INDEX idx_book_created_at ON book (created_at);
CREATE INDEX idx_book_isbn ON book (isbn);

--rollback DROP INDEX idx_book_public_id;
--rollback DROP INDEX idx_book_author;
--rollback DROP INDEX idx_book_publication_year;
--rollback DROP INDEX idx_book_created_at;
--rollback DROP INDEX idx_book_isbn;