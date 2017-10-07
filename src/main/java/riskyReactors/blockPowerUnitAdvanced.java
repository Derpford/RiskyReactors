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

public class blockPowerUnitAdvanced extends blockPowerUnitCopper implements ITileEntityProvider
{
	IIcon[] icons = new IIcon[6];
	public blockPowerUnitAdvanced()
	{
		super();
		setBlockName(SimplyMain.modid + "_" + "blockPowerUnitAdvanced");
		setBlockTextureName(SimplyMain.modid+":"+"blockPowerUnitAdvanced");
		setCreativeTab(CreativeTabs.tabMaterials);
	}
	@Override
	public void registerBlockIcons(IIconRegister reg)
	{
		this.icons[0] = reg.registerIcon(this.textureName + "_" + "0");
		this.icons[1] = reg.registerIcon(this.textureName + "_" + "1");
	}
	@Override
	public IIcon getIcon(int side, int meta) {
	    if(side == 1){
	    	return this.icons[1];
	    } else {
	    	return this.icons[0];
	    }
	}
	@Override
	public TileEntity createNewTileEntity(World world, int beans) {
		try
		{
			TileEntityPowerUnit t = new TileEntityPowerUnit();
			t.maxTransfer = 400;
			return t;
		}
		catch (Exception var3)
		{
			throw new RuntimeException(var3);
		}
	}
}
