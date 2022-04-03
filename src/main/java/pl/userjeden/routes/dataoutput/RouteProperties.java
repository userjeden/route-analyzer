package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteProperties {

    @JsonProperty("id")
    private String id;

    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("vesselId")
    private String vesselId;

    @JsonProperty("stroke")
    private String stroke;

    @JsonProperty("stroke-opacity")
    private Double strokeOpacity;
}
