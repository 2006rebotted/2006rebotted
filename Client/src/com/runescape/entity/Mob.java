package com.runescape.entity;

import com.runescape.Client;
import com.runescape.cache.anim.Animation;
import com.runescape.cache.anim.Graphic;

public class Mob extends Renderable {

	public final int[] pathX;
	public final int[] pathY;
	public int interactingEntity;
	public int anInt1503;
	public int degreesToTurn;
	public int runAnimIndex;
	public String spokenText;
	public int height;
	public int nextStepOrientation;
	public int idleAnimation;
	public int standTurnAnimIndex;
	public int textColour;
	public final int[] hitDamages;
	public final int[] hitMarkTypes;
	public final int[] hitsLoopCycle;
	public int movementAnimation;
	public int displayedMovementFrames;
	public int anInt1519;
	public int graphic;	
	public int currentAnimation;
	public int anInt1522;
	public int graphicDelay;
	public int graphicHeight;
	public int remainingPath;
	public int emoteAnimation;
	public int displayedEmoteFrames;
	public int emoteTimeRemaining;
	public int animationDelay;
	public int currentAnimationLoops;
	public int textEffect;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	public int textCycle;
	public int time;	
	public int faceX;
	public int faceY;
	public int size;	
	public boolean animationStretches;
	public int anInt1542;
	public int initialX;
	public int destinationX;
	public int initialY;
	public int destinationY;
	public int startForceMovement;
	public int endForceMovement;
	public int direction;
	public int x;
	public int y;
	public int orientation;
	public final boolean[] pathRun;
	public int walkAnimIndex;
	public int turn180AnimIndex;
	public int turn90CWAnimIndex;
	public int turn90CCWAnimIndex;

	public int nextAnimationFrame;
	public int nextGraphicsAnimationFrame;
	public int nextIdleAnimationFrame;

	public Mob() {
		pathX = new int[10];
		pathY = new int[10];
		interactingEntity = -1;
		degreesToTurn = 32;
		runAnimIndex = -1;
		height = 200;
		idleAnimation = -1;
		standTurnAnimIndex = -1;
		hitDamages = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		movementAnimation = -1;
		graphic = -1;
		emoteAnimation = -1;
		loopCycleStatus = -1000;
		textCycle = 100;
		size = 1;
		animationStretches = false;
		pathRun = new boolean[10];
		walkAnimIndex = -1;
		turn180AnimIndex = -1;
		turn90CWAnimIndex = -1;
		turn90CCWAnimIndex = -1;
	}

	public final void setPos(int x, int y, boolean flag) {
		if (emoteAnimation != -1 && Animation.animations[emoteAnimation].walkingPrecedence == 1) {
			emoteAnimation = -1;
		}
		
		if (!flag) {
			
			int dx = x - pathX[0];
			int dy = y - pathY[0];
			
			if (dx >= -8 && dx <= 8 && dy >= -8 && dy <= 8) {
				
				if (remainingPath < 9) {
					remainingPath++;
				}
				
				for (int index = remainingPath; index > 0; index--) {					
					pathX[index] = pathX[index - 1];
					pathY[index] = pathY[index - 1];
					pathRun[index] = pathRun[index - 1];
				}

				pathX[0] = x;
				pathY[0] = y;
				pathRun[0] = false;
				return;
			}
		}
		remainingPath = 0;
		anInt1542 = 0;
		anInt1503 = 0;
		pathX[0] = x;
		pathY[0] = y;
		this.x = pathX[0] * 128 + size * 64;
		this.y = pathY[0] * 128 + size * 64;
	}

	public final void resetPath() {
		remainingPath = 0;
		anInt1542 = 0;
	}

	public final void updateHitData(int hitType, int hitDamage, int currentTime) {
		for (int hitPtr = 0; hitPtr < 4; hitPtr++)
			if (hitsLoopCycle[hitPtr] <= currentTime) {
				hitDamages[hitPtr] = hitDamage;
				hitMarkTypes[hitPtr] = hitType;
				hitsLoopCycle[hitPtr] = currentTime + 70;
				return;
			}
	}
	
  public void nextPreForcedStep() {
        int remaining = startForceMovement - Client.tick;        
        int tempX = initialX * 128 + size * 64;        
        int tempY = initialY * 128 + size * 64;        
        x += (tempX - x) / remaining;
        y += (tempY - y) / remaining;
        
        anInt1503 = 0;
        
        if (direction == 0) {
              nextStepOrientation = 1024;              
        }
        
        if (direction == 1) {
              nextStepOrientation = 1536;
        }
        
        if (direction == 2) {
              nextStepOrientation = 0;
        }
        
        if (direction == 3) {
              nextStepOrientation = 512;
        }
  }
  
  public void nextForcedMovementStep() {
        if (endForceMovement == Client.tick || emoteAnimation == -1
                    || animationDelay != 0
                    || emoteTimeRemaining
                                + 1 > Animation.animations[emoteAnimation]
                                            .duration(displayedEmoteFrames)) {
              int remaining = endForceMovement - startForceMovement;              
              int elapsed = Client.tick - startForceMovement;              
              int initialX = this.initialX * 128 + size * 64;              
              int initialY = this.initialY * 128 + size * 64;
              int endX = destinationX * 128 + size * 64;              
              int endY = destinationY * 128 + size * 64;              
              x = (initialX * (remaining - elapsed) + endX * elapsed) / remaining;
              y = (initialY * (remaining - elapsed) + endY * elapsed) / remaining;
        }
        anInt1503 = 0;
        
        if (direction == 0) {
              nextStepOrientation = 1024;
        }
        
        if (direction == 1) {
              nextStepOrientation = 1536;
        }
        
        if (direction == 2) {
              nextStepOrientation = 0;
        }
        
        if (direction == 3) {
              nextStepOrientation = 512;
        }
        
        orientation = nextStepOrientation;        
  }
  
  public void nextStep() {
        movementAnimation = idleAnimation;   
        
        if (remainingPath == 0) {              
              anInt1503 = 0;
              return;
        }
        
        if (emoteAnimation != -1 && animationDelay == 0) {
              Animation animation = Animation.animations[emoteAnimation];
              if (anInt1542 > 0 && animation.animatingPrecedence == 0) {
                    anInt1503++;
                    return;
              }
              if (anInt1542 <= 0 && animation.walkingPrecedence == 0) {
                    anInt1503++;
                    return;
              }
        }
        int tempX = x;        
        int tempY = y;          
        int nextX = pathX[remainingPath - 1] * 128 + size * 64;
        int nextY = pathY[remainingPath - 1] * 128 + size * 64;
        if (nextX - tempX > 256 || nextX - tempX < -256 || nextY - tempY > 256 || nextY - tempY < -256) {
              x = nextX;
              y = nextY;
              return;
        }
        if (tempX < nextX) {
              if (tempY < nextY) {
                    nextStepOrientation = 1280;
              } else if (tempY > nextY) {
                    nextStepOrientation = 1792;
              } else {
                    nextStepOrientation = 1536;
              }
        } else if (tempX > nextX) {
              if (tempY < nextY) {
                    nextStepOrientation = 768;
              } else if (tempY > nextY) {
                    nextStepOrientation = 256;
              } else {
                    nextStepOrientation = 512;
              }
        } else if (tempY < nextY) {
              nextStepOrientation = 1024;
        } else {
              nextStepOrientation = 0;
        }
        
        int rotation = nextStepOrientation - orientation & 0x7ff;
        
        if (rotation > 1024) {
              rotation -= 2048;
        }
        
        int animation = turn180AnimIndex;
        
        if (rotation >= -256 && rotation <= 256) {
              animation = walkAnimIndex;
        } else if (rotation >= 256 && rotation < 768) {
              animation = turn90CCWAnimIndex;
        } else if (rotation >= -768 && rotation <= -256) {
              animation = turn90CWAnimIndex;
        }
        
        if (animation == -1) {
              animation = walkAnimIndex;
        }
        
        movementAnimation = animation;
        
        int positionDelta = 4;
        
        if (orientation != nextStepOrientation && interactingEntity == -1
                    && degreesToTurn != 0) {
              positionDelta = 2;
        }
        
        if (remainingPath > 2) {
              positionDelta = 6;
        }
        
        if (remainingPath > 3) {
              positionDelta = 8;
        }
        
        if (anInt1503 > 0 && remainingPath > 1) {
              positionDelta = 8;
              anInt1503--;
        }
        
        if (pathRun[remainingPath - 1]) {
              positionDelta <<= 1;
        }
        
        if (positionDelta >= 8 && movementAnimation == walkAnimIndex
                    && runAnimIndex != -1) {
              movementAnimation = runAnimIndex;
        }
        
        if (tempX < nextX) {
              x += positionDelta;
              if (x > nextX) {
                    x = nextX;
              }
        } else if (tempX > nextX) {
              x -= positionDelta;
              if (x < nextX) {
                    x = nextX;
              }
        }
        if (tempY < nextY) {
              y += positionDelta;
              if (y > nextY) {
                    y = nextY;
              }
        } else if (tempY > nextY) {
              y -= positionDelta;
              
              if (y < nextY) {
                    y = nextY;
              }
        }
        if (x == nextX && y == nextY) {
              remainingPath--;
              
              if (anInt1542 > 0) {
                    anInt1542--;
              }
        }
  }

	public final void moveInDir(boolean run, int direction) {
		int x = pathX[0];
		int y = pathY[0];
		if (direction == 0) {
			x--;
			y++;
		}
		if (direction == 1)
			y++;
		if (direction == 2) {
			x++;
			y++;
		}
		if (direction == 3)
			x--;
		if (direction == 4)
			x++;
		if (direction == 5) {
			x--;
			y--;
		}
		if (direction == 6)
			y--;
		if (direction == 7) {
			x++;
			y--;
		}
		if (emoteAnimation != -1 && Animation.animations[emoteAnimation].walkingPrecedence == 1)
			emoteAnimation = -1;
		if (remainingPath < 9)
			remainingPath++;
		for (int l = remainingPath; l > 0; l--) {
			pathX[l] = pathX[l - 1];
			pathY[l] = pathY[l - 1];
			pathRun[l] = pathRun[l - 1];
		}
		pathX[0] = x;
		pathY[0] = y;
		pathRun[0] = run;
	}
	
  public void updateAnimation() {
        animationStretches = false;  
        
        if (movementAnimation != -1) {
              
              if (movementAnimation > Animation.animations.length) {
                    movementAnimation = 0;
              }
              
              Animation animation = Animation.animations[movementAnimation];
              anInt1519++;
              
              if (displayedMovementFrames < animation.frameCount
                          && anInt1519 > animation
                                      .duration(displayedMovementFrames)) {
                    anInt1519 = 1;
                    displayedMovementFrames++;
                    nextIdleAnimationFrame++;                    
              }
              
              nextIdleAnimationFrame = displayedMovementFrames + 1;
              
              if (nextIdleAnimationFrame >= animation.frameCount) {
                    
                    if (nextIdleAnimationFrame >= animation.frameCount) {
                          nextIdleAnimationFrame = 0;
                    }
              }
              
              if (displayedMovementFrames >= animation.frameCount) {
                    anInt1519 = 1;
                    displayedMovementFrames = 0;
              }
        }
        if (graphic != -1 && Client.tick >= graphicDelay) {
              if (currentAnimation < 0)
                    currentAnimation = 0;
              Animation animation_1 = Graphic.cache[graphic].animationSequence;
              for (anInt1522++; currentAnimation < animation_1.frameCount
                          && anInt1522 > animation_1.duration(
                                      currentAnimation); currentAnimation++)
                    anInt1522 -= animation_1.duration(currentAnimation);

              if (currentAnimation >= animation_1.frameCount
                          && (currentAnimation < 0
                                      || currentAnimation >= animation_1.frameCount))
                    graphic = -1;
              nextGraphicsAnimationFrame = currentAnimation + 1;
              if (nextGraphicsAnimationFrame >= animation_1.frameCount) {
                    if (nextGraphicsAnimationFrame < 0
                                || nextGraphicsAnimationFrame >= animation_1.frameCount)
                          graphic = -1;
              }
        }
        if (emoteAnimation != -1 && animationDelay <= 1) {
              if (emoteAnimation >= Animation.animations.length) {
                    emoteAnimation = -1;
              }
              Animation animation_2 = Animation.animations[emoteAnimation];
              if (animation_2.animatingPrecedence == 1 && anInt1542 > 0
                          && startForceMovement <= Client.tick
                          && endForceMovement < Client.tick) {
                    animationDelay = 1;
                    return;
              }
        }
        if (emoteAnimation != -1 && animationDelay == 0) {
              Animation animation_3 = Animation.animations[emoteAnimation];
              for (emoteTimeRemaining++; displayedEmoteFrames < animation_3.frameCount
                          && emoteTimeRemaining > animation_3.duration(
                                      displayedEmoteFrames); displayedEmoteFrames++)
                    emoteTimeRemaining -=
                                animation_3.duration(displayedEmoteFrames);

              if (displayedEmoteFrames >= animation_3.frameCount) {
                    displayedEmoteFrames -= animation_3.loopOffset;
                    currentAnimationLoops++;
                    
                    if (currentAnimationLoops >= animation_3.maximumLoops) {
                          emoteAnimation = -1;
                    }
                    
                    if (displayedEmoteFrames < 0 || displayedEmoteFrames >= animation_3.frameCount) {
                          emoteAnimation = -1;
                    }
              }
              
              nextAnimationFrame = displayedEmoteFrames + 1;
              
              if (nextAnimationFrame >= animation_3.frameCount) {
                    
                    if (currentAnimationLoops >= animation_3.maximumLoops) {
                          nextAnimationFrame = displayedEmoteFrames + 1;
                    }
                    
                    if (nextAnimationFrame < 0 || nextAnimationFrame >= animation_3.frameCount) {
                          nextAnimationFrame = displayedEmoteFrames;
                    }
                    
              }
              animationStretches = animation_3.stretches;              
        }
        
        if (animationDelay > 0) {
              animationDelay--;
        }
        
  }

	public int entScreenX;
	public int entScreenY;
	public final int index = -1;

	public boolean isVisible() {
		return false;
	}
}
