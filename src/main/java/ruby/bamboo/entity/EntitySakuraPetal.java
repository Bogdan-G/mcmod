package ruby.bamboo.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ruby.bamboo.BambooCore;
import ruby.bamboo.block.BlockSakuraLeaves.EnumLeave;

public class EntitySakuraPetal extends Entity {
    public float rx, ry, rz;
    boolean xflg = true;
    boolean yflg = true;
    boolean zflg = true;
    public boolean stopFall = false;
    float rad = 0.001F;
    protected int particleAge = 0;
    protected int particleMaxAge = 0;

    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    private String texPath = BambooCore.resourceDomain + "textures/entitys/petal.png";
    private int texNum = 0;

    public String getTexPath() {
        return texPath;
    }

    public void setTexNum(int num) {
        this.texNum = num;
    }

    public int getTexNum() {
        return texNum;
    }

    public void setTexPath(String path) {
        texPath = path;
    }

    public float getRx() {
        rx += xflg ? rad : -rad;
        xflg = !(xflg ? rx > 1 : rx > -1);
        return rx;
    }

    public float getRy() {
        ry += yflg ? rad : -rad;
        yflg = !(yflg ? ry > 1 : ry > -1);
        return ry;
    }

    public float getRz() {
        rz += zflg ? rad : -rad;
        zflg = !(zflg ? rz > 1 : rz > -1);
        return rz;
    }

    public EntitySakuraPetal(World world, double d, double d1, double d2, double d3, double d4, double d5, EnumLeave leave) {
        this(world, d, d1, d2, d3, d4, d5, leave.getColor());
        this.setTexNum(leave.getPetal());
    }

    public EntitySakuraPetal(World world, double d, double d1, double d2, double d3, double d4, double d5, int color) {
        super(world);
        rad += 0.025 * rand.nextFloat();
        this.setSize(0.2F, 0.2F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(d, d1, d2);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        motionX = d3;
        motionY = d4;
        motionZ = d5;
        float var14 = (float) (Math.random() + Math.random() + 1.0D) * 0.15F;
        float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / var15 * var14;
        this.motionY = this.motionY / var15;
        this.motionZ = this.motionZ / var15 * var14;

        if (d3 == 0 && d4 == 0 && d5 == 0) {
            motionX = (rand.nextFloat() - 0.5) * 0.1;
            motionY = -0.01D;
            motionZ = (rand.nextFloat() - 0.5) * 0.1;
        }

        particleMaxAge = rand.nextInt(120) + 60;
        rx = world.rand.nextFloat();
        ry = world.rand.nextFloat();
        rz = world.rand.nextFloat();

        this.particleRed = (color >> 16) / 255F;
        this.particleGreen = ((color >> 8) & 0xff) / 255F;
        this.particleBlue = (color & 0xff) / 255F;
    }

    public float getRedColorF() {
        return this.particleRed;
    }

    public float getGreenColorF() {
        return this.particleGreen;
    }

    public float getBlueColorF() {
        return this.particleBlue;
    }

    public void setStopFall() {
        stopFall = true;
        particleAge = 0;
        rad = 0.002F;
        motionX = motionZ = 0;
    }

    public boolean isStopFall() {
        return stopFall;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge) {
            setDead();
        }

        if (!stopFall) {
            motionY -= 0.004D;
        } else {
            if (motionY != 0) {
                motionY /= 1.2;
            }
        }

        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.95D;
        motionY *= 0.95D;
        motionZ *= 0.95D;

        if (Material.water == worldObj.getBlock((int) (posX + 0.5), (int) posY, (int) (posZ + 0.5)).getMaterial()) {
            if (!isStopFall()) {
                setStopFall();
            }
        }

        if (onGround) {
            rad = 0.0001F;
        }

    }

    @Override
    protected void entityInit() {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
}
