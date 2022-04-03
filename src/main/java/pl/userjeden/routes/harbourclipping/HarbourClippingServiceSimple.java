package pl.userjeden.routes.harbourclipping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.businesscnfg.BusinessProperties;
import pl.userjeden.routes.model.RouteEntry;

import java.util.List;

/**
 * Service responsible for correcting loaded data.
 */
@Slf4j
@Component
public class HarbourClippingServiceSimple implements HarbourClippingService {

    @Autowired
    private BusinessProperties businessProperties;

    /**
     * Accepts single RouteEntry and performs clipping by removing
     * parts of the route in the harbour area (harbour manouvers).
     */
    @Override
    public RouteEntry cropHarbourArea(RouteEntry routeEntry) {

        String harbourNameFrom = routeEntry.getFromPort();
        List<List<Double>> harbourFromLimits = businessProperties.harbours.get(harbourNameFrom);

        String harbourNameTo = routeEntry.getToPort();
        List<List<Double>> harbourToLimits = businessProperties.harbours.get(harbourNameTo);

        RouteEntry clippedEntry = routeEntry
                .clipByRectangle(harbourFromLimits.get(0).get(0), harbourFromLimits.get(1).get(0), harbourFromLimits.get(0).get(1), harbourFromLimits.get(1).get(1))
                .clipByRectangle(harbourToLimits.get(0).get(0), harbourToLimits.get(1).get(0), harbourToLimits.get(0).get(1), harbourToLimits.get(1).get(1));

        return clippedEntry;
    }

}
