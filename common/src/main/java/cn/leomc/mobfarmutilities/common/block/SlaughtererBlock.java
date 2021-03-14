package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.blockentity.SlaughtererBlockEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

public class SlaughtererBlock extends ActivatableBlock implements EntityBlock {
    public SlaughtererBlock() {
        super(BlockProperties.of(Material.METAL)
                .tool(ToolType.PICKAXE, 1)
                .strength(1.5F, 6.0F)
                .requiresCorrectToolForDrops()
        );
    }

    public static SlaughtererBlockEntity getBlockEntity() {
        if (Platform.isForge()) {
            Class<SlaughtererBlockEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<SlaughtererBlockEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeSlaughtererBlockEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new SlaughtererBlockEntity();
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
    public BlockEntity newBlockEntity(BlockGetter blockGetter) {
        return getBlockEntity();
    }
}
