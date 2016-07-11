package jmr;

import java.io.IOException;

/**
 * Created by mfrey on 11/07/2016.
 */
public interface RequestHandler {
    Request handle(Request req) throws IOException;
}
