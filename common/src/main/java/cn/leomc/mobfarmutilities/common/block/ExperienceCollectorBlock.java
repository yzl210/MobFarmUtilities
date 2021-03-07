package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.blockentity.ExperienceCollectorBlockEntity;
import cn.leomc.mobfarmutilities.common.registry.ItemRegistry;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class ExperienceCollectorBlock extends ActivatableBlock implements EntityBlock {

    public ExperienceCollectorBlock() {
        super(BlockProperties.of(Material.METAL)
                .tool(ToolType.PICKAXE, 1)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
        );
    }

    public static ExperienceCollectorBlockEntity getTileEntity() {
        if (Platform.isForge()) {
            Class<ExperienceCollectorBlockEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<ExperienceCollectorBlockEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeExperienceCollectorBlockEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new ExperienceCollectorBlockEntity();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide || handIn != InteractionHand.MAIN_HAND)
            return InteractionResult.CONSUME;

        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.BUCKET && tileEntity instanceof ExperienceCollectorBlockEntity && !player.isShiftKeyDown())
            if (((ExperienceCollectorBlockEntity) tileEntity).getAmount() >= 1000) {
                if (player.isCreative())
                    player.addItem(new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                else
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get()));
                ((ExperienceCollectorBlockEntity) tileEntity).addAmount(-1000);
                worldIn.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ItemRegistry.LIQUID_EXPERIENCE_BUCKET.get() && tileEntity instanceof ExperienceCollectorBlockEntity && !player.isShiftKeyDown())
            if (((ExperienceCollectorBlockEntity) tileEntity).getAmount() <= ((ExperienceCollectorBlockEntity) tileEntity).getLimit() - 1000) {
                if (!player.isCreative())
                    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BUCKET));
                ((ExperienceCollectorBlockEntity) tileEntity).addAmount(1000);
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
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock())
            return;
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity instanceof ExperienceCollectorBlockEntity) {
            ((ExperienceCollectorBlockEntity) tileEntity).dropAllExperience();
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }


    @Override
    public @Nullable BlockEntity newBlockEntity(BlockGetter worldIn) {
        return getTileEntity();
    }

}
