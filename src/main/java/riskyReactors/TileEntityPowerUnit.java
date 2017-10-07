package riskyReactors;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPowerUnit extends TileEntity implements IEnergyHandler{
	int energy = 0;
	int maxEnergy = 2000;
	int maxTransfer = 80;
	
	public void processActivate(EntityPlayer player, World world)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		//System.out.println(stack);
		if(stack == null)
		{
			ChatComponentText message = new ChatComponentText("This Power Unit contains " + energy + " Redstone Flux");
			player.addChatMessage(message);
		}
		else
		{
			Item stackThing = stack.getItem();
			if(stackThing instanceof IEnergyContainerItem)
			{
				if(energy > maxTransfer)
				{
					((IEnergyContainerItem) stackThing).receiveEnergy(stack, maxTransfer, false);
					System.out.println("Power Unit Sending Energy to "+stack.getDisplayName());
					energy = energy - maxTransfer;
					this.markDirty();
				}
				else if(energy > 0)
				{
					((IEnergyContainerItem) stackThing).receiveEnergy(stack, energy, false);
					System.out.println("Power Unit Sending All Energy to "+stack.getDisplayName());
					energy = 0;
					this.markDirty();
				}
			}
		}
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
	public boolean canConnectEnergy(ForgeDirection arg0) { //we don't have any sides that can't connect
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		//This block doesn't just give out energy!
		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		// TODO Auto-generated method stub
		
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		// TODO Auto-generated method stub
		return maxEnergy;
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		if(energy < maxEnergy - arg1)
		{
			int newEnergy = energy + arg1;
			energy = newEnergy;
			return arg1;
		}
		else if(energy < maxEnergy)
		{
			int newEnergy = maxEnergy;
			int oldEnergy = energy;
			energy = newEnergy;
			return maxEnergy - oldEnergy;

		}
		else
		{
		return 0;
		}
	}	
}
