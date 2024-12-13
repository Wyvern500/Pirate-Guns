package com.jg.jgpg.client.player.model.pose;

import com.jg.jgpg.PirateGuns;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JgPlayerPoses {

    public static final JgPlayerPose EMPTY = new JgPlayerPose(PirateGuns
            .prefix("empty"));

    private final List<JgPlayerPose> poses;

    public JgPlayerPoses() {
        poses = new ArrayList<>();
    }

    public JgPlayerPose getPlayerPose(ResourceLocation loc) {
        return poses.stream().filter((item) -> item.getLoc().equals(loc)).findFirst()
                .orElse(EMPTY);
    }

    public void addPose(JgPlayerPose pose) {
        poses.add(pose);
    }

    public void addPoses(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
               try {
                   Object obj = field.get(null);
                   if(obj instanceof JgPlayerPose) {
                       poses.add((JgPlayerPose) obj);
                   }
               } catch(IllegalAccessException e) {
                   e.fillInStackTrace();
               }
            }
        }
    }

    public List<JgPlayerPose> getPoses() {
        return poses;
    }

}
