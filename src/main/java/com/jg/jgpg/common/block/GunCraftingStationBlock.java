package com.jg.jgpg.common.block;

import com.jg.jgpg.common.container.GunCraftingStationContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class GunCraftingStationBlock extends JgBlock {

	private static final Component CONTAINER_TITLE = Component
			.translatable("gui.jgpg.gun_crafting_station_menu");
	
	public GunCraftingStationBlock(Properties p_49795_) {
		super(p_49795_);
	}
	
	@Override
	public RenderShape getRenderShape(BlockState p_60550_) {
		return RenderShape.MODEL;
	}
	
	public VoxelShape getOcclusionShape(BlockState p_196247_1_, BlockGetter p_196247_2_, BlockPos p_196247_3_) {
		return Shapes.empty();
	}

	public VoxelShape getVisualShape(BlockState p_230322_1_, BlockGetter p_230322_2_, BlockPos p_230322_3_,
			CollisionContext p_230322_4_) {
		return Shapes.empty();
	}

	public boolean propagatesSkylightDown(BlockState p_200123_1_, BlockGetter p_200123_2_, BlockPos p_200123_3_) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, 
				context.getHorizontalDirection());
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			player.openMenu(this.getMenuProvider(state, level, pos));
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public MenuProvider getMenuProvider(BlockState p_60563_, Level p_60564_,
										BlockPos p_60565_) {
		return new SimpleMenuProvider((p_52229_, p_52230_, p_52231_) ->
			new GunCraftingStationContainer(p_52229_, p_52230_), CONTAINER_TITLE);
	}

}
