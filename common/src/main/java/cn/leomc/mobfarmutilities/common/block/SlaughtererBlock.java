package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlaughtererBlock extends ActivatableBlock implements EntityBlock, WorldlyContainerHolder {
    public SlaughtererBlock() {
        super(Properties.of(Material.METAL)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
        );
    }

    public static SlaughtererBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (SlaughtererBlockEntity) PlatformCompatibility.getBlockEntity(PlatformCompatibility.BlockEntityType.SLAUGHTERER, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide || handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof MenuProvider) {
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) blockEntity, packetBuffer -> packetBuffer.writeBlockPos(blockEntity.getBlockPos()));
            return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Named Container not found!");
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof SlaughtererBlockEntity) {
            ((SlaughtererBlockEntity) tileEntity).remove();
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return getBlockEntity(pos, state);
    }

    @Override
    public WorldlyContainer getContainer(@NotNull BlockState state, LevelAccessor accessor, @NotNull BlockPos pos) {
        BlockEntity blockEntity = accessor.getBlockEntity(pos);
        if (blockEntity instanceof SlaughtererBlockEntity slaughterer) {
            return new InventoryWrapper(slaughterer.getUpgradeHandler().getInventory());
        }
        return null;
    }
}
