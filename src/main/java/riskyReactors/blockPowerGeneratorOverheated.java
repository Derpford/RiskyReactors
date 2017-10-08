package riskyReactors;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class blockPowerGeneratorOverheated extends Block
{
	IIcon[] icons = new IIcon[6];
	public blockPowerGeneratorOverheated()
	{
		super(Material.iron);
		
		setBlockName(main.modid + "_" + "blockPowerGeneratorBroken");
		setBlockTextureName(main.modid+":"+"blockPowerGenerator");
		setCreativeTab(CreativeTabs.tabDecorations);
		setHardness(4.0f);
	}
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		this.icons[0] = reg.registerIcon(this.textureName + "_" + "0");
		this.icons[1] = reg.registerIcon(this.textureName + "_" + "1");
		this.icons[2] = reg.registerIcon(this.textureName + "_" + "2");
	}
	@Override
	public IIcon getIcon(int side, int meta) {
	    if(side == 1){
	    	return this.icons[1];
	    } else if(side == 0) {
	    	return this.icons[2];
	    }
	    else {
	    	return this.icons[0];
	    }
	}
	@Override
	public boolean hasTileEntity(int metadata)
	{
	    return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		//System.out.println(stack);
		if(world.isRemote)
		{
			if(stack == null)
			{
				ChatComponentText message;
				message = new ChatComponentText("Looks like the Argent Reactor's control circuits melted. You'll need a Power Shard to fix this.");
				player.addChatMessage(message);
			}
			/*else if(stack.getItem()==main.powerShard)
			{
				ChatComponentText message = new ChatComponentText("You replace the Argent Reactor's inner mechanisms.");
				player.addChatMessage(message);
				world.setBlock(x, y, z, main.powerGenerator, 0, 3);
			}*/
		}
		return true;
	}
	
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random prandom)
	{
		float newX = x+(prandom.nextFloat()/2+0.5f)*2;
		float newY = y+(prandom.nextFloat()/2+0.5f)*2;
		float newZ = z+(prandom.nextFloat()/2+0.5f)*2;
		world.spawnParticle("reddust", newX, newY, newZ, 0,0,0);
	}
	
	/*@Override
	public TileEntity createNewTileEntity(World world, int beans) {
		try
		{
			return new TileEntityGenerator(800,80,true);
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	}*/
}