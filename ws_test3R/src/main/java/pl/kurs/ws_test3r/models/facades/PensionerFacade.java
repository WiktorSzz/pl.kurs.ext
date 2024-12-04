package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.dto.PensionerDTO;
import pl.kurs.ws_test3r.models.person.Pensioner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("pensionerFacade")
public class PensionerFacade implements EntityFacade<Pensioner, PensionerDTO> {


    @Override
    public Pensioner buildEntityFromMap(Map<String, String> parameters) {
        parameters.forEach((key, value) -> {
            log.info("Parameter: {} - Type: {}", key, value != null ? value.getClass().getName() : "null");
        });
        List<String> requiredParams = Arrays.asList(
                "firstName", "lastName", "pesel", "height", "weight",
                "email", "pension", "yearsWorked"
        );
        for (String param : requiredParams) {
            if (!parameters.containsKey(param)) {
                throw new IllegalArgumentException("Missing required parameters: " + param);
            }
        }

        Pensioner pensioner = new Pensioner();
        pensioner.setFirstName(parameters.get("firstName"));
        pensioner.setLastName(parameters.get("lastName"));
        pensioner.setPesel(parameters.get("pesel"));
        pensioner.setHeight(Double.parseDouble(parameters.get("height")));
        pensioner.setWeight(Double.parseDouble(parameters.get("weight")));
        pensioner.setEmail(parameters.get("email"));
        pensioner.setPension(Double.parseDouble(parameters.get("pension")));
        pensioner.setYearsWorked(Integer.parseInt(parameters.get("yearsWorked")));
        return pensioner;
    }


    @Override
    public Pensioner updateEntity(Pensioner pensioner, Map<String, Object> updates) {
        if (updates.containsKey("firstName")) {
            pensioner.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            pensioner.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("pesel")) {
            pensioner.setPesel((String) updates.get("pesel"));
        }
        if (updates.containsKey("height")) {
            pensioner.setHeight(Double.parseDouble(updates.get("height").toString()));
        }
        if (updates.containsKey("weight")) {
            pensioner.setWeight(Double.parseDouble(updates.get("weight").toString()));
        }
        if (updates.containsKey("email")) {
            pensioner.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("pension")) {
            pensioner.setPension(Double.parseDouble(updates.get("pension").toString()));
        }
        if (updates.containsKey("yearsWorked")) {
            pensioner.setYearsWorked(Integer.parseInt(updates.get("yearsWorked").toString()));
        }
        return pensioner;
    }

    @Override
    public PensionerDTO toDto(Pensioner pensioner) {
        return new PensionerDTO(
                pensioner.getId(),
                pensioner.getFirstName(),
                pensioner.getLastName(),
                pensioner.getPesel(),
                pensioner.getHeight(),
                pensioner.getWeight(),
                pensioner.getEmail(),
                pensioner.getVersion(),
                pensioner.getPension(),
                pensioner.getYearsWorked()
        );
    }
}
