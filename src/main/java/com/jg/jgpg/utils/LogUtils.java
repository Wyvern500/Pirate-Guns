package com.jg.jgpg.utils;

public class LogUtils {

	public static void log(String emisor, String msg) {
		com.mojang.logging.LogUtils.getLogger().info(emisor + ": " + msg);
	}
	
}
