INSERT INTO employees (name, surname, patronymic, email, phone_number, date_of_birth, date_of_employment, gender)
VALUES ('John', 'Doe', 'Smith', 'john.doe@example.com', '+375291234567', '1980-05-15', '2020-01-01', 'MALE'),
       ('Jane', 'Smith', 'Johnson', 'jane.smith@example.com', '+375441234567', '1985-08-20', '2019-07-01', 'MALE'),
       ('Michael', 'Williams', 'Brown', 'michael.williams@example.com', '+375251234567', '1992-03-10', '2022-02-15', 'MALE'),
       ('Emily', 'Johnson', 'Davis', 'emily.johnson@example.com', '+375291234568', '1990-11-25', '2018-05-01', 'FEMALE'),
       ('John', 'Miller', 'Taylor', 'david.miller@example.com', '+375441234568', '1987-06-18', '2017-09-15', 'MALE'),
       ('Olivia', 'Wilson', 'Anderson', 'olivia.wilson@example.com', '+375251234568', '1994-09-05', '2023-01-01', 'FEMALE'),
       ('James', 'Brown', 'Thomas', 'james.brown@example.com', '+375291234569', '1983-02-28', '2021-03-01', 'MALE'),
       ('Sophia', 'Davis', 'Harris', 'sophia.davis@example.com', '+375441234569', '1991-07-12', '2020-08-15', 'FEMALE'),
       ('John', 'Taylor', 'White', 'daniel.taylor@example.com', '+375251234569', '1988-12-07', '2019-11-01', 'MALE'),
       ('Isabella', 'Anderson', 'Lee', 'isabella.anderson@example.com', '+375291234570', '1996-04-22', '2022-09-01', 'FEMALE');

INSERT INTO bonuses (employee_id, amount, description, payment_date)
VALUES (1, 200.00, 'Extra Performance Bonus', '2023-06-15'),
       (1, 150.00, 'Incentive Bonus', '2023-06-15'),
       (1, 100.00, 'Special Achievement Bonus', '2023-06-15'),
       (2, 100.00, 'Quarterly Sales Bonus', '2023-06-15'),
       (2, 50.00, 'Customer Retention Bonus', '2023-06-15'),
       (2, 300.00, 'Top Sales Performer Bonus', '2023-06-15'),
       (3, 500.00, 'Year-end Performance Bonus', '2023-06-15'),
       (3, 200.00, 'Leadership Bonus', '2023-06-15'),
       (3, 150.00, 'Excellence Award Bonus', '2023-06-15'),
       (4, 150.00, 'Team Collaboration Bonus', '2023-06-15'),
       (4, 100.00, 'Project Milestone Bonus', '2023-06-15'),
       (4, 50.00, 'Quality Assurance Bonus', '2023-06-15'),
       (5, 75.00, 'Customer Service Excellence Bonus', '2023-06-15'),
       (5, 75.00, 'Upselling Bonus', '2023-06-15'),
       (5, 75.00, 'Repeat Customer Bonus', '2023-06-15'),
       (6, 400.00, 'Top Quarterly Performer Bonus', '2023-06-15'),
       (6, 200.00, 'Innovation Bonus', '2023-06-15'),
       (6, 200.00, 'Product Launch Bonus', '2023-06-15'),
       (7, 200.00, 'Project Milestone Bonus', '2023-06-15'),
       (7, 100.00, 'Team Collaboration Bonus', '2023-06-15'),
       (7, 100.00, 'Client Satisfaction Bonus', '2023-06-15'),
       (8, 100.00, 'Referral Program Bonus', '2023-06-15'),
       (8, 50.00, 'Referral Conversion Bonus', '2023-06-15'),
       (8, 50.00, 'Referral Sales Bonus', '2023-06-15'),
       (9, 300.00, 'Departmental Performance Bonus', '2023-06-15'),
       (9, 200.00, 'Cross-functional Collaboration Bonus', '2023-06-15'),
       (9, 100.00, 'Department Achievement Bonus', '2023-06-15'),
       (10, 150.00, 'Project Completion Bonus', '2023-06-15'),
       (10, 100.00, 'Goal Attainment Bonus', '2023-06-15'),
       (10, 100.00, 'Milestone Achievement Bonus', '2023-06-15');

INSERT INTO salaries (amount, start_date, end_date, current_salary, employee_id)
VALUES (5000.00, '2023-01-01', NULL, true, 1),
       (4000.00, '2022-01-01', '2023-01-01', false, 1),
       (3000.00, '2021-01-01', '2022-01-01', false, 1),
       (4500.00, '2022-12-01', NULL, true, 2),
       (3500.00, '2021-12-01', '2022-12-01', false, 2),
       (2500.00, '2020-12-01', '2021-12-01', false, 2),
       (5500.00, '2023-01-01', NULL, true, 3),
       (4500.00, '2022-01-01', '2023-01-01', false, 3),
       (3500.00, '2021-01-01', '2022-01-01', false, 3),
       (4000.00, '2023-01-01', NULL, true, 4),
       (3000.00, '2022-01-01', '2023-01-01', false, 4),
       (2000.00, '2021-01-01', '2022-01-01', false, 4),
       (4500.00, '2022-12-01', NULL, true, 5),
       (3500.00, '2021-12-01', '2022-12-01', false, 5),
       (2500.00, '2020-12-01', '2021-12-01', false, 5),
       (5500.00, '2023-01-01', NULL, true, 6),
       (4500.00, '2022-01-01', '2023-01-01', false, 6),
       (3500.00, '2021-01-01', '2022-01-01', false, 6),
       (4700.00, '2023-01-01', NULL, true, 7),
       (4700.00, '2023-01-01', '2023-06-22', false, 7),
       (4700.00, '2023-01-01', '2023-06-22', false, 7),
       (4300.00, '2022-12-01', NULL, true, 8),
       (4300.00, '2022-12-01', '2023-06-22', true, 8),
       (4300.00, '2022-12-01', '2023-06-22', false, 8),
       (5100.00, '2023-01-01', NULL, true, 9),
       (5100.00, '2023-01-01', '2023-06-22', false, 9),
       (5100.00, '2023-01-01', '2023-06-22', false, 9),
       (4600.00, '2022-11-01', NULL, true, 10),
       (4600.00, '2022-11-01', '2023-06-22', true, 10),
       (4600.00, '2022-11-01', '2023-06-22', false, 10);

INSERT INTO payrolls (payment_date, salary_id)
VALUES ('2023-06-15', 1),
       ('2023-07-15', 1),
       ('2023-08-15', 1),
       ('2023-06-15', 2),
       ('2023-07-15', 2),
       ('2023-08-15', 2),
       ('2023-06-15', 3),
       ('2023-07-15', 3),
       ('2023-08-15', 3),
       ('2023-06-15', 4),
       ('2023-07-15', 4),
       ('2023-08-15', 4),
       ('2023-06-15', 5),
       ('2023-07-15', 5),
       ('2023-08-15', 5),
       ('2023-06-15', 6),
       ('2023-07-15', 6),
       ('2023-08-15', 6),
       ('2023-06-15', 7),
       ('2023-07-15', 7),
       ('2023-08-15', 7),
       ('2023-06-15', 8),
       ('2023-06-15', 8),
       ('2023-07-15', 8),
       ('2023-08-15', 9),
       ('2023-09-15', 9),
       ('2023-10-15', 9),
       ('2023-06-15', 10),
       ('2023-07-15', 10),
       ('2023-08-15', 10);

INSERT INTO leaves (start_date, end_date, employee_id)
VALUES
    ('2023-06-10', '2023-06-20', 1),
    ('2023-07-05', '2023-07-08', 1),
    ('2023-07-07', '2023-07-10', 1),
    ('2023-06-12', '2023-06-14', 2),
    ('2023-07-02', '2023-07-04', 2),
    ('2023-07-06', '2023-07-09', 2),
    ('2023-06-15', '2023-06-18', 3),
    ('2023-07-03', '2023-07-06', 3),
    ('2023-07-05', '2023-07-08', 3),
    ('2023-06-17', '2023-06-19', 4),
    ('2023-07-07', '2023-07-10', 4),
    ('2023-07-09', '2023-07-12', 4),
    ('2023-06-19', '2023-06-22', 5),
    ('2023-07-11', '2023-07-14', 5),
    ('2023-07-13', '2023-07-16', 5),
    ('2023-06-21', '2023-06-23', 6),
    ('2023-07-15', '2023-07-18', 6),
    ('2023-07-17', '2023-07-20', 6),
    ('2023-06-24', '2023-06-27', 7),
    ('2023-07-20', '2023-07-23', 7),
    ('2023-07-22', '2023-07-25', 7),
    ('2023-06-26', '2023-06-28', 8),
    ('2023-07-24', '2023-07-27', 8),
    ('2023-07-26', '2023-07-29', 8),
    ('2023-06-29', '2023-07-03', 9),
    ('2024-06-29', '2023-07-03', 9),
    ('2025-06-29', '2023-07-03', 9),
    ('2023-07-01', '2023-07-05', 10),
    ('2024-07-01', '2023-07-05', 10),
    ('2025-07-01', '2023-07-05', 10);
