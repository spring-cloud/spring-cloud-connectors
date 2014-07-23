package org.springframework.cloud.localconfig;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Provides an easy way to stub the {@code openFile} method on the local connector.
 *
 * @author Christopher Smith
 *
 */
class StubbedOpenFileLocalConfigConnector extends LocalConfigConnector {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    private InputStreamProvider fileProvider;

    @Override
    InputStream openFile(File file) throws IOException {
        return fileProvider.openFile(file);
    }

    @Override
    boolean fileExists(File file) {
        return true;
    }

    public void setFileProvider(InputStreamProvider provider) {
        this.fileProvider = provider;
    }

    interface InputStreamProvider {
        InputStream openFile(File file) throws IOException;
    }

    /**
     * Returns the supplied input stream. Used for reading out of the classpath for testing.
     *
     * @param filename
     *            the filename we expect the connector to open
     * @param contents
     *            the contents to return
     */
    static InputStreamProvider fileContentsFromStream(final String expectedFilename, final InputStream stream) {
        return new InputStreamProvider() {
            @Override
            public InputStream openFile(File file) throws IOException {
                assertEquals(expectedFilename, file.getPath());
                return stream;
            }
        };
    }

    /**
     * Returns a stream view of the provided string.
     *
     * @param filename
     *            the filename we expect the connector to open
     * @param contents
     *            the contents to return
     */
    static InputStreamProvider fileContentsFromString(final String expectedFilename, final String contents) {
        return fileContentsFromStream(expectedFilename, new ByteArrayInputStream(contents.getBytes(UTF_8)));
    }
}
