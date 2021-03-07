package cn.leomc.mobfarmutilities.common.blockentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.menu.ExperienceCollectorMenu;
import cn.leomc.mobfarmutilities.common.registry.BlockEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExperienceCollectorBlockEntity extends BlockEntity implements TickableBlockEntity, MenuProvider, BlockEntityExtension {

    protected int limit = 5000;
    private int amount;


    public ExperienceCollectorBlockEntity() {
        super(BlockEntityRegistry.EXPERIENCE_COLLECTOR.get());
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
    public void tick() {
        if (level.isClientSide)
            return;
        BlockState state = level.getBlockState(worldPosition);
        RedstoneMode.updateRedstone(state, level, worldPosition);
        if (!state.getValue(ActivatableBlock.ACTIVE))
            return;
        if (amount >= limit)
            return;
        AABB area = new AABB(worldPosition
                .relative(Direction.NORTH, 5)
                .relative(Direction.WEST, 5)
                .relative(Direction.UP, 2)
                , worldPosition
                .relative(Direction.SOUTH, 5)
                .relative(Direction.EAST, 5)
                .relative(Direction.DOWN, 2)
                .offset(1, 1, 1));
        List<ExperienceOrb> entities = level.getEntitiesOfClass(ExperienceOrb.class, area);
        for (ExperienceOrb experienceOrbEntity : entities) {
            if (experienceOrbEntity.removed)
                continue;
            int xpValue = experienceOrbEntity.getValue();
            if (xpValue >= 0) {
                experienceOrbEntity.remove();
                addAmount(xpValue);
            }
        }

    }


    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        amount = nbt.getInt("Experience");
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.putInt("Experience", amount);
        return super.save(compound);
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
        amountChanged();
        syncData();
    }

    public void addAmount(int amount) {
        this.amount += amount;
        if (this.amount > limit)
            this.amount = limit;
        if (this.amount < 0)
            this.amount = 0;
        amountChanged();
        syncData();
    }

    public int getLimit() {
        return limit;
    }

    protected void amountChanged() {
    }

    public void dropAllExperience() {
        if (amount > 0)
            level.addFreshEntity(new ExperienceOrb(level, worldPosition.getX(), worldPosition.getY() + 0.5D, worldPosition.getZ(), amount));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void loadClientData(@NotNull BlockState pos, @NotNull CompoundTag tag) {
        amount = tag.getInt("Experience");
    }

    @Override
    public @NotNull CompoundTag saveClientData(@NotNull CompoundTag tag) {
        tag.putInt("Experience", amount);
        return tag;
    }
}
