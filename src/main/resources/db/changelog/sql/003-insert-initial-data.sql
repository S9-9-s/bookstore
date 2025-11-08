--liquibase formatted sql
--changeset saida:003-insert-initial-data

INSERT INTO book (public_id, title, author, isbn, price, publication_year)
VALUES ('550e8400-e29b-41d4-a716-446655440000', 'Clean Code: A Handbook of Agile Software Craftsmanship',
        'Robert C. Martin', '9780132350884', 45.99, 2008),
       ('550e8400-e29b-41d4-a716-446655440001', 'Effective Java', 'Joshua Bloch', '9780134685991', 54.99, 2018),
       ('550e8400-e29b-41d4-a716-446655440002', 'Design Patterns: Elements of Reusable Object-Oriented Software',
        'Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides', '9780201633610', 59.99, 1994),
       ('550e8400-e29b-41d4-a716-446655440003', 'The Clean Coder: A Code of Conduct for Professional Programmers',
        'Robert C. Martin', '9780137081073', 39.99, 2011),
       ('550e8400-e29b-41d4-a716-446655440004', 'Refactoring: Improving the Design of Existing Code', 'Martin Fowler',
        '9780134757599', 49.99, 2018);

--rollback DELETE FROM book WHERE public_id IN (
--rollback '550e8400-e29b-41d4-a716-446655440000',
--rollback '550e8400-e29b-41d4-a716-446655440001',
--rollback '550e8400-e29b-41d4-a716-446655440002',
--rollback '550e8400-e29b-41d4-a716-446655440003',
--rollback '550e8400-e29b-41d4-a716-446655440004'
--rollback );