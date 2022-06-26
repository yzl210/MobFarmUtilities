package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.IHasArea;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.menu.ExperienceCollectorMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExperienceCollectorBlockEntity extends BaseBlockEntity implements MenuProvider, IHasArea {

    protected int limit = 5000;
    private int amount;
    protected boolean renderArea = false;

    public ExperienceCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.EXPERIENCE_COLLECTOR.get(), pos, state);
        amount = 0;
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container." + MobFarmUtilities.MODID + ".experience_collector");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        syncData();
        return new ExperienceCollectorMenu(this, playerEntity, playerInventory, i);
    }

    @Override
    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        super.serverTick(level, pos, state);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;
        if (amount >= limit)
            return;

        List<ExperienceOrb> entities = level.getEntitiesOfClass(ExperienceOrb.class, getAABB());
        for (ExperienceOrb orb : entities) {
            if (orb.isRemoved())
                continue;
            int xpValue = orb.getValue();
            int canAdd = limit - amount;
            if (xpValue >= 0 && canAdd > 0) {
                int added = Math.min(xpValue, canAdd);
                addAmount(added);
                orb.discard();
                int left = xpValue - added;
                if(left > 0){
                    level.addFreshEntity(new ExperienceOrb(level, orb.getX(), orb.getY(), orb.getZ(), left));
                }
            }
        }

    }


    @Override
    public void loadData(CompoundTag tag) {
        amount = tag.getInt("Experience");
    }

    @Override
    public void saveData(CompoundTag tag) {
        tag.putInt("Experience", amount);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
        if (this.amount > limit)
            this.amount = limit;
        if (this.amount < 0)
            this.amount = 0;
        syncData();
    }

    public void addAmount(int amount) {
        this.amount += amount;
        if (this.amount > limit)
            this.amount = limit;
        if (this.amount < 0)
            this.amount = 0;
        syncData();
    }

    public int getLimit() {
        return limit;
    }


    public void dropAllExperience(Level level) {
        if (amount > 0)
            level.addFreshEntity(new ExperienceOrb(level, worldPosition.getX(), worldPosition.getY() + 0.5D, worldPosition.getZ(), amount));
    }

    @Override
    public void loadClientData(CompoundTag tag) {
        amount = tag.getInt("Experience");
    }

    @Override
    public void saveClientData(CompoundTag tag) {
        tag.putInt("Experience", amount);
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
