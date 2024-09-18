package com.jg.jgpg.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;

public class Config {

	public static final ForgeConfigSpec client_config;
	public static final Client CLIENT;

	public static final ForgeConfigSpec server_config;
	public static final Server SERVER;
	
	public static class Server {

		public final GunProperties pirateRifle;
		public final GunProperties piratePistol;
		public final GunProperties trabuco;
		public final GunProperties primitiveRevolver;
		public final GunProperties heavyHandCannon;
		public final MeleeProperties smallHammer;

		public final ForgeConfigSpec.ConfigValue<List<Integer>> smallHammerHitsRange;

		public final ForgeConfigSpec.IntValue musketBulletCraftingResult;
		public final ForgeConfigSpec.IntValue musketBulletMetal;
		public final ForgeConfigSpec.IntValue trabucoBulletCraftingResult;
		public final ForgeConfigSpec.IntValue trabucoBulletMetal;
		public final ForgeConfigSpec.IntValue heavyHandCannonBulletCraftingResult;
		public final ForgeConfigSpec.IntValue heavyHandCannonBulletMetal;
		public final ForgeConfigSpec.BooleanValue musketBulletCraft;
		public final ForgeConfigSpec.BooleanValue trabucoBulletCraft;

		public final ForgeConfigSpec.BooleanValue shouldPrvConsumeGunpowder;

		public Server(ForgeConfigSpec.Builder builder) {
			builder.push("Guns");

			pirateRifle = new GunProperties(builder, "PirateRifle")
					.openGunSection().setDamage(18).setPower(16).setInnacuracy(0.1f)
					.setRange(6).setRangeDamageReduction(0.93f).setMeleeDamage(4)
					.setShootTime(10).setRecoilTime(2).setRecoilWeight(2)
					.setHorizontalRecoil(0.3f).setVerticalRecoil(0.4f).setBulletsPerShot(1)
					.setGunpowerToReload(2).setBulletGravity(0.0f)
					.setAimingSensitivityReduction(0.3f).closeGunSection()
					.openGunCraftingSection().cancelCraft(false)
					.setMetal(8).setWood(8).closeGunCraftingSection();

			piratePistol = new GunProperties(builder, "PiratePistol")
					.openGunSection().setDamage(12).setPower(14).setInnacuracy(0.3f)
					.setRange(4).setRangeDamageReduction(0.7f).setMeleeDamage(3)
					.setShootTime(8).setRecoilTime(2).setRecoilWeight(2)
					.setHorizontalRecoil(0.2f).setVerticalRecoil(0.2f).setBulletsPerShot(1)
					.setGunpowerToReload(1).setBulletGravity(0.0f)
					.setAimingSensitivityReduction(0.7f).closeGunSection()
					.openGunCraftingSection().cancelCraft(false)
					.setMetal(4).setWood(4).closeGunCraftingSection();

			trabuco = new GunProperties(builder, "Trabuco")
					.openGunSection().setDamage(3).setPower(16).setInnacuracy(1.5f)
					.setRange(2).setRangeDamageReduction(0.3f).setMeleeDamage(7)
					.setShootTime(16).setRecoilTime(2).setRecoilWeight(2)
					.setHorizontalRecoil(0.6f).setVerticalRecoil(0.6f).setBulletsPerShot(8)
					.setGunpowerToReload(2).setBulletGravity(0.0f)
					.setAimingSensitivityReduction(0.7f).closeGunSection()
					.openGunCraftingSection().cancelCraft(false)
					.setMetal(10).setWood(4).closeGunCraftingSection();

			primitiveRevolver = new GunProperties(builder, "PrimitiveRevolver")
					.openGunSection().setDamage(11).setPower(14).setInnacuracy(0.25f)
					.setRange(5).setRangeDamageReduction(0.75f).setMeleeDamage(3)
					.setShootTime(8).setRecoilTime(2).setRecoilWeight(2)
					.setHorizontalRecoil(0.2f).setVerticalRecoil(0.24f)
					.setBulletsPerShot(1).setGunpowerToReload(1).setBulletGravity(0.0f)
					.setAimingSensitivityReduction(0.6f)
					.closeGunSection().openGunCraftingSection().cancelCraft(false)
					.setMetal(8).setWood(4).closeGunCraftingSection();

			heavyHandCannon = new GunProperties(builder, "HeavyHandCannon")
					.openGunSection().setDamage(20).setPower(8).setInnacuracy(0.6f)
					.setRange(8).setRangeDamageReduction(0.9f).setMeleeDamage(8)
					.setShootTime(20).setRecoilTime(4).setRecoilWeight(4)
					.setHorizontalRecoil(1f).setVerticalRecoil(1f).setBulletsPerShot(1)
					.setBulletGravity(0.05f).setAimingSensitivityReduction(0.7f)
					.openGunCraftingSection().cancelCraft(false)
					.setMetal(20).setWood(0).closeGunCraftingSection();

			builder.pop();

			builder.push("Melee Weapons");

			smallHammer = new MeleeProperties(builder, "SmallHammer")
					.openWeaponSection().setDamage(5).setRange(0).closeWeaponSection()
					.openWeaponCraftingSection().setMetal(8).setWood(5)
					.cancelCraft(false).closeWeaponCraftingSection();

			smallHammerHitsRange = builder.comment().define("SmallHammerHits",
					List.of(0, 5), obj -> {
				if (obj instanceof List<?> list) {
					try {
						int min = (int) list.get(0);
						int max = (int) list.get(1);
						return min >= 0 && max >= min;
					} catch (Exception e) {
						e.fillInStackTrace();
						return false;
					}
				}
				return false;
			});

			builder.pop();

			builder.push("Bullets");

			musketBulletCraft = builder.comment("Would you like to be able to craft musketBullet?")
					.define("Can Craft Musket Bullet?", true);
			trabucoBulletCraft = builder.comment("Would you like to be able to craft trabucoBullet?")
					.define("Can Craft Trabuco Bullet?", true);
			musketBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft musket bullets").defineInRange("MusketBulletCraftingResult", 4, 1, 100);
			musketBulletMetal = builder.comment("How much metal would you like to use for crafting musketBullet")
					.defineInRange("Musket Bullet Metal", 1, 1, 100);
			trabucoBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft musket bullets").defineInRange("TrabucoBulletCraftingResult", 16, 1, 100);
			trabucoBulletMetal = builder.comment("How much metal would you like to use for crafting trabucoBullet")
					.defineInRange("Musket Bullet Metal", 1, 1, 100);
			heavyHandCannonBulletCraftingResult = builder.comment("How many bullets would you like to get when you "
					+ "craft heavy hand cannon bullets").defineInRange("HeavyHandCannonCraftingResult", 1, 1, 100);
			heavyHandCannonBulletMetal = builder.comment("How much metal would you like to use for crafting heavy hand cannon bullets")
					.defineInRange("HeavyHandCannonCraftingResult Metal", 1, 1, 100);

			builder.pop();

			builder.push("General Stuff");

			shouldPrvConsumeGunpowder = builder.comment("Should Primitive Revolver consume gunpowder after shooting?")
					.define("ConsumeGunpowder?", false);

			builder.pop();
		}
		
	}
	
	public static class Client {
		
		public final ForgeConfigSpec.BooleanValue doDebugStuff;
		public final ForgeConfigSpec.IntValue hitmarkerTime;
		
		public Client(ForgeConfigSpec.Builder builder) {
			doDebugStuff = builder.comment("if you want to make transforms to the gun parts or "
					+ "hands active this").define("do debug stuff", false);
			hitmarkerTime = builder.comment("This value controls how long the hit marker is " +
					"displayed").defineInRange("HitmarkerTime", 8, 0, 10000);
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