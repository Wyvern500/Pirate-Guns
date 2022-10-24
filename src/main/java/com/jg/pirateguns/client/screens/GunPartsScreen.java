package com.jg.pirateguns.client.screens;

import java.util.ArrayList;
import java.util.List;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Transform;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.client.screens.widgets.GunPartKey;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList.Key;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class GunPartsScreen extends Screen {

	private JGSelectionList gunPartList;
	private AnimationScreen animScreen;
	private final List<EditBox> edits;
	private final List<Button> buttons;
	private GunModel model;
	
	public GunPartsScreen(GunModel gunModel) {
		super(new TranslatableComponent("GunParts Screen"));
		this.edits = new ArrayList<>();
		this.buttons = new ArrayList<>();
		this.model = gunModel;
	}
	
	@Override
	protected void init() {
		super.init();
		
		buttons.add(new Button(342, 2, 30, 20, new TranslatableComponent("Anim"), (b) -> {
			Minecraft.getInstance().screen = animScreen;
		}));
		
		GunPartKey[] gunParts = new GunPartKey[model.getGunParts().size()];
		for(int i = 0; i < model.getGunParts().size(); i++) {
			gunParts[i] = new GunPartKey(font, model.getGunParts().get(i));
		}
		gunPartList = new JGSelectionList(gunParts, this, this.font, 
				100, 2, 60, 14, 4, (k, i) -> {
						edits.get(0).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().pos[0]));
						edits.get(1).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().pos[1]));
						edits.get(2).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().pos[2]));
						edits.get(3).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().rot[0]));
						edits.get(4).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().rot[1]));
						edits.get(5).setValue(String.valueOf(model.getGunParts().get(i)
								.getTransform().rot[2]));
				});
		
		// Pos
				// x
				edits.add(new EditBox(font, 16, 0, 80, 20, new TranslatableComponent("animationScreen.pos.x")));
				edits.get(0).setValue("0");
				edits.get(0).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().pos[0] = val;
						}
					} catch (Exception e) {

					}
				});
				// y
				edits.add(new EditBox(font, 16, 20, 80, 20, new TranslatableComponent("animationScreen.pos.y")));
				edits.get(1).setValue("0");
				edits.get(1).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().pos[1] = val;
						}
					} catch (Exception e) {

					}
				});
				// z
				edits.add(new EditBox(font, 16, 40, 80, 20, new TranslatableComponent("animationScreen.pos.z")));
				edits.get(2).setValue("0");
				edits.get(2).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().pos[2] = val;
						}
					} catch (Exception e) {

					}
				});

				// Rot
				// rx
				edits.add(new EditBox(font, 16, 60, 80, 20, new TranslatableComponent("animationScreen.rot.x")));
				edits.get(3).setValue("0");
				edits.get(3).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().rot[0] = val;
						}
					} catch (Exception e) {

					}
				});
				// ry
				edits.add(new EditBox(font, 16, 80, 80, 20, new TranslatableComponent("animationScreen.rot.y")));
				edits.get(4).setValue("0");
				edits.get(4).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().rot[1] = val;
						}
					} catch (Exception e) {

					}
				});
				// rz
				edits.add(new EditBox(font, 16, 100, 80, 20, new TranslatableComponent("animationScreen.rot.z")));
				edits.get(5).setValue("0");
				edits.get(5).setResponder((s) -> {
					try {
						float val = Float.parseFloat(s);
						if(gunPartList.getSelected() != -1) {
							getTransform().rot[2] = val;
						}
					} catch (Exception e) {

					}
				});
				
		for(EditBox edit : edits) {
			addRenderableWidget(edit);
		}
		for(Button button : buttons) {
			addRenderableWidget(button);
		}
	}
	
	public Transform getTransform() {
		Key key = gunPartList.getSelectedKey();
		if(key != null) {
			return ((GunPartKey)gunPartList.getSelectedKey()).getPart().getTransform();
		}
		return Transform.EMPTY;
	}
	
	@Override
	public void render(PoseStack p_96562_, int p_96563_, int p_96564_, float p_96565_) {
		super.render(p_96562_, p_96563_, p_96564_, p_96565_);
		gunPartList.render(p_96562_, p_96563_, p_96564_, p_96565_);
	}
	
	@Override
	public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
		gunPartList.onClick((int)p_94695_, (int)p_94696_);
		return super.mouseClicked(p_94695_, p_94696_, p_94697_);
	}
	
	@Override
	public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
		gunPartList.check((int)p_94699_, (int)p_94700_);
		return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
	}
	
	@Override
	public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
		gunPartList.onScroll((float)(p_94686_*(-p_94688_)));
		return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
	}
	
	@Override
	public void tick() {
		super.tick();
		gunPartList.tick();
	}
	
	public GunPartsScreen setAnimScreen() {
		this.animScreen = new AnimationScreen(model, this);
		return this;
	}

}
