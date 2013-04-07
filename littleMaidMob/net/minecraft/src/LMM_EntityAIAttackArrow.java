package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.List;

public class LMM_EntityAIAttackArrow extends EntityAIBase implements LMM_IEntityAI {

	protected boolean fEnable;
	
	protected LMM_EntityLittleMaid fMaid;
	protected LMM_EntityLittleMaidAvatar fAvatar;
	protected LMM_InventoryLittleMaid fInventory;
	protected LMM_SwingStatus swingState;
	protected World worldObj;
	protected EntityLiving fTarget;
	protected int fForget;

	
	public LMM_EntityAIAttackArrow(LMM_EntityLittleMaid pEntityLittleMaid) {
		fMaid = pEntityLittleMaid;
		fAvatar = pEntityLittleMaid.maidAvatar;
		fInventory = pEntityLittleMaid.maidInventory;
		swingState = pEntityLittleMaid.getSwingStatusDominant();
		worldObj = pEntityLittleMaid.worldObj;
		fEnable = false;
		setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() {
		EntityLiving entityliving = fMaid.getAttackTarget();
		
		if (!fEnable || entityliving == null || entityliving.isDead) {
			fMaid.setAttackTarget(null);
			fMaid.setTarget(null);
			fMaid.getNavigator().clearPathEntity();
			return false;
		} else {
			fTarget = entityliving;
			return true;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		fMaid.playSound(fMaid.isBloodsuck() ? LMM_EnumSound.findTarget_B : LMM_EnumSound.findTarget_N, false);
	}

	@Override
	public boolean continueExecuting() {
		return shouldExecute() || !fMaid.getNavigator().noPath();
	}

	@Override
	public void resetTask() {
		fTarget = null;
	}

	@Override
	public void updateTask() {
		double lrange = 225D;
		double ldist = fMaid.getDistanceSqToEntity(fTarget);
		boolean lsee = fMaid.getEntitySenses().canSee(fTarget);
	
		// ���E�̊O�ɏo�����莞�ԂŖO����
		if (lsee) {
			fForget = 0;
		} else {
			fForget++;
		}
		
		// �U���Ώۂ�����
		fMaid.getLookHelper().setLookPositionWithEntity(fTarget, 30F, 30F);
		
		if (ldist < lrange) {
			// �L���˒���
			double atx = fTarget.posX - fMaid.posX;
			double aty = fTarget.posY - fMaid.posY;
			double atz = fTarget.posZ - fMaid.posZ;
			if (fTarget.isEntityAlive()) {
				ItemStack litemstack = fMaid.getCurrentEquippedItem();
				// �G�Ƃ̃x�N�g��
				double atl = atx * atx + aty * aty + atz * atz;
				double il = -1D;
				double milsq = 10D;
				Entity masterEntity = fMaid.getMaidMasterEntity();
				if (masterEntity != null) {
					// ��Ƃ̃x�N�g��
					double amx = masterEntity.posX - fMaid.posX;
					double amy = masterEntity.posY - fMaid.posY - 2D;
					double amz = masterEntity.posZ - fMaid.posZ;
					
					// ���̒l���O�`�P�Ȃ�^�[�Q�b�g�Ƃ̊ԂɎ傪����
					il = (amx * atx + amy * aty + amz * atz) / atl;
					
					// �ː��x�N�g���Ǝ�Ƃ̐����x�N�g��
					double mix = (fMaid.posX + il * atx) - masterEntity.posX;
					double miy = (fMaid.posY + il * aty) - masterEntity.posY + 2D;
					double miz = (fMaid.posZ + il * atz) - masterEntity.posZ;
					// �ː������Ƃ̋���
					milsq = mix * mix + miy * miy + miz * miz;
//                	mod_littleMaidMob.Debug(String.format("il:%f, milsq:%f", il, milsq));
				}
				
				
				if (litemstack != null && !(litemstack.getItem() instanceof ItemFood) && !fMaid.weaponReload) {
					int lastentityid = worldObj.loadedEntityList.size();
					int itemcount = litemstack.stackSize;
					fMaid.mstatAimeBow = true;
					fAvatar.getValueVectorFire(atx, aty, atz, atl);
					// �_�C���A���w�����Ȃ疡���ւ̌�˂��C�����y��
					boolean fsh = true;
					int helmid = !fMaid.isMaskedMaid() ? 0 : fInventory.armorInventory[3].getItem().itemID;
					if (helmid == Item.helmetDiamond.itemID || helmid == Item.helmetGold.itemID) {
						// �ː����̊m�F
						List list = worldObj.getEntitiesWithinAABBExcludingEntity(fMaid, fMaid.boundingBox.expand(16D, 16D, 16D));
						Vec3 vec3d = Vec3.createVectorHelper(fMaid.posX, fMaid.posY, fMaid.posZ);
						Vec3 vec3d1 = Vec3.createVectorHelper(fTarget.posX, fTarget.posY, fTarget.posZ);
						for(int l = 0; l < list.size(); l++) {
							Entity entity1 = (Entity)list.get(l);
							// �����□���ȊO�ɒ���Ȃ猂��
							if (entity1 == fMaid || entity1 == fTarget || !entity1.canBeCollidedWith() || !fMaid.getIFF(entity1)) { 
								continue;
							}
							float f5 = 0.3F;
							AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
							MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3d, vec3d1);
							if(movingobjectposition1 == null) {
								continue;
							}
							fsh = false;
							mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire to ID:%d.", fMaid.entityId, entity1.entityId));
						}
					}
					fsh &= (milsq > 3D || il < 0D);
//            		mod_littleMaidMob.Debug(String.format("id:%d at:%d", entityId, attackTime));
					if (((fMaid.weaponFullAuto && !fsh) || (fsh && fMaid.getSwingStatusDominant().canAttack())) && fAvatar.isItemTrigger) {
						// �V���[�g
						mod_LMM_littleMaidMob.Debug(String.format("id:%d shoot.", fMaid.entityId));
						fAvatar.stopUsingItem();
						fMaid.setSwing(30, LMM_EnumSound.shoot);
					} else {
						// �`���[�W
						if (litemstack.getMaxItemUseDuration() > 500) {
//                			mod_littleMaidMob.Debug(String.format("non reload.%b", isMaskedMaid));
							// �����[�h�����̒ʏ핺��
							if (!fAvatar.isUsingItemLittleMaid()) {
								// �\��
								if (!fMaid.weaponFullAuto || fsh) {
									// �t���I�[�g�����̏ꍇ�͎ː��m�F
									int at = ((helmid == Item.helmetIron.itemID) || (helmid == Item.helmetDiamond.itemID)) ? 26 : 16;
									if (swingState.attackTime < at) {
										fMaid.setSwing(at, LMM_EnumSound.sighting);
										litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
										mod_LMM_littleMaidMob.Debug(String.format("id:%d redygun.", fMaid.entityId));
									}
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire FullAuto.", fMaid.entityId));
								}
							}
						} 
						else if (litemstack.getMaxItemUseDuration() == 0) {
							// �ʏ퓊������
							if (swingState.canAttack() && !fAvatar.isUsingItem()) {
								if (fsh) {
									litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
									// �Ӑ}�I�ɃV���[�g�X�p���ŉ�����悤�ɂ��Ă���
									fMaid.setSwing(10, (litemstack.stackSize == itemcount) ? LMM_EnumSound.shoot_burst : LMM_EnumSound.Null);
									mod_LMM_littleMaidMob.Debug(String.format("id:%d throw weapon.", fMaid.entityId));
								} else {
									mod_LMM_littleMaidMob.Debug(String.format("ID:%d-friendly fire throw weapon.", fMaid.entityId));
								}
							}
						} else {
							// �����[�h�L��̓��ꕺ��
							if (!fAvatar.isUsingItemLittleMaid()) {
								litemstack = litemstack.useItemRightClick(worldObj, fAvatar);
								mod_LMM_littleMaidMob.Debug(String.format("%d reload.", fMaid.entityId));
							}
							// �����[�h�I���܂ŋ����I�ɍ\����
							swingState.attackTime = 5;
						}
					}
//            		maidAvatarEntity.setValueRotation();
					fAvatar.setValueVector();
					// �A�C�e�����S���Ȃ���
					if (litemstack.stackSize <= 0) {
						fMaid.destroyCurrentEquippedItem();
						fMaid.getNextEquipItem();
					} else {
						fInventory.setInventoryCurrentSlotContents(litemstack);
					}
					
					// ��������Entity���`�F�b�N����maidAvatarEntity�����Ȃ������m�F
					List<Entity> newentitys = worldObj.loadedEntityList.subList(lastentityid, worldObj.loadedEntityList.size());
					boolean shootingflag = false;
					if (newentitys != null && newentitys.size() > 0) {
						mod_LMM_littleMaidMob.Debug(String.format("new FO entity %d", newentitys.size()));
						for (Entity te : newentitys) {
							if (te.isDead) {
								shootingflag = true;
								continue;
							}
							try {
								// ���đ̂̎��u��������
								Field fd[] = te.getClass().getDeclaredFields();
//                				mod_littleMaidMob.Debug(String.format("%s, %d", e.getClass().getName(), fd.length));
								for (Field ff : fd) {
									// �ϐ���������Avatar�Ɠ������������ƒu��������
									ff.setAccessible(true);
									Object eo = ff.get(te);
									if (eo.equals(fAvatar)) {
										ff.set(te, this);
										mod_LMM_littleMaidMob.Debug("Replace FO Owner.");
									}
								}
							}
							catch (Exception exception) {
							}
						}
					}
					// ���ɖ������Ă����ꍇ�̏���
					if (shootingflag) {
						for (Object obj : worldObj.loadedEntityList) {
							if (obj instanceof EntityCreature && !(obj instanceof LMM_EntityLittleMaid)) {
								EntityCreature ecr = (EntityCreature)obj;
								if (ecr.entityToAttack == fAvatar) {
									ecr.entityToAttack = fMaid;
								}
							}
						}
					}
					
				}
				
			}
			
			
			//�I�[�r�b�g�̏����H����L�����H
/*
        	// TODO:�����ςȂ��ƂɂȂ��Ă��ňꎞ��~
        	if (fMaid.isBloodsuck()) {
        		// Bloodsuck�n�͋t����
            	fMaid.rotationYaw = (float)((Math.atan2(atz, atx) * 180D) / 3.1415927410125732D) + 100F;
        	} else {
            	fMaid.rotationYaw = (float)((Math.atan2(atz, atx) * 180D) / 3.1415927410125732D) - 90F;
        	}
*/
		} else {
			// �L���˒��O
			fMaid.getNavigator().tryMoveToEntityLiving(fTarget, fMaid.getAIMoveSpeed());
			if (fMaid.getNavigator().noPath()) {
				fMaid.setAttackTarget(null);
			}
			mod_LMM_littleMaidMob.Debug(String.format("id:%d Target renge out.", fMaid.entityId));
			if (fMaid.weaponFullAuto && fAvatar.isItemTrigger) {
				fAvatar.stopUsingItem();
			} else {
				fAvatar.clearItemInUse();
			}
			
		}
		
	}

	@Override
	public void setEnable(boolean pFlag) {
		fEnable = pFlag;
	}

	@Override
	public boolean getEnable() {
		return fEnable;
	}

}
