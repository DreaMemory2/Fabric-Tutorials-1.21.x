package com.crystal.bluecore.screen;

import com.crystal.bluecore.BlueCore;
import com.crystal.bluecore.screenhandler.OakChestScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OakChestInventoryBlockScreen extends HandledScreen<OakChestScreenHandler> {
    // 获取GUI纹理
    private static final Identifier TEXTURE = Identifier.of(BlueCore.MOD_ID, "textures/gui/container/oak_chest_inventory_block.png");

    public OakChestInventoryBlockScreen(OakChestScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 184;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // 绘画GUI材质
        // context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
