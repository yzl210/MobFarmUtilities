package cn.leomc.mobfarmutilities.fabric;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.FluidRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class FabricExperienceCollectorBlockEntity extends ExperienceCollectorBlockEntity {

    private final Storage<FluidVariant> storage = new SingleSlotStorage<>() {

        @Override
        public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            return 0;
        }

        @Override
        public boolean supportsInsertion() {
            return false;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            if(!FluidRegistry.LIQUID_EXPERIENCE.get().isSame(resource.getFluid()))
                return 0;
            int drained = Math.min((int) getAmount(), (int) maxAmount);
            addAmount(-drained);
            return drained;
        }

        @Override
        public boolean isResourceBlank() {
            return getAmount() <= 0;
        }

        @Override
        public FluidVariant getResource() {
            return FluidVariant.of(FluidRegistry.LIQUID_EXPERIENCE.get());
        }

        @Override
        public long getAmount() {
            return FabricExperienceCollectorBlockEntity.this.getAmount();
        }

        @Override
        public long getCapacity() {
            return limit;
        }

        @Override
        public @NotNull StorageView<FluidVariant> exactView(TransactionContext transaction, FluidVariant resource) {
            return this;
        }
    };

    public FabricExperienceCollectorBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public Storage<FluidVariant> getFluidStorage(){
        return storage;
    }

}
