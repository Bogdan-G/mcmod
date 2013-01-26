package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ���[�h�؂�ւ��p�g���K�[�A�C�e���̃R���e�i�B
 * �}���`�΍��p�B
 * �f�[�^�̓ǂݍ��݂�IFF�ōs���Ă���B
 */
public class LMM_TriggerSelect {

	public static List<String> selector = new ArrayList<String>();
	public static Map<String, Map<Integer, List<Integer>>> usersTrigger = new HashMap<String, Map<Integer,List<Integer>>>();
	public static Map<Integer, List<Integer>> defaultTrigger = new HashMap<Integer,List<Integer>>();


	public static Map<Integer, List<Integer>> getUserTrigger(String pUsername) {
		if (pUsername == null) {
			return defaultTrigger;
		}
		if (MMM_Helper.isClient && MMM_Helper.mc.isIntegratedServerRunning()) {
			// �V���O�����s���͖��̃u�����N�ɁB
			pUsername = "";
		}
		// ���݃`�F�b�N�A����������ǉ�
		if (!usersTrigger.containsKey(pUsername)) {
			if (pUsername.isEmpty()) {
				// ���̂��u�����N�̎��̓f�t�H���g�̂��̂փ����N�B
				usersTrigger.put(pUsername, defaultTrigger);
			} else {
				Map<Integer, List<Integer>> lmap = new HashMap<Integer, List<Integer>>();
				lmap.putAll(defaultTrigger);
				usersTrigger.put(pUsername, lmap);
			}
		}
		
		return usersTrigger.get(pUsername);
	}

	public static List<Integer> getuserTriggerList(String pUsername, String pSelector) {
		if (!selector.contains(pSelector)) {
			selector.add(pSelector);
		}
		int lindex = selector.indexOf(pSelector);
		Map<Integer, List<Integer>> lmap = getUserTrigger(pUsername);
		List<Integer> llist;
		if (lmap.containsKey(lindex)) {
			llist = lmap.get(lindex);
		} else {
			llist = new ArrayList<Integer>();
			lmap.put(lindex, llist);
		}
		return llist;
	}


	/**
	 * ���[�U�[���Ƀg���K�[�A�C�e����ݒ肷��B
	 */
	public static void appendTriggerItem(String pUsername, String pSelector, String pIndexstr) {
		// �g���K�[�A�C�e���̒ǉ�
		appendWeaponsIndex(pIndexstr, getuserTriggerList(pUsername, pSelector));
	}

	/**
	 * �g���K�[�A�C�e������͂��ēo�^�B
	 */
	private static void appendWeaponsIndex(String indexstr, List<Integer> indexlist) {
		if (indexstr.isEmpty()) return;
		String[] s = indexstr.split(",");
		for (String t : s) {
			indexlist.add(Integer.valueOf(t));
		}
	}

	/**
	 * �A�C�e�����w�肳�ꂽ�g���K�[�ɓo�^����Ă��邩�𔻒�
	 */
	public static boolean checkWeapon(String pUsername, String pSelector, ItemStack pItemStack) {
		if (!selector.contains(pSelector)) {
			return false;
		}
		if (!usersTrigger.containsKey(pUsername)) {
			return false;
		}
		
		return getuserTriggerList(pUsername, pSelector).contains(pItemStack.itemID);
	}

}
