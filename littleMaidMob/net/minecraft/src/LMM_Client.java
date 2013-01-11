package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;

public class LMM_Client {

	public static void init() {
		// �f�t�H���g���f���̐ݒ�
		MMM_TextureManager.defaultModel = new MMM_ModelBiped[] {
				new LMM_ModelLittleMaid(0.0F),
				new LMM_ModelLittleMaid(0.5F),
				new LMM_ModelLittleMaid(0.1F)
		};
	}

	public static void addRenderer(Map map) {
		map.put(net.minecraft.src.LMM_EntityLittleMaid.class, new LMM_RenderLittleMaid(new LMM_ModelLittleMaid(), 0.3F));
	}

	public static GuiContainer getContainerGUI(EntityClientPlayerMP var1, int var2,
			int var3, int var4, int var5) {
		Entity lentity = var1.worldObj.getEntityByID(var3);
		if (lentity instanceof LMM_EntityLittleMaid) {
			LMM_GuiInventory lgui = new LMM_GuiInventory(var1, (LMM_EntityLittleMaid)lentity);
//			var1.openContainer = lgui.inventorySlots;
			return lgui;
		} else {
			return null;
		}
	}

	public static void OpenIFF(LMM_EntityLittleMaid pLittleMaid, EntityPlayer pPlayer) {
		ModLoader.openGUI(pPlayer, new LMM_GuiIFF(pLittleMaid.worldObj, pLittleMaid));
		
	}
/*
	public static void setDefaultModel(LMM_EntityLittleMaid pEntity) {
		pEntity.textureName = pEntity.textureArmorName = "default";
		pEntity.textureModel0 = MMM_TextureManager.defaultModel[0];
		pEntity.textureModel1 = MMM_TextureManager.defaultModel[1];
		pEntity.textureModel2 = MMM_TextureManager.defaultModel[2];
		pEntity.setSize(pEntity.textureModel0.getWidth(), pEntity.textureModel0.getHeight());
		pEntity.setMaidColor(0x0c);
	}
*/
	// �e�N�X�`���A���f���A�F
	public static void setTextureValue(LMM_EntityLittleMaid pEntity) {
		if (pEntity.textureName == null) return;
		int i = pEntity.maidColor;
		if (!pEntity.isMaidContract()) i += MMM_TextureManager.tx_wild;
		
		pEntity.texture = MMM_TextureManager.getTextureName(pEntity.textureName, i);
//		mod_littleMaidMob.Debug(String.format("id:%d, tex:%s", entityId, texture));
		if (pEntity.texture == null) {
			mod_LMM_littleMaidMob.Debug("tex-null");
			setNextTexturePackege(pEntity, 0);
			i = pEntity.maidColor;
			if (!pEntity.isMaidContract()) i += MMM_TextureManager.tx_wild;
			pEntity.texture = MMM_TextureManager.getTextureName(pEntity.textureName, i);
		}
		// ���f���̐ݒ�
		MMM_TextureBox ltb = MMM_TextureManager.getTextureBox(pEntity.textureName);
		pEntity.textureModel0 = ltb.models[0];
		// �g���ύX�p
		pEntity.setSize(pEntity.textureModel0.getWidth(), pEntity.textureModel0.getHeight());
		pEntity.setPosition(pEntity.posX, pEntity.posY, pEntity.posZ);
		mod_LMM_littleMaidMob.Debug(String.format("ID:%d, TextureModel:%s", pEntity.entityId, ltb.modelName));
		// ���f���̏�����
		pEntity.textureModel0.changeModel(pEntity);
		// �X�^�r�̕t���ւ�
		for (Entry<String, MMM_EquippedStabilizer> le : pEntity.maidStabilizer.entrySet()) {
			if (le.getValue() != null) {
				le.getValue().updateEquippedPoint(pEntity.textureModel0);
			}
		}
		// �A�[�}�[
		setArmorTextureValue(pEntity);
		pEntity.maidSoundRate = LMM_SoundManager.getSoundRate(pEntity.textureName, pEntity.maidColor);
	}

	public static void setArmorTextureValue(LMM_EntityLittleMaid pEntity) {
		if (!pEntity.worldObj.isRemote) return;
		if (pEntity.textureArmorName == null) return;
		// �A�[�}�[���f��
		MMM_TextureBox ltb = MMM_TextureManager.getTextureBox(pEntity.textureName);
		pEntity.textureModel1 = ltb.models[1];
		pEntity.textureModel2 = ltb.models[2];
		mod_LMM_littleMaidMob.Debug(String.format("Model:%s / %s", pEntity.textureModel0.getClass().getSimpleName(), pEntity.textureModel1.getClass().getSimpleName()));

		for (int i = 0; i < 4; i++) {
			ItemStack is = pEntity.maidInventory.armorItemInSlot(i);
			pEntity.textureArmor0[i] = MMM_TextureManager.getArmorTextureName(pEntity.textureArmorName, MMM_TextureManager.tx_armor1, is);
			pEntity.textureArmor1[i] = MMM_TextureManager.getArmorTextureName(pEntity.textureArmorName, MMM_TextureManager.tx_armor2, is);
		}
	}

	public static void setNextTexturePackege(LMM_EntityLittleMaid pEntity, int pTargetTexture) {
		if (pTargetTexture == 0) {
			if (pEntity.isMaidContract())
				pEntity.textureName = MMM_TextureManager.getNextPackege(pEntity.textureName, pEntity.maidColor);
			else
				pEntity.textureName = MMM_TextureManager.getNextPackege(pEntity.textureName, pEntity.maidColor + MMM_TextureManager.tx_wild);
			if (pEntity.textureName == null) {
				// �w��F�������ꍇ�͕W�����f����
				pEntity.textureName = pEntity.textureArmorName = "default";
				pEntity.maidColor = 12;
			} else {
				pEntity.textureArmorName = pEntity.textureName;
			}
			if (!MMM_TextureManager.getTextureBox(pEntity.textureArmorName).hasArmor()) {
				pTargetTexture = 1;
			}
		}
		if (pTargetTexture == 1) {
			pEntity.textureArmorName = MMM_TextureManager.getNextArmorPackege(pEntity.textureArmorName);
		}
//		pEntity.sendTextureToServer();
	}

	public static void setPrevTexturePackege(LMM_EntityLittleMaid pEntity, int pTargetTexture) {
		if (pTargetTexture == 0) {
			if (pEntity.isMaidContract())
				pEntity.textureName = MMM_TextureManager.getPrevPackege(pEntity.textureName,pEntity. maidColor);
			else
				pEntity.textureName = MMM_TextureManager.getPrevPackege(pEntity.textureName, pEntity.maidColor + MMM_TextureManager.tx_wild);
			pEntity.textureArmorName = pEntity.textureName;
			if (!MMM_TextureManager.getTextureBox(pEntity.textureArmorName).hasArmor())
				pTargetTexture = 1;
		}
		if (pTargetTexture == 1) {
			pEntity.textureArmorName = MMM_TextureManager.getPrevArmorPackege(pEntity.textureArmorName);
		}
//		pEntity.sendTextureToServer();
	}

// Avatarr
	
	public static void onItemPickup(LMM_EntityLittleMaidAvatar pAvatar, Entity entity, int i) {
		// �A�C�e������̃G�t�F�N�g
		// TODO:���������g�����H
//        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, avatar, -0.5F));
		pAvatar.mc.effectRenderer.addEffect(new EntityPickupFX(pAvatar.mc.theWorld, entity, pAvatar.avatar, 0.1F));
	}

	public static void onCriticalHit(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		pAvatar.mc.effectRenderer.addEffect(new EntityCrit2FX(pAvatar.mc.theWorld, par1Entity));
	}

	public static void onEnchantmentCritical(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		EntityCrit2FX entitycrit2fx = new EntityCrit2FX(pAvatar.mc.theWorld, par1Entity, "magicCrit");
		pAvatar.mc.effectRenderer.addEffect(entitycrit2fx);
	}

	
// Network

	public static void clientCustomPayload(NetClientHandler var1, Packet250CustomPayload var2) {
		// �N���C�A���g���̓���p�P�b�g��M����
		byte lmode = var2.data[0];
		int leid = 0;
		LMM_EntityLittleMaid lemaid = null;
		if ((lmode & 0x80) != 0) {
			leid = MMM_Helper.getInt(var2.data, 1);
			lemaid =LMM_Net.getLittleMaid(var2.data, 1, MMM_Helper.mc.theWorld);
			if (lemaid == null) return;
		}
		mod_LMM_littleMaidMob.Debug(String.format("LMM|Upd Clt Call[%2x:%d].", lmode, leid));
		
		switch (lmode) {
		case LMM_Net.LMN_Client_SwingArm : 
			// �r�U��
			byte larm = var2.data[5];
			lemaid.setSwinging(larm);
			break;
			
		case LMM_Net.LMN_Client_UpdateTexture : 
			// �����ւ�
			LMM_Client.setTextureValue(lemaid);
			break;
			
		case LMM_Net.LMN_Client_SetIFFValue:
			// IFF�̐ݒ�l����M
			int lval = var2.data[1];
			String lname = "";
			for (int li = 6; li < var2.data.length; li++) {
				lname += (char)var2.data[li];
			}
			
			// TODO:GUI�Ŏg�p����l��ݒ肷��悤��
			LMM_IFF.setIFFValue(null, lname, lval);
			break;
			
		case LMM_Net.LMN_Client_SetTextureIndex:
			// �e�N�X�`�����̂̃��N�G�X�g�ɑ΂��Ĕԍ���Ԃ�
			/*
			 * 0:id
			 * 1-4:EntityID
			 * 5:index
			 * 6-7:TextureIndex
			 */
			int li7 = MMM_Helper.getShort(var2.data, 6);
			String ls7 = MMM_TextureManager.getRequestString(var2.data[5]);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %d : %s", li7, var2.data[5], ls7 == null ? "NULL" : ls7));
			MMM_TextureManager.setStringToIndex(li7, ls7);
			lemaid.sendTextureToServer();
			break;
		case LMM_Net.LMN_Client_SetTextureStr:
			// �e�N�X�`�����̂̃��N�G�X�g�ɑ΂��Ĕԍ���Ԃ�
			/*
			 * 0:id
			 * 1-2:index �o�^�e�N�X�`���ԍ�
			 * 3-:Str ����
			 */
			int li8 = MMM_Helper.getShort(var2.data, 1);
			String ls8 = MMM_Helper.getStr(var2.data, 3);
			mod_LMM_littleMaidMob.Debug(String.format("%d : %s", li8, ls8 == null ? "NULL" : ls8));
			MMM_TextureManager.setStringToIndex(li8, ls8);
			break;
			
		}
	}



}
