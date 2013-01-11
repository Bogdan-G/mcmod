package net.minecraft.src;

import java.util.Map.Entry;

public class LMM_Net {
	
	/*
	 * ����p�萔�A8bit�ڂ𗧂Ă��Entity�v��
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	public static final byte LMN_Server_SetTexture		= (byte)0x81;
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	public static final byte LMN_Client_UpdateTexture	= (byte)0x83;
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	public static final byte LMN_Server_SaveIFF			= (byte)0x05;
	public static final byte LMN_Server_GetIFFValue		= (byte)0x06;
	public static final byte LMN_Server_GetTextureIndex	= (byte)0x07;
	public static final byte LMN_Client_SetTextureIndex	= (byte)0x87;
	public static final byte LMN_Server_GetTextureStr	= (byte)0x08;
	public static final byte LMN_Client_SetTextureStr	= (byte)0x08;
	


	
	
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
	 * Entity��Ԃ��B
	 */
	public static Entity getEntity(byte[] pData, int pIndex, World pWorld) {
		return pWorld.getEntityByID(MMM_Helper.getInt(pData, pIndex));
	}
	
	/**
	 * littleMaid��Entity��Ԃ��B
	 */
	public static LMM_EntityLittleMaid getLittleMaid(byte[] pData, int pIndex, World pWorld) {
		Entity lentity = getEntity(pData, pIndex, pWorld);
		if (lentity instanceof LMM_EntityLittleMaid) {
			return (LMM_EntityLittleMaid)lentity;
		} else {
			return null;
		}
	}

	// ��M�p�P�b�g�̏���
	
	public static void serverCustomPayload(NetServerHandler var1, Packet250CustomPayload var2) {
		// �T�[�o���̓���
		byte lmode = var2.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lemaid = getLittleMaid(var2.data, 1, var1.playerEntity.worldObj);
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
			
		case LMN_Server_SetTexture:
			// �e�N�X�`���ԍ����N���C�A���g����󂯎��
			int lindex = MMM_Helper.getShort(var2.data, 5);
			int larmor = MMM_Helper.getShort(var2.data, 7);
			lemaid.setTextureIndex(lindex, larmor);
			break;
			
		case LMN_Server_SetIFFValue:
			// IFF�̐ݒ�l����M
			int lval = var2.data[1];
			String lname = "";
			for (int li = 6; li < var2.data.length; li++) {
				lname += (char)var2.data[li];
			}
			LMM_IFF.setIFFValue(var1.playerEntity.username, lname, lval);
			break;
		case LMN_Server_SaveIFF:
			// IFF�t�@�C���̕ۑ�
			LMM_IFF.saveIFF(var1.playerEntity.username);
			break;
		case LMN_Server_GetIFFValue:
			// IFFGUI open
			for (Entry<String, Integer> le : LMM_IFF.DefaultIFF.entrySet()) {
				ldata = new byte[le.getKey().length() + 2];
				ldata[0] = LMN_Client_SetIFFValue;
				ldata[1] = (byte)le.getValue().intValue();
				LMM_Net.sendToClient(var1, ldata);
			}
			break;
		
		case LMN_Server_GetTextureIndex:
			// �e�N�X�`�����̂̃��N�G�X�g�ɑ΂��Ĕԍ���Ԃ�
			/*
			 * 0:ID
			 * 1-4:EntityID
			 * 5:index �v�����������̔ԍ�
			 * 6-9:colorBits
			 * 10-:Str
			 */
			String ls = MMM_Helper.getStr(var2.data, 10);
			int lc = MMM_Helper.getInt(var2.data, 6);
			int li = MMM_TextureManager.setStringToIndex(ls, lc);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %d : %04x : %s", li, var2.data[5], lc, ls == null ? "NULL" : ls));
			ldata = new byte[] {
					LMN_Client_SetTextureIndex,
					var2.data[1], var2.data[2], var2.data[3], var2.data[4],
					var2.data[5],
					0, 0
			};
			MMM_Helper.setShort(ldata, 6, li);
			sendToClient(var1, ldata);
			break;
		case LMN_Server_GetTextureStr:
			// �C���f�b�N�X����e�N�X�`�����̂�Ԃ�
			/*
			 * 0:ID
			 * 1-2:index �o�^�e�N�X�`���ԍ�
			 */
			int li8 = MMM_Helper.getShort(var2.data, 1);
			String ls8 = MMM_TextureManager.getIndexToString(li8);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %s", li8, ls8 == null ? "NULL" : ls8));
			ldata = new byte[3 + ls8.getBytes().length];
			ldata[0] = LMN_Client_SetTextureStr;
			ldata[1] = var2.data[1];
			ldata[2] = var2.data[2];
			MMM_Helper.setStr(ldata, 3, ls8);
			sendToClient(var1, ldata);
			break;
			
		}
	}

	
}
