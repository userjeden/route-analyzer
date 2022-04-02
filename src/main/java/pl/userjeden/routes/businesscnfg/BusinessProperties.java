package pl.userjeden.routes.businesscnfg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.model.HarbourArea;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "business")
public class BusinessProperties {

    // TODO: link
    public Map<String, List<List<Double>>> harbours;

}
