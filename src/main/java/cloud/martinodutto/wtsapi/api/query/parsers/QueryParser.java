package cloud.martinodutto.wtsapi.api.query.parsers;

import java.io.IOException;

/**
 * A parser useful to transform the output of invocations of the Windows task scheduler into more user-friendly entities.
 */
public interface QueryParser<T> {

    /**
     * Parses the provided string to an instance of the type <code>T</code>.
     *
     * @param rawCommandOutput The string to parse.
     * @return The result of the parsing.
     * @throws IOException When an I/O error occurs.
     */
    T parse(String rawCommandOutput) throws IOException;
}
