-- Tworzenie tabeli person
CREATE TABLE person (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        type VARCHAR(255),
                        first_name VARCHAR(255),
                        last_name VARCHAR(255),
                        pesel VARCHAR(11),
                        height DOUBLE,
                        weight DOUBLE,
                        email VARCHAR(255),
                        employment_start_date DATE,
                        actual_job_position VARCHAR(255),
                        actual_salary DOUBLE,
                        graduated_university VARCHAR(255),
                        year_of_study INT,
                        field_of_study VARCHAR(255),
                        scholarship DOUBLE,
                        pension DOUBLE,
                        years_worked INT,
                        version INT DEFAULT 0
);


CREATE TABLE employee_job_position (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       employee_id BIGINT NOT NULL,
                                       position_name VARCHAR(255),
                                       salary DECIMAL(10, 2),
                                       employment_start_date DATE,
                                       employment_end_date DATE,
                                       FOREIGN KEY (employee_id) REFERENCES person(id)
);
