package astraeus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
	
	/**
	 * Converts a file into an array of bytes.
	 * 
	 * @param file
	 * 		The file to convert.
	 * 
	 * @return The file in bytes.
	 * 
	 * @throws IOException
	 */
	public static byte[] fileToByteArray(File file) throws IOException {
        final byte[] data = new byte[Math.toIntExact(file.length())]; 
        
        try(FileInputStream fis = new FileInputStream(file)) {
      	  fis.read(data);
        }
        
        return data;
	}
	
	/**
	 * Unpacks a file store entry into uncompressed format.
	 * 
	 * @param file
	 *            The file to uncompress.
	 */
	public static byte[] decompressStoreEntry(File file) throws Exception {
		if (!file.exists()) {
			return null;
		}
		return CompressionUtils.degzip(FileUtils.fileToByteArray(file));
	}

}
