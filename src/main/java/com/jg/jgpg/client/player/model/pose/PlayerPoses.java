package com.jg.jgpg.client.player.model.pose;

import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.utils.Utils;

public class PlayerPoses {

    public static JgPlayerPose HOLDINGGUN = new JgPlayerPose(Utils.loc("holding_gun"));
    public static JgPlayerPose AIMING = new JgPlayerPose(Utils.loc("aiming"));
    public static JgPlayerPose RUNNING = new JgPlayerPose(Utils.loc("running"));
    public static JgPlayerPose RELOADING = new JgPlayerPose(Utils.loc("reloading"));
    public static JgPlayerPose MELEE = new JgPlayerPose(Utils.loc("melee"));

    public static void addPlayerPoses(ClientHandler client) {
        JgPlayerPoses playerPoses = client.getPlayerPoses();
        playerPoses.addPoses(PlayerPoses.class);
        /*playerPoses.addPose(HOLDINGGUN);
        playerPoses.addPose(AIMING);
        playerPoses.addPose(RUNNING);
        playerPoses.addPose(RELOADING);
        playerPoses.addPose(MELEE);*/
    }

}
