--liquibase formatted sql
--changeset saida:004-add-constraints

ALTER TABLE book
    ADD CONSTRAINT chk_book_price_positive CHECK (price > 0);
ALTER TABLE book
    ADD CONSTRAINT chk_book_publication_year CHECK (publication_year > 1500 AND
                                                    publication_year <= EXTRACT(YEAR FROM CURRENT_DATE));
ALTER TABLE book
    ADD CONSTRAINT chk_book_title_length CHECK (LENGTH(TRIM(title)) >= 1);
ALTER TABLE book
    ADD CONSTRAINT chk_book_author_length CHECK (LENGTH(TRIM(author)) >= 1);

--rollback ALTER TABLE book DROP CONSTRAINT chk_book_price_positive;
--rollback ALTER TABLE book DROP CONSTRAINT chk_book_publication_year;
--rollback ALTER TABLE book DROP CONSTRAINT chk_book_title_length;
--rollback ALTER TABLE book DROP CONSTRAINT chk_book_author_length;