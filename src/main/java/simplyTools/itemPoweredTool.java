package simplyTools;

import java.util.List;
import java.util.Set;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class itemPoweredTool extends ItemTool implements IEnergyContainerItem{
	protected itemPoweredTool(float eff, ToolMaterial mat, Set set) 
	{
		super(eff, mat, set);
		// TODO Auto-generated constructor stub
	}
	//Set important vars.
	public final int maxEnergy = 20000;
	public final int maxTransfer = 400;
	public final int useEnergy = 80;
	//Set us up the NBT
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		System.out.println("Making this item NBT!");
		stack.stackTagCompound = new NBTTagCompound();
		System.out.println("Making the Energy value!");
		stack.stackTagCompound.setInteger("energy", 0);
	}
	//Main tooltip turn on.
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) 
	{
		if (itemStack.stackTagCompound != null) {
			int displayEnergy = itemStack.stackTagCompound.getInteger("energy");
			list.add(EnumChatFormatting.RED + "Energy: " + displayEnergy);
		}
	}
	//It's a tool, but you can't repair it the usual way.
	public boolean isItemTool(ItemStack stack)
	{
	return true;
	}
	@Override
	public boolean isRepairable()
	{
	return false;
	}
	//How fast can we go?
	//Gotta go fast
	@Override
	public float getDigSpeed(ItemStack stack, Block block, int meta)
	{
		float eff = this.efficiencyOnProperMaterial;
		float multi = ((stack.stackTagCompound.getInteger("energy")/maxEnergy)*4)-2; //At full energy, it should add; in the middle, nothing; and at empty, it should subtract.
		if(getEnergyStored(stack) < 1)
		{return 0f;}
		else if(this.canHarvestBlock(block, stack))
		{return eff+multi;}
		else
		{return 0.2f;}
	}
	//Check for energy consumption
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player)
	{
		//Make the item not cause stats to happen if it's out of energy
		if(this.getEnergyStored(stack) > 0)
		{
			extractEnergy(stack, useEnergy, false);
			return true;
		} else {
			return false;
		}
	}
	public boolean hitEntity(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player)
	{
		//Make the item not cause stats to happen if it's out of energy
		if(this.getEnergyStored(stack) > 0)
		{
			extractEnergy(stack, useEnergy, false);
			return true;
		} else {
			return false;
		}
	}
	//All the energy transfer handlers.
	//Add energy functions.
		@Override
		public int extractEnergy(ItemStack arg0, int arg1, boolean arg2) {
			if(arg0.stackTagCompound == null)
			{
				System.out.println("Making this item NBT!");
				arg0.stackTagCompound = new NBTTagCompound();
				System.out.println("Making the Energy value!");
				arg0.stackTagCompound.setInteger("energy", 0);
			}
			int energyOld = arg0.stackTagCompound.getInteger("energy"); //Figure out what we have right now
			if(energyOld > arg1) //If we've got enough...
			{
				if(arg2 == false) //And this is a real transfer...
				{
					int energyNew = energyOld-arg1; //Work out what we'll have afterward
					arg0.stackTagCompound.setInteger("energy", energyNew); //Set our new energy!
				}
				return arg1; //And tell them that we've got enough
			}
			else if (energyOld > 0) //If we don't have enough, but we do have some...
			{
				int returnvar = energyOld; //Figure out how much we've got left
				if(arg2 == false) //If it's a real transfer...
				{
					arg0.stackTagCompound.setInteger("energy", 0); //Drain the batteries!
				}
				return returnvar; //And tell them we're almost out.
			}
			else //If we're completely out of energy...
			{
				return 0; //...tell them!
			}
		}

		@Override
		public int getEnergyStored(ItemStack arg0) {
			if(arg0.stackTagCompound == null)
			{
				System.out.println("Making this item NBT!");
				arg0.stackTagCompound = new NBTTagCompound();
				System.out.println("Making the Energy value!");
				arg0.stackTagCompound.setInteger("energy", 0);
			}
			int energyNow = arg0.stackTagCompound.getInteger("energy");
			if(energyNow != 0)
			{// Gotta tell them how much is in this item
				return energyNow;
			}
			else
			{
				return 0;
			}
		}

		@Override
		public int getMaxEnergyStored(ItemStack arg0) {
			// Maximum Energy doesn't change yet.
			return maxEnergy;
		}

		@Override
		public int receiveEnergy(ItemStack arg0, int arg1, boolean arg2) {
			if(arg0.stackTagCompound == null)
			{
				System.out.println("Making this item NBT!");
				arg0.stackTagCompound = new NBTTagCompound();
				System.out.println("Making the Energy value!");
				arg0.stackTagCompound.setInteger("energy", 0);
			}
			int energyOld = arg0.stackTagCompound.getInteger("energy"); //figure out how much energy we have
			int energyTransfer;
			if(arg1 > maxTransfer) //If we can't transfer that much...
			{
				energyTransfer = maxTransfer;
			}
			else
			{
				energyTransfer = arg1;
			}
			if(energyOld < maxEnergy - arg1) //If we've got enough room...
			{
				if(arg2 == false) //...and this is a real transfer...
				{
					int energyNew = energyOld + energyTransfer; //Make a number for the new energy
					arg0.stackTagCompound.setInteger("energy", energyNew); //And add it to our item
					//System.out.println("Drill:Got energy!");
				}
				return energyTransfer; //Even if we aren't really doing it, tell them it can be done
			}
			else if(energyOld < maxEnergy) //If we don't have enough room...
			{
				int returnvar = this.maxEnergy - energyOld; //...figure out how much we can store
				if(arg2 == false) //If it's real...
				{
					int energyNew = maxEnergy; //...you know the drill
					arg0.stackTagCompound.setInteger("energy", energyNew);
					//System.out.println("Drill:I'm full!");
				}
				return returnvar; //Either way, we gotta tell them how much could be sent
			}
			else
			{return 0;} //If there's just no room, we return 0
		}
		@Override
		public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
		{
			if(this.getEnergyStored(stack) > 1)
			{
				System.out.println("Smacked something!");
				this.extractEnergy(stack, 160, false);
				return true;
			}
			else
			{
				System.out.println("Smacked something!");
				return false;
			}
		}
		@Override
		public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean onItemUse(ItemStack stack, EntityPlayer player,
				World world, int x, int y, int z,
				int side, float hitX, float hitY, float hitZ) 
		{
			return super.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}
}
