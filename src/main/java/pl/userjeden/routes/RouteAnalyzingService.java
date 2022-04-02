package pl.userjeden.routes;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.businesscnfg.BusinessProperties;
import pl.userjeden.routes.datafeed.InputDataService;
import pl.userjeden.routes.dataoutput.OutputDataService;
import pl.userjeden.routes.dataoutput.RouteAssembly;
import pl.userjeden.routes.harbourclipping.HarbourClippingService;
import pl.userjeden.routes.model.RouteEntry;
import pl.userjeden.routes.routeanalytics.AnalyticService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RouteAnalyzingService {

    @Autowired
    private BusinessProperties businessProperties;

    @Autowired
    private InputDataService inputDataService;

    @Autowired
    private HarbourClippingService harbourClippingService;

    @Autowired
    private AnalyticService analyticService;

    @Autowired
    private OutputDataService outputDataService;

    private Set<String> possibleDestinations;

    /**
     * Accepts input file with sample bidirectional routes BETWEEN TWO HARBOURS.
     * Performs route analysis and produces output file with reduced routes.
     */
    @SneakyThrows
    public void runRouteAnalysis(String fileName){

        // parsing and preparing input data
        List<RouteEntry> historicalEntries = inputDataService.handleInput(fileName);
        List<RouteEntry> clippedEntries = historicalEntries.stream().map(e -> harbourClippingService.cropHarbourArea(e)).collect(Collectors.toList());

        // main analytic part, calculates reduced routes
        // for both destinations
        List<RouteEntry> reducedRoutes = historicalEntries.stream()
                .map(e -> e.getToPort()).collect(Collectors.toSet()).stream()
                .map(d -> analyticService.traceSingleRoute(clippedEntries, d))
                .collect(Collectors.toList());

        RouteAssembly routeAssembly = outputDataService.mapForOutput(reducedRoutes);
        outputDataService.writeOutput(routeAssembly);
    }

}
