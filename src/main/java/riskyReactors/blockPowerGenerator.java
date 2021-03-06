package riskyReactors;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class blockPowerGenerator extends BlockContainer implements ITileEntityProvider
{
	IIcon[] icons = new IIcon[6];
	public blockPowerGenerator()
	{
		super(Material.iron);
		
		setBlockName(main.modid + "_" + "blockPowerGenerator");
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
	    return true;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if(!world.isRemote) //This should run server-side
		{
			((TileEntityGenerator) world.getTileEntity(x, y, z)).processActivate(player, world);
		}
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int beans) {
		try
		{
			return new TileEntityGenerator(800,80,false);
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	}
}