package net.minecraft.src;

import static net.minecraft.src.LMM_Statics.*;
import java.util.Map.Entry;

public class LMM_Net {
	


	
	
	
	
	/**
	 * �n���ꂽ�f�[�^�̐擪�Ɏ�����EntityID��t�^���đS�ẴN���C�A���g�֑��M
	 */
	public static void sendToAllEClient(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		((WorldServer)pEntity.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(pEntity, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * �n���ꂽ�f�[�^�̐擪�Ɏ�����EntityID��t�^���ē���̂̃N���C�A���g�֑��M
	 */
	public static void sendToEClient(NetServerHandler pHandler, LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	public static void sendToClient(NetServerHandler pHandler, byte[] pData) {
		ModLoader.serverSendPacket(pHandler, new Packet250CustomPayload("LMM|Upd", pData));
	}

	/**
	 * �n���ꂽ�f�[�^�̐擪��EntityID��t�^���ăT�[�o�[�֑��M�B
	 * 0:Mode, 1-4:EntityID, 5-:Data
	 */
	public static void sendToEServer(LMM_EntityLittleMaid pEntity, byte[] pData) {
		MMM_Helper.setInt(pData, 1, pEntity.entityId);
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:send:%2x:%d", pData[0], pEntity.entityId));
	}

	public static void sendToServer(byte[] pData) {
		ModLoader.clientSendPacket(new Packet250CustomPayload("LMM|Upd", pData));
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd:%2x:NOEntity", pData[0]));
	}

	/**
	 * �T�[�o�[��IFF�̃Z�[�u�����N�G�X�g
	 */
	public static void saveIFF() {
		sendToServer(new byte[] {LMN_Server_SaveIFF});
	}

	/**
	 * littleMaid��Entity��Ԃ��B
	 */
	public static LMM_EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld) {
		Entity lentity = MMM_Helper.getEntity(pData, pIndex, pWorld);
		if (lentity instanceof LMM_EntityLittleMaid) {
			return (LMM_EntityLittleMaid)lentity;
		} else {
			return null;
		}
	}

	// ��M�p�P�b�g�̏���
	
	public static void serverCustomPayload(NetServerHandler pNetHandler, Packet250CustomPayload pPayload) {
		// �T�[�o���̓���
		byte lmode = pPayload.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(pPayload.data, 1);
			lemaid = getLittleMaid(pPayload.data, 1, pNetHandler.playerEntity.worldObj);
			if (lemaid == null) return;
		}
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd Srv Call[%2x:%d].", lmode, leid));
		byte[] ldata;
		int lindex;
		int lval;
		String lname;
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// ����X�V�Ƃ�
			// �C���x���g���̍X�V
			lemaid.maidInventory.clearChanged();
			for (LMM_SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
		case LMN_Server_DecDyePowder:
			// �J���[�ԍ����N���C�A���g����󂯎��
			// �C���x���g��������������炷�B
			int lcolor2 = pPayload.data[1];
			if (!pNetHandler.playerEntity.capabilities.isCreativeMode) {
				for (int li = 0; li < pNetHandler.playerEntity.inventory.mainInventory.length; li++) {
					ItemStack lis = pNetHandler.playerEntity.inventory.mainInventory[li];
					if (lis != null && lis.itemID == Item.dyePowder.itemID) {
						if (lis.getItemDamage() == (15 - lcolor2)) {
							MMM_Helper.decPlayerInventory(pNetHandler.playerEntity, li, 1);
						}
					}
				}
			}
			break;
			
		case LMN_Server_SetIFFValue:
			// IFF�̐ݒ�l����M
			lval = pPayload.data[1];
			lindex = MMM_Helper.getInt(pPayload.data, 2);
			lname = MMM_Helper.getStr(pPayload.data, 6);
			mod_LMM_littleMaidMob.Debug("setIFF-SV user:%s %s(%d)=%d", pNetHandler.playerEntity.username, lname, lindex, lval);
			LMM_IFF.setIFFValue(pNetHandler.playerEntity.username, lname, lval);
			sendIFFValue(pNetHandler, lval, lindex);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			lindex = MMM_Helper.getInt(pPayload.data, 1);
			lname = MMM_Helper.getStr(pPayload.data, 5);
			lval = LMM_IFF.getIFF(pNetHandler.playerEntity.username, lname);
			mod_LMM_littleMaidMob.Debug("getIFF-SV user:%s %s(%d)=%d", pNetHandler.playerEntity.username, lname, lindex, lval);
			sendIFFValue(pNetHandler, lval, lindex);
			break;
		case LMN_Server_SaveIFF:
			// IFF�t�@�C���̕ۑ�
			LMM_IFF.saveIFF(pNetHandler.playerEntity.username);
			if (!MMM_Helper.isClient) {
				LMM_IFF.saveIFF("");
			}
			break;
			
		}
	}

	/**
	 * �N���C�A���g��IFF�̐ݒ�l��ʒm����B
	 * @param pNetHandler
	 * @param pValue
	 * @param pIndex
	 */
	protected static void sendIFFValue(NetServerHandler pNetHandler, int pValue, int pIndex) {
		byte ldata[] = new byte[] {
				LMN_Client_SetIFFValue,
				0,
				0, 0, 0, 0
		};
		ldata[1] = (byte)pValue;
		MMM_Helper.setInt(ldata, 2, pIndex);
		sendToClient(pNetHandler, ldata);
	}

}
