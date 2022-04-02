package pl.userjeden.routes.datafeed;

import pl.userjeden.routes.model.RouteEntry;
import java.util.List;

/**
 * Service responsible for importing and parsing input data.
 */
public interface InputDataService {

    /**
     * Imports referenced file, checks for file correctness and integrity,
     * and performs mapping to internal data model. In case of errors, throws
     * an exception containing list of problems.
     */
    List<RouteEntry> handleInput(String inputFileName) throws Exception;
}
