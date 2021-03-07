package cn.leomc.mobfarmutilities.common.block;

import cn.leomc.mobfarmutilities.common.api.InventoryWrapper;
import cn.leomc.mobfarmutilities.common.api.blockstate.IHasDirection;
import cn.leomc.mobfarmutilities.common.blockentity.FanBlockEntity;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockProperties;
import me.shedaniel.architectury.registry.MenuRegistry;
import me.shedaniel.architectury.registry.ToolType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;

import java.lang.reflect.InvocationTargetException;


public class FanBlock extends ActivatableBlock implements EntityBlock, IHasDirection, WorldlyContainerHolder {

    public FanBlock() {
        super(BlockProperties.of(Material.METAL)
                .tool(ToolType.PICKAXE, 1)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 6.0F)
        );
    }

    public static FanBlockEntity getTileEntity() {
        if (Platform.isForge()) {
            Class<FanBlockEntity> tileEntityClass;
            try {
                tileEntityClass = (Class<FanBlockEntity>) Class.forName("cn.leomc.mobfarmutilities.forge.ForgeFanBlockEntity");
                return tileEntityClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | ClassCastException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new FanBlockEntity();
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
        if (tileEntity instanceof FanBlockEntity) {
            ((FanBlockEntity) tileEntity).dropAllItems();
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getDirection(context, super.getStateForPlacement(context));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter worldIn) {
        return getTileEntity();
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor world, BlockPos pos) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof FanBlockEntity) {
            return new InventoryWrapper(((FanBlockEntity) tileEntity).getUpgradeHandler().getInventory());
        }
        return null;
    }

}
