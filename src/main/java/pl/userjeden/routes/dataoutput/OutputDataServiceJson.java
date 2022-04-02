package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.model.RouteEntry;

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
    private ObjectMapper objectMapper;

    @Override
    public String writeOutput(List<RouteEntry> routeEntries) throws JsonProcessingException {

        RouteAssembly outputData = map(routeEntries);
        String outputJson = objectMapper.writeValueAsString(outputData);
        return outputJson;
    }


    private RouteAssembly map(List<RouteEntry> routeEntries){
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

    private String combineId(RouteEntry source){
        return String.valueOf(source.getFromSeq()) + "_" + String.valueOf(source.getToSeq());
    }


}
