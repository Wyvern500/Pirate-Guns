package com.jg.jgpg.common.network;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.animation.Animation;
import com.jg.jgpg.client.animation.RepetitiveAnimation;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.client.jgmodel.itemmodel.JgModel;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class RequestPlayerDataSynchronizationMessage extends AbstractPacket {

    public static final CustomPacketPayload.Type<RequestPlayerDataSynchronizationMessage> TYPE =
            new CustomPacketPayload.Type<>(PirateGuns.prefix("request_player_data_synchronization"));
    public static final StreamCodec<RegistryFriendlyByteBuf,
            RequestPlayerDataSynchronizationMessage> CODEC = StreamCodec.ofMember(RequestPlayerDataSynchronizationMessage::toBytes,
            RequestPlayerDataSynchronizationMessage::new);

    public UUID requesterUUID;
    public boolean sentFromServerToClient;

    public RequestPlayerDataSynchronizationMessage(UUID requesterUUID) {
        this.requesterUUID = requesterUUID;
    }

    @Override
    void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(requesterUUID);
    }

    public RequestPlayerDataSynchronizationMessage(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    @Override
    public void onClientReceived(Minecraft minecraft, IPayloadContext ctx, Player player) {
        Minecraft mc = Minecraft.getInstance();
        String pose = ClientEventHandler.getClientHandler().getCurrentPose()
                .getLoc().toString();
        String itemId = BuiltInRegistries.ITEM.getKey(mc.player.getMainHandItem()
                .getItem()).toString();
        JgModel<?> model = ClientEventHandler.getClientHandler().getModel();
        Animation currentAnimation = model.getAnimator().getAnimation();
        int animationIndex = model.getAnimationIndex(currentAnimation);
        int cycles = -1;
        if(currentAnimation instanceof RepetitiveAnimation repetitiveAnimation) {
            cycles = repetitiveAnimation.getCycles();
        }
        int animationTick = (int) model.getAnimator().getTick();

        Networking.sendToServer(new PlayerDataSynchronizationMessage(requesterUUID,
                itemId, pose, animationIndex, cycles, animationTick));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
