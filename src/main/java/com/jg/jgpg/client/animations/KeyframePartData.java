package com.jg.jgpg.client.animations;

import com.jg.jgpg.client.handlers.AnimationDataHandler;
import com.jg.jgpg.client.model.JgModelPart;

public class KeyframePartData {

	AnimationDataHandler parent;
	Animation animation;
	Keyframe kf;
	JgModelPart part;
	TransformData posData;
	TransformData rotData;
	
	public KeyframePartData(AnimationDataHandler parent, Animation animation, Keyframe kf, 
			JgModelPart part) {
		this.parent = parent;
		this.animation = animation;
		this.kf = kf;
		this.part = part;
		this.posData = new TransformData(this, animation, kf, false, getPosVal(), getPosEasing());
		this.rotData = new TransformData(this, animation, kf, true, getRotVal(), getRotEasing());
	}
	
	private float[] getPosVal() {
		float[] val = null;
		KeyframeTransformData data =  kf.getTraslations().get(part);
		if(data != null) {
			val = data.getVal();
		}
		return val;
	}
	
	private float[] getRotVal() {
		float[] val = null;
		KeyframeTransformData data =  kf.getRotations().get(part);
		if(data != null) {
			val = data.getVal();
		}
		return val;
	}
	
	private void setPos(float[] pos) {
		kf.getTraslations().get(part).setVal(pos);
	}
	
	private void setRot(float[] rot) {
		kf.getRotations().get(part).setVal(rot);
	}
	
	private void setPosEasing(String easing) {
		kf.getTraslations().get(part).setEasing(easing);
	}
	
	private void setRotEasing(String easing) {
		kf.getRotations().get(part).setEasing(easing);
	}
	
	private String getPosEasing() {
		String easing = null;
		KeyframeTransformData data =  kf.getTraslations().get(part);
		if(data != null) {
			easing = data.getEasing();
		}
		return easing;
	}
	
	private String getRotEasing() {
		String easing = null;
		KeyframeTransformData data =  kf.getRotations().get(part);
		if(data != null) {
			easing = data.getEasing();
		}
		return easing;
	}
	
	public void update() {
		parent.setup();
	}

	public Animation getAnimation() {
		return animation;
	}

	public Keyframe getKeyframe() {
		return kf;
	}

	public JgModelPart getPart() {
		return part;
	}

	public TransformData getPosData() {
		return posData;
	}

	public TransformData getRotData() {
		return rotData;
	}
	
	public static class TransformData {

		KeyframePartData parent;
		Animation animation;
		Keyframe kf;
		boolean isRot;
		float[] val;
		String easing;
		
		public TransformData(KeyframePartData parent, Animation animation, Keyframe kf, boolean isRot, 
				float[] val, String easing) {
			this.parent = parent;
			this.animation = animation;
			this.kf = kf;
			this.isRot = isRot;
			this.val = val;
			this.easing = easing;
		}

		public Animation getAnimation() {
			return animation;
		}

		public Keyframe getKf() {
			return kf;
		}
		
		public void setValue(float[] value) {
			this.val = value;
			
			if(!isRot) {
				this.parent.setPos(value);
			} else {
				this.parent.setRot(value);
			}
		}

		public float[] getValue() {
			return val;
		}
		
		public String getEasing() {
			if(!isRot) {
				return this.parent.getPosEasing();
			}
			return this.parent.getRotEasing();
		}
		
		public void setEasing(String easing) {
			this.easing = easing;
			
			if(!isRot) {
				this.parent.setPosEasing(easing);
			} else {
				this.parent.setRotEasing(easing);
			}
		}
		
		public boolean isRot() {
			return isRot;
		}
		
		public KeyframePartData getParent() {
			return parent;
		}
		
		public void remove() {
			if(isRot) {
				kf.getRotations().remove(parent.getPart());
			} else {
				kf.getTraslations().remove(parent.getPart());
			}
			checkIfKfNeedsToBeCleaned();
		}
		
		public boolean isEmpty() {
			float[] toCompare = isRot ? parent.getRotVal() : parent.getPosVal();
			return toCompare == null;
		}
		
		private void checkIfKfNeedsToBeCleaned() {
			if(kf.getTraslations().isEmpty() && kf.getRotations().isEmpty()) {
				animation.removeKeyframe(kf);
			}
 		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof TransformData) {
				TransformData other = (TransformData) obj;
				return kf.getTick() == other.getKf().getTick() && parent.getPart() == other.getParent()
						.getPart() && isRot == other.isRot;
			}
			return super.equals(obj);
		}
		
		@Override
		public String toString() {
			return "Anim: " + animation.getName() + " kfTick: " + kf.getTick() + " part: " + 
					parent.getPart().getName() + " isRot: " + isRot;
		}
		
	}
	
}
