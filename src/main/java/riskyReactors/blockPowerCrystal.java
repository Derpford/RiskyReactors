package riskyReactors;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class blockPowerCrystal extends Block {//Container implements ITileEntityProvider{

	int numShards = 2;
	int numDust = 2;
	protected blockPowerCrystal() {
		super(Material.rock);
		
		setBlockName(main.modid + "_" + "blockPowerCrystal");
		setBlockTextureName(main.modid+":"+"blockPowerCrystal");
		setCreativeTab(CreativeTabs.tabBlock);
		setHardness(4.0f);
		setLightLevel(10);
	}
	
	//@Override
	//public TileEntity createNewTileEntity(World world, int beans) {
	//	try
	//	{
	//		return new TileEntityPowerCrystal();
	//	}
	//	catch (Exception var3)
	//	{
	//		throw new RuntimeException(var3);
	//	}
	//}
	
	//@Override
	//public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	//{
	//	if(!world.isRemote) //This should run server-side
	//	{
	//		((TileEntityPowerUnit) world.getTileEntity(x, y, z)).processActivate(player, world);
	//	}
	//	return true;
	//}
	
	
	
	protected boolean canSilkHarvest() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z,
			int metadata, int fortune) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		numShards = 1 + world.rand.nextInt(2);
		numDust = 4 - numShards;
		items.add(new ItemStack(main.powerShard, numShards));
		items.add(new ItemStack(Items.redstone, numDust));
		return items;
	}
	
}
