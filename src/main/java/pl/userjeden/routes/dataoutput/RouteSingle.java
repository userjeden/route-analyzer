package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteSingle {

    @JsonProperty("type")
    private String type;

    @JsonProperty("geometry")
    private RouteGeometry geometry;

    @JsonProperty("properties")
    private RouteProperties properties;
}
