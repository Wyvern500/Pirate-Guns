package com.jg.jgpg.client.animation;

import com.jg.jgpg.client.handlers.EasingHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.utils.MathUtils;
import com.jg.jgpg.utils.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelPartKeyframesHandler {

	List<Keyframe> translations;
	List<Keyframe> rotations;
	DataHolder data;
	JgModelPart part;
	
	public ModelPartKeyframesHandler(JgModelPart part) {
		translations = new ArrayList<>();
		rotations = new ArrayList<>();
		data = new DataHolder();
		this.part = part;
	}
	
	// Misc
	
	public void tick(float tick, float oldTick) {
		if(data.getTranslationIndex() < translations.size()) {
			tryToPickTranslationKeyframes(tick, oldTick);
			lerpTranslations(tick);
		}
		if(data.getRotationIndex() < rotations.size()) {
			tryToPickRotationKeyframes(tick, oldTick);
			lerpRotations(tick);
		}
	}

	public void update(float tick, float oldTick) {
		// Translations
		if(!translations.isEmpty()) {
			for(int i = 0; i < translations.size(); i++) {
				Keyframe kf = translations.get(i);
				if(kf.getTick() >= tick) {
					// Is the keyframe 0
					if(i == 0) {
						// KfTick is different of 0, so we need to create the 0 keyframe
						if(kf.getTick() != 0) {
							Keyframe zeroKf = kf.copy().setTick(0).toZero();
							zeroKf.setRepetitive(false);
							translations.add(zeroKf);
							data.setTrCurrentAndPrev(kf, zeroKf);
							data.setTranslationIndex(translations.indexOf(kf));
							/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
									+ translations.indexOf(kf));*/
							break;
						} else if(i + 1 < translations.size()){
							// KfTick is 0 and there is a next keyframe so we should pick the next keyframe
							Keyframe nextKf = translations.get(i + 1);
							data.setTrCurrentAndPrev(nextKf, kf);
							data.setTranslationIndex(translations.indexOf(nextKf));
							/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
									+ translations.indexOf(kf));*/
							break;
						}
					} else {
						// Normal process
						Keyframe prevKf = translations.get(i - 1);
						data.setTrCurrentAndPrev(kf, prevKf);
						data.setTranslationIndex(translations.indexOf(kf));
						/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
								+ translations.indexOf(kf));*/
						break;
					}
				}
			}
		}
		
		// Rotations
		if(!rotations.isEmpty()) {
			for(int i = 0; i < rotations.size(); i++) {
				Keyframe kf = rotations.get(i);
				if(kf.getTick() >= tick) {
					// Is the keyframe 0
					if(i == 0) {
						// KfTick is different of 0, so we need to create the 0 keyframe
						if(kf.getTick() != 0) {
							Keyframe zeroKf = kf.copy().setTick(0).toZero();
							zeroKf.setRepetitive(false);
							rotations.add(zeroKf);
							data.setRtCurrentAndPrev(kf, zeroKf);
							data.setRotationIndex(rotations.indexOf(kf));
							/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
									+ rotations.indexOf(kf));*/
							break;
						} else if(i + 1 < rotations.size()){
							// KfTick is 0 and there is a next keyframe so we should pick the next keyframe
							Keyframe nextKf = rotations.get(i + 1);
							data.setRtCurrentAndPrev(nextKf, kf);
							data.setRotationIndex(rotations.indexOf(nextKf));
							/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
									+ rotations.indexOf(kf));*/
							break;
						}
					} else {
						// Normal process
						Keyframe prevKf = rotations.get(i - 1);
						data.setRtCurrentAndPrev(kf, prevKf);
						data.setRotationIndex(rotations.indexOf(kf));
						/*LogUtils.log("MPKH", "KfTick: " + kf.getTick()+  " tick: " + tick + " index: "
								+ rotations.indexOf(kf));*/
						break;
					}
				}
			}
		}
		sortTranslations();
		sortRotations();
	}
	
	// Model part transform resolvers
	
	public void lerp(float tick) {
		if(data.getCurrentTranslation() != null && data.getPrevTranslation() != null && 
				data.getTranslationIndex() < translations.size()) {
			float tp = (tick - data.getPrevTranslation().getTick()) / (data.getCurrentTranslation().getTick()
					- data.getPrevTranslation().getTick());
			float x = MathUtils.lerp(data.getPrevTranslation().getVec().x, data.getCurrentTranslation()
					.getVec().x, tp);
			float y = MathUtils.lerp(data.getPrevTranslation().getVec().y, data.getCurrentTranslation()
					.getVec().y, tp);
			float z = MathUtils.lerp(data.getPrevTranslation().getVec().z, data.getCurrentTranslation()
					.getVec().z, tp);
			part.getTransform().setTranslation(x, y, z);
			/*LogUtils.log("MPKH", "T Part: " + part.getName() + " Progress: " + tp + " tick: " + tick + 
					" current " + data.getCurrentTranslation().getTick());*/
			/*LogUtils.log("MPKH", "T Progress: " + tp + " tick: " + tick + " tick - prev: " + 
					(tick - data.getPrevTranslation().getTick()) + " current - tick: " + 
					(data.getCurrentTranslation().getTick() - tick));*/
		}
		if(data.getCurrentRotation() != null && data.getPrevRotation() != null &&
				data.getRotationIndex() < rotations.size()) {
			float rp = (tick - data.getPrevRotation().getTick()) / (data.getCurrentRotation().getTick()
					- data.getPrevRotation().getTick());
			float rx = MathUtils.lerp(data.getPrevRotation().getVec().x, data.getCurrentRotation()
					.getVec().x, rp);
			float ry = MathUtils.lerp(data.getPrevRotation().getVec().y, data.getCurrentRotation()
					.getVec().y, rp);
			float rz = MathUtils.lerp(data.getPrevRotation().getVec().z, data.getCurrentRotation()
					.getVec().z, rp);
			part.getTransform().setRotation(rx, ry, rz);
			/*LogUtils.log("MPKH", "R Part: " + part.getName() + " Progress: " + rp + " tick: " + tick + 
					" current " + data.getCurrentRotation().getTick());*/
			/*LogUtils.log("MPKH", "R Progress: " + rp + " tick: " + tick + " tick - prev: " + 
					(tick - data.getPrevRotation().getTick()) + " current - tick: " + 
					(data.getCurrentRotation().getTick() - tick));*/
		}
	}
	
	public void lerpTranslations(float tick) {
		if(data.getCurrentTranslation() != null && data.getPrevTranslation() != null && 
				data.getTranslationIndex() < translations.size()) {
			float tp = (tick - data.getPrevTranslation().getTick()) / (data.getCurrentTranslation().getTick()
					- data.getPrevTranslation().getTick());
			tp = EasingHandler.INSTANCE.getEasing(data.getCurrentTranslation().getEasing()).get(tp);
			float x = MathUtils.lerp(data.getPrevTranslation().getVec().x, data.getCurrentTranslation()
					.getVec().x, tp);
			float y = MathUtils.lerp(data.getPrevTranslation().getVec().y, data.getCurrentTranslation()
					.getVec().y, tp);
			float z = MathUtils.lerp(data.getPrevTranslation().getVec().z, data.getCurrentTranslation()
					.getVec().z, tp);
			part.getTransform().setTranslation(x, y, z);
			/*LogUtils.log("MPKH", "T Part: " + part.getName() + " Progress: " + tp + " tick: " + tick + 
					" current " + data.getCurrentTranslation().getTick() + " part: " + Arrays.toString(part
							.getCombined().pos));*/
		}
	}
	
	public void lerpRotations(float tick) {
		if(data.getCurrentRotation() != null && data.getPrevRotation() != null &&
				data.getRotationIndex() < rotations.size()) {
			float rp = (tick - data.getPrevRotation().getTick()) / (data.getCurrentRotation().getTick()
					- data.getPrevRotation().getTick());
			rp = EasingHandler.INSTANCE.getEasing(data.getCurrentRotation().getEasing()).get(rp);
			float rx = MathUtils.lerp(data.getPrevRotation().getVec().x, data.getCurrentRotation()
					.getVec().x, rp);
			float ry = MathUtils.lerp(data.getPrevRotation().getVec().y, data.getCurrentRotation()
					.getVec().y, rp);
			float rz = MathUtils.lerp(data.getPrevRotation().getVec().z, data.getCurrentRotation()
					.getVec().z, rp);
			part.getTransform().setRotation(rx, ry, rz);
			/*LogUtils.log("MPKH", "R Part: " + part.getName() + " Progress: " + rp + " tick: " + tick + 
					" current " + data.getCurrentRotation().getTick() + " part: " + Arrays.toString(part
							.getCombined().rot));*/
		}
	}
	
	// Data
	
	public void resetData() {
		data.setCurrentTranslation(null);
		data.setCurrentRotation(null);
		data.setPrevTranslation(null);
		data.setPrevRotation(null);
		data.setTranslationIndex(0);
		data.setRotationIndex(0);
	}
	
	// Keyframes

	public void sortTranslations() {
		Collections.sort(translations, (i1, i2) -> Integer.compare(i1.getTick(), i2.getTick()));
	}
	
	public void sortRotations() {
		Collections.sort(rotations, (i1, i2) -> Integer.compare(i1.getTick(), i2.getTick()));
	}
	
	public void addRepetitiveKeyframe(int tick, String easing, boolean translate, Vector3 vec, 
			boolean isRepetitive) {
		Keyframe kf = new Keyframe(tick, easing, vec, translate, isRepetitive); 
		if (translate) {
			translations.add(kf);
		} else {
			rotations.add(kf);
		}
	}

	public void addRepetitiveKeyframe(int tick, String easing, boolean translate, boolean isRepetitive) {
		Transform t = part.getTransform();
		Keyframe kf = new Keyframe(tick, easing, new Vector3(t.pos), translate, isRepetitive);
		if (translate) {
			translations.add(kf);
		} else {
			rotations.add(kf);
		}
	}
	
	public void addKeyframe(int tick, String easing, boolean translate, Vector3 vec) {
		Keyframe kf = new Keyframe(tick, easing, vec, translate); 
		if (translate) {
			translations.add(kf);
		} else {
			rotations.add(kf);
		}
	}

	public Keyframe addKeyframe(int tick, String easing, boolean translate) {
		Transform t = part.getTransform();
		float[] data = translate ? t.pos : t.rot;
		Keyframe kf = new Keyframe(tick, easing, new Vector3(data), translate);
		if (translate) {
			translations.add(kf);
		} else {
			rotations.add(kf);
		}
		return kf;
	}
	
	public void addKeyframe(Keyframe kf, boolean translate) {
		if (translate) {
			translations.add(kf);
		} else {
			rotations.add(kf);
		}
	}
	
	public void tryToPickTranslationKeyframes(float tick, float oldTick) {
		// Translation
		//LogUtils.log("MPKH", "One");
        if(data.getCurrentTranslation() != null) {
        	//LogUtils.log("MPKH", "Two");
        	if(tick - oldTick > 0) {
	            if(tick > data.getCurrentTranslation().getTick()) {
	            	//LogUtils.log("MPKH", "Three");
	                pickNextTrKeyframes(data.getCurrentTranslation());
	            }
        	} else {
        		/*LogUtils.log("MPKH", "Backward: " + translations.indexOf(data.getCurrentTranslation())
        			+ " tick: " + data.getCurrentTranslation().getTick() + " prev: " + 
        			translations.indexOf(data.getPrevTranslation()) + " prevTick: " + data
        			.getPrevTranslation().getTick());*/
        	}
        } else {
        	// Keyframes are null
        	int size = translations.size();
        	if(!translations.isEmpty()) {
        		int index = data.getTranslationIndex();
        		Keyframe firstKf = translations.get(index);
        		if(firstKf.getTick() != 0) {
        			Keyframe zeroKf = firstKf.copy().setTick(0).toZero();
        			data.setTrCurrentAndPrev(firstKf, zeroKf);
        			//LogUtils.log("MPKH", "ZeroKf current: " + firstKf.getTick());
        		} else if(index + 1 < size){
        			data.setTrCurrentAndPrev(translations.get(data.growTrIndex()), firstKf);
        			//LogUtils.log("MPKH", "Not ZeroKf");
        		} else {
        			// The limit has been reached
        		}
        	} else {
        		// Translations is empty
        	}
        }
	}
	
	public void tryToPickRotationKeyframes(float tick, float oldTick) {
		// Rotation
        if(data.getCurrentRotation() != null) {
            if(tick > data.getCurrentRotation().getTick()) {
                pickNextRtKeyframes(data.getCurrentRotation());
            }
        } else {
        	// Keyframes are null
        	int size = rotations.size();
        	if(!rotations.isEmpty()) {
        		int index = data.getRotationIndex();
        		Keyframe firstKf = rotations.get(index);
        		if(firstKf.getTick() != 0) {
        			Keyframe zeroKf = firstKf.copy().setTick(0).toZero();
        			data.setRtCurrentAndPrev(firstKf, zeroKf);
        		} else if(index + 1 < size){
        			data.setRtCurrentAndPrev(rotations.get(data.growRtIndex()), firstKf);
        		} else {
        			// The limit has been reached
        		}
        	} else {
        		// Translations is empty
        	}
        }
	}
	
	public void pickNextTrKeyframes(Keyframe kf) {
		int index = data.getTranslationIndex();
		if(index + 1 < translations.size()) {
			data.setTrCurrentAndPrev(translations.get(index + 1), kf);
			data.growTrIndex();
		} else {
			data.growTrIndex();
		}
	}
	
	public void pickNextRtKeyframes(Keyframe kf) {
		int index = data.getRotationIndex();
		if(index + 1 < rotations.size()) {
			data.setRtCurrentAndPrev(rotations.get(index + 1), kf);
			data.growRtIndex();
		} else {
			data.growRtIndex();
		}
	}
	
	public List<Keyframe> getTranslations() {
		return translations;
	}

	public List<Keyframe> getRotations() {
		return rotations;
	}

	public JgModelPart getPart() {
		return part;
	}

	public DataHolder getData() {
		return data;
	}

	public class DataHolder {
		
		Keyframe currentTr;
		Keyframe currentRt;
		Keyframe prevTr;
		Keyframe prevRt;
		int trIndex;
		int rtIndex;
		
		public DataHolder() {
			
		}

		public void setTrCurrentAndPrev(Keyframe current, Keyframe prev) {
			this.currentTr = current;
			this.prevTr = prev;
		}
		
		public void setRtCurrentAndPrev(Keyframe current, Keyframe prev) {
			this.currentRt = current;
			this.prevRt = prev;
		}
		
		public Keyframe getCurrentTranslation() {
			return currentTr;
		}

		public void setCurrentTranslation(Keyframe currentTr) {
			this.currentTr = currentTr;
		}

		public Keyframe getCurrentRotation() {
			return currentRt;
		}

		public void setCurrentRotation(Keyframe currentRt) {
			this.currentRt = currentRt;
		}

		public Keyframe getPrevTranslation() {
			return prevTr;
		}

		public void setPrevTranslation(Keyframe prevTr) {
			this.prevTr = prevTr;
		}

		public Keyframe getPrevRotation() {
			return prevRt;
		}

		public void setPrevRotation(Keyframe prevRt) {
			this.prevRt = prevRt;
		}

		public int getTranslationIndex() {
			return trIndex;
		}

		public void setTranslationIndex(int trIndex) {
			this.trIndex = trIndex;
		}
		
		public int growTrIndex() {
			trIndex++;
			return trIndex;
		}

		public int getRotationIndex() {
			return rtIndex;
		}

		public void setRotationIndex(int rtIndex) {
			this.rtIndex = rtIndex;
		}
		
		public int growRtIndex() {
			rtIndex++;
			return rtIndex;
		}
		
	}
	
}
