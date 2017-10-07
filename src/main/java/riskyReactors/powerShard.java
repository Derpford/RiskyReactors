package riskyReactors;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class powerShard extends Item {
	public powerShard()
	{
		setTextureName(main.modid+":"+"powerShard");
		setUnlocalizedName(main.modid+"_"+"powerShard");
		setCreativeTab(CreativeTabs.tabMaterials);
	}
}
