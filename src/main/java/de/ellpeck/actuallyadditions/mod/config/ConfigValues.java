/*
 * This file ("ConfigValues.java") is part of the Actually Additions mod for Minecraft.
 * It is created and owned by Ellpeck and distributed
 * under the Actually Additions License to be found at
 * http://ellpeck.de/actaddlicense
 * View the source code at https://github.com/Ellpeck/ActuallyAdditions
 *
 * © 2015-2017 Ellpeck
 */

package de.ellpeck.actuallyadditions.mod.config;

import net.minecraft.world.item.Item;

public final class ConfigValues {
    //
    public static Item itemRedstoneTorchConfigurator;
    public static Item itemCompassConfigurator;
//
//    public static void defineConfigValues(Configuration config) {
//        for (ConfigIntValues currConf : ConfigIntValues.values()) {
//            currConf.currentValue = config.get(currConf.category, currConf.name, currConf.defaultValue, currConf.desc, currConf.min, currConf.max).getInt();
//        }
//
//        for (ConfigBoolValues currConf : ConfigBoolValues.values()) {
//            currConf.currentValue = config.get(currConf.category, currConf.name, currConf.defaultValue, currConf.desc).getBoolean();
//        }
//
//        for (ConfigIntListValues currConf : ConfigIntListValues.values()) {
//            currConf.currentValue = config.get(currConf.category, currConf.name, currConf.defaultValue, currConf.desc).getIntList();
//        }
//
//        for (ConfigStringListValues currConf : ConfigStringListValues.values()) {
//            currConf.currentValue = config.get(currConf.category, currConf.name, currConf.defaultValue, currConf.desc).getStringList();
//        }
//
//        parseConfiguratorConfig();
//    }
//
//    private static void parseConfiguratorConfig() {
//        itemRedstoneTorchConfigurator = null;
//        itemCompassConfigurator = null;
//
//        String[] conf = ConfigStringListValues.CONFIGURE_ITEMS.getValue();
//        if (conf.length == 2) {
//            itemRedstoneTorchConfigurator = Item.REGISTRY.getObject(ResourceLocation.tryParse(conf[0]));
//            itemCompassConfigurator = Item.REGISTRY.getObject(ResourceLocation.tryParse(conf[1]));
//        }
//
//        if (itemRedstoneTorchConfigurator == null || itemCompassConfigurator == null) {
//            ActuallyAdditions.LOGGER.error("Parsing the Configuration Items config failed, reverting back to the default settings!");
//
//            itemRedstoneTorchConfigurator = Item.byBlock(Blocks.REDSTONE_TORCH);
//            itemCompassConfigurator = Items.COMPASS;
//        }
//    }
}
