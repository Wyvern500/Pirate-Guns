package com.jg.jgpg.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PlayerCapability {

	public static final Capability<IPlayerCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
	
	public PlayerCapability() {
		
	}
	
}
