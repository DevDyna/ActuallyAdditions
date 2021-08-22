package de.ellpeck.actuallyadditions.data;

import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;

import java.nio.file.Path;
import java.util.function.Consumer;

public class ItemRecipeGenerator extends RecipeProvider {
    public ItemRecipeGenerator(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
        super.buildShapelessRecipes(consumer);
    }

    @Override
    protected void saveAdvancement(DirectoryCache cache, JsonObject cache2, Path advancementJson) {
        //Nope...
    }
}
