package cloud.martinodutto.wtsapi.internal.services;

import cloud.martinodutto.wtsapi.exceptions.WtsInvocationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class WtsInvoker {

    private WtsInvoker() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static String submitCommands(List<String> commands) throws IOException, InterruptedException {

        ProcessBuilder pb = new ProcessBuilder(commands);
        Process p = pb.start();

        // capture standard output
        String stdOutput;
        try (InputStream is = p.getInputStream()) {
            stdOutput = consumeStream(is);
        }

        // capture standard error
        String stdError;
        try (InputStream es = p.getErrorStream()) {
            stdError = consumeStream(es);
        }

        // wait for the external process to end
        p.waitFor();

        if (!stdError.isEmpty()) {
            throw new WtsInvocationException(stdError);
        }
        return stdOutput;
    }

    private static String consumeStream(InputStream is) throws IOException {
        final StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}
