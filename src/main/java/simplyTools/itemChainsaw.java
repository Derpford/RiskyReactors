package simplyTools;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class itemChainsaw extends itemPoweredTool implements IEnergyContainerItem
{
	float damageVsEntity = 6.0f;
	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player)
	{
		if(world.isRemote) {
			player.addChatMessage(new ChatComponentText("A chainsaw! Find some trees!"));
		}
		System.out.println("Making this item NBT!");
		stack.stackTagCompound = new NBTTagCompound();
		System.out.println("Making the Energy value!");
		stack.stackTagCompound.setInteger("energy", 0);
	}
	protected itemChainsaw(float eff, ToolMaterial mat, Set set) {
		super(eff, mat, set);
		setTextureName(SimplyMain.modid+":"+"itemSaw");
		setUnlocalizedName(SimplyMain.modid+"_"+"itemSaw");
		setCreativeTab(CreativeTabs.tabTools);
		setMaxStackSize(1);
	}
	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		if(getEnergyStored(stack) < 1)//If we're out of energy
		{
			return false;
		} else if //If we're mining the right things
				(block.getMaterial() == net.minecraft.block.material.Material.wood ||
				block.getMaterial() == net.minecraft.block.material.Material.cactus ||
				block.getMaterial() == net.minecraft.block.material.Material.carpet ||
				block.getMaterial() == net.minecraft.block.material.Material.cloth ||
				block.getMaterial() == net.minecraft.block.material.Material.gourd ||
				block.getMaterial() == net.minecraft.block.material.Material.web ||
				block.getMaterial() == net.minecraft.block.material.Material.leaves)
		{
			return true;
		} else { //Otherwise, nope.
			return false;
		}
	}
	@Override
	public Multimap getAttributeModifiers(ItemStack stack)
	{
	double dmg = (double)this.damageVsEntity;
	// Broken tool
	if (this.getEnergyStored(stack) < 1)
	{
	dmg = 1.0d;
	}
	else
	{
	dmg = 6.0d;
	}
	Multimap<String, AttributeModifier> multimap = HashMultimap.create();
	multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", dmg, 0));
	return multimap;
	}
	

}
