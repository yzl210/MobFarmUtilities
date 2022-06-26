package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.common.blockentity.FanBlockEntity;
import cn.leomc.mobfarmutilities.fabric.api.IItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FabricFanBlockEntity extends FanBlockEntity implements IItemStorage {

    private final Storage<ItemVariant> storage = InventoryStorage.of(upgradeHandler.getInventory(), null);


    public FabricFanBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }
    @Override
    public Storage<ItemVariant> getStorage() {
        return storage;
    }
}
