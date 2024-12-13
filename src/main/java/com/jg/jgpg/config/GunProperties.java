package com.jg.jgpg.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GunProperties {
	
	public String gunName;
	public ModConfigSpec.Builder builder;
	
	public ModConfigSpec.DoubleValue damage;
	public ModConfigSpec.IntValue shootTime;
	public ModConfigSpec.DoubleValue recoilTime;
	public ModConfigSpec.DoubleValue recoilWeight;
	public ModConfigSpec.DoubleValue verticalRecoil;
	public ModConfigSpec.DoubleValue horizontalRecoil;
	public ModConfigSpec.DoubleValue bulletGravity;
	public ModConfigSpec.IntValue power;
	public ModConfigSpec.IntValue range;
	public ModConfigSpec.DoubleValue rangeDamageReduction;
	public ModConfigSpec.DoubleValue innacuracy;
	public ModConfigSpec.DoubleValue meleeDamage;
	public ModConfigSpec.IntValue bulletsPerShot;
	public ModConfigSpec.IntValue gunpowderToReload;
	public ModConfigSpec.DoubleValue aimingSensitivityReduction;

	public ModConfigSpec.IntValue metal;
	public ModConfigSpec.IntValue wood;
	public ModConfigSpec.BooleanValue cancelCraft;
	
	public GunProperties(ModConfigSpec.Builder builder, String gunName) {
		this.gunName = gunName;
		this.builder = builder;
	}
	
	public GunProperties openGunSection() {
		builder.push(gunName);
		return this;
	}
	
	public GunProperties closeGunSection() {
		builder.pop();
		return this;
	}
	
	public GunProperties openGunCraftingSection() {
		builder.push("crafting");
		return this;
	}
	
	public GunProperties closeGunCraftingSection() {
		builder.pop();
		return this;
	}
	
	public GunProperties setDamage(float val) {
		damage = builder.comment(gunName + "Damage").defineInRange(gunName 
				+ "Damage", val, 0, 20000);
		return this;
	}
	
	public GunProperties setShootTime(int val) {
		shootTime = builder.comment(gunName + "ShootTime").defineInRange(gunName 
				+ "ShootTime", val, 0, 20000);
		return this;
	}
	
	public GunProperties setRecoilTime(float val) {
		recoilTime = builder.comment(gunName + "RecoilTime").defineInRange(gunName 
				+ "RecoilTime", val, 0, 20000);
		return this;
	}
	
	public GunProperties setRecoilWeight(float val) {
		recoilWeight = builder.comment(gunName + "RecoilWeight").defineInRange(gunName 
				+ "RecoilWeight", val, 0, 20000);
		return this;
	}
	
	public GunProperties setVerticalRecoil(float val){
		verticalRecoil = builder.comment(gunName + "VerticalRecoil")
				.defineInRange(gunName + "VerticalRecoil", val, 0, 20000);
		return this;
	}
	
	public GunProperties setHorizontalRecoil(float val){
		horizontalRecoil = builder.comment(gunName + "HorizontalRecoil")
				.defineInRange(gunName + "HorizontalRecoil", val, 0, 20000);
		return this;
	}

	public GunProperties setBulletGravity(float val) {
		bulletGravity = builder.comment(gunName + "BulletGravity").defineInRange(gunName
				+ "BulletGravity", val, 0, 200000);
		return this;
	}

	public GunProperties setPower(int val) {
		power = builder.comment(gunName + "Power").defineInRange(gunName 
				+ "Power", val, 0, 200000);
		return this;
	}
	
	public GunProperties setRange(int val) {
		range = builder.comment(gunName + " - This value will multiply gunDmg when "
				+ "bullet range is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * 0.9 = "
				+ "3.6 <- this will happen every time the range is reached")
				.defineInRange(gunName + "Range", val, 0, 20000);
		return this;
	}
	
	public GunProperties setRangeDamageReduction(float val) {
		rangeDamageReduction = builder
				.comment("aksRangeDmgRed - This value will multiply gunDmg when bullet range "
						+ "is rechead - for example: dmgRed: 0.9 dmg: 4 -> 4 * 0.9 = 3.6 <- "
						+ "this will happen every time the range is reached")
				.defineInRange(gunName + "RangeDmgRed", val, 0.1D, 20000);
		return this;
	}
	
	public GunProperties setInnacuracy(float val) {
		innacuracy = builder.comment(gunName + "Innacuracy")
				.defineInRange(gunName + "Innacuracy", val, 0.01D, 20000);
		return this;
	}
	
	public GunProperties setMeleeDamage(float val) {
		meleeDamage = builder.comment(gunName + "MeleeDamage")
				.defineInRange(gunName + "MeleeDamage", val, 0.1D, 20000);
		return this;
	}
	
	public GunProperties setBulletsPerShot(int val) {
		bulletsPerShot = builder.comment(gunName + "BulletsPerShot")
				.defineInRange(gunName + "BulletsPerShot", val, 1, 20000);
		return this;
	}

	public GunProperties setGunpowerToReload(int val) {
		gunpowderToReload = builder.comment(gunName + "GunpowderToReload")
				.defineInRange(gunName + "GunpowderToReload", val, 0, 20000);
		return this;
	}

	public GunProperties setAimingSensitivityReduction(float val) {
		aimingSensitivityReduction = builder.comment(gunName + "AimingSensitivityReduction")
				.defineInRange(gunName + "AimingSensitivityReduction", val, 0, 20000);
		return this;
	}

	public GunProperties cancelCraft(boolean val) {
		cancelCraft = builder.comment("Do you want to prevent players from crafting " + gunName + "?")
				.define("cancel " + gunName + " crafting?", val);
		return this;
	}

	public GunProperties setWood(int val) {
		wood = builder.comment("How many wood planks do you think aks should need for crafting?")
				.defineInRange(gunName + "WoodPlanks", val, 0, 99999);
		return this;
	}

	public GunProperties setMetal(int val) {
		metal = builder.comment("How many steel ingots do you think aks should need for crafting?")
				.defineInRange(gunName + "MetalIngots", val, 0, 99999);
		return this;
	}

}
