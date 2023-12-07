package com.jg.jgpg.config;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;

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
		public final ForgeConfigSpec.DoubleValue prMeleeHitDamage;
		public final ForgeConfigSpec.IntValue prCooldown;
		
		public final ForgeConfigSpec.IntValue prGunpowderToReload;
		
		// Pirate Pistol
		public final ForgeConfigSpec.DoubleValue ppDamage;
		public final ForgeConfigSpec.IntValue ppPower;
		public final ForgeConfigSpec.DoubleValue ppInaccuracy;
		public final ForgeConfigSpec.IntValue ppRange;
		public final ForgeConfigSpec.DoubleValue ppRangeDamageReduction;
		public final ForgeConfigSpec.DoubleValue ppMeleeHitDamage;
		public final ForgeConfigSpec.IntValue ppCooldown;
		
		public final ForgeConfigSpec.IntValue ppGunpowderToReload;
		
		// Pirate Pistol
		public final ForgeConfigSpec.DoubleValue prvDamage;
		public final ForgeConfigSpec.IntValue prvPower;
		public final ForgeConfigSpec.DoubleValue prvInaccuracy;
		public final ForgeConfigSpec.IntValue prvRange;
		public final ForgeConfigSpec.DoubleValue prvRangeDamageReduction;
		public final ForgeConfigSpec.DoubleValue prvMeleeHitDamage;
		public final ForgeConfigSpec.IntValue prvCooldown;
		
		public final ForgeConfigSpec.IntValue prvGunpowderToReload;
		
		// Trabuco
		public final ForgeConfigSpec.DoubleValue tDamage;
		public final ForgeConfigSpec.IntValue tPower;
		public final ForgeConfigSpec.DoubleValue tInaccuracy;
		public final ForgeConfigSpec.IntValue tRange;
		public final ForgeConfigSpec.DoubleValue tRangeDamageReduction;
		public final ForgeConfigSpec.IntValue tBulletsPerShoot;
		public final ForgeConfigSpec.DoubleValue tMeleeHitDamage;
		public final ForgeConfigSpec.IntValue tCooldown;
		
		public final ForgeConfigSpec.IntValue tGunpowderToReload;
		
		// Crafting stuff
		
		public final ForgeConfigSpec.IntValue musketBulletCraftingResult;
		public final ForgeConfigSpec.IntValue musketBulletMetal;
		public final ForgeConfigSpec.IntValue trabucoBulletCraftingResult;
		public final ForgeConfigSpec.IntValue trabucoBulletMetal;
		public final ForgeConfigSpec.IntValue ppMetal;
		public final ForgeConfigSpec.IntValue ppWood;
		public final ForgeConfigSpec.IntValue prMetal;
		public final ForgeConfigSpec.IntValue prWood;
		public final ForgeConfigSpec.IntValue tMetal;
		public final ForgeConfigSpec.IntValue tWood;
		
		public final ForgeConfigSpec.BooleanValue musketBulletCraft;
		public final ForgeConfigSpec.BooleanValue trabucoBulletCraft;
		public final ForgeConfigSpec.BooleanValue ppCraft;
		public final ForgeConfigSpec.BooleanValue prCraft;
		public final ForgeConfigSpec.BooleanValue tCraft;
		
		public Server(ForgeConfigSpec.Builder builder) {
			
			builder.push("Guns");
			// Pirate Rifle
			builder.push("Pirate Rifle");
			prDamage = builder.comment("Damage").defineInRange("Pirate Rifle Damage", 18f, 0f, 
					1000f);
			prPower = builder.comment("Shoot Power").defineInRange("Pirate Rifle Power", 16, 0, 1000);
			prInaccuracy = builder.comment("Inaccuracy").defineInRange("Pirate Rifle Inaccuracy", 0.1f, 0f, 
					1000f);
			prRange = builder.comment("Range - If this value is reached, bullet will be gradually losing damage")
					.defineInRange("Pirate Rifle Range", 6, 0, 1000);
			prRangeDamageReduction = builder.comment("Range Damage Reduction - The closer to 1, the less "
					+ "damage you will lose - This value will multiply "
					+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
					+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Pirate Rifle Range DamageReduction", 0.93f, 0f, 
					1000f);
			prMeleeHitDamage = builder.comment("Melee Damage").defineInRange("Pirate Rifle Melee Damage", 6f, 0f, 
					1000f);
			prCooldown = builder.comment("Shoot Cooldown").defineInRange("Pirate Rifle Shoot Cooldown", 10, 0, 
					1000);
			prGunpowderToReload = builder.comment("Gunpowder to reload")
					.defineInRange("Pirate Rifle Gunpowder needed for reload", 1, 0, 100);
			builder.pop();
			// Pirate Pistol
			builder.push("Pirate Pistol");
			ppDamage = builder.comment("Damage").defineInRange("Pirate Pistol Damage", 12f, 0f, 1000f);
			ppPower = builder.comment("Shoot Power").defineInRange("Pirate Pistol Power", 14, 0, 1000);
			ppInaccuracy = builder.comment("Inaccuracy").defineInRange("Pirate Pistol Inaccuracy", 0.3f, 0f, 
					1000f);
			ppRange = builder.comment("Range - If this value is reached, bullet will be gradually losing "
					+ "damage")
					.defineInRange("Pirate Pistol Range", 4, 0, 1000);
			ppRangeDamageReduction = builder
					.comment("Range Damage Reduction - The closer to 1, the less "
							+ "damage you will lose - This value will multiply "
							+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
							+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Pirate Pistol Range DamageReduction", 0.7f, 0f, 1000f);
			ppMeleeHitDamage = builder.comment("Melee Damage").defineInRange("Pirate Pistol Melee Damage", 3f, 0f, 
					1000f);
			ppCooldown = builder.comment("Shoot Cooldown").defineInRange("Pirate Pistol Shoot Cooldown", 8, 0, 
					1000);
			ppGunpowderToReload = builder.comment("Gunpowder to reload")
					.defineInRange("Pirate Pistol Gunpowder needed for reload", 1, 0, 100);
			builder.pop();
			// Primitive Revolver
			builder.push("Primitive Revolver");
			prvDamage = builder.comment("Damage").defineInRange("Primitive Revolver Damage", 12f, 0f, 1000f);
			prvPower = builder.comment("Shoot Power").defineInRange("Primitive Revolver Power", 14, 0, 1000);
			prvInaccuracy = builder.comment("Inaccuracy").defineInRange("Primitive Revolver Inaccuracy", 0.3f, 0f, 
					1000f);
			prvRange = builder.comment("Range - If this value is reached, bullet will be gradually losing "
					+ "damage")
					.defineInRange("Primitive Revolver Range", 4, 0, 1000);
			prvRangeDamageReduction = builder
					.comment("Range Damage Reduction - The closer to 1, the less "
					+ "damage you will lose - This value will multiply "
					+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
					+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Primitive Revolver Range DamageReduction", 0.7f, 0f, 1000f);
			prvMeleeHitDamage = builder.comment("Melee Damage").defineInRange("Primitive Revolver Melee Damage", 3f, 0f, 
					1000f);
			prvCooldown = builder.comment("Shoot Cooldown").defineInRange("Primitive Revolver Shoot Cooldown", 8, 0, 
					1000);
			prvGunpowderToReload = builder.comment("Gunpowder to reload")
					.defineInRange("Primitive Revolver Gunpowder needed for reload", 1, 0, 100);
			builder.pop();
			// Trabuco
			builder.push("Trabuco");
			tDamage = builder.comment("Damage").defineInRange("Trabuco Damage", 3f, 0f, 1000f);
			tPower = builder.comment("Shoot Power").defineInRange("Trabuco Power", 16, 0, 1000);
			tInaccuracy = builder.comment("Inaccuracy").defineInRange("Trabuco Inaccuracy", 1.5f, 0f, 1000f);
			tRange = builder.comment("Range - If this value is reached, bullet will be gradually losing " 
					+ "damage")
					.defineInRange("Trabuco Range", 2, 0, 1000);
			tRangeDamageReduction = builder
					.comment("Range Damage Reduction - The closer to 1, the less "
							+ "damage you will lose - This value will multiply "
							+ "gunDmg when bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * "
							+ "0.9 = 3.6 <- this will happen every time the range is reached")
					.defineInRange("Trabuco Range DamageReduction", 0.3f, 0f, 1000f);
			tBulletsPerShoot = builder.comment("BulletsPerShoot - How much bullets would you like trabuco to shoot, this will also define how much bullets you will need to reload")
					.defineInRange("Trabuco Bullets Per Shoot", 8, 1, 1000);
			tMeleeHitDamage = builder.comment("Melee Damage").defineInRange("Trabuco Melee Damage", 7f, 0f, 
					1000f);
			tCooldown = builder.comment("Shoot Cooldown").defineInRange("Trabuco Shoot Cooldown", 16, 0, 
					1000);
			tGunpowderToReload = builder.comment("Gunpowder to reload")
					.defineInRange("Trabuco Gunpowder needed for reload", 2, 0, 100);
			builder.pop();
			builder.pop();
			
			builder.push("Crafting");
			
			musketBulletCraft = builder.comment("Would you like to be able to craft musketBullet?")
					.define("Can Craft Musket Bullet?", true);
			trabucoBulletCraft = builder.comment("Would you like to be able to craft trabucoBullet?")
					.define("Can Craft Trabuco Bullet?", true);
			ppCraft = builder.comment("Would you like to be able to craft Pirate Pistol?")
					.define("Can Craft Pirate Pistol?", true);
			prCraft = builder.comment("Would you like to be able to craft Pirate Rifle?")
					.define("Can Craft Pirate Rifle?", true);
			tCraft = builder.comment("Would you like to be able to craft Trabuco?")
					.define("Can Craft Trabuco?", true);
			
			musketBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft musket bullets").defineInRange("MusketBulletCraftingResult", 4, 1, 100);
			musketBulletMetal = builder.comment("How much metal would you like to use for crafting musketBullet")
					.defineInRange("Musket Bullet Metal", 1, 1, 100);
			trabucoBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft musket bullets").defineInRange("TrabucoBulletCraftingResult", 16, 1, 100);
			trabucoBulletMetal = builder.comment("How much metal would you like to use for crafting trabucoBullet")
					.defineInRange("Musket Bullet Metal", 1, 1, 100);
			ppMetal = builder.comment("How much metal would you like to use for crafting Pirate Pistol")
					.defineInRange("Pirate Pistol Metal", 4, 1, 100);
			ppWood = builder.comment("How much wood would you like to use for crafting Pirate Pistol")
					.defineInRange("Pirate Pistol Wood", 4, 1, 100);
			prMetal = builder.comment("How much metal would you like to use for crafting Pirate Rifle")
					.defineInRange("Pirate Rifle Metal", 8, 1, 100);
			prWood = builder.comment("How much wood would you like to use for crafting Pirate Rifle")
					.defineInRange("Pirate Rifle Wood", 8, 1, 100);
			tMetal = builder.comment("How much metal would you like to use for crafting Trabuco")
					.defineInRange("Trabuco Metal", 8, 1, 100);
			tWood = builder.comment("How much wood would you like to use for crafting Trabuco")
					.defineInRange("Trabuco Wood", 8, 1, 100);
			builder.pop();
		}
	}
	
	public static class Client {
		
		public final ForgeConfigSpec.IntValue hitmarkerTime;
		public final ForgeConfigSpec.BooleanValue debugMode;
		
		public Client(ForgeConfigSpec.Builder builder) {
			hitmarkerTime = builder.comment("Hitmarker duration")
					.defineInRange("Hitmarker Duration", 4, 0, 50);
			debugMode = builder.comment("Debug Mode - used for making animations for guns")
					.define("Debug Mode - Dont touch this", false);
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
