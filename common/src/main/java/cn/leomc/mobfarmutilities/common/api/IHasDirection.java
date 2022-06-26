package cn.leomc.mobfarmutilities.common.api;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public interface IHasDirection {

    DirectionProperty FACING = BlockStateProperties.FACING;

    default BlockState getDirection(BlockPlaceContext context, BlockState blockState) {
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
            return blockState.setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
        return blockState.setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

}
