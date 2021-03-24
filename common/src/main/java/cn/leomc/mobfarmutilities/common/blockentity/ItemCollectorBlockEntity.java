package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.menu.ItemCollectorMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemCollectorBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider, IHasArea {

    protected SimpleContainer inventory;
    protected boolean renderArea = false;

    public ItemCollectorBlockEntity() {
        super(BlockEntityRegistry.ITEM_COLLECTOR.get());
        inventory = new SimpleContainer(18);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MobFarmUtilities.MODID + ".item_collector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return new ItemCollectorMenu(this, playerEntity, playerInventory, i);
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;
        BlockState state = level.getBlockState(worldPosition);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;

        List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, getAABB());
        for (ItemEntity itemEntity : entities) {
            ItemStack itemStack = inventory.addItem(itemEntity.getItem());
            itemEntity.setItem(itemStack);
        }
    }


    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        inventory.fromTag(nbt.getList("inventory", 10));
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put("inventory", inventory.createTag());
        return super.save(compound);
    }

    public SimpleContainer getInventory() {
        return inventory;
    }

    public void dropAllItems() {
        Containers.dropContents(level, worldPosition, inventory);
        inventory.clearContent();
    }

    @Override
    public AABB getAABB() {
        return new AABB(worldPosition
                .relative(Direction.NORTH, 5)
                .relative(Direction.WEST, 5)
                .relative(Direction.UP, 2)
                , worldPosition
                .relative(Direction.SOUTH, 5)
                .relative(Direction.EAST, 5)
                .relative(Direction.DOWN, 2)
                .offset(1, 1, 1));
    }

    @Override
    public AABB getRenderAABB() {
        return getAABB().move(-worldPosition.getX(), -worldPosition.getY(), -worldPosition.getZ());
    }

    @Override
    public boolean doRenderArea() {
        return renderArea;
    }

    @Override
    public void setRenderArea(boolean renderArea) {
        this.renderArea = renderArea;
    }

}
