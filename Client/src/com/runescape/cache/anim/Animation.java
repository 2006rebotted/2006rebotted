package com.runescape.cache.anim;
import com.runescape.cache.FileArchive;
import com.runescape.io.Buffer;

public final class Animation {
	
    public static Animation animations[];
    
    /**
     * The amount of frames in this Animation.
     */
    public int frameCount;  
    
    /**
     * The primary frame ids of this Animation.
     */
    public int primaryFrames[];
    
    /**
     * The secondary frame ids of this Animation.
     */
    public int secondaryFrames[];
    
    /**
     * The duration of each frame in this Animation.
     */
    public int[] durations;
    
    /**
     * The amount of frames subtracted to restart the loop.
     */
    public int loopOffset;    
    public int interleaveOrder[];
    public boolean stretches;
    public int priority;
    
    /**
     * Indicates whether or not this player's shield will be displayed whilst this animation is played.
     */
    public int playerOffhand;
    
    /**
     * Indicates whether or not this player's weapon will be displayed whilst this animation is played.
     */
    public int playerMainhand;
    
    /**
     * The maximum times this animation will loop.
     */
    public int maximumLoops;
    
    /**
     * The animation precedence (will this animation 'override' other animations or will this one yield).
     */
    public int animatingPrecedence;
    public int walkingPrecedence;
    public int replayMode;
    
    private Animation() {
        loopOffset = -1;
        stretches = false;
        priority = 5;
        playerOffhand = -1;
        playerMainhand = -1;
        maximumLoops = 99;
        animatingPrecedence = -1;
        walkingPrecedence = -1;
        replayMode = 1; 
    }

    public static void init(FileArchive streamLoader)  {
		Buffer stream = new Buffer(streamLoader.readFile("seq.dat"));
        int length = stream.readUShort();
        if(animations == null) {
            animations = new Animation[length + 5000];
        }
        System.out.println("Animations Loaded: " + length);
        for(int id = 0; id < length; id++) {              
            if(animations[id] == null) {
                animations[id] = new Animation();
            }
            animations[id].decode(stream);
			try {
				if (id == 4000) {
					animations[id].frameCount = 28;
					animations[id].durations = new int[]{3,2,2,2,3,3,3,3,3,2,2,2,2,2,3,3,2,1,1,3,3,3,3,3,3,3,3,3};
					animations[id].primaryFrames = new int[]{223019263,223019511,223019120,223019119,223019242,223019278,223019027,223019504,223019417,223019428,223019705,223019087,223019664,223019465,223019589,223019707,223019322,223019644,223019174,223019574,223019108,223019335,223019521,223019401,223019300,223019029,223019276,223019115};
					animations[id].animatingPrecedence = 0;
					animations[id].walkingPrecedence = 0;
				}
				if (id == 4001) {
					animations[id].frameCount = 15;
					animations[id].durations = new int[]{9,3,3,3,3,3,2,2,15,4,3,3,3,3,3};
					animations[id].primaryFrames = new int[]{219742346,219742338,219742330,219742335,219742341,219742348,219742322,219742325,219742318,219742320,219742323,219742349,219742334,219742317,219742347};
				}
				if (id == 4002) {
					animations[id].frameCount = 40;
					animations[id].durations = new int[]{3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3};
					animations[id].primaryFrames = new int[]{219742278,219742256,219742218,219742282,219742223,219742222,219742253,219742232,219742300,219742239,219742254,219742252,219742245,219742224,219742219,219742294,219742209,219742241,219742299,219742230,219742279,219742238,219742221,219742214,219742283,219742305,219742312,219742280,219742265,219742211,219742210,219742208,219742212,219742234,219742314,219742240,219742292,219742313,219742267,219742263};
				}
				if (id == 355) {
					animations[355].frameCount = 59;
					animations[355].durations = new int[]{15, 15, 4, 4, 4, 4, 4, 4, 7, 5, 9, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 6, 7, 5, 4, 4, 5, 5, 5, 5, 5, 7, 7, 12, 12, 6, 6, 5, 4, 5, 5, 13, 13, 7, 7, 3, 3, 3, 3, 3, 3, 3, 2, 2, 4, 4, 4, 4, 7};
					animations[355].primaryFrames = new int[]{236191750, 236191782, 236191797, 236191757, 236191784, 236191749, 236191789, 236191796, 236191765, 236191759, 236191800, 236191794, 236191764, 236191774, 236191763, 236191776, 236191792, 236191777, 236191760, 236191802, 236191788, 236191785, 236191783, 236191778, 236191770, 236191752, 236191768, 236191798, 236191751, 236191747, 236191758, 236191754, 236191781, 236191755, 236191801, 236191795, 236191779, 236191799, 236191756, 236191746, 236191772, 236191775, 236191748, 236191787, 236191773, 236191780, 236191767, 236191790, 236191761, 236191793, 236191786, 236191766, 236191745, 236191769, 236191744, 236191762, 236191771, 236191753, 236191791};
				}
				if (id == 352) {
					animations[id].frameCount = 78;
					animations[id].priority = 8;
					animations[id].playerOffhand = animations[id].playerMainhand = 512;
					animations[id].durations = new int[]{3, 3, 3, 3, 3, 3, 3, 4, 4, 2, 2, 2, 2, 2, 2, 2, 4, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 6, 7, 5, 4, 4, 5, 5, 5, 5, 5, 7, 7, 12, 12, 6, 6, 5, 4, 5, 5, 13, 13, 7, 7, 3, 3, 3, 3, 3, 3, 3, 2, 2, 4, 4, 4, 4, 7};
					animations[id].primaryFrames = new int[]{236257300, 236257510, 236257440, 236257525, 236257611, 236257570, 236257477, 236257565, 236257360, 236257350, 236257329, 236257513, 236257379, 236257463, 236257388, 236257303, 236257476, 236257302, 236257458, 236257580, 236257501, 236257539, 236257331, 236257524, 236257322, 236257382, 236257659, 236257523, 236257381, 236257612, 236257560, 236257574, 236257488, 236257394, 236257317, 236257422, 236257385, 236257471, 236257538, 236257409, 236257619, 236257368, 236257645, 236257459, 236257603, 236257405, 236257288, 236257520, 236257294, 236257646, 236257546, 236257420, 236257586, 236257467, 236257535, 236257328, 236257657, 236257656, 236257583, 236257606, 236257376, 236257295, 236257371, 236257514, 236257358, 236257533, 236257287, 236257297, 236257313, 236257506, 236257356, 236257359, 236257386, 236257343, 236257340, 236257620, 236257551, 236257487};
					animations[id].animatingPrecedence = 0;
					animations[id].walkingPrecedence = 0;
				}
				animations[15069] = new Animation();
				animations[15069].frameCount = 24;
				animations[15069].durations = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };
				animations[15069].primaryFrames = new int[] { 227803309, 227803140, 227803217, 227803251, 227803297, 227803222, 227803158, 227803280, 227803266, 227803224, 227803268, 227803288, 227803218, 227803157, 227803187, 227803152, 227803286, 227803179, 227803168, 227803229, 227803248, 227803213, 227803180, 227803284 };
				animations[15069].animatingPrecedence = 0;
				animations[15069].walkingPrecedence = 0;
				
				animations[15070] = new Animation();
				animations[15070].frameCount = 24;
				animations[15070].durations = new int[] { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };
				animations[15070].primaryFrames = new int[] { 227803277, 227803235, 227803154, 227803141, 227803238, 227803321, 227803151, 227803203, 227803205, 227803299, 227803137, 227803262, 227803245, 227803174, 227803242, 227803139, 227803305, 227803317, 227803254, 227803167, 227803185, 227803267, 227803257, 227803265 };
				animations[15070].animatingPrecedence = 0;
				animations[15070].walkingPrecedence = 0;
			
				animations[15071] = new Animation();
				animations[15071].frameCount = 16;
				animations[15071].durations = new int[] { 4, 3, 3, 4, 4, 3, 2, 2, 1, 1, 2, 2, 3, 3, 3, 1 };
				animations[15071].primaryFrames = new int[] { 227803302, 227803143, 227803240, 227803163, 227803159, 227803155, 227803323, 227803183, 227803276, 227803287, 227803285, 227803219, 227803210, 227803147, 227803204, 227803302 };
				animations[15071].priority = 6;
				animations[15071].maximumLoops = 1;
				animations[15071].animatingPrecedence = 2;
				animations[15071].walkingPrecedence = 2;
				
				animations[15072] = new Animation();
				animations[15072].frameCount = 23;
				animations[15072].durations = new int[] { 3, 4, 4, 3, 4, 2, 2, 1, 1, 1, 1, 1, 3, 3, 3, 2, 2, 2, 3, 3, 3, 4, 1 };
				animations[15072].primaryFrames = new int[] { 227803212, 227803308, 227803199, 227803292, 227803227, 227803241, 227803271, 227803327, 227803296, 227803173, 227803314, 227803247, 227803263, 227803237, 227803243, 227803156, 227803138, 227803190, 227803198, 227803231, 227803304, 227803233, 227803212 };
				animations[15072].priority = 6;
				animations[15072].maximumLoops = 1;
				animations[15072].animatingPrecedence = 2;
				animations[15072].walkingPrecedence = 2;
				
				animations[15073] = new Animation();
				animations[15073].frameCount = 16;
				animations[15073].durations = new int[] { 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2, 3, 2 };
				animations[15073].primaryFrames = new int[] { 227803319, 227803320, 227803223, 227803197, 227803177, 227803259, 227803211, 227803145, 227803312, 227803252, 227803279, 227803209, 227803166, 227803176, 227803207, 227803261 };
				animations[15073].animatingPrecedence = 2;
				animations[15073].walkingPrecedence = 2;
				
				animations[15074] = new Animation();
				animations[15074].frameCount = 17;
				animations[15074].durations = new int[] { 1, 2, 2, 2, 3, 3, 3, 4, 2, 1, 3, 1, 2, 2, 1, 1, 1 };
				animations[15074].primaryFrames = new int[] { 227803221, 227803322, 227803275, 227803256, 227803283, 227803272, 227803293, 227803318, 227803303, 227803206, 227803164, 227803146, 227803289, 227803255, 227803225, 227803169, 227803221 };
				animations[15074].maximumLoops = 1;
				animations[15074].animatingPrecedence = 2;
				animations[15074].walkingPrecedence = 2;
				
				animations[15075] = new Animation();
				animations[15075].frameCount = 16;
				animations[15075].durations = new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 };
				animations[15075].primaryFrames = new int[] { 227803194, 227803226, 227803171, 227803228, 227803232, 227803136, 227803253, 227803175, 227803316, 227803162, 227803186, 227803192, 227803294, 227803144, 227803189, 227803282 };
				animations[15075].animatingPrecedence = 0;
				animations[15075].walkingPrecedence = 0;
				
				animations[15076] = new Animation();
				animations[15076].frameCount = 16;
				animations[15076].durations = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
				animations[15076].primaryFrames = new int[] { 227803193, 227803298, 227803201, 227803234, 227803216, 227803160, 227803315, 227803306, 227803325, 227803220, 227803182, 227803149, 227803150, 227803313, 227803208, 227803258 };
				animations[15076].animatingPrecedence = 0;
				animations[15076].walkingPrecedence = 0;
				
				animations[15077] = new Animation();
				animations[15077].frameCount = 16;
				animations[15077].durations = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
				animations[15077].primaryFrames = new int[] { 227803148, 227803326, 227803290, 227803270, 227803202, 227803269, 227803244, 227803307, 227803260, 227803153, 227803250, 227803196, 227803278, 227803246, 227803236, 227803195 };
				animations[15077].animatingPrecedence = 0;
				animations[15077].walkingPrecedence = 0;
			} catch (Exception _ex) {}
        }
    }

    public int duration(int frameId) {
        int duration = durations[frameId];        
        if(duration == 0) {
            Frame frame = Frame.method531(primaryFrames[frameId]);   
            
            if(frame != null) {
                duration = durations[frameId] = frame.duration;
            }
            
        }
        
        return duration == 0 ? 1 : duration;
    }

	public void decode(Buffer stream) {
		do {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			if (opcode == 1) {
				frameCount = stream.readUShort();
				
				primaryFrames = new int[frameCount];				
				secondaryFrames = new int[frameCount];				
				durations = new int[frameCount];			
				
				for (int frame = 0; frame < frameCount; frame++) {				      
					durations[frame] = stream.readUShort();
					secondaryFrames[frame] = -1;
				}
				
				for (int frame = 0; frame < frameCount; frame++) {				      
					primaryFrames[frame] = stream.readUShort();
				}
				
				for (int frame = 0; frame < frameCount; frame++) {				      
					primaryFrames[frame] = (stream.readUShort() << 16) + primaryFrames[frame];
				}
				
			} else if (opcode == 2) {
				loopOffset = stream.readUShort();
			} else if (opcode == 3) {
				int count = stream.readUnsignedByte();				
				interleaveOrder = new int[count + 1];				
				for (int index = 0; index < count; index++) {				      
					interleaveOrder[index] = stream.readUnsignedByte();
				}
				interleaveOrder[count] = 0x98967f;
			} else if (opcode == 4) {			      
				stretches = true;
			} else if (opcode == 5) {
				priority = stream.readUnsignedByte();				
			} else if (opcode == 6) {
				playerOffhand = stream.readUShort();				
			} else if (opcode == 7) {
				playerMainhand = stream.readUShort();				
			} else if (opcode == 8) {
				maximumLoops = stream.readUnsignedByte();				
			} else if (opcode == 9) {
				animatingPrecedence = stream.readUnsignedByte();				
			} else if (opcode == 10) {
				walkingPrecedence = stream.readUnsignedByte();				
			} else if (opcode == 11) {
				replayMode = stream.readUnsignedByte();				
			} else if (opcode == 12) {
				stream.readInt();
			} else {
				System.out.println("Error unrecognised seq config code: " + opcode);
			}
		} while (true);
		if (frameCount == 0) {
			frameCount = 1;
			primaryFrames = new int[1];
			primaryFrames[0] = -1;
			secondaryFrames = new int[1];
			secondaryFrames[0] = -1;
			durations = new int[1];
			durations[0] = -1;
		}
		if (animatingPrecedence == -1) {
			if (interleaveOrder != null) {
				animatingPrecedence = 2;
			} else {
				animatingPrecedence = 0;
			}
		}
		if (walkingPrecedence == -1) {
			if (interleaveOrder != null) {
				walkingPrecedence = 2;
				return;
			}
			walkingPrecedence = 0;
		}
	}
	
}