/*
 * This file ("GuiCoalGenerator.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.inventory.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import de.ellpeck.actuallyadditions.mod.inventory.ContainerCoalGenerator;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityCoalGenerator;
import de.ellpeck.actuallyadditions.mod.util.AssetUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nonnull;
import java.util.List;


public class GuiCoalGenerator extends AAScreen<ContainerCoalGenerator> {

    private static final ResourceLocation RES_LOC = AssetUtil.getGuiLocation("gui_coal_generator");
    private final TileEntityCoalGenerator generator;
    private EnergyDisplay energy;

    public GuiCoalGenerator(ContainerCoalGenerator container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.generator = container.generator;
        this.imageWidth = 176;
        this.imageHeight = 93 + 86;
    }

    @Override
    public void init() {
        super.init();
        this.energy = new EnergyDisplay(this.leftPos + 42, this.topPos + 5, this.generator.storage);
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.energy.render(guiGraphics, mouseX, mouseY);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void renderBg(GuiGraphics guiGraphics, float f, int x, int y) {
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        guiGraphics.blit(AssetUtil.GUI_INVENTORY_LOCATION, this.leftPos, this.topPos + 93, 0, 0, 176, 86);

        guiGraphics.blit(RES_LOC, this.leftPos, this.topPos, 0, 0, 176, 93);

        if (this.generator.currentBurnTime > 0) {
            int i = this.generator.getBurningScaled(13);
            guiGraphics.blit(RES_LOC, this.leftPos + 87, this.topPos + 27 + 12 - i, 176, 96 - i, 14, i);
        }

        this.energy.draw(guiGraphics);
    }

    @Nonnull
    @Override
    protected List<Component> getTooltipFromContainerItem(@Nonnull ItemStack stack) {
        var tooltip = super.getTooltipFromContainerItem(stack);

        int burnTime = stack.getBurnTime(RecipeType.SMELTING);
        if (burnTime > 0) {
            tooltip.add(Component.translatable("tooltip.actuallyadditions.coal_generator_stats",
                    burnTime * TileEntityCoalGenerator.ENERGY_PER_TICK,
                    TileEntityCoalGenerator.ENERGY_PER_TICK,
                    burnTime).withStyle(ChatFormatting.GRAY));
        }

        return tooltip;
    }
}
