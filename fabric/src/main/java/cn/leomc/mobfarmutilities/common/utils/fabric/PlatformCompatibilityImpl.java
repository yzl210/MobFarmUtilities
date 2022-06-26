package cn.leomc.mobfarmutilities.common.utils.fabric;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.fluid.LiquidExperienceFluid;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import cn.leomc.mobfarmutilities.fabric.FabricExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.fabric.FabricFanBlockEntity;
import cn.leomc.mobfarmutilities.fabric.FabricItemCollectorBlockEntity;
import cn.leomc.mobfarmutilities.fabric.FabricSlaughtererBlockEntity;
import dev.architectury.utils.GameInstance;
import dev.cafeteria.fakeplayerapi.server.FakePlayerBuilder;
import dev.cafeteria.fakeplayerapi.server.FakeServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class PlatformCompatibilityImpl {
    public static BlockEntity getBlockEntity(PlatformCompatibility.BlockEntityType type, BlockPos pos, BlockState state) {
        return switch (type){
            case EXPERIENCE_COLLECTOR -> new FabricExperienceCollectorBlockEntity(pos, state);
            case ITEM_COLLECTOR -> new FabricItemCollectorBlockEntity(pos, state);
            case FAN -> new FabricFanBlockEntity(pos, state);
            case SLAUGHTERER -> new FabricSlaughtererBlockEntity(pos, state);
        };
    }

    public static LiquidExperienceFluid getFluid(boolean flowing) {
        if(flowing)
            return new LiquidExperienceFluid.Flowing();
        else
            return new LiquidExperienceFluid.Still();
    }

    public static BucketItem getBucketItem(Supplier<FlowingFluid> fluid, Item.Properties properties) {
        return new BucketItem(fluid.get(), properties);
    }

    private static final Map<String, FakeServerPlayer> FAKE_PLAYERS = new HashMap<>();

    public static void unloadLevel(MinecraftServer server, ServerLevel level) {
        FAKE_PLAYERS.entrySet().removeIf((entry) -> entry.getValue().level == level);
    }

    public static ServerPlayer getFakePlayer(ServerLevel level, String name) {
        FakeServerPlayer player = FAKE_PLAYERS.computeIfAbsent(name, s -> new FakePlayerBuilder(new ResourceLocation(MobFarmUtilities.MODID, s.toLowerCase(Locale.ROOT))).create(GameInstance.getServer(), level, s));
        player.setGameMode(GameType.CREATIVE);
        player.setInvulnerable(true);
        return player;
    }
}
