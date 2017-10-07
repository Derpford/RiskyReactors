package riskyReactors;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class powerShard extends Item {
	public powerShard()
	{
		setTextureName(main.modid+":"+"powerShard");
		setUnlocalizedName(main.modid+"_"+"powerShard");
		setCreativeTab(CreativeTabs.tabMaterials);
	}
}
