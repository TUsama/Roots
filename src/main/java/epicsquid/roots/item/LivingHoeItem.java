package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemHoeBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class LivingHoeItem extends ItemHoeBase implements ILivingRepair {

  public LivingHoeItem(ToolMaterial material, String name) {
    super(material, name, 3, 192, 22);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
    if (entityLiving instanceof PlayerEntity) {
      PlayerEntity player = (PlayerEntity) entityLiving;
      Block block = state.getBlock();
      if (block instanceof IPlantable) {
        for (int x = -2; x < 3; x++) {
          for (int z = -2; z < 3; z++) {
            BlockPos nPos = pos.add(x, 0, z);
            BlockState state2 = worldIn.getBlockState(nPos);
            block = state2.getBlock();
            if (!(block instanceof IPlantable)) continue;
            block.harvestBlock(worldIn, player, nPos, state2, worldIn.getTileEntity(nPos), stack);
            worldIn.setBlockToAir(nPos);
            // Honestly I don't know what this does
            worldIn.playEvent(2001, nPos, Block.getIdFromBlock(block) + (block.getMetaFromState(state2) << 12));
          }
        }
      }
    }

    return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    tooltip.add("");
    tooltip.add(TextFormatting.GREEN + "Breaks plants in a large radius.");
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && ModItems.barks.contains(repair.getItem());
  }
}