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

CREATE TABLE positions
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE employees_positions
(
    employee_id INT NOT NULL,
    position_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES positions (id) ON DELETE CASCADE,
    PRIMARY KEY (employee_id, position_id)
);

CREATE TABLE bonuses
(
    id          SERIAL PRIMARY KEY,
    employee_id INT            NOT NULL,
    amount      NUMERIC(19, 2) NOT NULL,
    description VARCHAR(255)   NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);

CREATE TABLE salaries
(
    id             SERIAL PRIMARY KEY,
    amount         NUMERIC(19, 2) NOT NULL,
    start_date     DATE           NOT NULL,
    end_date       DATE,
    current_salary BOOLEAN        NOT NULL,
    employee_id    INT            NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE
);

CREATE TABLE payrolls
(
    id           SERIAL PRIMARY KEY,
    payment_date DATE NOT NULL,
    salary_id    INT  NOT NULL,
    FOREIGN KEY (salary_id) REFERENCES salaries (id) ON DELETE CASCADE
);
