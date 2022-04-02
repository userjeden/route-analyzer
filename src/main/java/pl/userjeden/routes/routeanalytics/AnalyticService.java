package pl.userjeden.routes.routeanalytics;

import pl.userjeden.routes.model.RouteEntry;
import java.util.List;

/**
 * Service responsible for route reducing.
 */
public interface AnalyticService {

    /**
     * Selects most suitable sample routes with specific criteria (reasonable travel time)
     * and performs reduce operation to calculate an example representative for the whole subset.
     */
    RouteEntry traceSingleRoute(List<RouteEntry> routeEntries, String direction);

}
