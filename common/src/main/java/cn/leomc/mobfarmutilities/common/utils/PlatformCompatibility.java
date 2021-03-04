package cn.leomc.mobfarmutilities.common.utils;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

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

    public static FlowingFluidBlock getFlowingFluidBlock(Supplier<Fluid> fluid, Block.Properties properties) {
        if (Platform.isForge()) {
            try {
                return FlowingFluidBlock.class.getConstructor(Supplier.class, Block.Properties.class).newInstance(fluid, properties);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new cn.leomc.mobfarmutilities.common.api.FlowingFluidBlock((FlowingFluid) fluid.get(), properties);
    }

    public static Supplier<List<Item>> getTagItems(ResourceLocation resourceLocation, Item... items) {
        return () -> {
            List<Item> itemList = new ArrayList<>();
            ITag<Item> tag = ItemTags.getCollection().get(resourceLocation);
            if (tag == null)
                Collections.addAll(itemList, items);
            else
                itemList.addAll(tag.getAllElements());


            return itemList;
        };
    }

}
