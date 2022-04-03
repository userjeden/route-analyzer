package pl.userjeden.routes.datafeed;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.userjeden.routes.model.EntryPoint;
import pl.userjeden.routes.model.RouteEntry;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for importing and parsing input data.
 */
@Slf4j
@Component
public class InputDataServiceCsv implements InputDataService {

    private List<String> parsingProblems;

    /**
     * Imports referenced file, checks for file correctness and integrity,
     * and performs mapping to internal data model. In case of errors, throws
     * an exception containing list of problems.
     */
    @Override
    public List<RouteEntry> handleInput(String inputFileName) throws Exception {
        parsingProblems = new ArrayList<>();

        List<String[]> rows = readInputFile(inputFileName);
        List<RouteEntry> entries = parseAndMapAndValidate(rows);

        if(!parsingProblems.isEmpty()){
            String combinedProblems = String.join(", ", parsingProblems);
            throw new InputDataException("Malformed input file. Errors: " + combinedProblems);
        }

        log.info("Successfully imported: {}", inputFileName);
        return entries;
    }

    private List<String[]> readInputFile(String inputFileName) throws InputDataException {
        List<String[]> rows = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(inputFileName))) {
            rows = reader.readAll();

        } catch (FileNotFoundException e) {
            throw new InputDataException("Missing input file. Error: " + e.getMessage());

        } catch (IOException e) {
            throw new InputDataException("Error opening file. Error: " + e.getMessage());

        } catch (CsvException e) {
            throw new InputDataException("Error in CSV structure. Error: " + e.getMessage());
        }

        // remove header row
        rows.remove(0);
        return rows;
    }

    private List<RouteEntry> parseAndMapAndValidate(List<String[]> rows){
        List<RouteEntry> entries = rows.stream()
                .filter(r -> r != null && r.length == 8)
                .map(e -> createRouteEntry(e))
                .peek(e -> validateIntegrity(e))
                .collect(Collectors.toList());

        return entries;
    }

    private RouteEntry createRouteEntry(String[] input){
        return new RouteEntry(input[0], parseInteger(input[1]), parseInteger(input[2]), input[3], input[4],
                parseLong(input[5]), parseInteger(input[6]), parsePointsFromEntry(input[7]));
    }

    private List<EntryPoint> parsePointsFromEntry(String entry) {
        List<EntryPoint> output = new ArrayList<>();

        String[] entries = entry.replaceAll("\\[", "").replaceAll("\\]", "").split(", ");
        for (int i = 0; i < entries.length; i = i + 4) {

            try{
                EntryPoint point = new EntryPoint(parseDouble(entries[i]), parseDouble(entries[i + 1]), parseLong(entries[i + 2]), parseDouble(entries[i + 3]));
                output.add(point);

            }catch (Exception ex){
                String area = entries[i] + " " + entries[i + 1] + " " + entries[i + 2] + " " + entries[i + 3];
                String problem = "Error while parsing " + ex.getMessage() + " within: " +  area;
                parsingProblems.add(problem);
            }
        }
        return output;
    }

    private void validateIntegrity(RouteEntry entry){
        if(!entry.isIntegrity()){
            parsingProblems.add("Integrity problem in input id: " + entry.getId());
        }
    }

    private Integer parseInteger(String input){
        return (input == null || input.equals("null")) ? null : Integer.parseInt(input);
    }

    private Double parseDouble(String input){
        return (input == null || input.equals("null")) ? null : Double.parseDouble(input);
    }

    private Long parseLong(String input){
        return (input == null || input.equals("null")) ? null : Long.parseLong(input);
    }

}
