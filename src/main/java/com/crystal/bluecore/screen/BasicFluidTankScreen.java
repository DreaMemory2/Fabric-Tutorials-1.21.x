package com.crystal.bluecore.screen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.screenhandler.BasicFluidTankScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BasicFluidTankScreen extends HandledScreen<BasicFluidTankScreenHandler> {
    // 获取GUI纹理
    private static final Identifier TEXTURE = Identifier.of(BlueCore.MOD_ID, "textures/gui/container/basic_fluid_tank.png");
    public BasicFluidTankScreen(BasicFluidTankScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // 绘画
        // context.drawTexture(context, Identifier.of(""), this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 0, 0, 0xFFFFF);

        // TODO: Draw fluid tank
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 渲染
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);

        // TODO: Draw fluid tooltip
    }
}
