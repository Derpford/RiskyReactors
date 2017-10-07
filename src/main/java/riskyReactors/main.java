package riskyReactors;


import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
			modid=main.modid,
			name=main.name,
			version=main.version,
			dependencies="required-after:Forge@[10.13.2.1232,); required-after:CoFHCore",
			acceptedMinecraftVersions="[1.7.10]"
	)


public class main {
	public static Block getAdjacentBlock(World world, int side, int x, int y, int z) {
		
		if(i == 2)
		{
			z -=1;
		}
		if(i == 3)
		{
			x +=1;
		}
		if(i == 4)
		{
			z +=1;
		}
		if(i == 5)
		{
			x-=1;
		}
		Block target = world.getBlock(x, y, z);
		return target;
	}
	public static final String modid = "simplypowertools";
	public static final String name = "Simply Power Tools!";
	public static final String version = "0.1";
	public static simplyWorldGen worldgen = new simplyWorldGen();
	public static Block powerUnitCopper;
	public static Block powerUnitAdvanced;
	public static Block powerCrystal;
	public static Item powerDrill;
	public static Item powerDiamondDrill;
	public static Item powerPiercingDrill;
	public static Item powerSaw;
	public static Item chainSword;
	public static Class<? extends TileEntity> tilePowerUnit;
	public static Item powerShard;
	public Class<? extends TileEntity> tilePowerCrystal;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		//modMetadata = event.getModMetadata();
		System.out.println("Simply Power Tools: Now in Forge.");
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println("Simply Power Tools: Making the worldgen");
		GameRegistry.registerWorldGenerator(worldgen, 3);
		System.out.println("Simply Power Tools: Making Sounds");
		
		System.out.println("Simply Power Tools: Making the Tool Materials");
		ToolMaterial powerbasic = EnumHelper.addToolMaterial("powerbasic", 2, 0, 6.0F, 2, 0);
		ToolMaterial powerdiamond = EnumHelper.addToolMaterial("powerdiamond", 4, 0, 6.0f, 4, 0);
		
		System.out.println("Simply Power Tools: Making TEs");
		tilePowerUnit = TileEntityPowerUnit.class;
		tilePowerCrystal = TileEntityPowerCrystal.class;
		GameRegistry.registerTileEntity(tilePowerUnit, "TEPowerUnit");
		GameRegistry.registerTileEntity(tilePowerCrystal, "TEPowerCrystal");
		
		System.out.println("Simply Power Tools: Making Blocks");
		powerUnitCopper = new blockPowerUnitCopper();
		powerUnitAdvanced = new blockPowerUnitAdvanced();
		powerCrystal = new blockPowerCrystal();
		GameRegistry.registerBlock(powerUnitCopper, "powerUnitCopper");
		GameRegistry.registerBlock(powerUnitAdvanced, "powerUnitAdvanced");
		GameRegistry.registerBlock(powerCrystal, "powerCrystal");
		
		System.out.println("Simply Power Tools: Making Items");
		powerShard = new powerShard();
		powerDrill = new itemDrill(3.0f, powerbasic, null);
		powerDiamondDrill = new itemDiamondDrill(4.0f, powerdiamond, null);
		powerPiercingDrill = new itemPiercingDrill(3.0f, powerbasic, null);
		powerSaw = new itemChainsaw(3.0f, powerbasic, null);
		chainSword = new itemChainSword(3.0f, powerbasic, null);
		GameRegistry.registerItem(powerShard, "powerShard");
		GameRegistry.registerItem(powerDrill, "powerDrill");
		GameRegistry.registerItem(powerDiamondDrill, "powerDiamondDrill");
		GameRegistry.registerItem(powerPiercingDrill, "powerPiercingDrill");
		GameRegistry.registerItem(powerSaw, "powerSaw");
		GameRegistry.registerItem(chainSword, "chainSword");
		
		System.out.println("Simply Power Tools: Making Recipes");
		//Power Unit T1
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerUnitCopper), new Object[]{
			" G ",
			"CRC",
			"CCC",
			'G', "gearIron", 'C', "ingotCopper", 'R', "dustRedstone"
		}));
		//Power Unit T2
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerUnitAdvanced), new Object[]{
			" G ",
			"IRI",
			"III",
			'G', SimplyMain.powerUnitCopper, 'I', "ingotIron", 'R', "blockRedstone"
		}));
		//Power Drill
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerDrill), new Object[]{
			"II ",
			"IB ",
			"  P",
			'I', "ingotIron", 'B', new ItemStack(Items.iron_pickaxe), 'P', SimplyMain.powerUnitCopper
		}));
		//Power Drill--Diamond Upgrade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerDiamondDrill), new Object[]{
			" D ",
			"DUD",
			"   ",
			'D', "gemDiamond", 'U', SimplyMain.powerDrill
		}));
		//Power Drill--Piercing Upgrade
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerPiercingDrill), new Object[]{
			" F ",
			"FDF",
			" U ",
			'F', new ItemStack(Items.flint), 'D', "blockIron", 'U', SimplyMain.powerDrill
		}));
		//Power Saw
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.powerSaw), new Object[]{
			"II ",
			"IB ",
			"  P",
			'I', "ingotIron", 'B', new ItemStack(Items.iron_axe), 'P', SimplyMain.powerUnitCopper
		}));
		//Chain Sword
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SimplyMain.chainSword), new Object[]{
			"S  ",
			" B ",
			"  I",
			'I', new ItemStack(SimplyMain.powerSaw), 'B', new ItemStack(SimplyMain.powerUnitAdvanced), 'S', new ItemStack(Items.iron_sword)
		}));
	}
	private Item itemPiercingDrill(float f, ToolMaterial powerbasic,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	
}


