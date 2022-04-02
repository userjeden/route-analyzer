package pl.userjeden.routes.dataoutput;

import com.fasterxml.jackson.core.JsonProcessingException;
import pl.userjeden.routes.model.RouteEntry;
import java.io.IOException;
import java.util.List;

public interface OutputDataService {

    RouteAssembly mapForOutput(List<RouteEntry> entries);

    void writeOutput(RouteAssembly routeAssembly) throws IOException;
}
