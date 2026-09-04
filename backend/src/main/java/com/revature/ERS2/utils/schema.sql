DROP TABLE IF EXISTS reimbursements;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS departments;

CREATE TABLE departments (
    department_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Added username
CREATE TABLE users (
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    department_id INT REFERENCES departments(department_id),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(30) NOT NULL UNIQUE,
    hashed_password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'EMPLOYEE'
    CHECK (role IN ('EMPLOYEE', 'MANAGER'))
);

CREATE TABLE reimbursements (
    reimbursement_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    author_id INT NOT NULL REFERENCES users(user_id),
    resolver_id INT REFERENCES users(user_id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'DENIED')),
    type VARCHAR(20) NOT NULL
        CHECK (type IN ('TRAVEL', 'FOOD', 'LODGING', 'MEDICAL','TRANSPORTATION', 'OTHER')),
    amount DECIMAL(10, 2) NOT NULL
        CHECK (amount > 0 AND amount <= 999999),
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    description VARCHAR(255)
);


/* Changes:     /*email VARCHAR(100) NOT NULL UNIQUE,*/