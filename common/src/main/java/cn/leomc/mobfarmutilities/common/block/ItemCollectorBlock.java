package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.blockentity.ItemCollectorBlockEntity;
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
import org.jetbrains.annotations.Nullable;

public class ItemCollectorBlock extends ActivatableBlock implements EntityBlock, WorldlyContainerHolder {

    public ItemCollectorBlock() {
        super(Properties.of(Material.METAL)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
        );
    }

    public static ItemCollectorBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (ItemCollectorBlockEntity) PlatformCompatibility.getBlockEntity(PlatformCompatibility.BlockEntityType.ITEM_COLLECTOR, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide || handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof MenuProvider) {
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) tileEntity, packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getBlockPos()));
            return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Named Container not found!");
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof ItemCollectorBlockEntity) {
            ((ItemCollectorBlockEntity) tileEntity).dropAllItems();
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof ItemCollectorBlockEntity) {
            SimpleContainer inventory = ((ItemCollectorBlockEntity) tileEntity).getInventory();
            return new InventoryWrapper(inventory);
        }
        return null;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntity(pos, state);
    }
}
