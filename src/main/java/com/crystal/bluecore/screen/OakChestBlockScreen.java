package com.crystal.bluecore.screen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.screenhandler.OakChestScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OakChestBlockScreen extends HandledScreen<OakChestScreenHandler> {
    // 获取GUI纹理
    private static final Identifier TEXTURE = Identifier.of(BlueCore.MOD_ID, "textures/gui/container/oak_chest_screen.png");

    public OakChestBlockScreen(OakChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundWidth = 176;
        backgroundHeight = 184;
        playerInventoryTitleY = backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // 绘画GUI材质
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
