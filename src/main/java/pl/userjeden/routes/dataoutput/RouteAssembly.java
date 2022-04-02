package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteAssembly {

    @JsonProperty("type")
    private String type;

    @JsonProperty("features")
    public List<RouteSingle> features;
}
