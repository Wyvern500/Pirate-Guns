package com.jg.jgpg.server;

public interface IJgPlayerData {

    void setModel(String model);

    String getModel();

    void setPose(String pose);

    String getPose();

    void setAnimationIndex(int animationIndex);

    int getAnimationIndex();

    void copyFrom(IJgPlayerData data);

}
