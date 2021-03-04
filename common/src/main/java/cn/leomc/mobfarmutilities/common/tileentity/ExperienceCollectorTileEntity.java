package cn.leomc.mobfarmutilities.common.tileentity;

import cn.leomc.mobfarmutilities.MobFarmUtilities;
import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.block.ActivatableBlock;
import cn.leomc.mobfarmutilities.common.container.ExperienceCollectorContainer;
import cn.leomc.mobfarmutilities.common.registry.TileEntityRegistry;
import me.shedaniel.architectury.extensions.BlockEntityExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ExperienceCollectorTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider, BlockEntityExtension {

    protected int limit = 5000;
    private int amount;


    public ExperienceCollectorTileEntity() {
        super(TileEntityRegistry.EXPERIENCE_COLLECTOR.get());
        amount = 0;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container." + MobFarmUtilities.MODID + ".experience_collector");
    }

    @Override
    public @Nullable Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        syncData();
        return new ExperienceCollectorContainer(this, playerEntity, playerInventory, i);
    }

    @Override
    public void tick() {
        if (world.isRemote)
            return;
        BlockState state = world.getBlockState(pos);
        RedstoneMode.updateRedstone(state, world, pos);
        if (!state.get(ActivatableBlock.ACTIVE))
            return;
        if (amount >= limit)
            return;
        AxisAlignedBB area = new AxisAlignedBB(pos
                .offset(Direction.NORTH, 5)
                .offset(Direction.WEST, 5)
                .offset(Direction.UP, 2)
                , pos
                .offset(Direction.SOUTH, 5)
                .offset(Direction.EAST, 5)
                .offset(Direction.DOWN, 2)
                .add(1, 1, 1));
        List<ExperienceOrbEntity> entities = world.getEntitiesWithinAABB(ExperienceOrbEntity.class, area);
        for (ExperienceOrbEntity experienceOrbEntity : entities) {
            if (experienceOrbEntity.removed)
                continue;
            int xpValue = experienceOrbEntity.getXpValue();
            if (xpValue >= 0) {
                experienceOrbEntity.remove();
                addAmount(xpValue);
            }
        }

    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        amount = nbt.getInt("Experience");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Experience", amount);
        return super.write(compound);
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
            world.addEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY() + 0.5D, pos.getZ(), amount));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void loadClientData(@NotNull BlockState pos, @NotNull CompoundNBT tag) {
        amount = tag.getInt("Experience");
    }

    @Override
    public @NotNull CompoundNBT saveClientData(@NotNull CompoundNBT tag) {
        tag.putInt("Experience", amount);
        return tag;
    }
}
