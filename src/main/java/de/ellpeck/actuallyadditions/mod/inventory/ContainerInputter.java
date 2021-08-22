/*
 * This file ("ContainerInputter.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.inventory;

import de.ellpeck.actuallyadditions.mod.inventory.gui.GuiInputter;
import de.ellpeck.actuallyadditions.mod.inventory.slot.SlotFilter;
import de.ellpeck.actuallyadditions.mod.inventory.slot.SlotItemHandlerUnconditioned;
import de.ellpeck.actuallyadditions.mod.tile.TileEntityInputter;
import de.ellpeck.actuallyadditions.mod.util.StackUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.Objects;

public class ContainerInputter extends Container {
    public final TileEntityInputter tileInputter;
    public final boolean isAdvanced;

    public static ContainerInputter fromNetwork(int windowId, PlayerInventory inv, PacketBuffer data) {
        return new ContainerInputter(windowId, inv, (TileEntityInputter) Objects.requireNonNull(inv.player.level.getBlockEntity(data.readBlockPos())));
    }

    public ContainerInputter(int windowId, PlayerInventory inventory, TileEntityInputter tile) {
        super(ActuallyContainers.INPUTTER_CONTAINER.get(), windowId);
        this.tileInputter = tile;
        this.isAdvanced = tile.isAdvanced;

        this.addSlot(new SlotItemHandlerUnconditioned(this.tileInputter.inv, 0, 80, 21 + (this.isAdvanced
            ? 12
            : 0)));

        if (this.isAdvanced) {
            for (int i = 0; i < 2; i++) {
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 4; y++) {
                        this.addSlot(new SlotFilter(i == 0
                            ? this.tileInputter.leftFilter
                            : this.tileInputter.rightFilter, y + x * 4, 20 + i * 84 + x * 18, 6 + y * 18));
                    }
                }
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 101 + i * 18 + (this.isAdvanced
                    ? GuiInputter.OFFSET_ADVANCED
                    : 0)));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 159 + (this.isAdvanced
                ? GuiInputter.OFFSET_ADVANCED
                : 0)));
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slot) {
        int inventoryStart = this.isAdvanced
            ? 25
            : 1;
        int inventoryEnd = inventoryStart + 26;
        int hotbarStart = inventoryEnd + 1;
        int hotbarEnd = hotbarStart + 8;

        Slot theSlot = this.slots.get(slot);

        if (theSlot != null && theSlot.hasItem()) {
            ItemStack newStack = theSlot.getItem();
            ItemStack currentStack = newStack.copy();

            //Other Slots in Inventory excluded
            if (slot >= inventoryStart) {
                //Shift from Inventory
                if (!this.moveItemStackTo(newStack, 0, 1, false)) {
                    //
                    if (slot >= inventoryStart && slot <= inventoryEnd) {
                        if (!this.moveItemStackTo(newStack, hotbarStart, hotbarEnd + 1, false)) {
                            return StackUtil.getEmpty();
                        }
                    } else if (slot >= inventoryEnd + 1 && slot < hotbarEnd + 1 && !this.moveItemStackTo(newStack, inventoryStart, inventoryEnd + 1, false)) {
                        return StackUtil.getEmpty();
                    }
                }
            } else if (!this.moveItemStackTo(newStack, inventoryStart, hotbarEnd + 1, false)) {
                return StackUtil.getEmpty();
            }

            if (!StackUtil.isValid(newStack)) {
                theSlot.set(StackUtil.getEmpty());
            } else {
                theSlot.setChanged();
            }

            if (newStack.getCount() == currentStack.getCount()) {
                return StackUtil.getEmpty();
            }
            theSlot.onTake(player, newStack);

            return currentStack;
        }
        return StackUtil.getEmpty();
    }

    @Override
    public ItemStack clicked(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if (SlotFilter.checkFilter(this, slotId, player)) {
            return StackUtil.getEmpty();
        } else {
            return super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return this.tileInputter.canPlayerUse(player);
    }
}
