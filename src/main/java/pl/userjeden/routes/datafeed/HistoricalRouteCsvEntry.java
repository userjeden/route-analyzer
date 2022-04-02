package pl.userjeden.routes.datafeed;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class HistoricalRouteCsvEntry {

    @CsvBindByName(column = "id")
    String id;

    @CsvBindByName(column = "from_seq")
    String fromSeq;

    @CsvBindByName(column = "to_seq")
    String toSeq;

    @CsvBindByName(column = "from_port")
    String fromPort;

    @CsvBindByName(column = "toPort")
    String toPort;

    @CsvBindByName(column = "leg_duration")
    String legDuration;

    @CsvBindByName(column = "count")
    String count;

    @CsvBindByName(column = "points")
    String points;
}
