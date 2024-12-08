package pl.kurs.ws_test3r.models.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class UpdateEntityCommand {

    private String classType;
    private Map<String, Object> attributes;
    private Long version;
}