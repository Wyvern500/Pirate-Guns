package com.jg.jgpg.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;

public class Config {

	public static final ForgeConfigSpec client_config;
	public static final Client CLIENT;

	public static final ForgeConfigSpec server_config;
	public static final Server SERVER;
	
	public static class Server {
		
		// Pirate Rifle
		public final ForgeConfigSpec.DoubleValue prDamage;
		public final ForgeConfigSpec.IntValue prPower;
		public final ForgeConfigSpec.DoubleValue prInaccuracy;
		public final ForgeConfigSpec.IntValue prRange;
		public final ForgeConfigSpec.DoubleValue prRangeDamageReduction;
		
		// Pirate Pistol
		public final ForgeConfigSpec.DoubleValue ppDamage;
		public final ForgeConfigSpec.IntValue ppPower;
		public final ForgeConfigSpec.DoubleValue ppInaccuracy;
		public final ForgeConfigSpec.IntValue ppRange;
		public final ForgeConfigSpec.DoubleValue ppRangeDamageReduction;

		// Trabuco
		public final ForgeConfigSpec.DoubleValue tDamage;
		public final ForgeConfigSpec.IntValue tPower;
		public final ForgeConfigSpec.DoubleValue tInaccuracy;
		public final ForgeConfigSpec.IntValue tRange;
		public final ForgeConfigSpec.DoubleValue tRangeDamageReduction;
		public final ForgeConfigSpec.IntValue tBulletsPerShoot;
		
		// Bullets
		public final ForgeConfigSpec.IntValue musketBulletCraftingResult;
		
		public Server(ForgeConfigSpec.Builder builder) {
			builder.push("Bullet");
			// Musket bullet
			musketBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft musket bullets").defineInRange("MuskerBulletCraftingResult", 4, 1, 100);
			builder.pop();
			
			builder.push("Guns");
			// Pirate Rifle
			builder.push("Pirate Rifle");
			prDamage = builder.comment("Damage").defineInRange("Pirate Rifle Damage", 14f, 0f, 
					1000f);
			prPower = builder.comment("Shoot Power").defineInRange("Pirate Rifle Power", 16, 0, 1000);
			prInaccuracy = builder.comment("Inaccuracy").defineInRange("Pirate Rifle Inaccuracy", 0.1f, 0f, 
					1000f);
			prRange = builder.comment("Range - If this value is reached, bullet will be gradually losing damage")
					.defineInRange("Pirate Rifle Range", 20, 0, 1000);
			prRangeDamageReduction = builder.comment("Range Damage Reduction - The closer to 1, the less "
					+ "damage you will lose - This value will multiply "
					+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
					+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Pirate Rifle Range DamageReduction", 0.93f, 0f, 
					1000f);
			builder.pop();
			// Pirate Pistol
			builder.push("Pirate Pistol");
			ppDamage = builder.comment("Damage").defineInRange("Pirate Pistol Damage", 8f, 0f, 1000f);
			ppPower = builder.comment("Shoot Power").defineInRange("Pirate Pistol Power", 14, 0, 1000);
			ppInaccuracy = builder.comment("Inaccuracy").defineInRange("Pirate Pistol Inaccuracy", 0.3f, 0f, 
					1000f);
			ppRange = builder.comment("Range - If this value is reached, bullet will be gradually losing "
					+ "damage")
					.defineInRange("Pirate Pistol Range", 14, 0, 1000);
			ppRangeDamageReduction = builder
					.comment("Range Damage Reduction - The closer to 1, the less "
							+ "damage you will lose - This value will multiply "
							+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
							+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Pirate Pistol Range DamageReduction", 0.7f, 0f, 1000f);
			builder.pop();
			// Trabuco
			builder.push("Trabuco");
			tDamage = builder.comment("Damage").defineInRange("Trabuco Damage", 3f, 0f, 1000f);
			tPower = builder.comment("Shoot Power").defineInRange("Trabuco Power", 16, 0, 1000);
			tInaccuracy = builder.comment("Inaccuracy").defineInRange("Trabuco Inaccuracy", 1.5f, 0f, 1000f);
			tRange = builder.comment("Range - If this value is reached, bullet will be gradually losing " 
					+ "damage")
					.defineInRange("Trabuco Range", 4, 0, 1000);
			tRangeDamageReduction = builder
					.comment("Range Damage Reduction - The closer to 1, the less "
							+ "damage you will lose - This value will multiply "
							+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
							+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Trabuco Range DamageReduction", 0.3f, 0f, 1000f);
			tBulletsPerShoot = builder.comment("BulletsPerShoot - How much bullets would you like trabuco to shoot")
					.defineInRange("Trabuco Range", 8, 1, 1000);
			builder.pop();
			builder.pop();
		}
	}
	
	public static class Client {
		public Client(ForgeConfigSpec.Builder builder) {
			
		}
	}
	
	static {
		final Pair<Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(Client::new);
		client_config = clientSpecPair.getRight();
		CLIENT = clientSpecPair.getLeft();

		final Pair<Server, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(Server::new);
		server_config = serverSpecPair.getRight();
		SERVER = serverSpecPair.getLeft();
	}

	public static void loadConfig(ForgeConfigSpec config, String path) {
		final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().autosave()
				.writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);

	}

	public static void saveClientConfig() {
		client_config.save();
	}
	
}
