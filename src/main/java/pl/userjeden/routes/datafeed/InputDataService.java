package pl.userjeden.routes.datafeed;

import pl.userjeden.routes.model.RouteEntry;

import java.util.List;

public interface InputDataService {

    public List<RouteEntry> parseInput(String inputFileName) throws Exception;
}
