package cn.leomc.mobfarmutilities.common.tileentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.FanUpgradeItemStackHandler;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeItem;
import cn.leomc.mobfarmutilities.common.registry.TileEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;

public class FanTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private final FanUpgradeItemStackHandler item = new FanUpgradeItemStackHandler();
    private final LazyOptional<FanUpgradeItemStackHandler> itemOptional = LazyOptional.of(() -> item);

    public FanTileEntity() {
        super(TileEntityRegistry.FAN.get());
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState state = world.getBlockState(pos);
        updateRedstone();
        if (!state.get(ActivatableBlock.ACTIVE))
            return;
        Direction facing = state.get(DirectionalBlock.FACING);
        Direction widthDirection;
        Direction heightDirection;
        if (facing == Direction.UP || facing == Direction.DOWN) {
            widthDirection = Direction.EAST;
            heightDirection = Direction.NORTH;
        } else {
            widthDirection = facing.rotateY();
            heightDirection = Direction.UP;
        }
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(pos
                .offset(widthDirection.getOpposite(), item.getUpgrades(UpgradeItem.Type.FAN_WIDTH))
                .offset(heightDirection.getOpposite(), item.getUpgrades(UpgradeItem.Type.FAN_HEIGHT))
                , pos
                .offset(facing, 5 + item.getUpgrades(UpgradeItem.Type.FAN_DISTANCE))
                .offset(widthDirection, item.getUpgrades(UpgradeItem.Type.FAN_WIDTH))
                .offset(heightDirection, item.getUpgrades(UpgradeItem.Type.FAN_HEIGHT))
                .add(1, 1, 1));
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, axisAlignedBB);
        double speed = 0.9 - (item.getUpgrades(UpgradeItem.Type.FAN_SPEED) * 0.25);
        double x = facing.getXOffset();
        if (x == 1)
            x -= speed;
        else if (x == -1)
            x += speed;
        double y = facing.getYOffset();
        if (y == 1)
            y -= speed;
        else if (y == -1)
            y += speed;
        double z = facing.getZOffset();
        if (z == 1)
            z -= speed;
        else if (z == -1)
            z += speed;
        for (Entity entity : entities) {
            try {
                if (entity instanceof PlayerEntity)
                    MobFarmUtilities.PROXY.getClientPlayer().setMotion(entity.getMotion().add(x, y, z));
            } catch (NullPointerException ignored) {
            }
            entity.setMotion(entity.getMotion().add(x, y, z));
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
        item.deserializeNBT(nbt.getCompound("inv"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", item.serializeNBT());
        return super.write(compound);
    }

    public void dropAllItems() {
        InventoryHelper.dropItems(world, pos, item.getStacks());
        item.clear();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return itemOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new FanContainer(this, playerEntity, playerInventory, windowID);
    }


    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + MobFarmUtilities.MODID + ".fan");
    }


}
