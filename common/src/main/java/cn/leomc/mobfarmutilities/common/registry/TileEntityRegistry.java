package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.ExperienceCollectorBlock;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import cn.leomc.mobfarmutilities.common.block.ItemCollectorBlock;
import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.blockentity.FanBlockEntity;
import cn.leomc.mobfarmutilities.common.blockentity.ItemCollectorBlockEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;


public class TileEntityRegistry {

    private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);


    public static final RegistrySupplier<BlockEntityType<FanBlockEntity>> FAN = TILE_ENTITIES.register("fan",
            () -> BlockEntityType.Builder.of(FanBlock::getTileEntity, BlockRegistry.FAN.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ItemCollectorBlockEntity>> ITEM_COLLECTOR = TILE_ENTITIES.register("item_collector",
            () -> BlockEntityType.Builder.of(ItemCollectorBlock::getTileEntity, BlockRegistry.ITEM_COLLECTOR.get()).build(null));

    public static final RegistrySupplier<BlockEntityType<ExperienceCollectorBlockEntity>> EXPERIENCE_COLLECTOR = TILE_ENTITIES.register("experience_collector",
            () -> BlockEntityType.Builder.of(ExperienceCollectorBlock::getTileEntity, BlockRegistry.EXPERIENCE_COLLECTOR.get()).build(null));


    public static void register() {
        TILE_ENTITIES.register();
    }

}
