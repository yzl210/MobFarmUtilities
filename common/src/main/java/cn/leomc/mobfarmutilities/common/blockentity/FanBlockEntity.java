package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.api.UpgradeHandler;
import cn.leomc.mobfarmutilities.common.api.UpgradeType;
import cn.leomc.mobfarmutilities.common.api.blockstate.Upgradable;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.compat.IInfoProvider;
import cn.leomc.mobfarmutilities.common.menu.FanMenu;
import cn.leomc.mobfarmutilities.common.network.NetworkHandler;
import cn.leomc.mobfarmutilities.common.network.message.MotionChangeMessage;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FanBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider, Upgradable, BlockEntityExtension, IInfoProvider, IHasArea {


    protected final UpgradeHandler upgradeHandler = new UpgradeHandler(this, UpgradeType.FAN_SPEED, UpgradeType.FAN_DISTANCE, UpgradeType.FAN_WIDTH, UpgradeType.FAN_HEIGHT);
    protected boolean renderArea = false;


    public FanBlockEntity() {
        super(BlockEntityRegistry.FAN.get());
    }

    @Override
    public void tick() {
        if (level.isClientSide)
            return;
        BlockState state = level.getBlockState(worldPosition);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;

        Direction facing = state.getValue(DirectionalBlock.FACING);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, getAABB());
        double speed = 0.9 - (upgradeHandler.getUpgradeLevel(UpgradeType.FAN_SPEED) * 0.25);
        double x = facing.getStepX();
        if (x == 1)
            x -= speed;
        else if (x == -1)
            x += speed;
        double y = facing.getStepY();
        if (y == 1)
            y -= speed;
        else if (y == -1)
            y += speed;
        double z = facing.getStepZ();
        if (z == 1)
            z -= speed;
        else if (z == -1)
            z += speed;
        for (Entity entity : entities) {
            try {
                if (entity instanceof ServerPlayer)
                    NetworkHandler.INSTANCE.sendToPlayer((ServerPlayer) entity, new MotionChangeMessage(new Vec3(x, y, z)));

            } catch (NullPointerException ignored) {
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(x, y, z));
        }
    }


    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }


    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        upgradeHandler.read(nbt);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        return upgradeHandler.write(super.save(compound));
    }

    public void dropAllItems() {
        upgradeHandler.dropAllItem(level, worldPosition);
    }


    @Override
    public AbstractContainerMenu createMenu(int windowID, Inventory playerInventory, Player playerEntity) {
        syncData();
        return new FanMenu(this, playerEntity, playerInventory, windowID);
    }


    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MobFarmUtilities.MODID + ".fan");
    }


    @Override
    public void loadClientData(@NotNull BlockState pos, @NotNull CompoundTag tag) {
        upgradeHandler.read(tag);
    }

    @Override
    public @NotNull CompoundTag saveClientData(@NotNull CompoundTag tag) {
        return upgradeHandler.write(tag, false);
    }

    @Override
    public List<Component> getInfo() {
        List<Component> components = new ArrayList<>();
        upgradeHandler.getSupportedUpgrades().forEach(type -> components.add(new TranslatableComponent("text.mobfarmutilities.info.upgrade", new TranslatableComponent(type.getTranslationKey()), upgradeHandler.getUpgradeLevel(type), type.getMaxLevel())));
        return components;
    }

    @Override
    public AABB getAABB() {
        BlockState state = level.getBlockState(worldPosition);
        Direction facing = state.getValue(DirectionalBlock.FACING);
        Direction widthDirection;
        Direction heightDirection;
        if (facing == Direction.UP || facing == Direction.DOWN) {
            widthDirection = Direction.EAST;
            heightDirection = Direction.NORTH;
        } else {
            widthDirection = facing.getClockWise();
            heightDirection = Direction.UP;
        }
        return new AABB(worldPosition
                .relative(widthDirection.getOpposite(), upgradeHandler.getUpgradeLevel(UpgradeType.FAN_WIDTH))
                .relative(heightDirection.getOpposite(), upgradeHandler.getUpgradeLevel(UpgradeType.FAN_HEIGHT))
                , worldPosition
                .relative(facing, 5 + upgradeHandler.getUpgradeLevel(UpgradeType.FAN_DISTANCE))
                .relative(widthDirection, upgradeHandler.getUpgradeLevel(UpgradeType.FAN_WIDTH))
                .relative(heightDirection, upgradeHandler.getUpgradeLevel(UpgradeType.FAN_HEIGHT))
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
