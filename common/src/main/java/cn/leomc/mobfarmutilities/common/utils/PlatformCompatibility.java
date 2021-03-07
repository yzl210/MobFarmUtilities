package cn.leomc.mobfarmutilities.common.utils;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class PlatformCompatibility {

    public static BucketItem getBucketItem(Supplier<Fluid> fluid, Item.Properties properties) {
        if (Platform.isForge()) {
            try {
                return BucketItem.class.getConstructor(Supplier.class, Item.Properties.class).newInstance(fluid, properties);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new BucketItem(fluid.get(), properties);
    }

    public static LiquidBlock getFlowingFluidBlock(Supplier<Fluid> fluid, Block.Properties properties) {
        if (Platform.isForge()) {
            try {
                return LiquidBlock.class.getConstructor(Supplier.class, Block.Properties.class).newInstance(fluid, properties);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new cn.leomc.mobfarmutilities.common.api.FlowingFluidBlock((FlowingFluid) fluid.get(), properties);
    }

    public static Supplier<List<Item>> getTagItems(ResourceLocation resourceLocation, Item... items) {
        return () -> {
            List<Item> itemList = new ArrayList<>();
            Tag<Item> tag = ItemTags.getAllTags().getTag(resourceLocation);
            if (tag == null)
                Collections.addAll(itemList, items);
            else
                itemList.addAll(tag.getValues());


            return itemList;
        };
    }

}
