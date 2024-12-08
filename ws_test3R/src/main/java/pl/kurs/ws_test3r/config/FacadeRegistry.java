package pl.kurs.ws_test3r.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.kurs.ws_test3r.models.facades.EntityFacade;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class FacadeRegistry {

    private final Map<String, EntityFacade<?, ?>> registry = new ConcurrentHashMap<>();

    private final Map<String, EntityFacade<?, ?>> facades;

    @Autowired
    public FacadeRegistry(Map<String, EntityFacade<?, ?>> facades) {
        this.facades = facades;
    }

    @PostConstruct
    public void registerFacades() {
        for (Map.Entry<String, EntityFacade<?, ?>> entry : facades.entrySet()) {
            registry.put(entry.getKey(), entry.getValue());
            log.info("Registered facade with key: " + entry.getKey());
        }
    }

    public <P, D> EntityFacade<P, D> getFacade(String key, Class<P> clazzP, Class<D> clazzD) {
        EntityFacade<?, ?> facade = registry.get(key);
        if (facade == null) {
            throw new IllegalArgumentException("No facade found for key: " + key);
        }
        return (EntityFacade<P, D>) facade;
    }


    public Set<String> getFacadeKeys() {
        return registry.keySet();
    }


}
