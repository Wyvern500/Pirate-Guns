package com.jg.jgpg.client.animation.gui;

import com.jg.jgpg.client.animation.gui.widget.JgEditBox;
import com.jg.jgpg.client.handler.ClientHandler;
import com.jg.jgpg.client.handler.handlers.ClientEventHandler;
import com.jg.jgpg.utils.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DataManagerGui extends Screen {

    JgEditBox translationStep;
    JgEditBox rotationStep;

    CycleButton<Boolean> isDisplayMode;
    CycleButton<Boolean> isRotationMode;
    CycleButton<Boolean> renderDefault;

    public DataManagerGui() {
        super(Component.translatable("Data Manager Gui"));
    }

    @Override
    protected void init() {
        int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        ClientHandler client = ClientEventHandler.getClientHandler();

        addRenderableWidget(translationStep = new JgEditBox(font, windowWidth / 2 - 100,
                windowHeight / 2 - 100,
                100, 20, (s) -> {
            String finalText = "";
            finalText = translationStep.getValue().replaceAll("[^0-9.-]", "");
            if(finalText.isEmpty() || !finalText.matches(".*\\d.*")) {
                finalText = "0.0";
            }
            client.setTranslationStep(Float.parseFloat(finalText));
        }));
        addRenderableWidget(rotationStep = new JgEditBox(font, windowWidth / 2 - 100,
                windowHeight / 2 - 70,
                100, 20, (s) -> {
            String finalText = "";
            finalText = rotationStep.getValue().replaceAll("[^0-9.-]", "");
            if(finalText.isEmpty() || !finalText.matches(".*\\d.*")) {
                finalText = "0.0";
            }
            client.setTranslationStep(Float.parseFloat(finalText));
        }));

        addRenderableWidget(isDisplayMode = CycleButton.onOffBuilder(false)
                .create(windowWidth / 2 - 100, windowHeight / 2 - 40, 100, 20
                , Component.translatable("IsDisplayMode"), (cycleBtn, val) -> {
                    client.setDisplayMode(val);
                        }));

        addRenderableWidget(isRotationMode = CycleButton.onOffBuilder(false)
                .create(windowWidth / 2 - 100, windowHeight / 2 - 10, 100, 20
                        , Component.translatable("IsRotationMode"), (cycleBtn, val) -> {
                    client.setRotationMode(val);
                        }));

        addRenderableWidget(renderDefault = CycleButton.onOffBuilder(false)
                .create(windowWidth / 2 - 100, windowHeight / 2 + 20, 100, 20
                        , Component.translatable("RenderDefault"), (cycleBtn, val) -> {
                    client.setRenderDefault(val);
                        }));

        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(4);
        df.setRoundingMode(RoundingMode.DOWN);

        translationStep.setValue(String.valueOf(client.getTranslationStep()));
        rotationStep.setValue(df.format(Math.toDegrees(client
                .getRotationStep())));
        isDisplayMode.setValue(client.isDisplayMode());
        isRotationMode.setValue(client.isRotationMode());
        renderDefault.setValue(client.shouldRenderDefault());

        super.init();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

        int windowWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int windowHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();


        pGuiGraphics.drawString(font, "Data Manager Gui", (int) (windowWidth / 2)
                - (font.width("Data Manager Gui") / 2),
                4, Color.rgba(255, 255, 255, 255));

        pGuiGraphics.drawString(font, "TranslationStep: ", (int) (windowWidth / 2 - 184),
                (int)  (windowHeight / 2 - 94), Color.rgba(255, 255, 255, 255));
        pGuiGraphics.drawString(font, "RotationStep: ", (int) (windowWidth / 2 - 184),
                (int)  (windowHeight / 2 - 64), Color.rgba(255, 255, 255, 255));
    }
}
