package pl.userjeden.routes;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import pl.userjeden.routes.businesscnfg.BusinessProperties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(value = BusinessProperties.class)
public class RouteAnalyzerApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(RouteAnalyzerApplication.class, args);
		RouteAnalyzingService routeAnalyzingService = applicationContext.getBean(RouteAnalyzingService.class);
		routeAnalyzingService.runRouteAnalysis();
	}

}
