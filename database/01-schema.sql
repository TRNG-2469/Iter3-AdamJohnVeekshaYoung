-- ============================================
-- Departments
-- ============================================

CREATE TABLE public.departments (
department_id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
"name" varchar(100) NOT NULL,

CONSTRAINT departments_pkey
    PRIMARY KEY (department_id)
);


-- ============================================
-- Users
-- ============================================

CREATE TABLE public.users (
user_id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
department_id int4 NULL,
first_name varchar(50) NOT NULL,
last_name varchar(50) NOT NULL,
username varchar(30) NOT NULL,
hashed_password varchar(255) NOT NULL,
"role" varchar(20) DEFAULT 'EMPLOYEE' NOT NULL,

CONSTRAINT users_pkey
  PRIMARY KEY (user_id),

CONSTRAINT users_username_key
  UNIQUE (username),

CONSTRAINT users_role_check
  CHECK ("role" IN ('EMPLOYEE', 'MANAGER')),

CONSTRAINT users_department_id_fkey
  FOREIGN KEY (department_id)
      REFERENCES public.departments(department_id)
);


-- ============================================
-- Reimbursements
-- ============================================

CREATE TABLE public.reimbursements (
reimbursement_id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
author_id int4 NOT NULL,
resolver_id int4 NULL,
status varchar(20) DEFAULT 'PENDING' NOT NULL,
"type" varchar(20) NOT NULL,
amount numeric(10,2) NOT NULL,
submitted_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
resolved_at timestamp NULL,
description varchar(255) NULL,

CONSTRAINT reimbursements_pkey
   PRIMARY KEY (reimbursement_id),

CONSTRAINT reimbursements_amount_check
   CHECK (amount > 0 AND amount <= 999999),

CONSTRAINT reimbursements_status_check
   CHECK (status IN ('PENDING', 'APPROVED', 'DENIED')),

CONSTRAINT reimbursements_type_check
   CHECK ("type" IN (
                     'TRAVEL',
                     'FOOD',
                     'LODGING',
                     'MEDICAL',
                     'TRANSPORTATION',
                     'OTHER'
       )),

CONSTRAINT reimbursements_author_id_fkey
   FOREIGN KEY (author_id)
       REFERENCES public.users(user_id),

CONSTRAINT reimbursements_resolver_id_fkey
   FOREIGN KEY (resolver_id)
       REFERENCES public.users(user_id)
);