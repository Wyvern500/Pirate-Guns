package com.jg.jgpg.client.animation;

public class KeyframeItem {

	private ModelPartKeyframesHandler handler;
	private Keyframe keyframe;
	private int index;
	private boolean translate;
	
	public KeyframeItem(ModelPartKeyframesHandler handler, Keyframe kf, int index, boolean translate) {
		this.handler = handler;
		this.keyframe = kf;
		this.index = index;
		this.translate = translate;
	}

	public KeyframeItem copy() {
		return new KeyframeItem(handler, keyframe.copy(), index, translate);
	}
	
	public ModelPartKeyframesHandler getHandler() {
		return handler;
	}

	public Keyframe getKeyframe() {
		return keyframe;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isTranslate() {
		return translate;
	}
	
}
