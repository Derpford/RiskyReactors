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
		if(side == 0) { y -= 1;}
		if(side == 1) { y += 1;}
		if(side == 2) { z -= 1;}
		if(side == 3) {	x += 1;}
		if(side == 4) {	z += 1;}
		if(side == 5) {	x -= 1;}
		Block target = world.getBlock(x, y, z);
		return target;
	}
	public static final String modid = "riskyreactors";
	public static final String name = "Risky Reactors";
	public static final String version = "0.1";
	public static worldGen worldgen = new worldGen();
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
		System.out.println(main.name+": Now in Forge.");
	}
	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println(main.name+": Making the worldgen");
		GameRegistry.registerWorldGenerator(worldgen, 3);
		System.out.println(main.name+": Making Sounds");
		
		System.out.println(main.name+": Making the Tool Materials");
		//ToolMaterial powerbasic = EnumHelper.addToolMaterial("powerbasic", 2, 0, 6.0F, 2, 0);
		//ToolMaterial powerdiamond = EnumHelper.addToolMaterial("powerdiamond", 4, 0, 6.0f, 4, 0);
		
		System.out.println(main.name+": Making TEs");
		tilePowerUnit = TileEntityPowerUnit.class;
		tilePowerCrystal = TileEntityPowerCrystal.class;
		GameRegistry.registerTileEntity(tilePowerUnit, "TEPowerUnit");
		GameRegistry.registerTileEntity(tilePowerCrystal, "TEPowerCrystal");
		
		System.out.println(main.name+": Making Blocks");
		powerUnitCopper = new blockPowerUnitCopper();
		powerUnitAdvanced = new blockPowerUnitAdvanced();
		powerCrystal = new blockPowerCrystal();
		GameRegistry.registerBlock(powerUnitCopper, "powerUnitCopper");
		GameRegistry.registerBlock(powerUnitAdvanced, "powerUnitAdvanced");
		GameRegistry.registerBlock(powerCrystal, "powerCrystal");
		
		System.out.println(main.name+": Making Items");
		powerShard = new powerShard();
		GameRegistry.registerItem(powerShard, "powerShard");
		
		System.out.println(main.name+": Making Recipes");
		//Power Unit T1
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerUnitCopper), new Object[]{
			" G ",
			"CRC",
			"CCC",
			'G', "gearIron", 'C', "ingotCopper", 'R', "dustRedstone"
		}));
		//Power Unit T2
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerUnitAdvanced), new Object[]{
			" G ",
			"IRI",
			"III",
			'G', main.powerUnitCopper, 'I', "ingotIron", 'R', "blockRedstone"
		}));
	}
}


