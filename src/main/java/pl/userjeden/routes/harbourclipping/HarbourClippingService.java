package pl.userjeden.routes.harbourclipping;

import pl.userjeden.routes.model.RouteEntry;

/**
 * Service responsible for correcting loaded data.
 */
public interface HarbourClippingService {

    /**
     * Accepts single RouteEntry and performs clipping by removing
     * parts of the route in the harbour area (harbour manouvers).
     */
    RouteEntry cropHarbourArea(RouteEntry routeEntry);
}
