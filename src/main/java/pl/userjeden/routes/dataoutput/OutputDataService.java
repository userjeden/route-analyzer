package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.userjeden.routes.model.RouteEntry;

import java.util.List;

public interface OutputDataService {

    String writeOutput(List<RouteEntry> entries) throws JsonProcessingException;
}
