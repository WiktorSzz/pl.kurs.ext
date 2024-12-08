
INSERT INTO person (type, first_name, last_name, pesel, height, weight, email, employment_start_date, actual_job_position, actual_salary)
VALUES
    ('Employee', 'Magdalena', 'Krawczyk', '80100212628', 160.04, 77.78, 'magdalena.krawczyk@testmail.com', '2019-02-21', 'Software Engineer', 4662.28),
    ('Employee', 'Marek', 'Nowicki', '51001029243', 186.02, 74.85, 'marek.nowicka2@email.com', '2021-09-08', 'Data Analyst', 6214.09);

INSERT INTO person (type, first_name, last_name, pesel, height, weight, email, graduated_university, year_of_study, field_of_study, scholarship)
VALUES
    ('Student', 'Pablo', 'Chmielewski', '26012659138', 179.41, 75.66, 'pawel.chmielewski1@testmail.com', 'AGH', 5, 'Biology', 1640.11),
    ('Student', 'Joanna', 'Nowicka', '54101104128', 174.5, 82.56, 'joanna.nowicka2@example.com', 'PW', 5, 'Engineering', 1007.26);

INSERT INTO person (type, first_name, last_name, pesel, height, weight, email, pension, years_worked)
VALUES
    ('Pensioner', 'Magdalena', 'Wiśniewski', '22062568893', 179.41, 64.28, 'magdalena.wisniewski1@email.com', 2788.78, 59),
    ('Pensioner', 'Joanna', 'Lewandowska', '28120510335', 169.56, 51.54, 'joanna.lewandowska2@example.com', 2782.44, 57),
    ('Pensioner', 'Karolina', 'Krawczyk', '96051399842', 155.9, 55.52, 'karolina.krawczyk@example.com', 2739.52, 20),
    ('Pensioner', 'Krzysztof', 'Baran', '26010957742', 164.57, 64.24, 'krzysztof.baran@testmail.com', 2291.52, 30),
    ('Pensioner', 'Marek', 'Chmielewski', '91070787081', 158.86, 70.15, 'marek.chmielewski@testmail.com', 3456.67, 26),
    ('Pensioner', 'Magdalena', 'Nowicka', '17122469900', 162.33, 73.64, 'magdalena.nowicka2@email.com', 2806.01, 33);



INSERT INTO employee_job_position (employee_id, position_name, salary, employment_start_date, employment_end_date)
VALUES (1, 'Junior Developer', 3200.00, '2017-01-01', '2019-01-01');

INSERT INTO employee_job_position (employee_id, position_name, salary, employment_start_date, employment_end_date)
VALUES (1, 'Software Engineer', 4200.00, '2019-02-01', '2022-01-01');

INSERT INTO employee_job_position (employee_id, position_name, salary, employment_start_date, employment_end_date)
VALUES (2, 'Intern', 1800.00, '2019-06-01', '2020-06-01');

INSERT INTO employee_job_position (employee_id, position_name, salary, employment_start_date, employment_end_date)
VALUES (2, 'Data Analyst', 6200.00, '2020-07-01', '2021-07-01');

INSERT INTO employee_job_position (employee_id, position_name, salary, employment_start_date, employment_end_date)
VALUES (1, 'HR Assistant', 4000.00, '2015-03-01', '2017-03-01');

