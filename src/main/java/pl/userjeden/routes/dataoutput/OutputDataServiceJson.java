package pl.userjeden.routes.dataoutput;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.businesscnfg.BusinessProperties;
import pl.userjeden.routes.model.RouteEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class OutputDataServiceJson implements OutputDataService {

    private static final String FEATURE_COLL = "FeatureCollection";
    private static final String FEATURE_TYPE = "Feature";
    private static final String LINE_TYPE = "LineString";
    private static final String COLOR_RED = "red";
    private static final Double OPACITY = 0.3d;

    @Autowired
    private BusinessProperties businessProperties;


    @Override
    public RouteAssembly mapForOutput(List<RouteEntry> routeEntries){
        RouteAssembly example = new RouteAssembly();
        example.setFeatures(new ArrayList<>());

        routeEntries.forEach(re -> {
            List<List<Double>> coordinates = new ArrayList<>();
            re.getEntryPoints().forEach(p -> coordinates.add(Arrays.asList(p.getLongitude(), p.getLatitide())));
            RouteGeometry geometry = new RouteGeometry(LINE_TYPE, coordinates);
            RouteProperties properties = new RouteProperties(combineId(re), re.getFromPort(), re.getToPort(), re.getId(), COLOR_RED, OPACITY);
            RouteSingle feature = new RouteSingle(FEATURE_TYPE, geometry, properties);
            example.getFeatures().add(feature);
        });

        example.setType(FEATURE_COLL);
        return example;
    }

    @Override
    public void writeOutput(RouteAssembly routeAssembly) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String output = gson.toJson(routeAssembly);

        String fileName = businessProperties.outputFile;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(output);
        writer.close();

        log.info("Saved output file: {}", fileName);
    }

    private String combineId(RouteEntry source){
        return String.valueOf(source.getFromSeq()) + "_" + String.valueOf(source.getToSeq());
    }

}
