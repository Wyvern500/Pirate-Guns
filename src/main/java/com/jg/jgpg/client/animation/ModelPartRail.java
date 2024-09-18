package com.jg.jgpg.client.animation;

public class ModelPartRail {

	ModelPartKeyframesHandler handler;
	boolean translate;
	
	public ModelPartRail(ModelPartKeyframesHandler handler, boolean translate) {
		this.handler = handler;
		this.translate = translate;
	}

	public ModelPartKeyframesHandler getHandler() {
		return handler;
	}

	public void setHandler(ModelPartKeyframesHandler handler) {
		this.handler = handler;
	}

	public boolean isTranslate() {
		return translate;
	}

	public void setTranslate(boolean translate) {
		this.translate = translate;
	}
	
}
