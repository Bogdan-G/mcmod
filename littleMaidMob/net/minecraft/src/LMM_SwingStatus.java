package net.minecraft.src;

public class LMM_SwingStatus {

	public int index;
	public int lastIndex;
	public boolean isSwingInProgress;
	public float swingProgress;
	public float prevSwingProgress;
	public int swingProgressInt;
	public float onGround;
	public int attackTime;


	public LMM_SwingStatus() {
		index = lastIndex = -1;
		isSwingInProgress = false;
		swingProgress = prevSwingProgress = 0.0F;
		onGround = 0F;
		attackTime = 0;
	}

	/**
	 * TODO:���l�̍X�V�p�AonEntityUpdate���ŌĂԎ�:����񂩁H
	 */
	public void onEntityUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
	}

	/**
	 * ���l�̍X�V�p�AonUpdate���ŌĂԎ�
	 */
	public void onUpdate(LMM_EntityLittleMaid pEntity) {
		prevSwingProgress = swingProgress;
		if (attackTime > 0) {
			attackTime--;
		}
		
		// �r�U��
		int li = pEntity.getSwingSpeedModifier();
		if (isSwingInProgress) {
			if (swingProgressInt == 0) {
				pEntity.playLittleMaidSound(pEntity.maidAttackSound, true);
			}
			pEntity.maidAttackSound = LMM_EnumSound.Null;
			
			swingProgressInt++;
			if(swingProgressInt >= li) {
				swingProgressInt = 0;
				isSwingInProgress = false;
			}
		} else {
			swingProgressInt = 0;
		}
		swingProgress = (float)swingProgressInt / (float)li;
	}

	/**
	 * �I�𒆂̃X���b�g�ԍ���ݒ�
	 */
	public void setSlotIndex(int pIndex) {
		index = pIndex;
		lastIndex = -2;
	}

	/**
	 * �I�𒆂̃C���x���g�����A�C�e���X�^�b�N��Ԃ�
	 */
	public ItemStack getItemStack(LMM_EntityLittleMaid pEntity) {
		if (index > -1) {
			return pEntity.maidInventory.getStackInSlot(index);
		} else {
			return null;
		}
	}

	public boolean canAttack() {
		return attackTime <= 0;
	}

	public float getSwingProgress(float ltime) {
		float lf = swingProgress - prevSwingProgress;
		
		if (lf < 0.0F) {
			++lf;
		}
		
		return onGround = prevSwingProgress + lf * ltime;
	}

	/**
	 * �ύX�����邩�ǂ�����Ԃ��A�t���O���N���A����B
	 */
	public boolean checkChanged() {
		boolean lflag = index != lastIndex;
		lastIndex = index;
		return lflag;
	}

}
