package simplyTools;

import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;

public class itemChainSword extends itemChainsaw implements IEnergyContainerItem
{
	public final int maxEnergy = 100000;
	public final int maxTransfer = 2000;
	float damageVsEntity = 9.0f;
	protected itemChainSword(float eff, ToolMaterial mat, Set set) {
		super(eff, mat, set);
		// TODO Auto-generated constructor stub
		setTextureName(SimplyMain.modid+":"+"itemChainSword");
		setUnlocalizedName(SimplyMain.modid+"_"+"itemChainSword");
		setCreativeTab(CreativeTabs.tabCombat);
		setMaxStackSize(1);
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
	dmg = 9.0d;
	}
	Multimap<String, AttributeModifier> multimap = HashMultimap.create();
	multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Tool modifier", dmg, 0));
	return multimap;
	}
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase player)
	{
		if(this.getEnergyStored(stack) > 1)
		{
			//System.out.println("Smacked something!");
			this.extractEnergy(stack, 80, false);
			return true;
		}
		else
		{
			//System.out.println("Smacked something!");
			return false;
		}
	}
}
