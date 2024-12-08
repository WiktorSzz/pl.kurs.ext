package pl.kurs.ws_test3r.models.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@AllArgsConstructor
@Getter
@ToString
public class CreateEntityCommand {

    private String type;
    private Map<String, String> parameters;


}