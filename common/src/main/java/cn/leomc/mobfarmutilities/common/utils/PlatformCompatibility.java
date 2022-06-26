package cn.leomc.mobfarmutilities.common.utils;

import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class PlatformCompatibility {

    @ExpectPlatform
    public static BlockEntity getBlockEntity(BlockEntityType type, BlockPos pos, BlockState state){
        throw new AssertionError();
    }


    public enum BlockEntityType{
        EXPERIENCE_COLLECTOR,
        ITEM_COLLECTOR,
        FAN,
        SLAUGHTERER
    }

    @ExpectPlatform
    public static LiquidExperienceFluid getFluid(boolean flowing){
        throw new AssertionError();
    }


    @ExpectPlatform
    public static BucketItem getBucketItem(Supplier<FlowingFluid> fluid, Item.Properties properties){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ServerPlayer getFakePlayer(ServerLevel level, String name){
        throw new AssertionError();
    }
}
