package cn.leomc.mobfarmutilities.common.utils;

import me.shedaniel.architectury.platform.Platform;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class PlatformCompatibility {

    public static Constructor<? extends BucketItem> BUCKET_ITEM_CONSTRUCTOR;


    static {
        try {
            BUCKET_ITEM_CONSTRUCTOR = (Constructor<? extends BucketItem>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeBucketItem").getConstructor(Supplier.class, Item.Properties.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static BucketItem getBucketItem(Supplier<Fluid> fluid, Item.Properties properties) {
        if (Platform.isForge() && BUCKET_ITEM_CONSTRUCTOR != null) {
            try {
                return BUCKET_ITEM_CONSTRUCTOR.newInstance(fluid, properties);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new BucketItem(fluid.get(), properties);
    }

    public static Constructor<? extends LiquidBlock> LIQUID_BLOCK_CONSTRUCTOR;

    static {
        try {
            LIQUID_BLOCK_CONSTRUCTOR = (Constructor<? extends LiquidBlock>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeLiquidBlock").getConstructor(Supplier.class, BlockBehaviour.Properties.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static LiquidBlock getLiquidBlock(Supplier<Fluid> fluid, BlockBehaviour.Properties properties) {
        if (Platform.isForge() && LIQUID_BLOCK_CONSTRUCTOR != null) {
            try {
                return LIQUID_BLOCK_CONSTRUCTOR.newInstance(fluid, properties);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new cn.leomc.mobfarmutilities.common.api.LiquidBlock((FlowingFluid) fluid.get(), properties);
    }


}
