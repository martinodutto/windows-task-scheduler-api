package cloud.martinodutto.wtsapi.api.query.parsers;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.RFC4180Parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parses the results of calling the Windows task scheduler command with the following parameters:
 * <code>/Query /FO CSV</code>.
 */
public final class CSVQueryParser implements QueryParser<List<Map<String, String>>> {

    @Override
    public List<Map<String, String>> parse(String rawCommandOutput) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        // we need to use the RFC4180 parser because otherwise backslashes in the values are treated as escape characters
        RFC4180Parser rfc4180Parser = new RFC4180Parser();
        try (StringReader stringReader = new StringReader(rawCommandOutput);
             CSVReaderHeaderAware csvReader = (CSVReaderHeaderAware) new CSVReaderHeaderAwareBuilder(stringReader)
                     .withCSVParser(rfc4180Parser).build()) {

            Map<String, String> rowMap;
            while ((rowMap = csvReader.readMap()) != null) {
                if (!isFurtherHeader(rowMap)) {
                    list.add(rowMap);
                }
            }
        }
        return list;
    }

    /**
     * Identifies if a row is another header, by comparing it to the content of the first row of the CSV file.
     *
     * <p>This method is useful because of the way the results of the <code>/Query</code> method are presented: the
     * headers can be repeated many times.</p>
     *
     * @param rowMap The mapping that represents the current row.
     * @return True if this row is another header.
     */
    private boolean isFurtherHeader(Map<String, String> rowMap) {
        return rowMap.entrySet()
                .stream()
                .allMatch(entry -> entry.getKey().equals(entry.getValue()));
    }
}
