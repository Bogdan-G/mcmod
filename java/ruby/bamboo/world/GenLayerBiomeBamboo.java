package ruby.bamboo.world;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import ruby.bamboo.world.biomegen.BiomeGenBaseBamboo;

public class GenLayerBiomeBamboo extends GenLayer {
    private BiomeGenBase[] field_151623_c;
    private BiomeGenBase[] field_151621_d;
    private BiomeGenBase[] field_151622_e;
    private BiomeGenBase[] field_151620_f;

    public GenLayerBiomeBamboo(long par1, GenLayer par3GenLayer, WorldType worldType) {
        super(par1);
        //desert
        this.field_151623_c = new BiomeGenBase[] { BiomeGenBaseBamboo.sakuraForest };
        //forest
        this.field_151621_d = new BiomeGenBase[] { BiomeGenBaseBamboo.sakuraForest };
        //plains
        this.field_151622_e = new BiomeGenBase[] { BiomeGenBaseBamboo.sakuraForest };
        //ice
        this.field_151620_f = new BiomeGenBase[] { BiomeGenBaseBamboo.sakuraForest };
        this.parent = par3GenLayer;
    }

    @Override
    public int[] getInts(int par1, int par2, int par3, int par4) {
        int[] aint = this.parent.getInts(par1, par2, par3, par4);
        int[] aint1 = IntCache.getIntCache(par3 * par4);

        for (int i1 = 0; i1 < par4; ++i1) {
            for (int j1 = 0; j1 < par3; ++j1) {
                this.initChunkSeed((long) (j1 + par1), (long) (i1 + par2));
                int k1 = aint[j1 + i1 * par3];
                int l1 = (k1 & 3840) >> 8;
                k1 &= -3841;

                if (isBiomeOceanic(k1)) {
                    aint1[j1 + i1 * par3] = k1;
                } else if (k1 == BiomeGenBase.mushroomIsland.biomeID) {
                    aint1[j1 + i1 * par3] = k1;
                } else if (k1 == 1) {
                    if (l1 > 0) {
                        if (this.nextInt(3) == 0) {
                            aint1[j1 + i1 * par3] = BiomeGenBase.mesaPlateau.biomeID;
                        } else {
                            aint1[j1 + i1 * par3] = BiomeGenBase.mesaPlateau_F.biomeID;
                        }
                    } else {
                        aint1[j1 + i1 * par3] = this.field_151623_c[this.nextInt(this.field_151623_c.length)].biomeID;
                    }
                } else if (k1 == 2) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * par3] = BiomeGenBase.jungle.biomeID;
                    } else {
                        aint1[j1 + i1 * par3] = this.field_151621_d[this.nextInt(this.field_151621_d.length)].biomeID;
                    }
                } else if (k1 == 3) {
                    if (l1 > 0) {
                        aint1[j1 + i1 * par3] = BiomeGenBase.megaTaiga.biomeID;
                    } else {
                        aint1[j1 + i1 * par3] = this.field_151622_e[this.nextInt(this.field_151622_e.length)].biomeID;
                    }
                } else if (k1 == 4) {
                    aint1[j1 + i1 * par3] = this.field_151620_f[this.nextInt(this.field_151620_f.length)].biomeID;
                } else {
                    aint1[j1 + i1 * par3] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }

        return aint1;
    }

}
