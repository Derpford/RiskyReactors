package riskyReactors;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import cpw.mods.fml.common.IWorldGenerator;

public class simplyWorldGen implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world,
			IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for(int k = 0; k < 2; k++)
		{
			int crystalgenX = chunkX*16 + random.nextInt(16);
			int crystalgenY = 4 + random.nextInt(32);
			int crystalgenZ = chunkZ*16 + random.nextInt(16);
			(new WorldGenMinable(SimplyMain.powerCrystal, 20)).generate(world, random, crystalgenX, crystalgenY, crystalgenZ); 
			//System.out.println("Placing Power Crystal at " + crystalgenX + "/" + crystalgenY + "/" + crystalgenZ);
		}
		// TODO Auto-generated method stub
		
	}


}
