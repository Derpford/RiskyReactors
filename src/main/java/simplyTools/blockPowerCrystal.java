package simplyTools;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class blockPowerCrystal extends BlockContainer implements ITileEntityProvider{

	int numShards = 2;
	int numDust = 2;
	protected blockPowerCrystal() {
		super(Material.rock);
		
		setBlockName(SimplyMain.modid + "_" + "blockPowerCrystal");
		setBlockTextureName(SimplyMain.modid+":"+"blockPowerCrystal");
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(4.0f);
		setLightLevel(10);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int beans) {
		// TODO Auto-generated method stub
		try
		{
			return new TileEntityPowerCrystal();
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(!world.isRemote) //This should run server-side
		{
			((TileEntityPowerUnit) world.getTileEntity(x, y, z)).processActivate(player, world);
		}
		return true;
	}
	
	
	
	@Override
	protected boolean canSilkHarvest() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		numShards = 1 + world.rand.nextInt(2);
		numDust = 4 - numShards;
		items.add(new ItemStack(SimplyMain.powerShard, numShards));
		items.add(new ItemStack(Items.redstone, numDust));
		return items;
	}
	
}
