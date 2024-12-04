package pl.kurs.ws_test3r.sqlquery;

import org.springframework.stereotype.Component;

@Component("pensionerQuery")
public class PensionerSqlQuery implements PersonSqlQuery {

    @Override
    public String sqlQueryToInsert() {
        return "INSERT INTO person (type, first_name, last_name, pesel, height, weight, email, pension, years_worked) " +
                "VALUES ('pensioner', ?, ?, ?, ?, ?, ?, ?, ?)";
    }

}
