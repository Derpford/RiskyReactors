package simplyTools;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cofh.api.energy.IEnergyContainerItem;

public class itemDrill extends itemPoweredTool implements IEnergyContainerItem
{
	int useTime = 40;
	int charge = 0;
	int replayTime = 0;
	public itemDrill(Float eff, ToolMaterial mat, Set set)	
	{
		super(eff, mat, set);
		float efficiencyOnProperMaterial = eff;
		float damageVsEntity;
		setTextureName(SimplyMain.modid+":"+"itemDrill");
		setUnlocalizedName(SimplyMain.modid+"_"+"itemDrill");
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(1);
	}

	//Totally not ripped from itemPiercingDrill
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(charge < 1)
		{
			player.playSound(SimplyMain.modid+":"+"drillStart", 3.0f, 1.0f);
		}
		player.setItemInUse(stack, useTime);
		System.out.println(charge);
		return stack;
	};
	
	@Override
	public void onUpdate(ItemStack stack, World world,
			Entity player, int slot, boolean select) {
	if(charge >= useTime && replayTime < 1)
		{
			player.playSound(SimplyMain.modid+":"+"drillLoop", 3.0f, 1.0f);
			replayTime = 15;
		}
		replayTime -=1;
		if(!select)
		{
			charge = 0;
		}
		super.onUpdate(stack, world, player, slot, select);
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(this.getEnergyStored(stack) > 0)
		{
			charge +=1;
		}
		else
		{
			charge = 0;
			player.playSound(SimplyMain.modid+":"+"drillStop", 3.0f, 1.0f );
		}
		super.onUsingTick(stack, player, count);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world,
			EntityPlayer player, int time) {
		if(charge < useTime)
		{
			player.playSound(SimplyMain.modid+":"+"drillStop", 3.0f, 1.0f);
			charge = 0;
		}
		else
		{
			
		}
		super.onPlayerStoppedUsing(stack, world, player, time);
	}
	
	//We can't harvest if we don't have any energy left.
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
