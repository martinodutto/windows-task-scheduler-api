package cloud.martinodutto.wtsapi.query;

import java.io.IOException;

/**
 * A parser useful to transform the output of invocations of the Windows task scheduler into more user-friendly entities.
 */
public interface QueryParser<T> {

    T parse(String rawCommandOutput) throws IOException;
}
