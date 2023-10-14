package com.jg.jgpg.client.model.loader;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.jg.jgpg.client.model.loader.model.JgSimpleModel;

import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class JgModelLoader implements IGeometryLoader<JgSimpleModel> {

	public static final JgModelLoader INSTANCE = new JgModelLoader();
	
	public JgModelLoader() {
		
	}
	
	@Override
	public JgSimpleModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
			throws JsonParseException {
		//LogUtils.log("JgModelLoader", "Reading: " + jsonObject.toString());
		return null;
	}

}
