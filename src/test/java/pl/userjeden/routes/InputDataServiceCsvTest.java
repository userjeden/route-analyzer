package pl.userjeden.routes;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import pl.userjeden.routes.datafeed.InputDataException;
import pl.userjeden.routes.datafeed.InputDataServiceCsv;

public class InputDataServiceCsvTest {


    @Test
    void shouldThrowExceptionOnWrongIntegrityValue(){

        Exception exception = assertThrows(InputDataException.class, () -> {
            InputDataServiceCsv inputParsingServiceCsv = new InputDataServiceCsv();
            inputParsingServiceCsv.handleInput("src/test/resources/DEBRV_DEHAM_historical_routes-wrong_integrity.csv");
        });

        String expectedMessage = "Malformed input file. Errors: Integrity problem in input id: imo_9454230";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionOnAdditionalComma(){

        Exception exception = assertThrows(InputDataException.class, () -> {
            InputDataServiceCsv inputParsingServiceCsv = new InputDataServiceCsv();
            inputParsingServiceCsv.handleInput("src/test/resources/DEBRV_DEHAM_historical_routes-addiional_comma.csv");
        });

        String expectedMessage = "Malformed input file. Errors: Error while parsing For input string: \"151,1932247447\" within: 8.495334 53.609833 151,1932247447 11.2, Integrity problem in input id: imo_9372200";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldThrowExceptionOnWrongValue(){

        Exception exception = assertThrows(InputDataException.class, () -> {
            InputDataServiceCsv inputParsingServiceCsv = new InputDataServiceCsv();
            inputParsingServiceCsv.handleInput("src/test/resources/DEBRV_DEHAM_historical_routes-wrong_value.csv");
        });

        String expectedMessage = "Malformed input file. Errors: Error while parsing For input string: \"nullz\" within: 8.495334 53.609833 1511932247447 nullz, Integrity problem in input id: imo_9372200";
        assertEquals(expectedMessage, exception.getMessage());
    }


}
