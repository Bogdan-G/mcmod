package net.minecraft.src;

import java.util.List;

/**
 * LMM�p�Ǝ�AI�����Ɏg�p�B
 * ���̌p���N���X��AI�����Ƃ��ēn�����Ƃ��ł���B
 * �܂��AAI�����I�𒆂͓���̊֐��������đI�𒆂̃N���X�݂̂����������B
 * �C���X�^���X�����鎖�ɂ�胍�[�J���ϐ���ێ��B
 */
public abstract class LMM_EntityModeBase {

	public final LMM_EntityLittleMaid owner;
	
	/**
	 * ������
	 */
	public LMM_EntityModeBase(LMM_EntityLittleMaid pEntity) {
		owner = pEntity;
	}
	
	
	public int fpriority;
	/**
	 * �D�揇�ʁB
	 */
	public abstract int priority();
	
	/**
	 * �N�����̏������B
	 */
	public void init() {
	}
	
	/**
	 * ���[�h�̒ǉ��B
	 */
	public abstract void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting);

	/**
	 * �Ǝ��f�[�^�ۑ��p�B
	 */
	public void writeEntityToNBT(NBTTagCompound par1nbtTagCompound) {
	}
	/**
	 * �Ǝ��f�[�^�Ǎ��p�B
	 */
	public void readEntityFromNBT(NBTTagCompound par1nbtTagCompound) {
	}

	/**
	 * renderSpecial�̒ǉ������p�B
	 */
	public void showSpecial(LMM_RenderLittleMaid prenderlittlemaid, double px, double py, double pz) {
	}

	/**
	 * �T�[�o�[���݂̖̂��������B
	 */
	public void updateAITick(int pMode) {
	}

	/**
	 * ���������B
	 */
	public void onUpdate(int pMode) {
	}
	
	/**
	 * ���̂ւ�̏����͎኱���Ԃ������Ă��ǂ��B
	 * ���̃A�C�e�����g�p���������B
	 */
	public boolean interact(EntityPlayer pentityplayer, ItemStack pitemstack) {
		return false;
	}

	/**
	 * �����Ń��[�h�`�F���W�������B
	 */
	public boolean changeMode(EntityPlayer pentityplayer) {
		return false;
	}

	/**
	 * ���[�h�`�F���W���̐ݒ菈���̖{�́B
	 * �������ɏ����������Ȃ��ƃ��[�h���ɂ��������Ȃ邩���H
	 */
	public boolean setMode(int pMode) {
		return false;
	}

	/**
	 * �g�p�A�C�e���̑I���B
	 * �߂�l�̓X���b�g�ԍ�
	 */
	public int getNextEquipItem(int pMode) {
		// ���I��
		return -1;
	}
	
	/**
	 * �A�C�e������ۂ̔��莮�B
	 * �E���ɍs���A�C�e���̔���B
	 */
	public boolean checkItemStack(ItemStack pItemStack) {
		// ����ΏۃA�C�e���̐ݒ�Ȃ�
		return false;
	}

	/**
	 * �U�����菈���B
	 * ����ȍU������͂����Ŏ����B
	 */
	public boolean attackEntityAsMob(int pMode, Entity pEntity) {
		// ����U���̐ݒ�Ȃ�
		return false;
	}

	/**
	 * �u���b�N�̃`�F�b�N��������邩�ǂ����B
	 * ���莮�̂ǂ�����g����������őI���B
	 */
	public boolean isSearchBlock() {
		return false;
	}
	
	/**
	 * isSearchBlock=false�̂Ƃ��ɔ��肳���B
	 */
	public boolean shouldBlock(int pMode) {
		return false;
	}
	
	/**
	 * �T�����߂��u���b�N�ł��邩�B
	 * true��Ԃ��ƌ����I���B
	 */
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		return false;
	}
	
	/**
	 * �����͈͂ɍ��G�Ώۂ��Ȃ������B
	 */
	public TileEntity overlooksBlock(int pMode) {
		return null;
	}

	/**
	 * �˒������ɓ���������s�����B
	 * �߂�l��true�̎��͏I�������ɓ���p��
	 */
	public boolean executeBlock(int pMode, int px, int py, int pz) {
		return false;
	}
	
	/**
	 * AI���s���ɌĂ΂��B
	 */
	public void startBlock(int pMode) {
	}

	/**
	 * AI�I�����ɌĂ΂��B
	 */
	public void resetBlock(int pMode) {
	}

	
	/**
	 * �Ǝ����G�����̎g�p�L��
	 */
	public boolean isSearchEntity() {
		return false;
	}
	
	/**
	 * �Ǝ����G����
	 */
	public boolean checkEntity(int pMode, Entity pEntity) {
		return false;
	}
	
	/**
	 * ���������p
	 */
	public int colorMultiplier(float pLight, float pPartialTicks) {
		return 0;
	}
	
	/**
	 * ��_�����̏����P�B
	 * 0�ȏ��Ԃ��Ə�����������B
	 * 1:false�Ō��̏������I������B
	 * 2:true�Ō��̏������I������B
	 */
	public int attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return 0;
	}
	/**
	 * ��_�����̏����Q�B
	 * true��Ԃ��Ə�����������B
	 */
	public boolean damageEntity(int pMode, DamageSource par1DamageSource, int par2) {
		return false;
	}
	
	/**
	 * �������g���Ă���Tile�Ȃ�True��Ԃ��B
	 */
	public boolean isUsingTile(TileEntity pTile) {
		return false;
	}

	/**
	 * �����Ă�Tile��Ԃ��B
	 */
	public List<TileEntity> getTiles() {
		return null;
	}

	/**
	 * �L���˒������𒴂������̏���
	 */
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		return owner.getNavigator().tryMoveToXYZ(pX, pY, pZ, owner.getAIMoveSpeed());
	}

	/**
	 * ���E�����𒴂������̏���
	 */
	public void farrangeBlock() {
		owner.getNavigator().clearPathEntity();
	}


}
