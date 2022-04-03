package pl.userjeden.routes.businesscnfg;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "business")
public class BusinessProperties {

    /**
     * Harbour manouvering areas,
     * to be excluded from routes.
     */
    public Map<String, List<List<Double>>> harbours;

    /**
     * Suggested reasonable route times.
     * Of course should refer to the route not just target.
     */
    public Map<String, Long> timeframes;

    /**
     * Output file name.
     */
    public String outputFile;

}
