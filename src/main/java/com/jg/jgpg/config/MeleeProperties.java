package com.jg.jgpg.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class MeleeProperties {

    public String meleeWeaponName;
    public ModConfigSpec.Builder builder;

    public ModConfigSpec.DoubleValue damage;
    public ModConfigSpec.IntValue range;

    public ModConfigSpec.IntValue metal;
    public ModConfigSpec.IntValue wood;
    public ModConfigSpec.BooleanValue usesIron;
    public ModConfigSpec.BooleanValue cancelCraft;

    public MeleeProperties(ModConfigSpec.Builder builder, String weaponName) {
        this.meleeWeaponName = weaponName;
        this.builder = builder;
    }

    public MeleeProperties openWeaponSection() {
        builder.push(meleeWeaponName);
        return this;
    }

    public MeleeProperties closeWeaponSection() {
        builder.pop();
        return this;
    }

    public MeleeProperties openWeaponCraftingSection() {
        builder.push("crafting");
        return this;
    }

    public MeleeProperties closeWeaponCraftingSection() {
        builder.pop();
        return this;
    }

    public MeleeProperties setDamage(float val) {
        damage = builder.comment(meleeWeaponName + "Damage").defineInRange(meleeWeaponName
                + "Damage", val, 0, 20000);
        return this;
    }

    public MeleeProperties setRange(int val) {
        range = builder.comment("How much range would you like " + meleeWeaponName + " to have?")
                .defineInRange(meleeWeaponName + "Range", val, 0, 20000);
        return this;
    }

    public MeleeProperties cancelCraft(boolean val) {
        cancelCraft = builder.comment("Do you want to prevent players from crafting " + meleeWeaponName + "?")
                .define("cancel " + meleeWeaponName + " crafting?", val);
        return this;
    }

    public MeleeProperties usesIron(boolean val) {
        usesIron = builder.comment("Do you want players to use iron to craft " + meleeWeaponName + "?")
                .define("does " + meleeWeaponName + " use iron?", val);
        return this;
    }

    public MeleeProperties setWood(int val) {
        wood = builder.comment("How many wood planks do you think "+ meleeWeaponName
                        + " should need for crafting?")
                .defineInRange(meleeWeaponName + "WoodPlanks", val, 0, 99999);
        return this;
    }

    public MeleeProperties setMetal(int val) {
        metal = builder.comment("How many steel ingots do you think " + meleeWeaponName
                        + " should need for crafting?")
                .defineInRange(meleeWeaponName + "MetalIngots", val, 0, 99999);
        return this;
    }
    
}
