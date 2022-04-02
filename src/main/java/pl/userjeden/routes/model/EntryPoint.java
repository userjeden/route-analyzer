package pl.userjeden.routes.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntryPoint {

    private Double longitude;
    private Double latitide;
    private Long epochTime;
    private Double speedKnots;
}
