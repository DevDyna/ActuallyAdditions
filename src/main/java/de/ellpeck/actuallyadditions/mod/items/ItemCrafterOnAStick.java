/*
 * This file ("ItemCrafterOnAStick.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.items;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import de.ellpeck.actuallyadditions.mod.inventory.GuiHandler;
import de.ellpeck.actuallyadditions.mod.items.base.ItemBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemCrafterOnAStick extends ItemBase {

    public ItemCrafterOnAStick() {
        super(ActuallyItems.defaultNonStacking());
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClientSide) {
            player.openGui(ActuallyAdditions.INSTANCE, GuiHandler.GuiTypes.CRAFTER.ordinal(), world, (int) player.posX, (int) player.posY, (int) player.posZ);
        }
        return new ActionResult<>(ActionResultType.SUCCESS, player.getItemInHand(hand));
    }
}
