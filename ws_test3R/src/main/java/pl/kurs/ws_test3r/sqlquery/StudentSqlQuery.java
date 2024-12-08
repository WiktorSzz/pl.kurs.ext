package pl.kurs.ws_test3r.sqlquery;

import org.springframework.stereotype.Component;

@Component("studentQuery")
public class StudentSqlQuery implements PersonSqlQuery {
    @Override
    public String sqlQueryToInsert() {
        return "INSERT INTO person (type,first_name, last_name, pesel, height, weight, email, graduated_university, year_of_study, field_of_study, scholarship) " +
                "VALUES ('student', ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
}
