package cn.leomc.mobfarmutilities.common.utils.forge;

import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import cn.leomc.mobfarmutilities.forge.*;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.common.util.FakePlayerFactory;

import java.util.function.Supplier;

public class PlatformCompatibilityImpl {
    public static BlockEntity getBlockEntity(PlatformCompatibility.BlockEntityType type, BlockPos pos, BlockState state) {
        return switch (type){
            case EXPERIENCE_COLLECTOR -> new ForgeExperienceCollectorBlockEntity(pos, state);
            case ITEM_COLLECTOR -> new ForgeItemCollectorBlockEntity(pos, state);
            case FAN -> new ForgeFanBlockEntity(pos, state);
            case SLAUGHTERER -> new ForgeSlaughtererBlockEntity(pos, state);
        };
    }

    public static LiquidExperienceFluid getFluid(boolean flowing) {
        if(flowing)
            return new ForgeLiquidExperienceFluid.Flowing();
        else
            return new ForgeLiquidExperienceFluid.Still();
    }

    public static BucketItem getBucketItem(Supplier<FlowingFluid> fluid, Item.Properties properties) {
        return new BucketItem(fluid, properties);
    }

    public static ServerPlayer getFakePlayer(ServerLevel level, String name) {
        return FakePlayerFactory.get(level, new GameProfile(null, name));
    }
}
