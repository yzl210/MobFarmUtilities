package cn.leomc.mobfarmutilities.common.block;


import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import cn.leomc.mobfarmutilities.common.api.TickableBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

public abstract class ActivatableBlock extends Block implements EntityBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final EnumProperty<RedstoneMode> MODE = EnumProperty.create("mode", RedstoneMode.class);

    protected ActivatableBlock(Properties builder) {
        super(builder);
    }

    public void updateRedstone(Level worldIn, BlockState state, BlockPos pos) {
        if (worldIn.isClientSide)
            return;
        RedstoneMode mode = state.getValue(MODE);
        if (mode == RedstoneMode.HIGH)
            if (worldIn.hasNeighborSignal(pos))
                worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
            else
                worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
        if (mode == RedstoneMode.LOW)
            if (worldIn.hasNeighborSignal(pos))
                worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, false));
            else
                worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
        if (mode == RedstoneMode.IGNORED)
            worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, true));
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (level1, pos, state, blockEntity) -> {
            if(blockEntity instanceof TickableBlockEntity tickable){
                if(level1.isClientSide)
                    tickable.clientTick((ClientLevel) level1, pos, state);
                else
                    tickable.serverTick((ServerLevel) level1, pos, state);
            }
        };
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        updateRedstone(worldIn, state, pos);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        updateRedstone(worldIn, state, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE, MODE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(ACTIVE, false).setValue(MODE, RedstoneMode.HIGH);
    }




}
