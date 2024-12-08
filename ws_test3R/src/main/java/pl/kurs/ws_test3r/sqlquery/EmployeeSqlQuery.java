package pl.kurs.ws_test3r.sqlquery;

import org.springframework.stereotype.Component;

@Component("employeeQuery")
public class EmployeeSqlQuery implements PersonSqlQuery {

    @Override
    public String sqlQueryToInsert() {
        return "INSERT INTO person (type, first_name, last_name, pesel, height, weight, email, employment_start_date, actual_job_position, actual_salary) " +
                "VALUES ('employee', ?, ?, ?, ?, ?, ?, ?, ?,?)";
    }
}
