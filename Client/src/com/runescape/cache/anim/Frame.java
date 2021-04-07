package com.runescape.cache.anim;
import java.util.Hashtable;

import com.runescape.Client;
import com.runescape.io.Buffer;

public final class Frame {

	public static Hashtable<Integer, Frame> frameList = new Hashtable<Integer, Frame>();

	public static void load(byte data[], int fileId) {	      
		Buffer buffer = new Buffer(data);		
		buffer.currentPosition = data.length - 12;
		
		int i = buffer.readInt();
		int j = buffer.readInt();
		int k = buffer.readInt();
		
		int offset = 0;
		
		Buffer head = new Buffer(data);		
		head.currentPosition = offset;
		offset += i + 4;
		Buffer stream_2 = new Buffer(data);
		stream_2.currentPosition = offset;
		offset += j;
		Buffer stream_3 = new Buffer(data);
		stream_3.currentPosition = offset;
		offset += k;
		Buffer stream_4 = new Buffer(data);
		stream_4.currentPosition = offset;
		
		FrameBase base = new FrameBase(stream_4);
		
		int frameCount = head.readInt();		
		int translationIndices[] = new int[500];		
		int transformX[] = new int[500];		
		int transformY[] = new int[500];		
		int transformZ[] = new int[500];		
		for (int frameIndex = 0; frameIndex < frameCount; frameIndex++) {		      
			int id = head.readInt();			
			Frame frame = new Frame();			
			frameList.put(new Integer((fileId << 16) + id), frame);
			frame.base = base;
			int transformations = head.readUnsignedByte();			
			int lastIndex = -1;			
			int transformation = 0;
			for (int index = 0; index < transformations; index++) {			      
				int attribute = stream_2.readUnsignedByte();				
				if (attribute > 0) {
					if (base.transformationType[index] != 0) {
						for (int next = index - 1; next > lastIndex; next--) {						      
							if (base.transformationType[next] != 0)
								continue;
							translationIndices[transformation] = next;
							transformX[transformation] = 0;
							transformY[transformation] = 0;
							transformZ[transformation] = 0;
							transformation++;
							break;
						}

					}
					translationIndices[transformation] = index;
					
					char c = '\0';
					
					if (base.transformationType[index] == 3) {
						c = '\200';
					}
					
					if ((attribute & 1) != 0) {
						transformX[transformation] = stream_3.readSmart();
					} else {
						transformX[transformation] = c;
					}
					
					if ((attribute & 2) != 0) {
						transformY[transformation] = stream_3.readSmart();
					}	else {
						transformY[transformation] = c;
					}
					
					if ((attribute & 4) != 0) {
						transformZ[transformation] = stream_3.readSmart();
					}	else {
						transformZ[transformation] = c;
					}
					
					lastIndex = index;
					transformation++;
				}
			}

			frame.transformationCount = transformation;
			frame.transformationIndices = new int[transformation];
			frame.transformX = new int[transformation];
			frame.transformY = new int[transformation];
			frame.transformZ = new int[transformation];
			
			for (int index = 0; index < transformation; index++) {				      
				frame.transformationIndices[index] = translationIndices[index];
				frame.transformX[index] = transformX[index];
				frame.transformY[index] = transformY[index];
				frame.transformZ[index] = transformZ[index];
			}

		}

	}

	public static void clear() {
		frameList = null;
	}

	public static Frame method531(int frameId) {	      
		try {
			int fileId = frameId >> 16;			
			Frame frame = (Frame) frameList.get(new Integer(frameId));
			if (frame == null) {
				Client.instance.resourceProvider.provide(1, fileId);
				return null;
			} else
				return frame;
		} catch (Exception ex) {		      
			ex.printStackTrace();
		}
		return null;
	}

	public static boolean noAnimationInProgress(int frame) {
		return frame == -1;		
	}

	public Frame() {
	}

	public int duration;	
	public FrameBase base;
	public int transformationCount;	
	public int transformationIndices[];	
	public int transformX[];	
	public int transformY[];	
	public int transformZ[];	

}
