package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.common.blockentity.ItemCollectorBlockEntity;
import cn.leomc.mobfarmutilities.fabric.api.IItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class FabricItemCollectorBlockEntity extends ItemCollectorBlockEntity implements IItemStorage {

    private final Storage<ItemVariant> storage = InventoryStorage.of(inventory, null);

    public FabricItemCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public Storage<ItemVariant> getStorage() {
        return storage;
    }
}
