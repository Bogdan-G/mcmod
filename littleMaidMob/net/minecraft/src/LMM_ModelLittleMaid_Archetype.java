package net.minecraft.src;

import java.util.Random;

import org.lwjgl.opengl.GL11;

/**
 * ���^���f���݊��̃x�[�X���f��
 */
public class LMM_ModelLittleMaid_Archetype extends LMM_ModelLittleMaid {

	// fields
	public MMM_ModelRenderer ChignonR;
	public MMM_ModelRenderer ChignonL;
	public MMM_ModelRenderer ChignonB;
	public MMM_ModelRenderer Tail;
	public MMM_ModelRenderer SideTailR;
	public MMM_ModelRenderer SideTailL;

	public LMM_ModelLittleMaid_Archetype() {
		super();
	}

	public LMM_ModelLittleMaid_Archetype(float f) {
		super(f);
	}

	public LMM_ModelLittleMaid_Archetype(float f, float f1) {
		super(f, f1);
	}

	@Override
	public void initModel(float psize, float pyoffset) {
		heldItemLeft = 0;
		heldItemRight = 0;
		isSneak = false;
		isWait = false;
		aimedBow = false;

		pyoffset += 8F;

		// ���̂Q�̓_�~�[
		bipedCloak = new ModelRenderer(this, 0, 0);
		bipedCloak.showModel = false;
		bipedEars = new ModelRenderer(this, 0, 16);
		bipedEars.showModel = false;

		// �����ʒu
		Arms = new MMM_ModelRenderer[1];
		Arms[0] = new MMM_ModelRenderer(this, 0, 0);
		Arms[0].setRotationPointLM(-1F, 5F, -1F);
		HeadMount.setRotationPoint(0F, -4F, 0F);

		bipedHead = new MMM_ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, psize);
		bipedHead.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(HeadMount);

		bipedHeadwear = new MMM_ModelRenderer(this, 24, 0);
		bipedHeadwear.addBox(-4F, 0F, 1F, 8, 4, 3, psize);
		bipedHeadwear.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(bipedHeadwear);

		bipedBody = new MMM_ModelRenderer(this, 32, 8);
		bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, psize);
		bipedBody.setRotationPoint(0F, 0F, 0F);

		bipedRightArm = new MMM_ModelRenderer(this, 48, 0);
		bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, psize);
		bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);
		bipedRightArm.addChild(Arms[0]);

		bipedLeftArm = new MMM_ModelRenderer(this, 56, 0);
		bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, psize);
		bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);

		bipedRightLeg = new MMM_ModelRenderer(this, 32, 19);
		bipedRightLeg.addBox(-2F, 0F, -2F, 3, 9, 4, psize);
		bipedRightLeg.setRotationPoint(-1F, 7F, 0F);

		bipedLeftLeg = new MMM_ModelRenderer(this, 32, 19);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-1F, 0F, -2F, 3, 9, 4, psize);
		bipedLeftLeg.setRotationPoint(1F, 7F, 0F);

		Skirt = new MMM_ModelRenderer(this, 0, 16);
		Skirt.addBox(-4F, -2F, -4F, 8, 8, 8, psize);
		Skirt.setRotationPoint(0F, 7F, 0F);

		ChignonR = new MMM_ModelRenderer(this, 24, 18);
		ChignonR.addBox(-5F, -7F, 0.2F, 1, 3, 3, psize);
		ChignonR.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonR);

		ChignonL = new MMM_ModelRenderer(this, 24, 18);
		ChignonL.addBox(4F, -7F, 0.2F, 1, 3, 3, psize);
		ChignonL.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonL);

		ChignonB = new MMM_ModelRenderer(this, 52, 10);
		ChignonB.addBox(-2F, -7.2F, 4F, 4, 4, 2, psize);
		ChignonB.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(ChignonB);

		Tail = new MMM_ModelRenderer(this, 46, 20);
		Tail.addBox(-1.5F, -6.8F, 4F, 3, 9, 3, psize);
		Tail.setRotationPoint(0F, 0F, 0F);
		bipedHead.addChild(Tail);

		SideTailR = new MMM_ModelRenderer(this, 58, 21);
		SideTailR.addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, psize);
		SideTailR.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(SideTailR);

		SideTailL = new MMM_ModelRenderer(this, 58, 21);
		SideTailL.mirror = true;
		SideTailL.addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, psize);
		SideTailL.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedHead.addChild(SideTailL);

		mainFrame = new MMM_ModelRenderer(this, 0, 0);
		mainFrame.setRotationPoint(0F, 0F + pyoffset, 0F);
		mainFrame.addChild(bipedHead);
		mainFrame.addChild(bipedBody);
		mainFrame.addChild(bipedRightArm);
		mainFrame.addChild(bipedLeftArm);
		mainFrame.addChild(bipedRightLeg);
		mainFrame.addChild(bipedLeftLeg);
		mainFrame.addChild(Skirt);

	}

	public float getHeight() {
		// �g��
		return 1.35F;
	}

	public float getWidth() {
		// ����
		return 0.5F;
	}

	public void equippedBlockPosition() {
		// �莝���u���b�N�̕\���ʒu
		GL11.glTranslatef(0.0F, 0.1275F, -0.3125F);
	}

	public void equippedItemPosition3D() {
		// �莝���RD�A�C�e���̕\���ʒu
		GL11.glTranslatef(0.02F, 0.1300F, 0.0F);
	}

	public void equippedItemPosition() {
		// �莝���A�C�e���̕\���ʒu
		GL11.glTranslatef(0.20F, 0.0800F, -0.0875F);
	}

	public void equippedHeadItemPosition() {
		// ���������A�C�e���̕\���ʒu
		GL11.glTranslatef(0.0F, 1.0F, 0.0F);
	}

	public void equippedItemBow() {
		// �莝���|�̕\���ʒu
		// GL11.glTranslatef(-0.07F, 0.005F, 0.3F);
		equippedItemPosition3D();
		// GL11.glTranslatef(-0.09F, -0.125F, 0.3F);
		GL11.glTranslatef(-0.05F, -0.075F, 0.1F);
	}

	public boolean isItemHolder() {
		// �A�C�e���������Ă���Ƃ��Ɏ��O�ɏo�����ǂ����B
		return false;
	}

	@Override
	public void setLivingAnimations(EntityLiving entityliving, float f,
			float f1, float renderPartialTicks) {
		super.setLivingAnimations(entityliving, f, f1, renderPartialTicks);
		if (modelCaps != null) {
			float f3 = modelCaps.getCapsValueFloat(caps_interestedAngle, renderPartialTicks);
			bipedHead.rotateAngleZ = f3;
			bipedHeadwear.rotateAngleZ = f3;
			bipedEars.rotateAngleZ = f3;
		}
	}

	@Override
	public void setRotationAngles(float f, float f1, float ticksExisted,
			float pheadYaw, float pheadPitch, float f5, Entity pEntity) {
//		super.setRotationAngles(f, f1, ticksExisted, pheadYaw, pheadPitch, f5, pEntity);
		bipedHead.rotateAngleY = pheadYaw / 57.29578F;
		bipedHead.rotateAngleX = pheadPitch / 57.29578F;
		bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
		bipedHeadwear.rotateAngleX = bipedHead.rotateAngleX;
		bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F)
				* 2.0F * f1 * 0.5F;
		bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1
				* 0.5F;
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.0F;
		bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
		bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + 3.141593F)
				* 1.4F * f1;
		bipedRightLeg.rotateAngleY = 0.0F;
		bipedLeftLeg.rotateAngleY = 0.0F;

		if (isRiding) {
			// ��蕨�ɏ���Ă���
			bipedRightArm.rotateAngleX += -0.6283185F;
			bipedLeftArm.rotateAngleX += -0.6283185F;
			bipedRightLeg.rotateAngleX = -1.256637F;
			bipedLeftLeg.rotateAngleX = -1.256637F;
			bipedRightLeg.rotateAngleY = 0.3141593F;
			bipedLeftLeg.rotateAngleY = -0.3141593F;
		}
		// �A�C�e�������Ă�Ƃ��̘r�U���}����
		if (heldItemLeft != 0) {
			bipedLeftArm.rotateAngleX = bipedLeftArm.rotateAngleX * 0.5F
					- 0.3141593F * (float) heldItemLeft;
		}
		if (heldItemRight != 0) {
			bipedRightArm.rotateAngleX = bipedRightArm.rotateAngleX * 0.5F
					- 0.3141593F * (float) heldItemRight;
		}

		bipedRightArm.rotateAngleY = 0.0F;
		bipedLeftArm.rotateAngleY = 0.0F;
		if (onGround > -9990F && !aimedBow) {
			// �r�U��
			float f6 = onGround;
			bipedBody.rotateAngleY = MathHelper
					.sin(MathHelper.sqrt_float(f6) * 3.141593F * 2.0F) * 0.2F;
			Skirt.rotateAngleY = bipedBody.rotateAngleY;
			bipedRightArm.rotationPointZ = MathHelper
					.sin(bipedBody.rotateAngleY) * 4F;
			bipedRightArm.rotationPointX = -MathHelper
					.cos(bipedBody.rotateAngleY) * 4F + 1.0F;
			bipedLeftArm.rotationPointZ = -MathHelper
					.sin(bipedBody.rotateAngleY) * 4F;
			bipedLeftArm.rotationPointX = MathHelper
					.cos(bipedBody.rotateAngleY) * 4F - 1.0F;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleY += bipedBody.rotateAngleY;
			bipedLeftArm.rotateAngleX += bipedBody.rotateAngleY;
			f6 = 1.0F - onGround;
			f6 *= f6;
			f6 *= f6;
			f6 = 1.0F - f6;
			float f7 = MathHelper.sin(f6 * 3.141593F);
			float f8 = MathHelper.sin(onGround * 3.141593F)
					* -(bipedHead.rotateAngleX - 0.7F) * 0.75F;
			bipedRightArm.rotateAngleX -= (double) f7 * 1.2D + (double) f8;
			bipedRightArm.rotateAngleY += bipedBody.rotateAngleY * 2.0F;
			bipedRightArm.rotateAngleZ = MathHelper.sin(onGround * 3.141593F)
					* -0.4F;
		}
		if (isSneak) {
			// ���Ⴊ��
			bipedBody.rotateAngleX = 0.5F;
			bipedRightLeg.rotateAngleX -= 0.0F;
			bipedLeftLeg.rotateAngleX -= 0.0F;
			bipedRightArm.rotateAngleX += 0.4F;
			bipedLeftArm.rotateAngleX += 0.4F;
			bipedRightLeg.rotationPointZ = 3F;
			bipedLeftLeg.rotationPointZ = 3F;
			bipedRightLeg.rotationPointY = 6F;
			bipedLeftLeg.rotationPointY = 6F;
			bipedHead.rotationPointY = 1.0F;
			bipedHeadwear.rotationPointY = 1.0F;
			bipedHeadwear.rotateAngleX += 0.5F;
			Skirt.rotationPointY = 5.8F;
			Skirt.rotationPointZ = 2.7F;
			Skirt.rotateAngleX = 0.2F;
		} else {
			// �ʏ헧��
			bipedBody.rotateAngleX = 0.0F;
			bipedRightLeg.rotationPointZ = 0.0F;
			bipedLeftLeg.rotationPointZ = 0.0F;
			bipedRightLeg.rotationPointY = 7F;
			bipedLeftLeg.rotationPointY = 7F;
			bipedHead.rotationPointY = 0.0F;
			bipedHeadwear.rotationPointY = 0.0F;
			Skirt.rotationPointY = 7.0F;
			Skirt.rotationPointZ = 0.0F;
			Skirt.rotateAngleX = 0.0F;
		}
		if (isWait) {
			// �ҋ@��Ԃ̓��ʕ\��
			bipedRightArm.rotateAngleX = MathHelper.sin(ticksExisted * 0.067F) * 0.05F - 0.7F;
			bipedRightArm.rotateAngleY = 0.0F;
			bipedRightArm.rotateAngleZ = -0.4F;
			bipedLeftArm.rotateAngleX = MathHelper.sin(ticksExisted * 0.067F) * 0.05F - 0.7F;
			bipedLeftArm.rotateAngleY = 0.0F;
			bipedLeftArm.rotateAngleZ = 0.4F;
		} else {
			if (aimedBow) {
				// �|�\��
				float f6 = MathHelper.sin(onGround * 3.141593F);
				float f7 = MathHelper.sin((1.0F - (1.0F - onGround)
						* (1.0F - onGround)) * 3.141593F);
				bipedRightArm.rotateAngleZ = 0.0F;
				bipedLeftArm.rotateAngleZ = 0.0F;
				bipedRightArm.rotateAngleY = -(0.1F - f6 * 0.6F);
				bipedLeftArm.rotateAngleY = 0.1F - f6 * 0.6F;
				// bipedRightArm.rotateAngleX = -1.570796F;
				// bipedLeftArm.rotateAngleX = -1.570796F;
				bipedRightArm.rotateAngleX = -1.470796F;
				bipedLeftArm.rotateAngleX = -1.470796F;
				bipedRightArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedLeftArm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
				bipedRightArm.rotateAngleZ += MathHelper
						.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= MathHelper
						.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += MathHelper
						.sin(ticksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= MathHelper
						.sin(ticksExisted * 0.067F) * 0.05F;
				bipedRightArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedLeftArm.rotateAngleX += bipedHead.rotateAngleX;
				bipedRightArm.rotateAngleY += bipedHead.rotateAngleY;
				bipedLeftArm.rotateAngleY += bipedHead.rotateAngleY;
			} else {
				// �ʏ�
				bipedRightArm.rotateAngleZ += 0.5F;
				bipedLeftArm.rotateAngleZ -= 0.5F;
				bipedRightArm.rotateAngleZ += MathHelper
						.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedLeftArm.rotateAngleZ -= MathHelper
						.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
				bipedRightArm.rotateAngleX += MathHelper
						.sin(ticksExisted * 0.067F) * 0.05F;
				bipedLeftArm.rotateAngleX -= MathHelper
						.sin(ticksExisted * 0.067F) * 0.05F;
			}
		}
	}

	@Override
	public void renderItems(EntityLiving pEntity, Render pRender) {
		// �莝���̕\��
		GL11.glPushMatrix();
		boolean lflag = true;
		if (modelCaps != null) {
			ItemStack[] litemstacks = (ItemStack[])modelCaps.getCapsValue(caps_Items);
			EnumAction[] lactions = (EnumAction[])modelCaps.getCapsValue(caps_Actions);
			int ldominant = modelCaps.getCapsValueInt(caps_dominantArm);
			if (litemstacks != null) {
				// R
				Arms[0].loadMatrix().renderItems(pEntity, pRender, false, lactions[ldominant] ,litemstacks[ldominant]);
				// L
//				Arms[1].loadMatrix().renderItems(pEntity, pRender, false, lactions[1], litemstacks[1]);
				lflag = false;
			}
			// ���������i
			boolean lplanter = modelCaps.getCapsValueBoolean(caps_isPlanter);
			if (modelCaps.getCapsValueBoolean(caps_isCamouflage) || lplanter) {
				HeadMount.loadMatrix();
				if (lplanter) {
					GL11.glTranslatef(0F, -0.56F, 0F);
				}
				HeadMount.renderItems(pEntity, pRender, true, null, (ItemStack)modelCaps.getCapsValue(caps_HeadMount));
			}
		}
		if (lflag) {
			Arms[0].loadMatrix().renderItems(pEntity, pRender, false, null, pEntity.getHeldItem());
		}
		GL11.glPopMatrix();
	}

}
