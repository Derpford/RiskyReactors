package riskyReactors;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityZombie;
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
	float maxChance = 0.25f;
	boolean isOverheated = false;
	boolean isShutdown = false;
	
	public TileEntityGenerator(int maxstorage, int rate)
	{
		storage.setCapacity(maxstorage);
		storage.setMaxExtract(rate);
	}
	
	@Override
	public void updateEntity() {
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
		if(this.worldObj.rand.nextFloat()<=1.0)
		{
			if(argentHeat < maxArgentHeat) {argentHeat += 1+storage.getEnergyStored()/storage.getMaxEnergyStored();} // heat increases faster when power isn't flowing}
		
			if(!isOverheated && this.worldObj.rand.nextFloat()*maxArgentHeat<argentHeat*maxChance) //Chance of a Bad Thing goes up as argent heat goes up.
			{
				if(FMLCommonHandler.instance().getEffectiveSide()==Side.SERVER)
				{
					//Thanks to Lunacy for helping with this part
					double angle = worldObj.rand.nextDouble()*2*Math.PI;
					float dist = (worldObj.rand.nextFloat()/2+0.5f)*3;
					float newX = (float) (this.xCoord + dist*Math.cos(angle));
					float newZ = (float) (this.zCoord + dist*Math.sin(angle));
					float newY = this.yCoord;
					int newXInt = Math.round(newX); int newYInt = Math.round(newY); int newZInt = Math.round(newZ);
					if(worldObj.getBlock(newXInt,newYInt,newZInt)==Block.getBlockFromName("minecraft:air"))
					{
						EntityBlaze blaze = new EntityBlaze(worldObj);
						blaze.setPositionAndRotation(newX, newY, newZ, 0.0f, 0.0f);
						this.worldObj.spawnEntityInWorld(blaze);
						blaze.spawnExplosionParticle();
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
						isOverheated = true;
					}
				}
			}
			if(isOverheated || isShutdown)
			{
				if(argentHeat > 0) {argentHeat -=1;}
			}
		}
	
	}
	
	public void processActivate(EntityPlayer player, World world)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		//System.out.println(stack);
		if(stack == null)
		{
			ChatComponentText message;
			if(isOverheated)
			{
				message = new ChatComponentText("Looks like the Argent Reactor's control circuits melted. You'll need a Power Shard to fix this.");
			}
			else
			{
				message = new ChatComponentText("This Argent Reactor is running smoothly.");
			}
			player.addChatMessage(message);
		}
		else if(stack.getItem()==main.powerShard)
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