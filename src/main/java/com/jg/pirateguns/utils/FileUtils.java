package com.jg.pirateguns.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.serializers.AnimationSerializer;
import com.mojang.logging.LogUtils;

public class FileUtils {

	public static File locateAnimation(String path) {
		String completePath = "animations/" + path;
		if(!new File(completePath).exists()) {
			String[] sPath = completePath.split("/");
			String dirPath = "";
			for(int i = 0; i < sPath.length-1; i++) {
				dirPath += sPath[i] + "/";
			}
			File f = new File(dirPath + sPath[sPath.length-1]);
			File dirs = new File(dirPath);
			if(dirs.mkdirs()) {
				System.out.println("All Directory Created");
			}
			if(!f.exists()) {
				try {
					f.createNewFile();
					System.out.println("New file created");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("File already exists");
			}
			try {
				BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
				String defaultAnim = "animation>start\r\n"
						+ "animation>dur=4\n"
						+ "animation>gunModelItem=empty\n"
						+ "animation>name=\"Empty\n"
						+ "animation>current=1\n"
						+ "animation>prog=0.0\n"
						+ "animation>end " 
						+ "keyframe>start\n"
						+ "keyframe>dur=4\n"
						+ "keyframe>startTick=0\n"
						+ "keyframe>end";
				bfw.write(defaultAnim);
				bfw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return f;
		} else {
			return new File(completePath);
		}
	}
	
	public static String readFile(String animation) {
		String all = "";
		String line = "";
		try {
			BufferedReader bfr = new BufferedReader(new FileReader(
					locateAnimation(animation)));
			while((line = bfr.readLine()) != null) {
				all += line + "\n";
			}
			bfr.close();
			return all;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void writeFile(String animation, String text) {
		File f = locateAnimation(animation);
		if(!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
			bfw.write(text);
			bfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeFile(String animation, Animation anim) {
		File f = locateAnimation(animation);
		LogUtils.getLogger().info("Writing to file");
		try {
			BufferedWriter bfw = new BufferedWriter(new FileWriter(f));
			bfw.write(AnimationSerializer.serialize(anim));
			LogUtils.getLogger().info("Writing to file");
			bfw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
