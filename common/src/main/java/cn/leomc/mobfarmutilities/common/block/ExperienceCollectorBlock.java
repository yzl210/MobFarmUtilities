package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import cn.leomc.mobfarmutilities.common.utils.PlatformCompatibility;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExperienceCollectorBlock extends ActivatableBlock implements EntityBlock {

    public ExperienceCollectorBlock() {
        super(Properties.of(Material.METAL)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
        );
    }

    public static ExperienceCollectorBlockEntity getBlockEntity(BlockPos pos, BlockState state) {
        return (ExperienceCollectorBlockEntity) PlatformCompatibility.getBlockEntity(PlatformCompatibility.BlockEntityType.EXPERIENCE_COLLECTOR, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide || handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BUCKET && tileEntity instanceof ExperienceCollectorBlockEntity experienceCollector && !player.isShiftKeyDown())
            if (experienceCollector.getAmount() >= 1000) {
                if (player.isCreative())
                    player.addItem(new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                else
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                experienceCollector.addAmount(-1000);
                worldIn.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get() && tileEntity instanceof ExperienceCollectorBlockEntity experienceCollector && !player.isShiftKeyDown())
            if (experienceCollector.getAmount() <= experienceCollector.getLimit() - 1000) {
                if (!player.isCreative())
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                experienceCollector.addAmount(1000);
                worldIn.playSound(null, pos, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

        if (tileEntity instanceof MenuProvider) {
            if (player instanceof ServerPlayer)
                MenuRegistry.openExtendedMenu((ServerPlayer) player, (MenuProvider) tileEntity, packetBuffer -> packetBuffer.writeBlockPos(tileEntity.getBlockPos()));
            return InteractionResult.SUCCESS;
        } else
            throw new IllegalStateException("Named Container not found!");
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof ExperienceCollectorBlockEntity) {
            ((ExperienceCollectorBlockEntity) tileEntity).dropAllExperience(level);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return getBlockEntity(pos, state);
    }

}
