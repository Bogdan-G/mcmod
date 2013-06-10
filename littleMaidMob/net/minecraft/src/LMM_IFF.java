package net.minecraft.src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import net.minecraft.server.MinecraftServer;

/**
 * IFF���Ǘ����邽�߂̃N���X�A�قڃ}���`�p�B
 * username : null=���[�J���v���C���ADefault���g��
 */
public class LMM_IFF {

	public static final int iff_Enemy = 0;
	public static final int iff_Unknown = 1;
	public static final int iff_Friendry = 2;

	/**
	 * ���[�J���p�A�Ⴕ���̓}���`�̃f�t�H���g�ݒ�
	 */
	public static Map<String, Integer> DefaultIFF = new TreeMap<String, Integer>();
	/**
	 * ���[�U����IFF
	 */
	public static Map<String, Map<String, Integer>> UserIFF = new HashMap<String, Map<String, Integer>>();

	/**
	 * IFF�̃Q�b�g
	 */
	public static Map<String, Integer> getUserIFF(String pUsername) {
		if (pUsername == null) {
			return DefaultIFF;
		}
		if (MMM_Helper.isLocalPlay()) {
			pUsername = "";
		}
		
		if (!UserIFF.containsKey(pUsername)) {
			// IFF���Ȃ��̂ō쐬
			if (pUsername.isEmpty()) {
				UserIFF.put(pUsername, DefaultIFF);
			} else {
				Map<String, Integer> lmap = new HashMap<String, Integer>();
				lmap.putAll(DefaultIFF);
				UserIFF.put(pUsername, lmap);
			}
		}
		// ���ɂ���
		return UserIFF.get(pUsername);
	}

	public static void setIFFValue(String pUsername, String pName, int pValue) {
		Map<String, Integer> lmap = getUserIFF(pUsername);
		lmap.put(pName, pValue);
	}

	protected static int checkEntityStatic(String pName, Entity pEntity,
			int pIndex, Map<String, Entity> pMap) {
		int liff = LMM_IFF.iff_Unknown;
		if (pEntity instanceof EntityLiving) {
			if (pEntity instanceof LMM_EntityLittleMaid) {
				switch (pIndex) {
				case 0:
					// �쐶��
					liff = LMM_IFF.iff_Unknown;
					break;
				case 1:
					// �����̌_���
					pName = (new StringBuilder()).append(pName).append(":Contract").toString();
					((LMM_EntityLittleMaid) pEntity).setContract(true);
					liff = LMM_IFF.iff_Friendry;
					break;
				case 2:
					// ���l�̌_���
					pName = (new StringBuilder()).append(pName).append(":Others").toString();
					((LMM_EntityLittleMaid) pEntity).setContract(true);
					liff = LMM_IFF.iff_Friendry;
					break;
				}
			} else if (pEntity instanceof EntityTameable) {
				switch (pIndex) {
				case 0:
					// �쐶��
					break;
				case 1:
					// �����̉ƒ{
					pName = (new StringBuilder()).append(pName).append(":Taim").toString();
					((EntityTameable) pEntity).setTamed(true);
					liff = LMM_IFF.iff_Friendry;
					break;
				case 2:
					// ���l�̉ƒ{
					pName = (new StringBuilder()).append(pName)
							.append(":Others").toString();
					((EntityTameable) pEntity).setTamed(true);
					liff = LMM_IFF.iff_Unknown;
					break;
				}
				if (pIndex != 0) {
					if (pEntity instanceof EntityOcelot) {
						((EntityOcelot) pEntity).setTameSkin(1 + (new Random()).nextInt(3));
					}
				}
			}
			if (pMap != null) {
				// �\���pEntity�̒ǉ�
				pMap.put(pName, (EntityLiving) pEntity);
				mod_LMM_littleMaidMob.Debug(pName + " added.");
			}
			
			// IFF�̏����l
			if (!DefaultIFF.containsKey(pName)) {
				if (pEntity instanceof IMob) {
					liff = LMM_IFF.iff_Enemy;
				}
				DefaultIFF.put(pName, liff);
			}
		}
		
		return liff;
	}

	/**
	 * �G�������ʔ���
	 */
	public static int getIFF(String pUsername, String entityname) {
		if (entityname == null) {
			return mod_LMM_littleMaidMob.Aggressive ? iff_Enemy : iff_Friendry;
		}
		int lt = iff_Enemy;
		Map<String, Integer> lmap = getUserIFF(pUsername);
		if (lmap.containsKey(entityname)) {
			lt = lmap.get(entityname);
		} else if (lmap != DefaultIFF && DefaultIFF.containsKey(entityname)) {
			lt = DefaultIFF.get(entityname);
			lmap.put(entityname, lt);
		} else {
			int li = entityname.indexOf(":");
			String ls;
			if (li > -1) {
				ls = entityname.substring(0, li);
			} else {
				ls = entityname;
			}
			Entity lentity = EntityList.createEntityByName(ls, null);
			li = 0;
			if (entityname.indexOf(":Contract") > -1) {
				li = 1;
			} else 
			if (entityname.indexOf(":Taim") > -1) {
				li = 1;
			} else
			if (entityname.indexOf(":Others") > -1) {
				li = 2;
			}
			lt = checkEntityStatic(ls, lentity, li, null);
			lmap.put(entityname, lt);
		}
		return lt;
	}

	/**
	 * �G�������ʔ���
	 */
	public static int getIFF(String pUsername, Entity entity) {
		if (entity == null || !(entity instanceof EntityLiving)) {
			return mod_LMM_littleMaidMob.Aggressive ? iff_Enemy : iff_Friendry;
		}
		String lename = EntityList.getEntityString(entity);
		String lcname = lename;
		if (lename == null) {
			// ���̖���`MOB�A�v���[���[�Ƃ��H
			return iff_Friendry;
			// return mod_LMM_littleMaidMob.Aggressive ? iff_Unknown :
			// iff_Friendry;
		}
		int li = 0;
		if (entity instanceof LMM_EntityLittleMaid) {
			if (((LMM_EntityLittleMaid) entity).isContract()) {
				if (((LMM_EntityLittleMaid) entity).getMaidMaster().contentEquals(pUsername)) {
					// ������
					lcname = (new StringBuilder()).append(lename).append(":Contract").toString();
					li = 1;
				} else {
					// ���l��
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		} else if (entity instanceof EntityTameable) {
			if (((EntityTameable) entity).isTamed()) {
				if (((EntityTameable) entity).getOwnerName().contentEquals(pUsername)) {
					// ������
					lcname = (new StringBuilder()).append(lename).append(":Taim").toString();
					li = 1;
				} else {
					// ���l��
					lcname = (new StringBuilder()).append(lename).append(":Others").toString();
					li = 2;
				}
			}
		}
		if (!getUserIFF(pUsername).containsKey(lcname)) {
			checkEntityStatic(lename, entity, li, null);
		}
		return getIFF(pUsername, lcname);
	}

	public static void loadIFFs() {
		// �T�[�o�[����
		if (!MMM_Helper.isClient) {
			// �T�[�o�[������
			loadIFF("");
			File lfile = MinecraftServer.getServer().getFile("");
			for (File lf : lfile.listFiles()) {
				if (lf.getName().endsWith("littleMaidMob.iff")) {
					String ls = lf.getName().substring(17, lf.getName().length() - 20);
					mod_LMM_littleMaidMob.Debug(ls);
					loadIFF(ls);
				}
			}
		} else {
			// �N���C�A���g��
			loadIFF(null);
		}
	}

	protected static File getFile(String pUsername) {
		File lfile;
		if (pUsername == null) {
			lfile = new File(MMM_Helper.mc.getMinecraftDir(), "config/littleMaidMob.iff");
		} else {
			String lfilename;
			if (pUsername.isEmpty()) {
				lfilename = "config/littleMaidMob.iff";
			} else {
				lfilename = "config/littleMaidMob_".concat(pUsername).concat(".iff");
			}
			lfile = MinecraftServer.getServer().getFile(lfilename);
		}
		mod_LMM_littleMaidMob.Debug(lfile.getAbsolutePath());
		return lfile;
	}

	public static void loadIFF(String pUsername) {
		// IFF �t�@�C���̓Ǎ���
		// ����̓T�[�o�[���őz��
		File lfile = getFile(pUsername);
		if (!(lfile.exists() && lfile.canRead())) {
			return;
		}
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			FileReader fr = new FileReader(lfile);
			BufferedReader br = new BufferedReader(fr);
			
			String s;
			while ((s = br.readLine()) != null) {
				String t[] = s.split("=");
				if (t.length > 1) {
					if (t[0].startsWith("triggerWeapon")) {
						LMM_TriggerSelect.appendTriggerItem(pUsername, t[0].substring(13), t[1]);
						continue;
					}
					int i = Integer.valueOf(t[1]);
					if (i > 2) {
						i = iff_Unknown;
					}
					lmap.put(t[0], i);
				}
			}
			
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void saveIFF(String pUsername) {
		// IFF �t�@�C���̏�����
		File lfile = getFile(MMM_Helper.isClient ? null : pUsername);
		Map<String, Integer> lmap = getUserIFF(pUsername);
		
		try {
			if ((lfile.exists() || lfile.createNewFile()) && lfile.canWrite()) {
				FileWriter fw = new FileWriter(lfile);
				BufferedWriter bw = new BufferedWriter(fw);
				
				// �g���K�[�A�C�e���̃��X�g
				for (Entry<Integer, List<Integer>> le : LMM_TriggerSelect
						.getUserTrigger(pUsername).entrySet()) {
					StringBuilder sb = new StringBuilder();
					sb.append("triggerWeapon")
							.append(LMM_TriggerSelect.selector.get(le.getKey()))
							.append("=");
					if (!le.getValue().isEmpty()) {
						sb.append(le.getValue().get(0));
						for (int i = 1; i < le.getValue().size(); i++) {
							sb.append(",").append(le.getValue().get(i));
						}
					}
					sb.append("\r\n");
					bw.write(sb.toString());
				}
				
				for (Map.Entry<String, Integer> me : lmap.entrySet()) {
					bw.write(String.format("%s=%d\r\n", me.getKey(),
							me.getValue()));
				}
				
				bw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
