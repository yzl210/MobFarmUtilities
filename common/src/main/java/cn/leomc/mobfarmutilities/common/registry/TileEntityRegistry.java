package cn.leomc.mobfarmutilities.common.registry;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.tileentity.FanTileEntity;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;


public class TileEntityRegistry {

    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(MobFarmUtilities.MODID, Registry.BLOCK_ENTITY_TYPE_KEY);


    public static final RegistrySupplier<TileEntityType<FanTileEntity>> FAN = TILE_ENTITIES.register("fan",
            () -> TileEntityType.Builder.create(FanTileEntity::new, BlockRegistry.FAN.get()).build(null));


    public static void register() {
        TILE_ENTITIES.register();
    }

}
