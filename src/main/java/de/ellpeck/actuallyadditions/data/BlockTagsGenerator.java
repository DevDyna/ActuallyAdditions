package de.ellpeck.actuallyadditions.data;

import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.nio.file.Path;

public class BlockTagsGenerator extends BlockTagsProvider {
    public BlockTagsGenerator(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, ActuallyAdditions.MODID, existingFileHelper);
    }

    @Override
    public void addTags() {
        //        getOrCreateBuilder(BlockTags.WALLS).add(
        //            ActuallyBlocks.WHITE_WALL.get(),
        //            ActuallyBlocks.GREEN_WALL.get(),
        //            ActuallyBlocks.BLACK_QUARTZ_WALL.get(),
        //            ActuallyBlocks.BLACK_SMOOTH_QUARTZ_WALL.get(),
        //            ActuallyBlocks.BLACK_PILLAR_QUARTZ_WALL.get(),
        //            ActuallyBlocks.BLACK_CHISELED_QUARTZ_WALL.get(),
        //            ActuallyBlocks.BLACK_BRICK_QUARTZ_WALL.get()
        //        );
    }

    /**
     * Resolves a Path for the location to save the given tag.
     */
    @Override
    protected Path getPath(ResourceLocation id) {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/blocks/" + id.getPath() + ".json");
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    @Override
    public String getName() {
        return "Block Tags";
    }
}
