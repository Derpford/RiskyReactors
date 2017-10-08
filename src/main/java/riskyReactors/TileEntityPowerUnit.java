package riskyReactors;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityPowerUnit extends TileEntity implements IEnergyHandler{
	EnergyStorage storage = new EnergyStorage(2000,80);
	
	public TileEntityPowerUnit(int maxStorage,int rate)
	{
		storage.setCapacity(maxStorage);
		storage.setMaxExtract(rate);
	}
	
	public void processActivate(EntityPlayer player, World world)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		//System.out.println(stack);
		if(stack == null)
		{
			ChatComponentText message = new ChatComponentText("This Power Unit contains " + storage.getEnergyStored() + " Redstone Flux");
			player.addChatMessage(message);
		}
		else
		{
			Item stackThing = stack.getItem();
			if(stackThing instanceof IEnergyContainerItem)
			{
				if(storage.extractEnergy(storage.getMaxExtract(), true)!=0)
				{
					((IEnergyContainerItem) stackThing).receiveEnergy(stack, storage.getMaxExtract(), false);
					System.out.println("Power Unit Sending Energy to "+stack.getDisplayName());
					storage.extractEnergy(storage.getMaxExtract(), false);
				}
				
			}
			}
		}
	
	@Override
	public void updateEntity() {
		int[] target = main.getAdjacentCoord(this.worldObj, 0, this.xCoord, this.yCoord, this.zCoord);
		TileEntity tile = this.worldObj.getTileEntity(target[0], target[1], target[2]);
		if(tile instanceof IEnergyHandler)
		{
			int maxSent=storage.extractEnergy(storage.getMaxExtract(), true);
			int actualSent=((IEnergyHandler) tile).receiveEnergy(ForgeDirection.UP, maxSent, false);
			storage.extractEnergy(actualSent, false);
		}
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		storage.setEnergyStored(nbt.getInteger("energy"));
		storage.setMaxExtract(nbt.getInteger("maxTransfer"));
		super.readFromNBT(nbt);
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("energy", storage.getEnergyStored());
		nbt.setInteger("maxTransfer", storage.getMaxExtract());
		super.writeToNBT(nbt);
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) { //All sides can be connected to.
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		//Down only.
		int transferredEnergy = 0;
		if(arg0 == ForgeDirection.DOWN) { 
			if(storage.extractEnergy(arg1, true)!=0)
					{
						transferredEnergy = storage.extractEnergy(arg1, arg2);
					}
		}
		return transferredEnergy;
	}

	@Override
	public int getEnergyStored(ForgeDirection arg0) {
		// TODO Auto-generated method stub
		
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection arg0) {
		// TODO Auto-generated method stub
		return storage.getMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		if(arg0 != ForgeDirection.DOWN)
		{
			return storage.receiveEnergy(arg1, arg2);
		}
		else
		{
			return 0;
		}
	}	
}
