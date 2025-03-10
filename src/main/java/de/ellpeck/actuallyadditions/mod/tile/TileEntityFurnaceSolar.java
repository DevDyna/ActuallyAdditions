///*
// * This file ("TileEntityFurnaceSolar.java") is part of the Actually Additions mod for Minecraft.
// * It is created and owned by Ellpeck and distributed
// * under the Actually Additions License to be found at
// * http://ellpeck.de/actaddlicense
// * View the source code at https://github.com/Ellpeck/ActuallyAdditions
// *
// * © 2015-2017 Ellpeck
// */
//
//package de.ellpeck.actuallyadditions.mod.tile;
//
//import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
//import net.minecraft.block.BlockState;
//import net.minecraft.nbt.CompoundNBT;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.energy.IEnergyStorage;
//
//import de.ellpeck.actuallyadditions.mod.tile.TileEntityBase.NBTType;
//
//public class TileEntityFurnaceSolar extends TileEntityBase implements ISharingEnergyProvider, IEnergyDisplay {
//
//    public static final int PRODUCE = 8;
//    public final CustomEnergyStorage storage = new CustomEnergyStorage(30000, 0, 100);
//    public final LazyOptional<IEnergyStorage> lazyEnergy = LazyOptional.of(() -> this.storage);
//    private int oldEnergy;
//
//    public TileEntityFurnaceSolar() {
//        super(ActuallyBlocks.SO.SOLAR_TILE.get());
//    }
//
//    @Override
//    public void writeSyncableNBT(CompoundNBT compound, NBTType type) {
//        super.writeSyncableNBT(compound, lookupProvider, type);
//        this.storage.writeToNBT(compound);
//    }
//
//    @Override
//    public void readSyncableNBT(CompoundNBT compound, NBTType type) {
//        super.readSyncableNBT(compound, lookupProvider, type);
//        this.storage.readFromNBT(compound);
//    }
//
//    @Override
//    public void updateEntity() {
//        super.updateEntity();
//        if (!this.level.isClientSide) {
//            int power = this.getPowerToGenerate(PRODUCE);
//            if (this.level.isDay() && power > 0) {
//                if (power <= this.storage.getMaxEnergyStored() - this.storage.getEnergyStored()) {
//                    this.storage.receiveEnergy(power, false);
//                    this.setChanged();
//                }
//            }
//
//            if (this.oldEnergy != this.storage.getEnergyStored() && this.sendUpdateWithInterval()) {
//                this.oldEnergy = this.storage.getEnergyStored();
//            }
//        }
//    }
//
//    public int getPowerToGenerate(int power) {
//        for (int y = 1; y <= this.level.getMaxBuildHeight() - this.worldPosition.getY(); y++) {
//            if (power > 0) {
//                BlockPos pos = this.worldPosition.above(y);
//                BlockState state = this.level.getBlockState(pos);
//
//                if (state.getMaterial().isSolidBlocking()) {
//                    power = 0;
//                } else if (!state.getBlock().isAir(state, this.level, pos)) {
//                    power--;
//                }
//            } else {
//                break;
//            }
//        }
//
//        return power;
//    }
//
//    @Override
//    public CustomEnergyStorage getEnergyStorage() {
//        return this.storage;
//    }
//
//    @Override
//    public boolean needsHoldShift() {
//        return false;
//    }
//
//    @Override
//    public int getEnergyToSplitShare() {
//        return this.storage.getEnergyStored();
//    }
//
//    @Override
//    public boolean doesShareEnergy() {
//        return true;
//    }
//
//    @Override
//    public Direction[] getEnergyShareSides() {
//        return Direction.values();
//    }
//
//    @Override
//    public boolean canShareTo(TileEntity tile) {
//        return true;
//    }
//
//    @Override
//    public IEnergyStorage getEnergyStorage(Direction facing) {
//        return this.lazyEnergy;
//    }
//}
