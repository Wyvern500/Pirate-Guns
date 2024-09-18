package com.jg.jgpg.client.handler;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handlers.*;
import com.jg.jgpg.client.jgmodel.BakedModelHandler;
import com.jg.jgpg.client.jgmodel.ToShowModModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModelPart;
import com.jg.jgpg.client.player.model.pose.JgPlayerPose;
import com.jg.jgpg.client.player.model.pose.JgPlayerPoses;
import com.jg.jgpg.client.player.render.JgPlayerRenderer;
import com.jg.jgpg.network.SendPlayerPoseChangedMessage;
import com.jg.jgpg.network.SynchronizeJgModelMessage;
import com.jg.jgpg.utils.LogUtils;
import com.jg.jgpg.utils.NBTUtils;
import com.jg.jgpg.utils.Utils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.*;

public class ClientHandler extends AbstractClientHandler
        implements ResourceManagerReloadListener {

    public static final Map<UUID, PlayerData> playersData = new HashMap<>();

    AbstractClientHandler handler;
    public JgPlayerRenderer playerRenderer;

    BreathingHandler breathing;
    CooldownsHandler cooldown;
    SprintHandler sprint;
    JgPlayerPoses playerPoses;

    Random random;

    JgModel<?> model;
    List<JgModelPart> parts;
    JgPlayerPose currentPose;
    JgPlayerPose prevPose;

    private int index;
    private String prevId;

    private boolean renderDefault;
    private boolean isRotationMode;
    private boolean isDisplayMode;

    public static float partialTicks = 0.0f;

    private float trStep;
    private float rtStep;

    public ClientHandler() {
        super(null);
        handler = new PirateGunsClientHandler(this);
        trStep = 0.01f;
        rtStep = (float) Math.toRadians(1f);
        breathing = new BreathingHandler();
        cooldown = new CooldownsHandler(this);
        sprint = new SprintHandler(this);
        playerPoses = new JgPlayerPoses();
        random = new Random();
        parts = new ArrayList<>();
        prevId = "";
    }

    public void initPlayerRenderer() {
        playerRenderer = new JgPlayerRenderer(this);
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        if (handler instanceof ResourceManagerReloadListener) {
            ((ResourceManagerReloadListener) handler)
                    .onResourceManagerReload(resourceManager);
        }
    }

    public void render(LocalPlayer player, ItemStack stack, PoseStack matrix, MultiBufferSource buffer,
                       BufferSource impl, int packedLight) {
        if (model != null) {
            model.render(player, stack, buffer, impl, matrix, packedLight);
        }
    }

    public void renderOverlayPre(RenderGuiOverlayEvent.Pre e, LocalPlayer player, Font font) {
        if (model != null) {
            model.renderOverlayPre(e, player, font);
        }
    }

    public void renderPlayer(PoseStack matrix, MultiBufferSource bufferSource, float partialTicks,
                             int packedLight) {
        if (playerRenderer != null) {
            playerRenderer.render(Minecraft.getInstance().player, 0, partialTicks, matrix,
                    bufferSource, packedLight);
        }
    }

    public void pickModel(ItemStack stack) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(stack.getItem());
        String itemId = loc.toString();
        JgModel<?> model = JgModelsHandler.getSupplier(itemId).get();
        if (model != null) {
            setModel(model, itemId);
            if (model.animationIsNull()) {
                model.reset();
                model.onReset();
            }
        }

        handler.pickModel(stack);
    }

    public void tick(LocalPlayer player, ItemStack stack, String stackId) {
        if (!prevId.equals(stackId)) {
            String itemId = Objects
                    .requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem()))
                    .toString();
            JgModel<?> model = JgModelsHandler.getSupplier(itemId).get();
            if (model != null) {
                setModel(model, itemId);
                model.onSetModel(player, stack, stackId);
                if (model.animationIsNull()) {
                    model.reset();
                    model.onReset();
                }
                breathing.tick();
            }
        }

        for(Map.Entry<UUID, PlayerData> entry : playersData.entrySet()) {
            if(entry.getKey() != player.getUUID()) {
                PlayerData data = entry.getValue();
                if(data.animation != null) {
                    entry.getValue().getModel().tick();
                }
            }
        }

        if (model != null) {
            model.tick();

            currentPose = model.getPose(player);

            if (currentPose != prevPose) {
                ClientHandler.getPlayerData(player.getUUID()).setPose(currentPose);
                notifyPlayers();
            }

            prevPose = currentPose;

            handler.onTickPre(player, stack, stackId);
            sprint.onTickPre(player, stack, stackId);
            breathing.tick();
        } else {
            String itemId = Objects
                    .requireNonNull(ForgeRegistries.ITEMS.getKey(stack.getItem()))
                    .toString();
            JgModel<?> model = JgModelsHandler.getSupplier(itemId).get();
            if (model != null) {
                setModel(model, itemId);
            }
        }
        prevId = stackId;

        cooldown.onTickPost(player, stack, stackId);

        handler.onTickPost(player, stack, stackId);
    }

    private void notifyPlayers() {
        PirateGuns.channel.sendToServer(new SendPlayerPoseChangedMessage(Minecraft
                .getInstance().player.getUUID(), currentPose.getLoc().toString()));
    }

    public void handleMouse(InputEvent.MouseButton e) {
        if (model != null) {
            if (handler instanceof IClientInputHandler) {
                ((IClientInputHandler) handler).handleMouse(e);
            }
            tryToUse(e);
        }
    }

    public void handleKeyboard(InputEvent.Key e) {
        if (handler instanceof IClientInputHandler) {
            ((IClientInputHandler) handler).handleKeyboard(e);
        }
    }

    private void tryToUse(InputEvent.MouseButton e) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && Minecraft.getInstance().screen == null) {
            if (e.getAction() == GLFW.GLFW_PRESS) {
                if (e.getButton() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
                    if (model.canUse(player)) {
                        model.onUse(player, player.getMainHandItem());
                    }
                }
            }
        }
    }

    // Model Manipulation

    public void nextPartIndex() {
        index++;
        if (index >= parts.size()) {
            index = 0;
        }
        LogUtils.log("ClientHandler", "Index: " + index + " part: " + parts.get(index).getName());
    }

    public void prevPartIndex() {
        index--;
        if (index <= -1) {
            index = parts.size() - 1;
        }
        LogUtils.log("ClientHandler", "Index: " + index + " part: " + parts
                .get(index).getName());
    }

    public void addToModel(float x, float y, float z, boolean isRot, boolean display) {
        if (display) {
            if (isRot) {
                parts.get(index).getDtransform().add(x, y, z, true);
                float[] rot = parts.get(index).getDtransform().rot;
                for (int i = 0; i < rot.length; i++) {
                    if (rot[i] >= Math.PI) {
                        rot[i] = (float) -Math.PI;
                    } else if (rot[i] <= -Math.PI) {
                        rot[i] = (float) Math.PI;
                    }
                }
            } else {
                parts.get(index).getDtransform().add(x, y, z, false);
            }
        } else {
            if (isRot) {
                parts.get(index).getTransform().add(x, y, z, true);
                float[] rot = parts.get(index).getTransform().rot;
                for (int i = 0; i < rot.length; i++) {
                    if (rot[i] >= Math.PI) {
                        rot[i] = (float) -Math.PI;
                    } else if (rot[i] <= -Math.PI) {
                        rot[i] = (float) Math.PI;
                    }
                }
            } else {
                parts.get(index).getTransform().add(x, y, z, false);
            }
        }
    }

    public void switchRotationMode() {
        isRotationMode = !isRotationMode;
    }

    public void switchDisplayMode() {
        isDisplayMode = !isDisplayMode;
    }

    // Getters and setters

    public Random getRandom() {
        return random;
    }

    public boolean shouldRenderDefault() {
        return renderDefault;
    }

    public void switchRenderDefault() {
        renderDefault = !renderDefault;
        LogUtils.log("ClientHandler", "renderDefault: " + renderDefault);
    }

    public void setModel(JgModel<?> model, String itemId) {
        if (this.model != null) {
            if (this.model != model) {
                this.model.dispose();
            }
        }
        PirateGuns.channel.sendToServer(new SynchronizeJgModelMessage(Minecraft
                .getInstance().player.getUUID(), itemId));
        this.model = model;
        model.onPick();

        parts = new ArrayList<>(model.getParts().values());

        ToShowModModel modModel = BakedModelHandler.INSTANCE.getModelForId(NBTUtils.getId(Utils
                .handStack()));

        if (modModel != null) {
            modModel.tryToRebuild();
        }

        handler.onSetModel(model);
    }

    @Override
    public float getProgress(Item item) {
        return 0;
    }

    @Override
    public ClientHandler getClient() {
        return this;
    }

    public JgModel<?> getModel() {
        return model;
    }

    public BreathingHandler getBreathingHandler() {
        return breathing;
    }

    public SprintHandler getSprintHandler() {
        return sprint;
    }

    public CooldownsHandler getCooldownHandler() {
        return cooldown;
    }

    public JgPlayerPoses getPlayerPoses() {
        return playerPoses;
    }

    public AbstractClientHandler getHandler() {
        return handler;
    }

    public boolean isRotationMode() {
        return isRotationMode;
    }

    public boolean isDisplayMode() {
        return isDisplayMode;
    }

    public float getTranslationStep() {
        return trStep;
    }

    public void setTranslationStep(float translationStep) {
        this.trStep = translationStep;
    }

    public float getRotationStep() {
        return rtStep;
    }

    public void setRotationStep(float rotationStep) {
        this.rtStep = rotationStep;
    }

    public void setDisplayMode(boolean displayMode) {
        isDisplayMode = displayMode;
    }

    public void setRotationMode(boolean rotationMode) {
        isRotationMode = rotationMode;
    }

    public void setRenderDefault(boolean renderDefault) {
        this.renderDefault = renderDefault;
    }

    public static PlayerData getPlayerData(UUID id) {
        if(!playersData.containsKey(id)) {
            JgPlayerPose pose = JgPlayerPoses.EMPTY;
            playersData.put(id, new PlayerData().setPose(pose));
        }
        return playersData.get(id);
    }

}