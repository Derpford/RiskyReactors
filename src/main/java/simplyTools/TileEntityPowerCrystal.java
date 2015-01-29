package simplyTools;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class TileEntityPowerCrystal extends TileEntityPowerUnit {

	int heat;
	int maxEnergy = 200000;
	@Override
	public boolean canUpdate() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		if(energy < 1)
		{
			return 0;
		}
		else if(arg2 == false)
		{
			int newEnergy = energy - arg1;
			int oldEnergy = energy;
			energy = newEnergy;
			return arg1;

		}
		else
		{
		return 0;
		}
	}
	
	public int getHeatNearby(World world)
	{
		int totalTemp = 0;
		for(int i = 2; i < 6; i++)
		{
			int x;
			int y;
			int z;
			x = this.xCoord;
			y = this.yCoord;
			z = this.zCoord;
			if(i == 2)
			{
				x = this.xCoord;
				z = this.zCoord-1;
			}
			if(i == 3)
			{
				x = this.xCoord+1;
				z = this.zCoord;
			}
			if(i == 4)
			{
				x = this.xCoord;
				z = this.zCoord+1;
			}
			if(i == 5)
			{
				x = this.xCoord-1;
				z = this.zCoord;
			}
			Block target = world.getBlock(x, y, z);
			Fluid targetType = FluidRegistry.lookupFluidForBlock(target);
			if(targetType!=null)
			{
				int targetTemp = targetType.getTemperature();
				totalTemp= totalTemp + targetTemp;
			}
		}
		return totalTemp;
	}
	
	@Override
	public void updateEntity() {

		heat = getHeatNearby(this.worldObj);
		int energyOutput;
		energyOutput = Math.round(heat/100);
		if(energy+energyOutput<maxEnergy)
		{
			energy = energy + energyOutput;
		}
		else
		{
			energy = maxEnergy;
		}
		TileEntity topBlock = this.worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		
		//TODO: Make this block send energy elsewhere.
		this.markDirty();
		//System.out.println("Crystal: Tick!");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.energy = nbt.getInteger("energy");
		this.maxTransfer = nbt.getInteger("maxTransfer");
		super.readFromNBT(nbt);
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("energy", energy);
		nbt.setInteger("maxTransfer", maxTransfer);
		super.writeToNBT(nbt);
	}
	
	@Override
	public void processActivate(EntityPlayer player, World world) {
		// TODO Auto-generated method stub
		ItemStack stack = player.getCurrentEquippedItem();
		//System.out.println(stack);
		if(stack == null)
		{
			//int estimateEnergy = Math.round(energy/1000);
			ChatComponentText message = new ChatComponentText("This Power Crystal contains " + energy + " Redstone Flux");
			player.addChatMessage(message);
			ChatComponentText message2 = new ChatComponentText("Heat Level:" + heat);
			player.addChatMessage(message2);
		}
	}
}
