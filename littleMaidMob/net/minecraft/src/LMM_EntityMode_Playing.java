package net.minecraft.src;

public class LMM_EntityMode_Playing extends LMM_EntityModeBase {

	public static final int mpr_NULL = 0;
	public static final int mpr_QuickShooter = 10;
	public static final int mpr_StockShooter = 20;

	public LMM_EntityMode_Playing(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
	}

	@Override
	public int priority() {
		return 900;
	}

	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.T-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.F-Playing", "Playing");
		// ModLoader.addLocalization("littleMaidMob.mode.D-Playing", "Playing");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove,
			EntityAITasks pDefaultTargeting) {

	}

	@Override
	public void updateAITick(int pMode) {
		if (owner.isFreedom()) {
			// ���R�s�����̌ő̂͌Վ�ἁX�ƌ������������B
			if (owner.worldObj.isDaytime()) {
				// ���Ԃ̂��V��
				
				// �ጴ����
				if (!owner.isPlaying()) {
					// TODO:���V�є���
					int xx = MathHelper.floor_double(owner.posX);
					int yy = MathHelper.floor_double(owner.posY);
					int zz = MathHelper.floor_double(owner.posZ);
					
					// 3x3����̕����Ȃ炨�V�є��肪����
					boolean f = true;
					for (int z = -1; z < 2; z++) {
						for (int x = -1; x < 2; x++) {
							f &= owner.worldObj.getBlockId(xx + x, yy, zz + z) == Block.snow.blockID;
						}
					}
					int lpr = owner.rand.nextInt(100) - 97;
					lpr = (f && lpr > 0) ? lpr * 10 : 0;
					owner.setPlayingRole(lpr);
					if (f) {
						// mod_littleMaidMob.Debug(String.format("playRole-%d:%d", entityId, playingRole));
					}
					
				}
				
			} else {
				// ��̂��V��
			}
			
			// �`�F�X�g����
			if (owner.getAttackTarget() == null
					&& owner.maidInventory.getFirstEmptyStack() == -1) {
				
			}

		}
	}

}
