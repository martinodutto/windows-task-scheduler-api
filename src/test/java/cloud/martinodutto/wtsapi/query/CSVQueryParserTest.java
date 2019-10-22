package cloud.martinodutto.wtsapi.query;

import cloud.martinodutto.wtsapi.query.parsers.CSVQueryParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVQueryParserTest {

    private static final String WTS_TEST_STRING = "\"HostName\",\"TaskName\",\"Next Run Time\",\"Status\",\"Logon Mode\",\"Last Run Time\",\"Last Result\",\"Author\",\"Task To Run\",\"Start In\",\"Comment\",\"Scheduled Task State\",\"Idle Time\",\"Power Management\",\"Run As User\",\"Delete Task If Not Rescheduled\",\"Stop Task If Runs X Hours and X Mins\",\"Schedule\",\"Schedule Type\",\"Start Time\",\"Start Date\",\"End Date\",\"Days\",\"Months\",\"Repeat: Every\",\"Repeat: Until: Time\",\"Repeat: Until: Duration\",\"Repeat: Stop If Still Running\"\n" +
            "\"T99003\",\"\\Microsoft\\Windows\\Workplace Join\\Device-Sync\",\"N/A\",\"Disabled\",\"Interactive/Background\",\"30/11/1999 00:00:00\",\"267011\",\"Microsoft Corporation\",\"COM handler\",\"N/A\",\"Sync device attributes to Azure Active Directory.\",\"Disabled\",\"Disabled\",\"\",\"NT AUTHORITY\\SYSTEM\",\"Disabled\",\"00:10:00\",\"Scheduling data is not available in this format.\",\"Undefined\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",\"N/A\",\"N/A\"";

    @Test
    void canParseWtsOutput() throws IOException {
        final CSVQueryParser csvQueryParser = new CSVQueryParser();
        final List<Map<String, String>> parsed = csvQueryParser.parse(WTS_TEST_STRING);
        assertEquals(1, parsed.size());
        assertEquals(28, parsed.get(0).size());
    }
}
