package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteGeometry {

    @JsonProperty("type")
    private String type;

    @JsonProperty("coordinates")
    public List<List<Double>> coordinates;

}
