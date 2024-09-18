package com.jg.jgpg.client.player.model.pose;

import com.jg.jgpg.client.player.model.pose.playerposehandlers.CannonPoseHandler;
import com.jg.jgpg.client.player.model.pose.playerposehandlers.HammerPoseHandler;
import com.jg.jgpg.client.player.model.pose.playerposehandlers.OneHandedPoseHandler;
import com.jg.jgpg.client.player.model.pose.playerposehandlers.TwoHandedPoseHandler;

public class PlayerPoseHandlers {

    public static final JgPlayerPoseHandler TWO_HANDED = new TwoHandedPoseHandler();

    public static final JgPlayerPoseHandler ONE_HANDED = new OneHandedPoseHandler();

    public static final JgPlayerPoseHandler HAMMER = new HammerPoseHandler();

    public static final JgPlayerPoseHandler CANNON = new CannonPoseHandler();

}
