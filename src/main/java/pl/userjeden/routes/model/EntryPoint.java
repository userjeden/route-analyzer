package pl.userjeden.routes.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntryPoint {

    public EntryPoint(Double[] coordinates){
        this.longitude = coordinates[0];
        this.latitide = coordinates[1];
    }

    private Double longitude;

    private Double latitide;

    private Long epochTime;

    private Double speedKnots;

}
