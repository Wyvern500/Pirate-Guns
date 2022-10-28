package com.jg.pirateguns.client.screens;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import com.jg.pirateguns.animations.Animation;
import com.jg.pirateguns.animations.Keyframe;
import com.jg.pirateguns.animations.Transform;
import com.jg.pirateguns.animations.parts.GunModel;
import com.jg.pirateguns.animations.serializers.AnimationSerializer;
import com.jg.pirateguns.client.handlers.ClientHandler;
import com.jg.pirateguns.client.screens.widgets.GunPartKey;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList;
import com.jg.pirateguns.client.screens.widgets.JGSelectionList.Key;
import com.jg.pirateguns.client.screens.widgets.JgEditBox;
import com.jg.pirateguns.client.screens.widgets.KeyframeLineWidget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.logging.LogUtils;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.CycleButton.OnValueChange;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class AnimationScreen extends Screen {

	public static ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

	private GunPartsScreen gunPartScreen;
	private final List<Button> buttons;
	private final List<EditBox> edits;
	private final List<OptionsList> options;
	private final List<CycleButton<Integer>> integerCycles;
	private final List<CycleButton<Boolean>> booleanCycles;
	// private AnimationSelectionList list;
	private final GunModel model;

	KeyframeLineWidget keyframeLine;
	JGSelectionList gunPartList;
	JGSelectionList posList;
	JGSelectionList rotList;

	private int i;
	private int j;

	private int minX, maxX, deltaX;
	private int scrollMax, scale;

	private boolean ctrl;
	private boolean start;
	private boolean rot;
	private float prog;
	private float prev;
	private float MAX = 4f;
	private boolean[] keys;
	
	private Keyframe kf;

	public AnimationScreen(GunModel model, GunPartsScreen screen) {
		super(new TranslatableComponent("Animation Screen"));
		this.gunPartScreen = screen;
		this.buttons = new ArrayList<>();
		this.edits = new ArrayList<>();
		this.options = new ArrayList<>();
		this.integerCycles = new ArrayList<>();
		this.booleanCycles = new ArrayList<>();
		this.model = model;
		i = width / 2;
		j = height / 2;
		keys = new boolean[400];
	}

	@Override
	protected void init() {
		super.init();

		posList = new JGSelectionList(new JGSelectionList.Key[0], this, this.font, 202, 2, 60, 14, 4, (k, i) -> {
			if (model.getAnimation() != Animation.EMPTY) {
				Keyframe kf = model.getAnimation().getKeyframes().get(
						keyframeLine.getSelected());
				edits.get(0).setValue(String.valueOf(kf.getPos(k.getKey())[0]));
				edits.get(1).setValue(String.valueOf(kf.getPos(k.getKey())[1]));
				edits.get(2).setValue(String.valueOf(kf.getPos(k.getKey())[2]));
			}
		});
		rotList = new JGSelectionList(new JGSelectionList.Key[0], this, this.font, 273, 2, 60, 14, 4, (k, i) -> {
			if (model.getAnimation() != Animation.EMPTY) {
				Keyframe kf = model.getAnimation().getKeyframes().get(
						keyframeLine.getSelected());
				edits.get(3).setValue(String.valueOf(kf.getRot(k.getKey())[0]));
				edits.get(4).setValue(String.valueOf(kf.getRot(k.getKey())[1]));
				edits.get(5).setValue(String.valueOf(kf.getRot(k.getKey())[2]));
			}
		});

		GunPartKey[] gunParts = new GunPartKey[model.getGunParts().size()];
		for (int i = 0; i < model.getGunParts().size(); i++) {
			gunParts[i] = new GunPartKey(font, model.getGunParts().get(i));
		}
		gunPartList = new JGSelectionList(gunParts, this, this.font, 100, 2, 60, 14, 4, (k, i) -> {
			
		});

		keyframeLine = new KeyframeLineWidget(this, posList, rotList);

		booleanCycles.add(buildBooleanCycle((s) -> {
			if(s) {
				return new TranslatableComponent("R");
			} else {
				return new TranslatableComponent("T");
			}
		}, false, 335, 2, 30, 20, 
			new TranslatableComponent(""), (c, v) -> {
					rot = v;
			})
		);
		
		buttons.add(new Button(372, 2, 50, 20, new TranslatableComponent("GunParts"), (b) -> {
			Minecraft.getInstance().setScreen(gunPartScreen);
		}));

		// String.valueOf(((GunPartKey)gunPartList.getSelectedKey())
		// .getPart().getTransform().pos[0])
		// Pos
		// x
		edits.add(new EditBox(font, 16, 0, 80, 20, new TranslatableComponent("animationScreen.pos.x")));
		edits.get(0).setValue("0");
		edits.get(0).setResponder((s) -> {
			try {
				float val = Float.parseFloat(s);
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(false, 0, val);
					}
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
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(false, 1, val);
					}
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
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(false, 2, val);
					}
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
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(true, 0, val);
					}
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
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(true, 1, val);
					}
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
				if (!posList.getSelectedIndexes().isEmpty()) {
					if (model.getAnimation() != Animation.EMPTY) {
						setVecVal(true, 2, val);
					}
				}
			} catch (Exception e) {

			}
		});
		// File managing
		edits.add(new JgEditBox(font, 100, 59, 80, 20, new TranslatableComponent("animationScreen.file")) {

		});
		edits.get(6).setValue("");
		edits.get(6).setResponder((s) -> {

		});
		edits.add(new EditBox(font, 16, 120, 80, 20, new TranslatableComponent("animationScreen.dur")));
		edits.get(7).setValue("0");
		edits.get(7).setResponder((s) -> {
			/*if(keyframeLine.getSelected() != -1 && !edits.get(7).getValue().isBlank()) {
				keyframeLine.getKeyframes()[keyframeLine.getSelected()].dur = 
						Integer.parseInt(edits.get(7).getValue());
			}*/
		});
		edits.add(new EditBox(font, 16, 140, 80, 20, new TranslatableComponent("animationScreen.startTick")));
		edits.get(8).setValue("0");
		edits.get(8).setResponder((s) -> {
			/*if(keyframeLine.getSelected() != -1 && !edits.get(8).getValue().isBlank()) {
				keyframeLine.getKeyframes()[keyframeLine.getSelected()].startTick = 
						Integer.parseInt(edits.get(8).getValue());
			}*/
		});
		edits.add(new EditBox(font, 16, 160, 80, 20, new TranslatableComponent("animationScreen.duration")));
		edits.get(9).setValue("1");
		edits.get(9).setResponder((s) -> {
			if(!edits.get(9).getValue().isBlank()) {
				keyframeLine.setScale(Integer.parseInt(edits.get(9).getValue()));
			}
		});
		
		// Buttons
		buttons.add(new Button(342, 24, 30, 20, new TranslatableComponent("Play"), (b) -> {
			model.setPlayAnimation(true);
		}));
		buttons.add(new Button(374, 24, 30, 20, new TranslatableComponent("Stop"), (b) -> {
			model.setPlayAnimation(false);
		}));
		buttons.add(new Button(342, 44, 30, 20, new TranslatableComponent("Next"), (b) -> {
			if (model.getAnimation() != Animation.EMPTY) {
				Animation anim = model.getAnimation();
				anim.nextDebugKeyframe();
			}
		}));
		buttons.add(new Button(374, 44, 30, 20, new TranslatableComponent("Prev"), (b) -> {
			if (model.getAnimation() != Animation.EMPTY) {
				Animation anim = model.getAnimation();
				anim.prevDebugKeyframe();
			}
		}));
		buttons.add(new Button(202, 59, 30, 20, new TranslatableComponent("Update"), (b) -> {
			model.setShouldUpdateAnimation(true);
		}));

		buttons.add(new Button(234, 59, 30, 20, new TranslatableComponent("Add"), (b) -> {
			LogUtils.getLogger().info("edits value: " + edits.get(7).getValue());
			if(keyframeLine.getSelected() != -1) {
				String val = gunPartList.getSelectedKey().getKey();
				if(!rot) {
					if (model.getAnimation() != Animation.EMPTY && 
							keyframeLine.getSelected() != -1) {
						LogUtils.getLogger().info(val);
						model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
								.pos.put(val, new float[] { 0, 0, 0 });
						posList.addKey(new Key(font, val));
					}
				} else {
					if (model.getAnimation() != Animation.EMPTY && 
							keyframeLine.getSelected() != -1) {
						model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
								.rot.put(val, new float[] { 0, 0, 0 });
						rotList.addKey(new Key(font, val));
					}
				}
			} else if(model.getAnimation() != Animation.EMPTY 
					&& !edits.get(7).getValue().isBlank()){
				LogUtils.getLogger().info("Before size: " + model
						.getAnimation().getKeyframes().size() + " selected: " + 
								keyframeLine.getSelected());
				int dur = Integer.parseInt(edits.get(7).getValue());
				Keyframe kf = new Keyframe(dur);
				kf.startTick = model.getAnimation().getDuration()+dur;
				model.getAnimation().addKeyframe(kf);
				/*LogUtils.getLogger().info("Keyframe added, new size: " + model
						.getAnimation().getKeyframes().length + " selected: " + 
						keyframeLine.getSelected());*/
				keyframeLine.setAnimDur(model.getAnimation().getDuration());
				keyframeLine.setKeyframes(model.getAnimation().getKeyframes());
				/*keyframeLine.setSelected(model.getAnimation().getKeyframes()
						.length-1);*/
				//keyframeLine.processOnClick(keyframeLine.getSelected());
			}
		}));

		buttons.add(new Button(266, 59, 30, 20, new TranslatableComponent("Remove"), (b) -> {
			Key[] posKeys = posList.getSelectedKeys();
			Key[] rotKeys = rotList.getSelectedKeys();
			boolean pos = false;
			boolean rot = false;
			if(posKeys.length > 0) {
				for(Key key : posKeys) {
					model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
							.pos.remove(key.getKey());
					posList.removeKey(key);
					pos = true;
					LogUtils.getLogger().info("deleted");
				}
			}
			if(rotKeys.length > 0) {
				for(Key key : rotKeys) {
					model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
							.rot.remove(key.getKey());
					rotList.removeKey(key);
					rot = true;
				}
			}
			if(!pos && !rot) {
				if(model.getAnimation() != Animation.EMPTY && keyframeLine.getSelected() != -1) {
					model.getAnimation().removeKeyframe(keyframeLine.getSelected());
					keyframeLine.setSelected(-1);
					keyframeLine.setKeyframes(model.getAnimation().getKeyframes());
					keyframeLine.setAnimDur(model.getAnimation().getDuration());
				}
			}
		}));

		// Initializing widgets
		for (Button b : buttons) {
			addRenderableWidget(b);
		}

		for (EditBox e : edits) {
			addRenderableWidget(e);
		}

		for (OptionsList b : options) {
			addRenderableWidget(b);
		}
		for (CycleButton<Boolean> e : booleanCycles) {
			addRenderableWidget(e);
		}
		for (CycleButton<Integer> e : integerCycles) {
			addRenderableWidget(e);
		}

		if (model.getAnimation() != Animation.EMPTY) {
			keyframeLine.setAnimDur(model.getAnimation().getDuration());
			keyframeLine.setKeyframes(model.getAnimation().getKeyframes());
		}
	}

	@Override
	public void render(PoseStack matrix, int x, int y, float p_96565_) {
		// Field Indicators
		this.font.drawShadow(matrix, "x: ", (float) 10 + 
				(-AnimationScreen.this.font.width("x: ") / 2), (float) (4),
				16777215, true);
		this.font.drawShadow(matrix, "y: ", (float) 10 + 
				(-AnimationScreen.this.font.width("y: ") / 2), (float) (24),
				16777215, true);
		this.font.drawShadow(matrix, "z: ", (float) 10 + 
				(-AnimationScreen.this.font.width("z: ") / 2), (float) (44),
				16777215, true);
		this.font.drawShadow(matrix, "rx: ", (float) 10 + 
				(-AnimationScreen.this.font.width("rx: ") / 2), (float) (64),
				16777215, true);
		this.font.drawShadow(matrix, "ry: ", (float) 10 + 
				(-AnimationScreen.this.font.width("ry: ") / 2), (float) (84),
				16777215, true);
		this.font.drawShadow(matrix, "rz: ", (float) 10 + 
				(-AnimationScreen.this.font.width("rz: ") / 2), (float) (104),
				16777215, true);
		this.font.drawShadow(matrix, "d: ", (float) 10 + 
				(-AnimationScreen.this.font.width("d: ") / 2), (float) (124),
				16777215, true);
		this.font.drawShadow(matrix, "st: ", (float) 10 + 
				(-AnimationScreen.this.font.width("st: ") / 2), (float) (144),
				16777215, true);
		this.font.drawShadow(matrix, "s: ", (float) 10 + 
				(-AnimationScreen.this.font.width("st: ") / 2), (float) (164),
				16777215, true);
		this.font.drawShadow(matrix, "Animation Name: " + model.getAnimation().getName(),
				(float) 10
						+ (-AnimationScreen.this.font.width("Animation Name: " + 
				model.getAnimation().getName()) / 2),
				(float) (184), 16777215, true);

		/*if(model.getAnimation() != Animation.EMPTY) {
			LogUtils.getLogger().info("Keyframe size: " + model.getAnimation()
			.getKeyframes().length + " anim dur: " + model.getAnimation().getDuration());
			for(Keyframe kf : model.getAnimation().getKeyframes()) {
				LogUtils.getLogger().info(kf.toString());
			}
		}*/
		
		prev = prog;
		if (start) {
			if (prog < MAX) {
				prog += ClientHandler.partialTicks;
				if (prog > MAX) {
					prog = MAX;
				}
			} else {
				start = false;
			}
		} else {
			if (prog > 0) {
				prog -= ClientHandler.partialTicks;
				if (prog < 0) {
					prog = 0;
				}
			}
		}
		renderWidget(matrix, 100, 14, 0, 66, 200, 20, 100, 20);
		renderWidget(matrix, 100, 51, 0, 46, 200, 20, 100, -14);

		super.render(matrix, x, y, p_96565_);

		// Gun Parts Selection Rendering
		gunPartList.render(matrix, x, y, p_96565_);
		posList.render(matrix, x, y, p_96565_);
		rotList.render(matrix, x, y, p_96565_);

		keyframeLine.render(matrix, x, y, p_96565_);
		minX = 100;
		maxX = 400;
		deltaX = maxX - minX;
	}

	@Override
	public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
		keyframeLine.onClick((int) p_94695_, (int) p_94696_);
		posList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		rotList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		gunPartList.onClick((int) p_94695_, (int) p_94696_, ctrl);
		return super.mouseClicked(p_94695_, p_94696_, p_94697_);
	}

	@Override
	public void tick() {
		super.tick();
		this.posList.tick();
		this.rotList.tick();
		gunPartList.tick();
	}

	@Override
	public boolean mouseDragged(double p_94699_, double p_94700_, int p_94701_, double p_94702_, double p_94703_) {
		posList.check((int) p_94699_, (int) p_94700_);
		rotList.check((int) p_94699_, (int) p_94700_);
		gunPartList.check((int) p_94699_, (int) p_94700_);
		keyframeLine.check((int) p_94699_, (int) p_94700_);
		return super.mouseDragged(p_94699_, p_94700_, p_94701_, p_94702_, p_94703_);
	}

	@Override
	public boolean mouseReleased(double p_94722_, double p_94723_, int p_94724_) {
		return super.mouseReleased(p_94722_, p_94723_, p_94724_);
	}

	@Override
	public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_) {
		posList.onScroll((float) (p_94686_ * (-p_94688_)));
		rotList.onScroll((float) (p_94686_ * (-p_94688_)));
		gunPartList.onScroll((float) (p_94686_ * (-p_94688_)));
		return super.mouseScrolled(p_94686_, p_94687_, p_94688_);
	}

	@Override
	public boolean keyPressed(int key, int p_96553_, int p_96554_) {
		keys[key] = true;
		if(model.getAnimation() != Animation.EMPTY) {
			if(key == 261 && keyframeLine.getSelected() != -1) {
				posList.cleanKeys();
				rotList.cleanKeys();
				model.getAnimation().removeKeyframe(keyframeLine.getSelected());
			} else if(key == 341) {
				ctrl = true;
			}
			if(keyframeLine.getSelected() != -1) {
				if(keys[341] && keys[67]) {
					kf = model.getAnimation().getKeyframes().get(keyframeLine.getSelected())
						.copy();
				} else if(keys[341] && keys[86]) {
					if(kf != null) {
						Keyframe selectedKeyframe = model.getAnimation()
								.getKeyframes().get(keyframeLine.getSelected());
						selectedKeyframe.pos = Keyframe.copyMap(kf.pos);
						selectedKeyframe.rot = Keyframe.copyMap(kf.rot);
						try {
						String keyPosId = selectedKeyframe.pos.keySet().stream()
								.findFirst().get();
						String keyRotId = selectedKeyframe.rot.keySet().stream()
								.findFirst().get();
						edits.get(0).setValue(String.valueOf(selectedKeyframe
								.getPos(keyPosId)[0]));
						edits.get(1).setValue(String.valueOf(selectedKeyframe
								.getPos(keyPosId)[1]));
						edits.get(2).setValue(String.valueOf(selectedKeyframe
								.getPos(keyPosId)[2]));
						edits.get(3).setValue(String.valueOf(selectedKeyframe
								.getPos(keyRotId)[0]));
						edits.get(4).setValue(String.valueOf(selectedKeyframe
								.getPos(keyRotId)[1]));
						edits.get(5).setValue(String.valueOf(selectedKeyframe
								.getPos(keyRotId)[2]));
						} catch(NoSuchElementException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return super.keyPressed(key, p_96553_, p_96554_);
	}

	@Override
	public boolean keyReleased(int p_94715_, int p_94716_, int p_94717_) {
		keys[p_94715_] = false;
		((JgEditBox) edits.get(6)).onKeyRelease(p_94715_);
		if(p_94715_ == 341) {
			ctrl = false;
		}
		return super.keyReleased(p_94715_, p_94716_, p_94717_);
	}

	public Transform getTransform() {
		Key key = gunPartList.getSelectedKey();
		if (key != null) {
			return ((GunPartKey) gunPartList.getSelectedKey()).getPart().getTransform();
		}
		return Transform.EMPTY;
	}

	public void setVecVal(boolean rot, int i, float v) {
		Keyframe kf = model.getAnimation().getKeyframes().get(keyframeLine.getSelected());
		if (!rot) {
			if (!kf.pos.isEmpty()) {
				Key[] keys = posList.getSelectedKeys();
				for(Key key : keys) {
					kf.pos.get(key)[i] = v;
				}
			} 
		} else {
			if (!kf.rot.isEmpty()) {
				Key[] keys = rotList.getSelectedKeys();
				for(Key key : keys) {
					kf.rot.get(key)[i] = v;
				}
			} 
		}
	}

	public void renderWidget(PoseStack matrix, int x, int y, int i, int j, int w, int h, int w2, int h2) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, WIDGETS);
		BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
		Matrix4f last = matrix.last().pose();
		bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		bufferbuilder.vertex(last, (i) + x, ((j + h) + y) + h2, 0).uv(0, (j + h) / 256f).color(1.0F, 1.0F, 1.0F, 1.0f)
				.endVertex();
		bufferbuilder.vertex(last, ((i + w) + x) + w2, ((j + h) + y) + h2, 0).uv((i + w) / 256f, (j + h) / 256f)
				.color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		bufferbuilder.vertex(last, ((i + w) + x) + w2, (j) + y, 0).uv((i + w) / 256f, (j) / 256f)
				.color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		bufferbuilder.vertex(last, (i) + x, (j) + y, 0).uv(0, (j) / 256f).color(1.0F, 1.0F, 1.0F, 1.0f).endVertex();
		bufferbuilder.end();
		BufferUploader.end(bufferbuilder);
	}

	public void initKeyframes() {
		if (model.getAnimation() != Animation.EMPTY) {
			keyframeLine.setAnimDur(model.getAnimation().getDuration());
			keyframeLine.setKeyframes(model.getAnimation().getKeyframes());
		}
	}

	public Font getFont() {
		return font;
	}

	public GunModel getGunModel() {
		return model;
	}
	
	public KeyframeLineWidget getKeyframeLineWidget() {
		return keyframeLine;
	}

	public JGSelectionList getGunPartList() {
		return gunPartList;
	}

	public JGSelectionList getPosList() {
		return posList;
	}

	public JGSelectionList getRotList() {
		return rotList;
	}

	public List<EditBox> getEditBoxes() {
		return edits;
	}

	public List<Button> getButtons() {
		return buttons;
	}

	public List<CycleButton<Integer>> getIntCycles() {
		return integerCycles;
	}

	public List<CycleButton<Boolean>> getBooleanCycles() {
		return booleanCycles;
	}

	private CycleButton<Integer> buildIntCycle(Function<Integer, Component> f, int initVal, int x, int y, int w, int h,
			TranslatableComponent t, OnValueChange<Integer> ch, Integer... values) {
		CycleButton<Integer> cycle = CycleButton.builder(f).withValues(values).withInitialValue(initVal).create(x, y, w,
				h, t, ch);
		return cycle;
	}

	private CycleButton<Boolean> buildBooleanCycle(Function<Boolean, Component> f, boolean initVal, int x, int y, int w,
			int h, TranslatableComponent t, OnValueChange<Boolean> ch) {
		CycleButton<Boolean> cycle = CycleButton.builder(f).withValues(true, false).withInitialValue(initVal).create(x,
				y, w, h, t, ch);
		return cycle;
	}

}
