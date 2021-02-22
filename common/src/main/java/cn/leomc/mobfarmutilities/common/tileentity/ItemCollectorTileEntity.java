package cn.leomc.mobfarmutilities.common.tileentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.container.ItemCollectorContainer;
import cn.leomc.mobfarmutilities.common.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCollectorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    protected Inventory inventory;

    public ItemCollectorTileEntity() {
        super(TileEntityRegistry.ITEM_COLLECTOR.get());
        inventory = new Inventory(18);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + MobFarmUtilities.MODID + ".item_collector");
    }

    @Override
    public @Nullable Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ItemCollectorContainer(this, playerEntity, playerInventory, i);
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState state = world.getBlockState(pos);
        updateRedstone();
        if (!state.get(ActivatableBlock.ACTIVE))
            return;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos
                .offset(Direction.NORTH, 5)
                .offset(Direction.WEST, 5)
                .offset(Direction.UP, 2)
                , pos
                .offset(Direction.SOUTH, 5)
                .offset(Direction.EAST, 5)
                .offset(Direction.DOWN, 2)
                .add(1, 1, 1));
        List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, axisAlignedBB);
        for (ItemEntity itemEntity : entities) {
            ItemStack itemStack = inventory.addItem(itemEntity.getItem());
            itemEntity.setItem(itemStack);
        }
    }

    public void updateRedstone() {
        BlockState state = getBlockState();
        RedstoneMode mode = state.get(ActivatableBlock.MODE);
        if (mode == RedstoneMode.HIGH)
            if (world.isBlockPowered(pos))
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
            else
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, false));
        if (mode == RedstoneMode.LOW)
            if (world.isBlockPowered(pos))
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, false));
            else
                world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
        if (mode == RedstoneMode.IGNORED)
            world.setBlockState(pos, state.with(ActivatableBlock.ACTIVE, true));
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        inventory.read(nbt.getList("inventory", 10));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inventory", inventory.write());
        return super.write(compound);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void dropAllItems() {
        InventoryHelper.dropInventoryItems(world, pos, inventory);
        inventory.clear();
    }

}
