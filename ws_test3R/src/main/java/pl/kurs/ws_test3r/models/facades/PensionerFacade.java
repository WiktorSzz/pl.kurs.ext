package pl.kurs.ws_test3r.models.facades;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.commands.UpdateEntityCommand;
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
    public Pensioner updateEntity(Pensioner pensioner, UpdateEntityCommand command) {
        Map<String, Object> properties = command.getAttributes();
        Pensioner copiedPensioner = new Pensioner(pensioner);
        copiedPensioner.setVersion(command.getVersion());
        if (command.getVersion() == null) {
            throw new IllegalArgumentException("Version cannot be null");
        }
        properties.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> copiedPensioner.setFirstName((String) value);
                case "lastName" -> copiedPensioner.setLastName((String) value);
                case "pesel" -> copiedPensioner.setPesel((String) value);
                case "height" -> copiedPensioner.setHeight(Double.parseDouble(value.toString()));
                case "weight" -> copiedPensioner.setWeight(Double.parseDouble(value.toString()));
                case "email" -> copiedPensioner.setEmail((String) value);
                case "pension" -> copiedPensioner.setPension(Double.parseDouble(value.toString()));
                case "yearsWorked" -> copiedPensioner.setYearsWorked(Integer.parseInt(value.toString()));
                default -> throw new IllegalArgumentException("Unsupported property: " + key);
            }
        });

        return copiedPensioner;
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
