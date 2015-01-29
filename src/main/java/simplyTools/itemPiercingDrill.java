package simplyTools;

import java.util.Set;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class itemPiercingDrill extends itemDrill {

	int charge = 0;
	int useTime = 40;
	int replayTime = 0;
	public itemPiercingDrill(Float eff, ToolMaterial mat, Set set) {
		super(eff, mat, set);
		setTextureName(SimplyMain.modid+":"+"itemPiercingDrill");
		setUnlocalizedName(SimplyMain.modid+"_"+"itemPiercingDrill");
		setCreativeTab(CreativeTabs.tabCombat);
		setMaxStackSize(1);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world,
			Entity player, int slot, boolean select) {
	if(charge >= useTime && replayTime < 1)
		{
			player.playSound(SimplyMain.modid+":"+"drillLoop", 3.0f, 1.0f);
			replayTime = 15;
		}
		replayTime -=1;
		if(!select)
		{
			charge = 0;
		}
		super.onUpdate(stack, world, player, slot, select);
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(this.getEnergyStored(stack) > 0)
		{
			charge +=1;
			this.extractEnergy(stack, 10, false);
		}
		else
		{
			charge = 0;
			player.playSound(SimplyMain.modid+":"+"drillStop", 3.0f, 1.0f );
		}
		super.onUsingTick(stack, player, count);
	}
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity target)
	{
		if(charge >=useTime)
		{
			target.attackEntityFrom(DamageSource.causeMobDamage(player).setDamageBypassesArmor(), 12);
			charge = 0;
			player.playSound(SimplyMain.modid+":"+"drillStop", 3.0f, 1.0f);
			return true;
		}
		else
		{
			return false;
		}
	}
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(charge < 1)
		{
			player.playSound(SimplyMain.modid+":"+"drillStart", 3.0f, 1.0f);
		}
		player.setItemInUse(stack, useTime);
		System.out.println(charge);
		return stack;
	}
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world,
			EntityPlayer player, int time) {
		if(charge < useTime)
		{
			player.playSound(SimplyMain.modid+":"+"drillStop", 3.0f, 1.0f);
			charge = 0;
		}
		else
		{
			
		}
		System.out.println("Stopped drilling! "+charge);
		super.onPlayerStoppedUsing(stack, world, player, time);
	}
}
