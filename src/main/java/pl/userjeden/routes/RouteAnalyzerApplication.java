package pl.userjeden.routes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import pl.userjeden.routes.businesscnfg.BusinessProperties;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(value = BusinessProperties.class)
public class RouteAnalyzerApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(RouteAnalyzerApplication.class, args);

		// TODO: surely could be triggered from REST endpoint
		RouteAnalyzingService routeAnalyzingService = applicationContext.getBean(RouteAnalyzingService.class);
		routeAnalyzingService.runRouteAnalysis("DEBRV_DEHAM_historical_routes.csv");
	}

}
