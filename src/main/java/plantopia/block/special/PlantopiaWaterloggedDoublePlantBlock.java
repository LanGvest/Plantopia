package plantopia.block.special;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlantopiaWaterloggedDoublePlantBlock extends DoublePlantBlock implements BucketPickup {
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public PlantopiaWaterloggedDoublePlantBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, true));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
		builder.add(HALF, WATERLOGGED);
	}

	@Override
	protected boolean mayPlaceOn(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos) {
		return super.mayPlaceOn(state, getter, pos)
			&& getter.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)
			&& getter.getFluidState(pos.above(2)).isEmpty();
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
		if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
			return super.canSurvive(state, level, pos);
		} else {
			BlockPos posBelow = pos.below();
			BlockState stateBelow = level.getBlockState(posBelow);
			return mayPlaceOn(stateBelow, level, posBelow);
		}
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
		BlockState newState = super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		if(newState.is(this) && newState.getValue(WATERLOGGED)) worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
		return newState;
	}

	@Override
	@SuppressWarnings("deprecation")
	public @NotNull FluidState getFluidState(@NotNull BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public @NotNull ItemStack pickupBlock(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
		if(state.getValue(WATERLOGGED)) {
			level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 3);
			level.destroyBlock(pos, true);
			return new ItemStack(Items.WATER_BUCKET);
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public @NotNull Optional<SoundEvent> getPickupSound() {
		return Fluids.WATER.getPickupSound();
	}
}