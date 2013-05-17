package net.minecraft.src;

import java.util.Map.Entry;

public class LMM_Net {
	
	/*
	 * ����p�萔�A8bit�ڂ𗧂Ă��Entity�v��
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
//	public static final byte LMN_Server_SetTexture		= (byte)0x81;
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	public static final byte LMN_Server_DecDyePowder	= (byte)0x02;
//	public static final byte LMN_Client_UpdateTexture	= (byte)0x83;
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Server_SaveIFF			= (byte)0x05;
	public static final byte LMN_Server_GetIFFValue		= (byte)0x06;
//	public static final byte LMN_Server_GetTextureIndex	= (byte)0x07;
//	public static final byte LMN_Client_SetTextureIndex	= (byte)0x87;
//	public static final byte LMN_Server_GetTextureStr	= (byte)0x08;
//	public static final byte LMN_Client_SetTextureStr	= (byte)0x08;
	public static final byte LMN_Client_PlaySound		= (byte)0x89;
	


	
	
	/*
	 * LMMPacet�̃t�H�[�}�b�g
	 * (Byte)
	 * 0	: ����(1byte)
	 * 1 - 4: EntityID(4Byte)�ꍇ�Ɋ���Ă͏ȗ� 
	 * 5 - 	: Data
	 * 
	 */
			
	
	
	
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
		
		switch (lmode) {
		case LMN_Server_UpdateSlots : 
			// ����X�V�Ƃ�
			// �C���x���g���̍X�V
			lemaid.maidInventory.clearChanged();
			for (LMM_SwingStatus lswing : lemaid.mstatSwingStatus) {
				lswing.lastIndex = -1;
			}
			break;
			
//		case LMN_Server_SetTexture:
//			// �e�N�X�`���ԍ����N���C�A���g����󂯎��
//			int lindex1 = MMM_Helper.getShort(pPayload.data, 5);
//			int larmor1 = MMM_Helper.getShort(pPayload.data, 7);
//			int lcolor1 = pPayload.data[9];
//			lemaid.setTextureIndex(lindex1, larmor1);
//			lemaid.setMaidColor(lcolor1);
//			break;
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
			int lval = pPayload.data[1];
			String lname = "";
			for (int li = 6; li < pPayload.data.length; li++) {
				lname += (char)pPayload.data[li];
			}
			LMM_IFF.setIFFValue(pNetHandler.playerEntity.username, lname, lval);
			break;
		case LMN_Server_SaveIFF:
			// IFF�t�@�C���̕ۑ�
			LMM_IFF.saveIFF(pNetHandler.playerEntity.username);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			for (Entry<String, Integer> le : LMM_IFF.DefaultIFF.entrySet()) {
				ldata = new byte[le.getKey().length() + 2];
				ldata[0] = LMN_Client_SetIFFValue;
				ldata[1] = (byte)le.getValue().intValue();
				LMM_Net.sendToClient(pNetHandler, ldata);
			}
			break;
			
		}
	}

}
