package pl.userjeden.routes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteEntry {

    private String id;

    private Integer fromSeq;

    private Integer toSeq;

    private String fromPort;

    private String toPort;

    private Long legDuration;

    private Integer count;

    private List<EntryPoint> entryPoints;

    /**
     * Confirms integrity by comparing
     * entryPoints count and declared count.
     */
    public boolean isIntegrity(){
        return entryPoints != null && entryPoints.size() == count;
    }

    public RouteEntry clipByRectangle(Double lonA, Double lonB, Double latA, Double latB){

        List<EntryPoint> pointsForRemoval = new ArrayList<>();
        for(EntryPoint point : this.entryPoints){
            if(isWithinArea(point, lonA, lonB, latA, latB)){
                pointsForRemoval.add(point);
            }
        }

        this.entryPoints.removeAll(pointsForRemoval);
        this.count = count - pointsForRemoval.size();
        return this;
    }

    public boolean isWithinArea(EntryPoint entryPoint, Double lonA, Double lonB, Double latA, Double latB){
        return isWithinLatitude(entryPoint, latA, latB) && isWithinLongitude(entryPoint, lonA, lonB);
    }

    private boolean isWithinLongitude(EntryPoint entryPoint, Double lonA, Double lonB){
        return (entryPoint.getLongitude() > lonA && entryPoint.getLongitude() < lonB) ||
                (entryPoint.getLongitude() < lonA && entryPoint.getLongitude() > lonB);
    }

    private boolean isWithinLatitude(EntryPoint entryPoint, Double latA, Double latB){
        return (entryPoint.getLatitide() > latA && entryPoint.getLatitide() < latB) ||
                (entryPoint.getLatitide() < latA && entryPoint.getLatitide() > latB);
    }

}
