package com.runescape.cache;

import com.runescape.cache.bzip.BZip2Decompressor;
import com.runescape.io.Buffer;

public final class FileArchive {
	
	/**
	 * The buffer containing the decompressed data in this Archive.
	 */
	private final byte[] buffer;
	
	/**
	 * The amount of entries in this Archive.
	 */
	private final int entries;
	
	/**
	 * The identifiers (i.e. hashed names) of each of the entries in this Archive.
	 */
	private final int[] identifiers;
	
	/**
	 * The raw (i.e. decompressed) sizes of each of the entries in this Archive.
	 */
	private final int[] extractedSizes;
	
	/**
	 * The compressed sizes of each of the entries in this Archive.
	 */
	private final int[] sizes;
	private final int[] indices;
	
	/**
	 * Whether or not this Archive was compressed as a whole: if false, decompression will be performed on each of the
	 * individual entries.
	 */
	private final boolean extracted;

	public FileArchive(byte data[]) {
		Buffer buffer = new Buffer(data);
		
		int decompressedLength = buffer.readTriByte();		
		int compressedLength = buffer.readTriByte();		
		
		if (compressedLength != decompressedLength) {
			
			byte output[] = new byte[decompressedLength];
			
			BZip2Decompressor.decompress(output, decompressedLength, data, compressedLength, 6);
			
			this.buffer = output;
			
			buffer = new Buffer(this.buffer);
			extracted = true;
		} else {
			this.buffer = data;
			extracted = false;
		}
		entries = buffer.readUShort();
		identifiers = new int[entries];
		extractedSizes = new int[entries];
		sizes = new int[entries];
		indices = new int[entries];
		int offset = buffer.currentPosition + entries * 10;
		for (int file = 0; file < entries; file++) {
			identifiers[file] = buffer.readInt();
			extractedSizes[file] = buffer.readTriByte();
			sizes[file] = buffer.readTriByte();
			indices[file] = offset;
			offset += sizes[file];
		}
	}

	public byte[] readFile(String name) {		
		byte output[] = null;
		int hash = 0;
		name = name.toUpperCase();
		for (int index = 0; index < name.length(); index++) {
			hash = (hash * 61 + name.charAt(index)) - 32;
		}

		for (int file = 0; file < entries; file++) {
			if (identifiers[file] == hash) {
				if (output == null) {
					output = new byte[extractedSizes[file]];
				}
				if (!extracted) {
					BZip2Decompressor.decompress(output, extractedSizes[file], this.buffer,
							sizes[file], indices[file]);
				} else {
					System.arraycopy(this.buffer, indices[file], output,
							0, extractedSizes[file]);
				}
				return output;
			}
		}
		return null;
	}

}
