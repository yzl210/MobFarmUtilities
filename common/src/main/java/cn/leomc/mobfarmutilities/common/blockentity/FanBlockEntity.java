package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.*;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.compat.IInfoProvider;
import cn.leomc.mobfarmutilities.common.menu.FanMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import cn.leomc.mobfarmutilities.common.utils.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FanBlockEntity extends BaseBlockEntity implements MenuProvider, Upgradable, IInfoProvider, IHasArea {

    public static final EntityTypeTest<Entity, Entity> NO_LIVING_ENTITY = new EntityTypeTest<>() {
        @Override
        public Entity tryCast(@NotNull Entity entity) {
            return entity instanceof LivingEntity ? null : entity;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };


    protected final UpgradeHandler upgradeHandler = new UpgradeHandler(this, UpgradeType.FAN_SPEED, UpgradeType.FAN_DISTANCE, UpgradeType.FAN_WIDTH, UpgradeType.FAN_HEIGHT);
    protected boolean renderArea = false;


    public FanBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.FAN.get(), pos, state);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;

        Direction facing = state.getValue(DirectionalBlock.FACING);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, getAABB());
        double speed = 0.9 - (upgradeHandler.getUpgradeLevel(UpgradeType.FAN_SPEED) * 0.25);
        Vec3 motion = getMotion(speed, facing);

        for (Entity entity : entities) {
            if (!(entity instanceof ServerPlayer))
                entity.setDeltaMovement(entity.getDeltaMovement().add(motion));
        }
    }

    @Override
    public void clientTick(ClientLevel level, BlockPos pos, BlockState state) {
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;

        Vec3 motion = getMotion(0.9 - (upgradeHandler.getUpgradeLevel(UpgradeType.FAN_SPEED) * 0.25), state.getValue(DirectionalBlock.FACING));

        if(level.getEntitiesOfClass(LocalPlayer.class, getAABB()).contains(Utils.getLocalPlayer()))
            Utils.getLocalPlayer().setDeltaMovement(Utils.getLocalPlayer().getDeltaMovement().add(motion));

        level.getEntities(NO_LIVING_ENTITY, getAABB(), EntitySelector.NO_SPECTATORS)
                .forEach(entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(motion)));
    }

    private Vec3 getMotion(double speed, Direction facing){
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
        return new Vec3(x, y, z);
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }


    @Override
    public void loadData(CompoundTag tag) {
        upgradeHandler.read(tag.getCompound("upgrade"));
    }

    @Override
    public void saveData(CompoundTag tag) {
        tag.put("upgrade", upgradeHandler.write(new CompoundTag()));
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
    public void loadClientData(CompoundTag tag) {
        upgradeHandler.read(tag.getCompound("upgrade"));
    }

    @Override
    public void saveClientData(CompoundTag tag) {
        tag.put("upgrade", upgradeHandler.write(new CompoundTag(), false));
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
        Optional<Direction> optional = state.getOptionalValue(DirectionalBlock.FACING);
        if (optional.isEmpty())
            return null;
        Direction direction = optional.get();

        int width = upgradeHandler.getUpgradeLevel(UpgradeType.FAN_WIDTH);
        int height = upgradeHandler.getUpgradeLevel(UpgradeType.FAN_HEIGHT);

        int distance = 5 + upgradeHandler.getUpgradeLevel(UpgradeType.FAN_DISTANCE);
        for (int i = 1; i <= distance; i++)
            if (level.getBlockState(worldPosition.relative(direction, i)).isCollisionShapeFullBlock(level, worldPosition))
                distance = i - 1;



        BlockPos.MutableBlockPos start = new BlockPos.MutableBlockPos(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ());
        BlockPos.MutableBlockPos end = new BlockPos.MutableBlockPos(worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1);

        switch (direction){
            case DOWN -> {
                start.move(-height, -distance, -width);
                end.move(height, -1, width);
            }
            case UP -> {
                start.move(-height, 1, -width);
                end.move(height, distance, width);
            }
            case NORTH -> {
                start.move(-width, -height, -distance);
                end.move(width, height, -1);
            }
            case SOUTH -> {
                start.move(-width, -height, 1);
                end.move(width, height, distance);
            }
            case WEST -> {
                start.move(-distance, -height, -width);
                end.move(-1, height, width);
            }
            case EAST -> {
                start.move(1, -height, -width);
                end.move(distance, height, width);
            }
        }

        return new AABB(start, end);
    }

    @Override
    public AABB getRenderAABB() {
        AABB aabb = getAABB();
        return aabb == null ? null : aabb.move(-worldPosition.getX(), -worldPosition.getY(), -worldPosition.getZ());
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
