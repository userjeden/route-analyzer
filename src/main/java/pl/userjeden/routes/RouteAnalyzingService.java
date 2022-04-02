package pl.userjeden.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.businesscnfg.BusinessProperties;
import pl.userjeden.routes.datafeed.InputDataService;
import pl.userjeden.routes.dataoutput.OutputDataService;
import pl.userjeden.routes.harbourclipping.HarbourClippingService;
import pl.userjeden.routes.model.RouteEntry;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RouteAnalyzingService {

    @Autowired
    private BusinessProperties businessProperties;

    @Autowired
    private InputDataService inputParsingService;

    @Autowired
    private HarbourClippingService harbourClippingService;

    @Autowired
    private OutputDataService outputDataService;


    public void runRouteAnalysis(){

        try {
            List<RouteEntry> historicalEntries = inputParsingService.parseInput("DEBRV_DEHAM_historical_routes.csv");

            List<RouteEntry> clippedEntries = historicalEntries.stream().map(e -> harbourClippingService.cropHarbourArea(e)).limit(10).collect(Collectors.toList());

            String json = outputDataService.writeOutput(clippedEntries);

            log.info(json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }





}
