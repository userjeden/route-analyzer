package pl.userjeden.routes.routeanalytics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.businesscnfg.BusinessProperties;
import pl.userjeden.routes.model.EntryPoint;
import pl.userjeden.routes.model.RouteEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for route reducing.
 */
@Slf4j
@Component
public class AnalyticServiceImpl implements AnalyticService {

    private static final long ONE_MINUTE = 60000;
    private static final double FRONT_RADAR_RADIUS = 2.0;
    private static final double REAR_RADAR_RADIUS = 2.0;
    private static final double MAX_RADAR_RADIUS = 5.0;
    private static final double PROGRESS_STEP = 0.7;
    private static final int MIN_POINTS_REQUIRED = 3;

    @Autowired
    private BusinessProperties businessProperties;

    /**
     * Selects most suitable sample routes with specific criteria (reasonable travel time)
     * and performs reduce operation to calculate an example representative for the whole subset.
     */
    @Override
    public RouteEntry traceSingleRoute(List<RouteEntry> routeEntries, String direction) {

        long effectiveVoyageTime = businessProperties.timeframes.get(direction);
        List<RouteEntry> samplingRoutes = routeEntries.stream()
                .filter(r -> r.getFromPort() != null && r.getToPort().equals(direction))
                .filter(e -> e.getLegDuration() < effectiveVoyageTime).collect(Collectors.toList());

        log.info("Started analyse for route {} to {}.", samplingRoutes.get(0).getFromPort(), samplingRoutes.get(0).getToPort());
        log.info("Found {} routes shorter than suggested {} minutes.", samplingRoutes.size(), effectiveVoyageTime/ONE_MINUTE);

        return performRouteTracing(samplingRoutes);
    }

    private RouteEntry performRouteTracing(List<RouteEntry> samplingRoutes){
        // first two points of route - to calculate initial vector
        Double[] firstPointAvg = calculateNthPoint(samplingRoutes, 0);
        Double[] secondPointAvg = calculateNthPoint(samplingRoutes, 1);

        // all waypoints from availabale routes
        List<EntryPoint> availablePointsHeap = samplingRoutes.stream()
                .flatMap(e -> e.getEntryPoints().stream())
                .collect(Collectors.toList());

        // base route with cleared points
        RouteEntry outputRoute = new RouteEntry();
        outputRoute.setEntryPoints(new ArrayList<>());

        Double[] previous = {firstPointAvg[0], firstPointAvg[1]};
        Double[] current = {secondPointAvg[0], secondPointAvg[1]};

        // two initial points placed into the route
        outputRoute.getEntryPoints().add(new EntryPoint(previous));
        outputRoute.getEntryPoints().add(new EntryPoint(current));

        while(true){

            double progress = PROGRESS_STEP;
            double unit = distance(current[0], previous[0], current[1], previous[1]);
            double progressFactor = progress/unit;

            Double[] vector = {current[0] - previous[0], current[1] - previous[1]};
            Double[] next = {current[0] + vector[0], current[1] + vector[1]};
            Double[] preprevious = {current[0] - 2*vector[0], current[1] - 2*vector[1]};

            // clean points we have passed
            availablePointsHeap = availablePointsHeap.stream()
                    .filter(p -> !isWithinRadius(p, preprevious, unit * REAR_RADAR_RADIUS))
                    .collect(Collectors.toList());

            // find points ahead
            List<EntryPoint> pointsOnRadar = new ArrayList<>();
            double radarMultiplier = FRONT_RADAR_RADIUS;
            while(pointsOnRadar.size() < MIN_POINTS_REQUIRED && radarMultiplier < MAX_RADAR_RADIUS){
                final double multiplier = radarMultiplier++;
                pointsOnRadar = availablePointsHeap.stream()
                        .filter(p -> isWithinRadius(p, next, unit * multiplier))
                        .collect(Collectors.toList());
            }

            log.debug("progress: {}, radius: {}, in radius: {}", progress, unit, pointsOnRadar.size());

            if(pointsOnRadar.isEmpty() || pointsOnRadar.size() < MIN_POINTS_REQUIRED){
                break;
            }

            double avgLon = pointsOnRadar.stream().mapToDouble(p -> p.getLongitude()).average().getAsDouble();
            double avgLat = pointsOnRadar.stream().mapToDouble(p -> p.getLatitide()).average().getAsDouble();

            previous[0] = current[0];
            previous[1] = current[1];
            current[0] = current[0] + (progressFactor * (avgLon - current[0]));
            current[1] = current[1] + (progressFactor * (avgLat - current[1]));

            // store point to the route
            outputRoute.getEntryPoints().add(new EntryPoint(current[0], current[1], null, null));
        }

        return outputRoute;
    }


    private Double[] calculateNthPoint(List<RouteEntry> effectiveRoutes, int ordinal) {
        Double avgLon = effectiveRoutes.stream().mapToDouble(e -> e.getEntryPoints().get(ordinal).getLongitude()).average().getAsDouble();
        Double avgLat = effectiveRoutes.stream().mapToDouble(e -> e.getEntryPoints().get(ordinal).getLatitide()).average().getAsDouble();
        Double[] point = {avgLon, avgLat};
        return point;
    }

    private boolean isWithinRadius(EntryPoint entryPoint, Double[] center, double distance){
        double dist = distance(center[0], entryPoint.getLongitude(), center[1], entryPoint.getLatitide());
        return  dist < distance;
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956 for miles
        double r = 6371;
        return(c * r);
    }

}
