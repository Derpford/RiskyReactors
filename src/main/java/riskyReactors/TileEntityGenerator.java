package riskyReactors;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityGenerator extends TileEntity implements IEnergyHandler {
	EnergyStorage storage = new EnergyStorage(800,80);
	float argentHeat = 0;
	float maxArgentHeat = 16000;
	int overheatCounter = 0;
	int maxOverheat = 10;
	float maxChance = 0.25f; //Percent chance of spawning, as a float from 0 to 1
	int minHeat = (int) (maxArgentHeat/4); //Minimum amount of heat required for a spawning event.
	float maxDistance = 4.0f;
	boolean isOverheated = false;
	boolean isShutdown = false;
	
	public boolean getOverheated()
	{
		return this.isOverheated;
	}
	
	public TileEntityGenerator(int maxstorage, int rate, boolean isOverheated)
	{
		storage.setCapacity(maxstorage);
		storage.setMaxExtract(rate);
		this.isOverheated=isOverheated;
	}
	
	@Override
	public void updateEntity() {
		//This block is constantly changing.
		this.markDirty();
		//Turn off when redstone is on.
		if(worldObj.getBlockPowerInput(this.xCoord, this.yCoord, this.zCoord) > 0)
		{
			isShutdown = true;
		}
		else
		{
			isShutdown = false;
		}
		//Generate power.	
		if(!isOverheated && !isShutdown && storage.receiveEnergy(10, true)!=0)
		{
			//storage.setEnergyStored(Math.min(storage.getEnergyStored()+10, storage.getMaxEnergyStored()));
			storage.receiveEnergy(10, false);
		}
		int[] target = main.getAdjacentCoord(this.worldObj, 0, this.xCoord, this.yCoord, this.zCoord);
		TileEntity tile = this.worldObj.getTileEntity(target[0], target[1], target[2]);
		if(tile instanceof IEnergyHandler)
		{
			int maxSent=storage.extractEnergy(storage.getMaxExtract(), true);
			int actualSent=((IEnergyHandler) tile).receiveEnergy(ForgeDirection.UP, maxSent, false);
			storage.extractEnergy(actualSent, false);
		}
		//Thanks to Lunacy for helping with this part
		double angle = worldObj.rand.nextDouble()*2*Math.PI;
		float dist = (worldObj.rand.nextFloat()/2+0.5f)*maxDistance;
		float newX = (float) (this.xCoord+0.5f + dist*Math.cos(angle));
		float newZ = (float) (this.zCoord+0.5f + dist*Math.sin(angle));
		float maxX = (float) (this.xCoord+0.5f + maxDistance*Math.cos(angle));
		float maxZ = (float) (this.zCoord+0.5f + maxDistance*Math.sin(angle));
		float newY = this.yCoord;
		int newXInt = Math.round(newX); int newYInt = Math.round(newY); int newZInt = Math.round(newZ);
		if(!isOverheated && worldObj.rand.nextFloat()*maxArgentHeat<argentHeat*2) {worldObj.spawnParticle("reddust", maxX, newY+0.5f, maxZ, 0,0,0);}
		
		if(this.worldObj.rand.nextFloat()<=0.5)
		{
			if(!isShutdown && argentHeat < maxArgentHeat) 
			{
				argentHeat += 1+1*storage.getEnergyStored()/storage.getMaxEnergyStored();
			} // heat increases faster when power isn't flowing
			
			
			if(!isOverheated && argentHeat>=minHeat && this.worldObj.rand.nextFloat()*maxArgentHeat<argentHeat*maxChance) //Chance of a Bad Thing goes up as argent heat goes up.
			{
				
				if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
				{
					
					if(worldObj.getBlock(newXInt,newYInt,newZInt)==Block.getBlockFromName("minecraft:air"))
					{
						EntityBlaze blaze = new EntityBlaze(worldObj);
						blaze.setPositionAndRotation(newX, newY, newZ, 0.0f, 0.0f);
						worldObj.spawnEntityInWorld(blaze);
						worldObj.spawnParticle("explode", newX,newY,newZ, 0,0,0);;
					}
					else
					{
						EntityTNTPrimed tnt = new EntityTNTPrimed(worldObj);
						tnt.setPosition(newX, newY, newZ);
						this.worldObj.spawnEntityInWorld(tnt);
					}
					argentHeat/=3; // Spawning a Blaze dissipates a lot of Argent Heat.
					overheatCounter+=1;//It also inches closer to melting the circuits.
					if(overheatCounter >= maxOverheat)
					{
						worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, main.powerGeneratorBroken, 0, 3);
					}
				}
			}
		}
		if(isShutdown && argentHeat > 0)
		{
			argentHeat -= 2-1*storage.getEnergyStored()/storage.getMaxEnergyStored();
		} // heat decreases slower when power is near full
	}
	
	public void processActivate(EntityPlayer player, World world)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack == null)
		{
			ChatComponentText message;
			if(!isShutdown)
			{
				message = new ChatComponentText("The Argent Reactor is running at "+argentHeat/maxArgentHeat*100+"% heat capacity.");
			}
			else
			{
				message = new ChatComponentText("The Argent Reactor is shut down.");
			}
			player.addChatMessage(message);
		}
		if(stack!=null && stack.getItem()==main.powerShard)
		{
			ChatComponentText message = new ChatComponentText("No repairs necessary.");
			if(isOverheated||overheatCounter>0)
			{
				message = new ChatComponentText("You replace the Argent Reactor's inner mechanisms.");
				isOverheated = false;
				overheatCounter = 0;
			}
			player.addChatMessage(message);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		storage.setEnergyStored(nbt.getInteger("energy"));
		storage.setMaxExtract(nbt.getInteger("maxTransfer"));
		isOverheated=nbt.getBoolean("isOverheated");
		overheatCounter=nbt.getInteger("overheatCounter");
		argentHeat=nbt.getFloat("argentHeat");
		super.readFromNBT(nbt);
	}
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("energy", storage.getEnergyStored());
		nbt.setInteger("maxTransfer", storage.getMaxExtract());
		nbt.setInteger("overheatCounter",overheatCounter);
		nbt.setBoolean("isOverheated", isOverheated);
		nbt.setFloat("argentHeat", argentHeat);
		super.writeToNBT(nbt);
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection arg0) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int extractEnergy(ForgeDirection arg0, int arg1, boolean arg2) {
		// TODO Auto-generated method stub
		if(arg0==ForgeDirection.DOWN) {
			return storage.extractEnergy(arg1, arg2);
		}
		else
		{
			return 0;
		}
		
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
		// TODO Auto-generated method stub
		return storage.receiveEnergy(arg1, arg2);
	}
}