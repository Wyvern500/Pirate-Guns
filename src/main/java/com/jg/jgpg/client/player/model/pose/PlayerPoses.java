package com.jg.jgpg.client.player.model.pose;

import com.jg.jgpg.PirateGuns;
import com.jg.jgpg.client.handler.ClientHandler;

public class PlayerPoses {

    public static JgPlayerPose HOLDINGGUN = new JgPlayerPose(PirateGuns.prefix("holding_gun"));
    public static JgPlayerPose AIMING = new JgPlayerPose(PirateGuns.prefix("aiming"));
    public static JgPlayerPose RUNNING = new JgPlayerPose(PirateGuns.prefix("running"));
    public static JgPlayerPose RELOADING = new JgPlayerPose(PirateGuns.prefix("reloading"));
    public static JgPlayerPose MELEE = new JgPlayerPose(PirateGuns.prefix("melee"));

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
