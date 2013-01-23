package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class LMM_EntityModeManager extends MMM_ManagerBase {

	public static final String prefix = "EntityMode";
    public static List<LMM_EntityModeBase> maidModeList = new ArrayList<LMM_EntityModeBase>();

    
	public static void init() {
		// ���薼�̂��v���t�B�b�N�X�Ɏ���mod�t�@�C�����l��
		MMM_FileManager.getModFile("EntityMode", prefix);
	}
	
	public static void loadEntityMode() {
		(new LMM_EntityModeManager()).load();
	}

	@Override
	protected String getPreFix() {
		return prefix;
	}

	@Override
	protected boolean append(Class pclass) {
		// �v���C�I���e�B�[���ɒǉ�
		// �\�[�^�[�g���H
		if (!LMM_EntityModeBase.class.isAssignableFrom(pclass)) {
			return false;
		}
		
		try {
			LMM_EntityModeBase lemb = null;
			lemb = (LMM_EntityModeBase)pclass.getConstructor(LMM_EntityLittleMaid.class).newInstance((LMM_EntityLittleMaid)null);
			lemb.init();
			
			if (maidModeList.isEmpty() || lemb.priority() >= maidModeList.get(maidModeList.size() - 1).priority()) {
				maidModeList.add(lemb);
			} else {
				for (int li = 0; li < maidModeList.size(); li++) {
					if (lemb.priority() < maidModeList.get(li).priority()) {
						maidModeList.add(li, lemb);
						break;
					}
				}
			}

			return true;
		} catch (Exception e) {
		} catch (Error e) {
		}

		return false;
	}
	
	/**
	 * AI�ǉ��p�̃��X�g���l���B 
	 */
	public static List<LMM_EntityModeBase> getModeList(LMM_EntityLittleMaid pentity) {
		List<LMM_EntityModeBase> llist = new ArrayList<LMM_EntityModeBase>();
		for (LMM_EntityModeBase lmode : maidModeList) {
			try {
				llist.add(lmode.getClass().getConstructor(LMM_EntityLittleMaid.class).newInstance(pentity));
			} catch (Exception e) {
			} catch (Error e) {
			}
		}
		
		return llist;
	}

}
