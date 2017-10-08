package riskyReactors;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
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
	public static int[] getAdjacentCoord(World world, int side, int x, int y, int z) {
		int target[]=new int[3];
		if(side == 0) { y -= 1;}
		if(side == 1) { y += 1;}
		if(side == 2) { z -= 1;}
		if(side == 3) {	x += 1;}
		if(side == 4) {	z += 1;}
		if(side == 5) {	x -= 1;}
		target[0]=x; target[1]=y; target[2]=z;
		return target;
		
	}
	public static final String modid = "riskyreactors";
	public static final String name = "Risky Reactors";
	public static final String version = "0.1";
	//Worldgen
	public static worldGen worldgen = new worldGen();
	//Blocks
	public static Block powerUnitCopper;
	public static Block powerUnitAdvanced;
	public static Block powerGenerator;
	public static Block powerGeneratorBroken;
	public static Block powerCrystal;
	//Items
	public static Item powerShard;
	//TEs
	public static Class<? extends TileEntity> tilePowerUnit;
	public static Class<? extends TileEntity> tileEntityGenerator;

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
		
		System.out.println(main.name+": Making TEs");
		tilePowerUnit = TileEntityPowerUnit.class;
		tileEntityGenerator = TileEntityGenerator.class;
		GameRegistry.registerTileEntity(tilePowerUnit, "TEPowerUnit");
		GameRegistry.registerTileEntity(tileEntityGenerator, "TEGenerator");
		
		System.out.println(main.name+": Making Blocks");
		powerUnitCopper = new blockPowerUnitCopper();
		powerUnitAdvanced = new blockPowerUnitAdvanced();
		powerGenerator = new blockPowerGenerator();
		powerGeneratorBroken = new blockPowerGeneratorOverheated();
		powerCrystal = new blockPowerCrystal();
		GameRegistry.registerBlock(powerUnitCopper, "powerUnitCopper");
		GameRegistry.registerBlock(powerUnitAdvanced, "powerUnitAdvanced");
		GameRegistry.registerBlock(powerGenerator, "powerGenerator");
		GameRegistry.registerBlock(powerGeneratorBroken, "powerGeneratorBroken");
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
			'G', main.powerShard, 'C', "ingotCopper", 'R', "dustRedstone"
		}));
		//Power Unit T2
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerUnitAdvanced), new Object[]{
			" G ",
			"IRI",
			"III",
			'G', main.powerUnitCopper, 'I', "ingotIron", 'R', "blockRedstone"
		}));
		//Power Crystal
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerCrystal), new Object[] {
				" I ",
				"IGI",
				" I ",
				'I', main.powerCrystal, 'G', "dustRedstone"
		}));
		//Argent Reactor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerGenerator), new Object[] {
				" I ",
				"IGI",
				" R ",
				'I', "ingotIron", 'G', main.powerCrystal, 'R', "dustRedstone"
		}));
		//Fixing an Argent Reactor
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(main.powerGenerator), new Object[] {
				"   ",
				" G ",
				" R ",
				'G', main.powerGeneratorBroken, 'R', main.powerShard
		}));
	}
}


