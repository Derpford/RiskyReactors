package simplyTools;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyStorage;

public class itemDiamondDrill extends itemDrill implements IEnergyContainerItem{

	public final int maxEnergy = 100000;
	public final int maxTransfer = 2000;
	public itemDiamondDrill(Float eff, ToolMaterial mat, Set set) {
		super(eff, mat, set);
		setTextureName(SimplyMain.modid+":"+"itemDiamondDrill");
		setUnlocalizedName(SimplyMain.modid+"_"+"itemDiamondDrill");
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(1);
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		if(getEnergyStored(stack) < 1)//If we're out of energy
		{
			return false;
		} else if //If we're mining the right things
				(block.getMaterial() == net.minecraft.block.material.Material.rock ||
				block.getMaterial() == net.minecraft.block.material.Material.glass ||
				block.getMaterial() == net.minecraft.block.material.Material.ice ||
				block.getMaterial() == net.minecraft.block.material.Material.packedIce ||
				block.getMaterial() == net.minecraft.block.material.Material.iron ||
				block.getMaterial() == net.minecraft.block.material.Material.anvil ||
				block.getMaterial() == net.minecraft.block.material.Material.grass ||
				block.getMaterial() == net.minecraft.block.material.Material.clay ||
				block.getMaterial() == net.minecraft.block.material.Material.ground ||
				block.getMaterial() == net.minecraft.block.material.Material.sand ||
				block.getMaterial() == net.minecraft.block.material.Material.snow)
		{
			return true;
		} else { //Otherwise, nope.
			return false;
		}
	}
}
