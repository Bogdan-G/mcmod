package ruby.bamboo.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class RenderSquareBlock extends Render
{
    public static ModelSpaBlock modelSpa;
    private String tex;
    private ResourceLocation resource;
    public RenderSquareBlock(String s)
    {
        modelSpa = new ModelSpaBlock();
        resource = new ResourceLocation(s);
    }
    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
                         float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        func_110777_b(entity);
        //光源処理
        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.posY + f1 / 16F);
        int k = MathHelper.floor_double(entity.posZ);
        int l = renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
        //光源追加ここまで
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        modelSpa.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
    @Override
    protected ResourceLocation func_110775_a(Entity entity)
    {
        // TODO 自動生成されたメソッド・スタブ
        return resource;
    }
}
