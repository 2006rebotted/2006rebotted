package astraeus.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public final class CompressionUtils {
	
	/**
	 * Degzips <strong>all</strong> of the datain the specified {@link ByteBuffer}.
	 *
	 * @param compressed The compressed buffer.
	 * @return The decompressed array.
	 * @throws IOException If there is an error decompressing the buffer.
	 */
	public static byte[] degzip(byte[] compressed) throws IOException {
		try (InputStream is = new GZIPInputStream(new ByteArrayInputStream(compressed));
		     ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];

			while (true) {
				int read = is.read(buffer, 0, buffer.length);
				if (read == -1) {
					break;
				}

				out.write(buffer, 0, read);
			}

			return out.toByteArray();
		}
	}

}
