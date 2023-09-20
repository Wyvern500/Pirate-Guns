package com.jg.jgpg.client.handlers;

import java.util.ArrayList;
import java.util.List;

import com.jg.jgpg.client.animations.Animation;
import com.jg.jgpg.client.animations.Keyframe;
import com.jg.jgpg.client.animations.KeyframePartData;
import com.jg.jgpg.client.model.JgModelPart;

public class AnimationDataHandler {

	Animation animation;
	List<KeyframePartData> data;
	
	public AnimationDataHandler() {
		data = new ArrayList<>();
	}
	
	public void setup() {
		data.clear();
		if(animation != null) {
			List<JgModelPart> partsWithTransforms = new ArrayList<>();
			for(Keyframe kf : animation.getKeyframes()) {
				for(JgModelPart part : kf.getTraslations().keySet()) {
					if(!partsWithTransforms.contains(part)) {
						partsWithTransforms.add(part);
					}
				}
				for(JgModelPart part : kf.getRotations().keySet()) {
					if(!partsWithTransforms.contains(part)) {
						partsWithTransforms.add(part);
					}
				}
				for(JgModelPart part : partsWithTransforms) {
					data.add(new KeyframePartData(this, animation, kf, part));
				}
			}
		}
 	}
	
	public void setAnimation(Animation anim) {
		this.animation = anim;
		setup();
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public List<KeyframePartData> getKeyframesPartData() {
		return data;
	}
	
}
