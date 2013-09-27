package ruby.bamboo.entity;

import ruby.bamboo.BambooInit;
import ruby.bamboo.Config;
import ruby.bamboo.BambooCore;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityHuton extends Entity
{
    private boolean timechange;
    private int sleeptime;
    private long hour;
    private static int timeacc = 20;
    private final static int DIRECTION = 16;
    public EntityHuton(World world)
    {
        super(world);
        setSize(2.0F, 0.3F);
        timechange = false;
    }
    @Override
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        //server用
        if (!isDead && !this.worldObj.isRemote)
        {
            EntityPlayer entityplayer = null;

            if (par1DamageSource.damageType == "player")
            {
                entityplayer = (EntityPlayer)par1DamageSource.getEntity();
                setDead();
            }
            else
            {
                return false;
            }

            if (entityplayer != null && entityplayer.capabilities.isCreativeMode)
            {
                return true;
            }

            this.dropItem(BambooInit.kakezikuIID, 1);
        }

        return true;
    }
    public void setDir(int dir)
    {
        dataWatcher.updateObject(DIRECTION, (byte)dir);
    }
    //render用
    public byte getDir()
    {
        return dataWatcher.getWatchableObjectByte(DIRECTION);
    }
    //1.3.2用少し時間が経つとYが1.5ほど上がるバグの対策
    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
    }
    @Override
    public boolean func_130002_c(EntityPlayer entityplayer)
    {
        if (!worldObj.isRemote)
        {
            if (this.riddenByEntity == null)
            {
                entityplayer.mountEntity(this);
            }
            else
            {
                entityplayer.mountEntity(this);
            }

            timechange = true;
            sleeptime = 0;
            hour = ((worldObj.getWorldInfo().getWorldTime() + 6000) % 24000) / 1000;
        }

        return true;
    }
    @Override
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }
    @Override
    protected void entityInit()
    {
        dataWatcher.addObject(DIRECTION, (byte) 0);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        setDir(nbttagcompound.getByte("dir"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setByte("dir", getDir());
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        /*
        //クライアント用消去判定
        if (!isDead&&this.worldObj.isRemote&&dataWatcher.getWatchableObjectByte(ATTACK)==1)
        {
        	setDead();
        }*/
        /*
        //クライアント側で座らせる
        if(worldObj.isRemote){
        	Entity entity=getRiddenByEntity();
        	if(this.riddenByEntity==null&&getRiddenByEntityID()!=0){
        		if(entity!=null){
        			entity.mountEntity(this);
        		}
        	}else if(getRiddenByEntityID()==0){
        		if(entity!=null){
        			entity.mountEntity(this);
        		}
        	}
        }*/
        //時間加速
        if (BambooCore.getConf().timeAccel && timechange)
        {
            if (riddenByEntity instanceof EntityPlayer)
            {
                if (sleeptime++ > 100)
                {
                    long time = ((worldObj.getWorldInfo().getWorldTime() + 6000) % 24000) / 1000;

                    if (hour != time)
                    {
                        ((EntityPlayer)riddenByEntity).addChatMessage(hour + ":00" + (worldObj.getWorldInfo().isRaining() ? " RainTime at" + worldObj.getWorldInfo().getRainTime() : "") + (worldObj.getWorldInfo().isThundering() ? " ThunderTime at" + worldObj.getWorldInfo().getThunderTime() : ""));
                        hour = time;
                    }

                    worldObj.getWorldInfo().setWorldTime(worldObj.getWorldInfo().getWorldTime() + timeacc);

                    if (worldObj.getWorldInfo().isRaining())
                    {
                        if (worldObj.getWorldInfo().getRainTime() > timeacc)
                        {
                            worldObj.getWorldInfo().setRainTime(worldObj.getWorldInfo().getRainTime() - timeacc);
                        }
                    }

                    if (worldObj.getWorldInfo().isThundering())
                    {
                        if (worldObj.getWorldInfo().getThunderTime() > timeacc)
                        {
                            worldObj.getWorldInfo().setThunderTime(worldObj.getWorldInfo().getThunderTime() - timeacc);
                        }
                    }
                }
            }
            else
            {
                timechange = false;
            }
        }
    }
    /*
    private Entity getRiddenByEntity(){
    	return ((WorldClient)worldObj).getEntityByID(getRiddenByEntityID());
    }
    private int getRiddenByEntityID(){
    	return dataWatcher.getWatchableObjectInt(SIT);
    }*/
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return boundingBox;
    }
    @Override
    public boolean isBurning()
    {
        return false;
    }
}
