package cn.leomc.mobfarmutilities.common.tileentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.api.UpgradeHandler;
import cn.leomc.mobfarmutilities.common.api.blockstate.Upgradable;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.container.FanContainer;
import cn.leomc.mobfarmutilities.common.item.upgrade.UpgradeType;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.MotionChangeMessage;
import cn.leomc.mobfarmutilities.common.registry.TileEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FanTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, Upgradable, BlockEntityExtension {


    protected final UpgradeHandler upgradeHandler = new UpgradeHandler(this, UpgradeType.FAN_SPEED, UpgradeType.FAN_DISTANCE, UpgradeType.FAN_WIDTH, UpgradeType.FAN_HEIGHT);


    public FanTileEntity() {
        super(TileEntityRegistry.FAN.get());
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState state = world.getBlockState(pos);
        RedstoneMode.updateRedstone(state, world, pos);
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
                .offset(widthDirection.getOpposite(), upgradeHandler.getUpgradeLevel(UpgradeType.FAN_WIDTH))
                .offset(heightDirection.getOpposite(), upgradeHandler.getUpgradeLevel(UpgradeType.FAN_HEIGHT))
                , pos
                .offset(facing, 5 + upgradeHandler.getUpgradeLevel(UpgradeType.FAN_DISTANCE))
                .offset(widthDirection, upgradeHandler.getUpgradeLevel(UpgradeType.FAN_WIDTH))
                .offset(heightDirection, upgradeHandler.getUpgradeLevel(UpgradeType.FAN_HEIGHT))
                .add(1, 1, 1));
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, axisAlignedBB);
        double speed = 0.9 - (upgradeHandler.getUpgradeLevel(UpgradeType.FAN_SPEED) * 0.25);
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
                if (entity instanceof ServerPlayerEntity)
                    NetworkHandler.INSTANCE.sendToPlayer((ServerPlayerEntity) entity, new MotionChangeMessage(new Vector3d(x, y, z)));

            } catch (NullPointerException ignored) {
            }
            entity.setMotion(entity.getMotion().add(x, y, z));
        }
    }


    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        upgradeHandler.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return upgradeHandler.write(super.write(compound));
    }

    public void dropAllItems() {
        upgradeHandler.dropAllItem(world, pos);
    }


    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        syncData();
        return new FanContainer(this, playerEntity, playerInventory, windowID);
    }


    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + MobFarmUtilities.MODID + ".fan");
    }


    @Override
    public void loadClientData(@NotNull BlockState pos, @NotNull CompoundNBT tag) {
        upgradeHandler.read(tag);
    }

    @Override
    public @NotNull CompoundNBT saveClientData(@NotNull CompoundNBT tag) {
        return upgradeHandler.write(tag, false);
    }
}
