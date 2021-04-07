package com.runescape.scene;
import com.runescape.cache.anim.Frame;
import com.runescape.cache.anim.Graphic;
import com.runescape.entity.Renderable;
import com.runescape.entity.model.Model;

public final class AnimableObject extends Renderable {
	
	public final int anInt1560;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean aBoolean1567;
	private final Graphic graphic;	
	private int anInt1569;
	private int anInt1570;

	public AnimableObject(int i, int j, int l, int i1, int j1, int k1, int l1) {
		aBoolean1567 = false;
		graphic = Graphic.cache[i1];
		anInt1560 = i;
		anInt1561 = l1;
		anInt1562 = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		aBoolean1567 = false;
	}

	public Model getRotatedModel() {
		Model model = graphic.getModel();
		if(model == null) {
			return null;
		}
		int j = graphic.animationSequence.primaryFrames[anInt1569];
		Model model_1 = new Model(true, Frame.noAnimationInProgress(j), false, model);
		if(!aBoolean1567) {
			model_1.skin();
			model_1.apply(j);
			model_1.faceGroups = null;
			model_1.vertexGroups = null;
		}
		if(graphic.resizeXY != 128 || graphic.resizeZ != 128) {
			model_1.scale(graphic.resizeXY, graphic.resizeXY, graphic.resizeZ);
		}
		if(graphic.rotation != 0) {
			if(graphic.rotation == 90) {
				model_1.rotate90Degrees();
			}
			if(graphic.rotation == 180) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
			if(graphic.rotation == 270) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
		}
		model_1.light(64 + graphic.modelBrightness, 850 + graphic.modelShadow, -30, -50, -30, true);
		return model_1;
	}

	public void method454(int i) {
		for(anInt1570 += i; anInt1570 > graphic.animationSequence.duration(anInt1569);) {
			anInt1570 -= graphic.animationSequence.duration(anInt1569) + 1;
			anInt1569++;
			if(anInt1569 >= graphic.animationSequence.frameCount && (anInt1569 < 0 || anInt1569 >= graphic.animationSequence.frameCount)) {
				anInt1569 = 0;
				aBoolean1567 = true;
			}
		}
	}
}