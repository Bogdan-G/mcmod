package net.minecraft.src;

import java.util.Map;
import java.util.Map.Entry;

/**
 * �}���`�p�ɕ����B
 * �������Ƃ��Ȃ���NoSuchMethod�ŗ�����B
 *
 */
public class LMM_Client {

	public static void init() {
	}

	public static void addRenderer(Map map) {
		map.put(net.minecraft.src.LMM_EntityLittleMaid.class, new LMM_RenderLittleMaid(0.3F));
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

// Avatarr
	
	public static void onItemPickup(LMM_EntityLittleMaidAvatar pAvatar, Entity entity, int i) {
		// �A�C�e������̃G�t�F�N�g
		// TODO:���������g�����H
//        mc.effectRenderer.addEffect(new EntityPickupFX(mc.theWorld, entity, avatar, -0.5F));
		MMM_Helper.mc.effectRenderer.addEffect(new EntityPickupFX(MMM_Helper.mc.theWorld, entity, pAvatar.avatar, 0.1F));
	}

	public static void onCriticalHit(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		MMM_Helper.mc.effectRenderer.addEffect(new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity));
	}

	public static void onEnchantmentCritical(LMM_EntityLittleMaidAvatar pAvatar, Entity par1Entity) {
		EntityCrit2FX entitycrit2fx = new EntityCrit2FX(MMM_Helper.mc.theWorld, par1Entity, "magicCrit");
		MMM_Helper.mc.effectRenderer.addEffect(entitycrit2fx);
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
			LMM_EnumSound lsound = LMM_EnumSound.getEnumSound(MMM_Helper.getInt(var2.data, 6));
			lemaid.setSwinging(larm, lsound);
//			mod_LMM_littleMaidMob.Debug(String.format("SwingSound:%s", lsound.name()));
			break;
			
//		case LMM_Net.LMN_Client_UpdateTexture : 
//			// �����ւ�
//			LMM_Client.setTextureValue(lemaid);
//			break;
			
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
			
		case LMM_Net.LMN_Client_PlaySound : 
			// �����Đ�
			LMM_EnumSound lsound9 = LMM_EnumSound.getEnumSound(MMM_Helper.getInt(var2.data, 5));
			lemaid.playLittleMaidSound(lsound9, true);
			mod_LMM_littleMaidMob.Debug(String.format("playSound:%s", lsound9.name()));
			break;
			
		}
	}



}
