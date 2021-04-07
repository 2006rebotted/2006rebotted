package com.runescape.cache;

import java.io.*;

public final class FileStore {

	public static enum Store {

		ARCHIVE(0),

		MODEL(1),

		ANIMATION(2),

		MUSIC(3),

		MAP(4);

		private int index;

		private Store(int index) {
			this.index = index;
		}

		public int getIndex() {
			return this.index;
		}

	}
	
	private static final byte[] buffer = new byte[520];
	private final RandomAccessFile dataFile;
	private final RandomAccessFile indexFile;
	private final int storeIndex;

	public FileStore(RandomAccessFile data, RandomAccessFile index, int storeIndex) {
		this.storeIndex = storeIndex;
		dataFile = data;
		indexFile = index;
	}

	public synchronized byte[] decompress(int id) {
		try {
			seek(indexFile, id * 6);
			for (int in = 0, read = 0; read < 6; read += in) {
				in = indexFile.read(buffer, read, 6 - read);

				if (in == -1) {
					return null;
				}

			}

			int size = ((buffer[0] & 0xff) << 16) + ((buffer[1] & 0xff) << 8) + (buffer[2] & 0xff);
			int sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);

			if (sector <= 0 || (long) sector > dataFile.length() / 520L) {
				return null;
			}

			byte buf[] = new byte[size];

			int totalRead = 0;

			for (int part = 0; totalRead < size; part++) {

				if (sector == 0) {
					return null;
				}

				seek(dataFile, sector * 520);

				int unread = size - totalRead;

				if (unread > 512) {
					unread = 512;
				}

				for (int in = 0, read = 0; read < unread + 8; read += in) {
					in = dataFile.read(buffer, read, (unread + 8) - read);

					if (in == -1) {
						return null;
					}
				}
				int currentIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
				int currentPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
				int nextSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
				int currentFile = buffer[7] & 0xff;

				if (currentIndex != id || currentPart != part || currentFile != storeIndex) {
					return null;
				}

				if (nextSector < 0 || (long) nextSector > dataFile.length() / 520L) {
					return null;
				}

				for (int i = 0; i < unread; i++) {
					buf[totalRead++] = buffer[i + 8];
				}

				sector = nextSector;
			}

			return buf;
		} catch (IOException _ex) {
			return null;
		}
	}

	public synchronized boolean writeFile(int length, byte data[], int index) {
		return writeFile(data, index, length, true) ? true : writeFile(data, index, length, false);
	}

	private synchronized boolean writeFile(byte bytes[], int position, int length, boolean exists) {
		try {
			int sector;
			if (exists) {

				seek(indexFile, position * 6);

				for (int in = 0, read = 0; read < 6; read += in) {
					in = indexFile.read(buffer, read, 6 - read);

					if (in == -1) {
						return false;
					}

				}
				sector = ((buffer[3] & 0xff) << 16) + ((buffer[4] & 0xff) << 8) + (buffer[5] & 0xff);

				if (sector <= 0 || (long) sector > dataFile.length() / 520L) {
					return false;
				}

			} else {
				sector = (int) ((dataFile.length() + 519L) / 520L);
				if (sector == 0) {
					sector = 1;
				}
			}
			buffer[0] = (byte) (length >> 16);
			buffer[1] = (byte) (length >> 8);
			buffer[2] = (byte) length;
			buffer[3] = (byte) (sector >> 16);
			buffer[4] = (byte) (sector >> 8);
			buffer[5] = (byte) sector;
			seek(indexFile, position * 6);
			indexFile.write(buffer, 0, 6);

			for (int part = 0, written = 0; written < length; part++) {

				int nextSector = 0;

				if (exists) {
					seek(dataFile, sector * 520);

					int read = 0;

					for (int in = 0; read < 8; read += in) {

						in = dataFile.read(buffer, read, 8 - read);

						if (in == -1) {
							break;
						}
					}

					if (read == 8) {
						int currentIndex = ((buffer[0] & 0xff) << 8) + (buffer[1] & 0xff);
						int currentPart = ((buffer[2] & 0xff) << 8) + (buffer[3] & 0xff);
						nextSector = ((buffer[4] & 0xff) << 16) + ((buffer[5] & 0xff) << 8) + (buffer[6] & 0xff);
						int currentFile = buffer[7] & 0xff;

						if (currentIndex != position || currentPart != part || currentFile != storeIndex) {
							return false;
						}

						if (nextSector < 0 || (long) nextSector > dataFile.length() / 520L) {
							return false;
						}
					}
				}
				if (nextSector == 0) {
					exists = false;
					nextSector = (int) ((dataFile.length() + 519L) / 520L);

					if (nextSector == 0) {
						nextSector++;
					}

					if (nextSector == sector) {
						nextSector++;
					}

				}

				if (length - written <= 512) {
					nextSector = 0;
				}

				buffer[0] = (byte) (position >> 8);
				buffer[1] = (byte) position;
				buffer[2] = (byte) (part >> 8);
				buffer[3] = (byte) part;
				buffer[4] = (byte) (nextSector >> 16);
				buffer[5] = (byte) (nextSector >> 8);
				buffer[6] = (byte) nextSector;
				buffer[7] = (byte) storeIndex;
				seek(dataFile, sector * 520);
				dataFile.write(buffer, 0, 8);

				int unwritten = length - written;

				if (unwritten > 512) {
					unwritten = 512;
				}

				dataFile.write(bytes, written, unwritten);
				written += unwritten;
				sector = nextSector;
			}

			return true;
		} catch (IOException ex) {
			return false;
		}
	}

	private synchronized void seek(RandomAccessFile file, int position) throws IOException {		
		try {
			file.seek(position);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
