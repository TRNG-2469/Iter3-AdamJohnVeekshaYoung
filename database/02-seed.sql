-- ============================================
-- Departments
-- ============================================

INSERT INTO public.departments ("name") VALUES
('Engineering'),
('Finance'),
('Human Resources'),
('Marketing'),
('Sales');

-- ============================================
-- Development Manager
-- ============================================

INSERT INTO public.users (
    department_id,
    first_name,
    last_name,
    username,
    hashed_password,
    "role"
)
VALUES (
   1,
   'Manager',
   'Test',
   'usernamem',
   '$2a$10$HKYA/L0UVJ2mQ1/up7Z0kOAyqUeoaK9dw4UTjuI3Fsm.d3Dxa6hTy',
   'MANAGER'
       );
