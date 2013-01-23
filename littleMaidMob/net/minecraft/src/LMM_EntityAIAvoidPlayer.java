package net.minecraft.src;

import java.util.List;

public class LMM_EntityAIAvoidPlayer extends EntityAIBase implements LMM_IEntityAI {
	
    /** The entity we are attached to */
	protected LMM_EntityLittleMaid theMaid;
    protected EntityPlayer theMaster;
    protected float speedNormal;
    protected PathEntity avoidPath;
    /** The PathNavigate of our entity */
    protected PathNavigate entityPathNavigate;
    protected boolean isEnable;

    public boolean isActive;
    public int minDist; 

    
    public LMM_EntityAIAvoidPlayer(LMM_EntityLittleMaid pEntityLittleMaid, float pSpeed, int pMinDist) {
        theMaid = pEntityLittleMaid;
        speedNormal = pSpeed;
        entityPathNavigate = pEntityLittleMaid.getNavigator();
        isActive = false;
        isEnable = false;
        minDist = pMinDist;
        setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
    	if (!isEnable || !isActive || !theMaid.isMaidContract()) {
    		isActive = false;
    		return false;
    	}
    	
    	theMaster = theMaid.mstatMasterEntity;
    	
    	// �Ώۂ͌����邩�H�Ă����ꂢ��Ȃ��ˁH
        if (!theMaid.getEntitySenses().canSee(theMaster)) {
            return false;
        }

        // �ړ��������
        Vec3 vec3d = RandomPositionGenerator.findRandomTargetBlockAwayFrom(theMaid, minDist, 7, Vec3.createVectorHelper(theMaster.posX, theMaster.posY, theMaster.posZ));

        // �ړ��悪����
        if (vec3d == null) {
            return false;
        }
        // �ړ���̋������߂�
        if (theMaster.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < theMaid.mstatMasterDistanceSq) {
            return false;
        }

        avoidPath = entityPathNavigate.getPathToXYZ(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);

        if (avoidPath == null) {
            return false;
        }

        return avoidPath.isDestinationSame(vec3d);
    }

    @Override
    public boolean continueExecuting() {
        return !entityPathNavigate.noPath() && theMaid.getDistanceSqToEntity(theMaster) < 144D;
    }

    @Override
    public void startExecuting() {
        entityPathNavigate.setPath(avoidPath, speedNormal);
    }

    @Override
    public void resetTask() {
        isActive = false;
    }

    
    public void setActive() {
    	// ����J�n
    	isActive = true;
    }
    
	// ���s�\�t���O
    @Override
	public void setEnable(boolean pFlag) {
		isEnable = pFlag;
	}
	
    @Override
	public boolean getEnable() {
		return isEnable;
	}
	

    
}
