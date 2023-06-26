CREATE TABLE employees
(
    id                 SERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    surname            VARCHAR(255) NOT NULL,
    patronymic         VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE,
    phone_number       VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth      DATE         NOT NULL,
    date_of_employment DATE         NOT NULL,
    gender             VARCHAR(255) NOT NULL
);

CREATE TABLE bonuses
(
    id           SERIAL PRIMARY KEY,
    amount       DECIMAL      NOT NULL,
    description  VARCHAR(255) NOT NULL,
    payment_date DATE         NOT NULL,
    employee_id  BIGINT,
    CONSTRAINT fk_employee_bonus FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE leaves
(
    id          SERIAL PRIMARY KEY,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    employee_id BIGINT,
    CONSTRAINT fk_employee_leave FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE salaries
(
    id             SERIAL PRIMARY KEY,
    amount         DECIMAL NOT NULL,
    start_date     DATE    NOT NULL,
    end_date       DATE,
    current_salary BOOLEAN NOT NULL,
    employee_id    BIGINT,
    CONSTRAINT fk_employee_salary FOREIGN KEY (employee_id) REFERENCES employees (id)
);

CREATE TABLE payrolls
(
    id           SERIAL PRIMARY KEY,
    payment_date DATE NOT NULL,
    salary_id    BIGINT,
    CONSTRAINT fk_salary_payroll FOREIGN KEY (salary_id) REFERENCES salaries (id)
);
