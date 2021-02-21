package cn.leomc.mobfarmutilities.common.block;


import cn.leomc.mobfarmutilities.common.api.RedstoneMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActivatableBlock extends DirectionalBlock {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final EnumProperty<RedstoneMode> MODE = EnumProperty.create("mode", RedstoneMode.class);

    protected ActivatableBlock(Properties builder) {
        super(builder);
    }

    public void updateRedstone(World worldIn, BlockState state, BlockPos pos) {
        if (worldIn.isRemote)
            return;
        RedstoneMode mode = state.get(MODE);
        if (mode == RedstoneMode.HIGH)
            if (worldIn.isBlockPowered(pos))
                worldIn.setBlockState(pos, state.with(ACTIVE, true));
            else
                worldIn.setBlockState(pos, state.with(ACTIVE, false));
        if (mode == RedstoneMode.LOW)
            if (worldIn.isBlockPowered(pos))
                worldIn.setBlockState(pos, state.with(ACTIVE, false));
            else
                worldIn.setBlockState(pos, state.with(ACTIVE, true));
        if (mode == RedstoneMode.IGNORED)
            worldIn.setBlockState(pos, state.with(ACTIVE, true));
    }


    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
        updateRedstone(worldIn, state, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        updateRedstone(worldIn, state, pos);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(ACTIVE, MODE);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context).with(ACTIVE, false).with(MODE, RedstoneMode.HIGH);
    }


}
