package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.block.FanBlock;
import cn.leomc.mobfarmutilities.common.block.ItemCollectorBlock;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import cn.leomc.mobfarmutilities.common.tileentity.ItemCollectorTileEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;


public class TileEntityRegistry {

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_ENTITY_TYPE_KEY);


    public static final RegistrySupplier<TileEntityType<FanTileEntity>> FAN = TILE_ENTITIES.register("fan",
            () -> TileEntityType.Builder.create(FanBlock::getTileEntity, BlockRegistry.FAN.get()).build(null));

    public static final RegistrySupplier<TileEntityType<ItemCollectorTileEntity>> ITEM_COLLECTOR = TILE_ENTITIES.register("item_collector",
            () -> TileEntityType.Builder.create(ItemCollectorBlock::getTileEntity, BlockRegistry.ITEM_COLLECTOR.get()).build(null));


    public static void register() {
        TILE_ENTITIES.register();
    }

}
