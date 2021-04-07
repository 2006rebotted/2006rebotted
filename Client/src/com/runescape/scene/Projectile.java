package com.runescape.scene;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.anim.Graphic;
import com.runescape.entity.Renderable;
import com.runescape.entity.model.Model;

public final class Projectile extends Renderable {
	
	public final int startCycle;
	public final int stopCycle;
	private double xIncrement;
	private double yIncrement;
	private double diagonalIncrement;
	private double heightIncrement;
	private double aDouble1578;
	private boolean started;
	private final int projectileX;
	private final int projectileY;
	private final int startHeight;
	public final int endHeight;
	public double xPos;
	public double yPos;
	public double cnterHeight;
	private final int initialSlope;
	private final int initialDistance;
	public final int target;
	private final Graphic projectileGFX;
	private int gfxStage;
	private int gfxTickOfCurrentStage;
	public int turnValue;
	private int tiltAngle;
	public final int projectileZ;

	public void calculateIncrements(int currentCycle, int targetY, int targetCenterHeight, int targetX) {
		if(!started) {
			double xToGo = targetX - projectileX;
			double yToGo = targetY - projectileY;
			double distanceToGo = Math.sqrt(xToGo * xToGo + yToGo * yToGo);
			xPos = (double) projectileX + (xToGo * (double) initialDistance) / distanceToGo;
			yPos = (double) projectileY + (yToGo * (double) initialDistance) / distanceToGo;
			cnterHeight = startHeight;
		}
		double cyclesLeft = (stopCycle + 1) - currentCycle;
		xIncrement = ((double)targetX - xPos) / cyclesLeft;
		yIncrement = ((double)targetY - yPos) / cyclesLeft;
		diagonalIncrement = Math.sqrt(xIncrement * xIncrement + yIncrement * yIncrement);
		if(!started) {
            heightIncrement = -diagonalIncrement * Math.tan((double) initialSlope * 0.02454369D);
		}
		aDouble1578 = (2D * ((double)targetCenterHeight - cnterHeight - heightIncrement * cyclesLeft)) / (cyclesLeft * cyclesLeft);
	}

	public Model getRotatedModel() {
		Model modelGfx = projectileGFX.getModel();
		if(modelGfx == null) {
			return null;
		}
		int frameNumber = -1;
		if(projectileGFX.animationSequence != null) {
			frameNumber = projectileGFX.animationSequence.primaryFrames[gfxStage];
		}
		Model projectileModel = new Model(true, Frame.noAnimationInProgress(frameNumber), false, modelGfx);
		if(frameNumber != -1) {
			projectileModel.skin();
			projectileModel.apply(frameNumber);
			projectileModel.faceGroups = null;
			projectileModel.vertexGroups = null;
		}
		if(projectileGFX.resizeXY != 128 || projectileGFX.resizeZ != 128) {
			projectileModel.scale(projectileGFX.resizeXY, projectileGFX.resizeXY, projectileGFX.resizeZ);
		}
		projectileModel.leanOverX(tiltAngle);
		projectileModel.light(64 + projectileGFX.modelBrightness, 850 + projectileGFX.modelShadow, -30, -50, -30, true);
		return projectileModel;
	}

	public Projectile(int initialSlope, int endHeight, int creationCycle, int destructionCycle, int initialDistance, int startZ,  int startHeight, int y, int x, int target, int gfxMoving) {
		projectileGFX = Graphic.cache[gfxMoving];
		projectileZ = startZ;
		projectileX = x;
		projectileY = y;
		this.startHeight = startHeight;
		startCycle = creationCycle;
		stopCycle = destructionCycle;
		this.initialSlope = initialSlope;
		this.initialDistance = initialDistance;
		this.target = target;
		this.endHeight = endHeight;
		started = false;
	}

	public void progressCycles(int cyclesMissed) {
		started = true;
		xPos += xIncrement * (double)cyclesMissed;
		yPos += yIncrement * (double)cyclesMissed;
		cnterHeight += heightIncrement * (double)cyclesMissed + 0.5D * aDouble1578 * (double)cyclesMissed * (double)cyclesMissed;
		heightIncrement += aDouble1578 * (double)cyclesMissed;
        //noinspection SuspiciousNameCombination
        turnValue = (int)(Math.atan2(xIncrement, yIncrement) * 325.94900000000001D) + 1024 & 0x7ff;
		tiltAngle = (int)(Math.atan2(heightIncrement, diagonalIncrement) * 325.94900000000001D) & 0x7ff;
		if(projectileGFX.animationSequence != null) {
			for(gfxTickOfCurrentStage += cyclesMissed; gfxTickOfCurrentStage > projectileGFX.animationSequence.duration(gfxStage);) {
				gfxTickOfCurrentStage -= projectileGFX.animationSequence.duration(gfxStage) + 1;
				gfxStage++;
				if(gfxStage >= projectileGFX.animationSequence.frameCount) {
					gfxStage = 0;
				}
			}
		}
	}
}