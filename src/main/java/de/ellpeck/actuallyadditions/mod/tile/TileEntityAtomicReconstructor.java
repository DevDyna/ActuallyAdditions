/*
 * This file ("TileEntityAtomicReconstructor.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.tile;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import de.ellpeck.actuallyadditions.api.internal.IAtomicReconstructor;
import de.ellpeck.actuallyadditions.api.lens.ILensItem;
import de.ellpeck.actuallyadditions.api.lens.Lens;
import de.ellpeck.actuallyadditions.mod.AASounds;
import de.ellpeck.actuallyadditions.mod.blocks.ActuallyBlocks;
import de.ellpeck.actuallyadditions.mod.components.ActuallyComponents;
import de.ellpeck.actuallyadditions.mod.config.CommonConfig;
import de.ellpeck.actuallyadditions.mod.network.PacketHelperServer;
import de.ellpeck.actuallyadditions.mod.util.ItemStackHandlerAA.IAcceptor;
import de.ellpeck.actuallyadditions.mod.util.WorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class TileEntityAtomicReconstructor extends TileEntityInventoryBase implements IEnergyDisplay, IAtomicReconstructor {

    public static final int ENERGY_USE = 1000;
    public final CustomEnergyStorage storage;
    public int counter;
    private int currentTime;
    private int oldEnergy;
    private int ttl = 0;
    private int maxAge = 0;
    private int beamColor = 0x1b6dff;

    public TileEntityAtomicReconstructor(BlockPos pos, BlockState state) {
        super(ActuallyBlocks.ATOMIC_RECONSTRUCTOR.getTileEntityType(), pos, state, 1);
        int power = CommonConfig.Machines.RECONSTRUCTOR_POWER.get();
        int recieve = Mth.ceil(power * 0.016666F);
        this.storage = new CustomEnergyStorage(power, recieve, 0);
    }

    public static void shootLaser(IAtomicReconstructor tile, ServerLevel world, double startX, double startY, double startZ, double endX, double endY, double endZ, Lens currentLens) {
        world.playSound(null, startX, startY, startZ, AASounds.RECONSTRUCTOR.get(), SoundSource.BLOCKS, 0.35F, 1.0F);
        PacketHelperServer.spawnLaserWithTimeServer(world, startX, startY, startZ, endX, endY, endZ, currentLens.getColor(), 25, 0, 0.2F, 0.8F);
    }

    @Override
    public int getTTL() {
        return this.ttl;
    }

    @Override
    public void resetBeam(int maxAge) {
        this.resetBeam(maxAge, 0x1b6dff);
    }

    @Override
    public void resetBeam(int maxAge, int color) {
        this.ttl = maxAge;
        this.maxAge = maxAge;
        this.beamColor = color;
    }

    public int getBeamColor() {
        return this.beamColor;
    }

    public float getProgress(){
        if (maxAge > 0)
            return (float)ttl / (float)maxAge;
        else
            return 0.0f;
    }

    public void decTTL() {
        if (this.ttl > 0)
            this.ttl--;
    }

    @Override
    public void writeSyncableNBT(CompoundTag compound, HolderLookup.Provider lookupProvider, NBTType type) {
        super.writeSyncableNBT(compound, lookupProvider, type);
        if (type != NBTType.SAVE_BLOCK) {
            compound.putInt("CurrentTime", this.currentTime);
            compound.putInt("Counter", this.counter);
        }
        this.storage.writeToNBT(compound);
    }

    @Override
    public boolean shouldSyncSlots() {
        return true;
    }

    @Override
    public void readSyncableNBT(CompoundTag compound, HolderLookup.Provider lookupProvider, NBTType type) {
        super.readSyncableNBT(compound, lookupProvider, type);
        if (type != NBTType.SAVE_BLOCK) {
            this.currentTime = compound.getInt("CurrentTime");
            this.counter = compound.getInt("Counter");
        }
        if (compound.contains("Energy"))
            this.storage.readFromNBT(compound);
    }

    public static <T extends BlockEntity> void clientTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityAtomicReconstructor tile) {
            tile.clientTick();
        }
    }

    public static <T extends BlockEntity> void serverTick(Level level, BlockPos pos, BlockState state, T t) {
        if (t instanceof TileEntityAtomicReconstructor tile) {
            tile.serverTick();

            if (!tile.isRedstonePowered && !tile.isPulseMode) {
                if (tile.currentTime > 0) {
                    tile.currentTime--;
                    if (tile.currentTime <= 0) {
                        ActuallyAdditionsAPI.methodHandler.invokeReconstructor(tile);
                    }
                } else {
                    tile.currentTime = 100;
                }
            }

            if (tile.oldEnergy != tile.storage.getEnergyStored() && tile.sendUpdateWithInterval()) {
                tile.oldEnergy = tile.storage.getEnergyStored();
                level.updateNeighbourForOutputSignal(pos, ActuallyBlocks.ATOMIC_RECONSTRUCTOR.get());
            }
        }
    }

    @Override
    public Lens getLens() {
        Item item = this.inv.getStackInSlot(0).getItem();
        if (item instanceof ILensItem) {
            return ((ILensItem) item).getLens();
        }
        return ActuallyAdditionsAPI.lensDefaultConversion;
    }

    @Override
    public Direction getOrientation() {
        BlockState state = this.level.getBlockState(this.worldPosition);
        return WorldUtil.getDirectionByPistonRotation(state);
    }

    @Override
    public BlockPos getPosition() {
        return this.worldPosition;
    }

    @Override
    public int getX() {
        return this.getBlockPos().getX();
    }

    @Override
    public int getY() {
        return this.getBlockPos().getY();
    }

    @Override
    public int getZ() {
        return this.getBlockPos().getZ();
    }

    @Override
    public Level getWorldObject() {
        return this.getLevel();
    }

    @Override
    public void extractEnergy(int amount) {
        this.storage.extractEnergyInternal(amount, false);
    }

    @Override
    public IAcceptor getAcceptor() {
        return (slot, stack, automation) -> !stack.isEmpty() && stack.getItem() instanceof ILensItem;
    }

    @Override
    public int getEnergy() {
        return this.storage.getEnergyStored();
    }

    @Override
    public CustomEnergyStorage getEnergyStorage() {
        return this.storage;
    }

    @Override
    public boolean needsHoldShift() {
        return false;
    }

    @Override
    public boolean isRedstoneToggle() {
        return true;
    }

    @Override
    public void activateOnPulse() {
        ActuallyAdditionsAPI.methodHandler.invokeReconstructor(this);
    }

    @Override
    public IEnergyStorage getEnergyStorage(Direction facing) {
        return this.storage;
    }

    @Override
    protected void applyImplicitComponents(@Nonnull DataComponentInput input) {
        super.applyImplicitComponents(input);

        storage.setEnergyStored(input.getOrDefault(ActuallyComponents.ENERGY_STORAGE, 0));
        this.isPulseMode = input.getOrDefault(ActuallyComponents.PULSE_MODE, false);
    }

    @Override
    protected void collectImplicitComponents(@Nonnull DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);

        builder.set(ActuallyComponents.ENERGY_STORAGE, storage.getEnergyStored());
        builder.set(ActuallyComponents.PULSE_MODE, isPulseMode);
    }
}
