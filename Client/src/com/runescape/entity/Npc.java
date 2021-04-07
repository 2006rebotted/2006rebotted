package com.runescape.entity;

import com.runescape.cache.anim.Animation;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.anim.Graphic;
import com.runescape.cache.def.NpcDefinition;
import com.runescape.entity.model.Model;

public final class Npc extends Mob {

	public NpcDefinition desc;

	private Model getAnimatedModel() {
		if (super.emoteAnimation >= 0 && super.animationDelay == 0) {
			int emote = Animation.animations[super.emoteAnimation].primaryFrames[super.displayedEmoteFrames];
			int movement = -1;
			if (super.movementAnimation >= 0 && super.movementAnimation != super.idleAnimation)
				movement = Animation.animations[super.movementAnimation].primaryFrames[super.displayedMovementFrames];
			return desc.getAnimatedModel(movement, emote,
					Animation.animations[super.emoteAnimation].interleaveOrder);
		}
		int movement = -1;
		if (super.movementAnimation >= 0)
			movement = Animation.animations[super.movementAnimation].primaryFrames[super.displayedMovementFrames];
		return desc.getAnimatedModel(-1, movement, null);
	}

	public Model getRotatedModel() {
		if (desc == null)
			return null;
		Model animatedModel = getAnimatedModel();
		if (animatedModel == null)
			return null;
		super.height = animatedModel.modelBaseY;
		if (super.graphic != -1 && super.currentAnimation != -1) {
			Graphic spotAnim = Graphic.cache[super.graphic];
			Model graphicModel = spotAnim.getModel();
			if (graphicModel != null) {
				int frame = spotAnim.animationSequence.primaryFrames[super.currentAnimation];
				Model model = new Model(true, Frame.noAnimationInProgress(frame),
						false, graphicModel);
				model.translate(0, -super.graphicHeight, 0);
				model.skin();
				model.apply(frame);
				model.faceGroups = null;
				model.vertexGroups = null;
				if (spotAnim.resizeXY != 128 || spotAnim.resizeZ != 128)
					model.scale(spotAnim.resizeXY, spotAnim.resizeXY,
							spotAnim.resizeZ);
				model.light(64 + spotAnim.modelBrightness,
						850 + spotAnim.modelShadow, -30, -50, -30, true);
				Model models[] = { animatedModel, model };
				animatedModel = new Model(models);
			}
		}
		if (desc.size == 1)
			animatedModel.fits_on_single_square = true;
		return animatedModel;
	}

	public boolean isVisible() {
		return desc != null;
	}
}
