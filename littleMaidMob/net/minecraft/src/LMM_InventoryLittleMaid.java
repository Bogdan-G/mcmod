package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class LMM_InventoryLittleMaid extends InventoryPlayer {

	/**
	 * �ő�C���x���g����
	 */
	public static final int maxInventorySize = 18;
	/**
	 * �I�[�i�[
	 */
	public LMM_EntityLittleMaid entityLittleMaid;
	/**
	 * �X���b�g�ύX�`�F�b�N�p
	 */
	public ItemStack prevItems[];

	public LMM_InventoryLittleMaid(LMM_EntityLittleMaid par1EntityLittleMaid) {
		super(par1EntityLittleMaid.maidAvatar);

		entityLittleMaid = par1EntityLittleMaid;
		mainInventory = new ItemStack[maxInventorySize];
		prevItems = new ItemStack[mainInventory.length + armorInventory.length];
	}

	@Override
	public void readFromNBT(NBTTagList par1nbtTagList) {
		mainInventory = new ItemStack[maxInventorySize];
		armorInventory = new ItemStack[4];

		for (int i = 0; i < par1nbtTagList.tagCount(); i++) {
			NBTTagCompound nbttagcompound = (NBTTagCompound) par1nbtTagList
					.tagAt(i);
			int j = nbttagcompound.getByte("Slot") & 0xff;
			ItemStack itemstack = ItemStack
					.loadItemStackFromNBT(nbttagcompound);

			if (itemstack == null) {
				continue;
			}

			if (j >= 0 && j < mainInventory.length) {
				mainInventory[j] = itemstack;
			}

			if (j >= 100 && j < armorInventory.length + 100) {
				armorInventory[j - 100] = itemstack;
			}
		}
	}

	@Override
	public String getInvName() {
		return "InsideSkirt";
	}

	@Override
	public int getSizeInventory() {
		// �ꉞ
		return mainInventory.length + armorInventory.length;
	}

	@Override
	public void openChest() {
		entityLittleMaid.onGuiOpened();
	}

	@Override
	public void closeChest() {
		entityLittleMaid.onGuiClosed();
	}

	@Override
	public void decrementAnimations() {
		for (int li = 0; li < this.mainInventory.length; ++li) {
			if (this.mainInventory[li] != null) {
				this.mainInventory[li].updateAnimation(this.player.worldObj,
						entityLittleMaid, li, this.currentItem == li);
			}
		}
	}

	@Override
	public int getTotalArmorValue() {
		// �g�ɒ����Ă���A�[�}�[�̖h��͂̍��Z
		// �����ȊO
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		// int li = super.getTotalArmorValue() * 20 / 17;
		int li = super.getTotalArmorValue();
		// �����̕␳
		for (int lj = 0; lj < armorInventory.length; lj++) {
			if (armorInventory[lj] != null
					&& armorInventory[lj].getItem() instanceof ItemArmor) {
				li++;
			}
		}
		armorInventory[3] = lis;
		return li;
	}

	@Override
	public void damageArmor(int i) {
		// �����A�[�}�[�ɑ΂���_���[�W
		// �����͏��O
		ItemStack lis = armorInventory[3];
		armorInventory[3] = null;
		super.damageArmor(i);
		armorInventory[3] = lis;
	}

	@Override
	public int getDamageVsEntity(Entity entity) {
		return getDamageVsEntity(entity, currentItem);
	}

	public int getDamageVsEntity(Entity entity, int index) {
		if (index < 0 || index >= getSizeInventory()) return 1;
		ItemStack itemstack = getStackInSlot(index);
		if (itemstack != null) {
			if (itemstack.getItem() instanceof ItemAxe) {
				// �A�b�N�X�̍U���͂�␳
				return itemstack.getDamageVsEntity(entity) * 3 / 2 + 1;

			} else {
				return itemstack.getDamageVsEntity(entity);
			}
		} else {
			return 1;
		}
	}

	public void dropAllItems(boolean detonator) {
		// �C���x���g�����u�`�}�P���I
		armorInventory[3] = null;
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack it = getStackInSlot(i);
			if (it != null) {
				if (detonator && isItemExplord(i)) {
					int j = it.getItem().itemID;
					for (int l = 0; l < it.stackSize; l++) {
						// ����Ԃ��܂�
						((BlockTNT) Block.blocksList[j]).onBlockDestroyedByExplosion(
								entityLittleMaid.worldObj,
								MathHelper.floor_double(entityLittleMaid.posX)
								+ entityLittleMaid.rand.nextInt(7) - 3,
								MathHelper.floor_double(entityLittleMaid.posY)
								+ entityLittleMaid.rand.nextInt(7) - 3,
								MathHelper.floor_double(entityLittleMaid.posZ)
								+ entityLittleMaid.rand.nextInt(7) - 3);
					}
				} else {
					entityLittleMaid.entityDropItem(it, 0F);
				}
			}
			setInventorySlotContents(i, null);
		}
	}

	@Override
	public void dropAllItems() {
		dropAllItems(false);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		if (entityLittleMaid.isDead) {
			return false;
		}
		return entityplayer.getDistanceSqToEntity(entityLittleMaid) <= 64D;
	}

	@Override
	public ItemStack getCurrentItem() {
		if (currentItem >= 0 && currentItem < mainInventory.length) {
			return mainInventory[currentItem];
		} else {
			return null;
		}
	}

	@Override
	public boolean addItemStackToInventory(ItemStack par1ItemStack) {
		onInventoryChanged();
		return super.addItemStackToInventory(par1ItemStack);
	}

	/**
	 * �����̒ǉ��A�C�e����Ԃ��B
	 */
	public ItemStack getHeadMount() {
		return mainInventory[mainInventory.length - 1];
	}

	public void setInventoryCurrentSlotContents(ItemStack itemstack) {
		if (currentItem > -1) {
			setInventorySlotContents(currentItem, itemstack);
		}
	}

	protected int getInventorySlotContainItem(int itemid) {
		// �w�肳�ꂽ�A�C�e��ID�̕��������Ă���ΕԂ�
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null && mainInventory[j].itemID == itemid) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItem(Class itemClass) {
		// �w�肳�ꂽ�A�C�e���N���X�̕��������Ă���ΕԂ�
		for (int j = 0; j < mainInventory.length; j++) {
			// if (mainInventory[j] != null &&
			// mainInventory[j].getItem().getClass().isAssignableFrom(itemClass))
			// {
			if (mainInventory[j] != null
					&& itemClass.isAssignableFrom(mainInventory[j].getItem().getClass())) {
				return j;
			}
		}

		return -1;
	}

	protected int getInventorySlotContainItemAndDamage(int itemid, int damege) {
		// �ƃ_���[�W�l
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] != null && mainInventory[i].itemID == itemid
					&& mainInventory[i].getItemDamage() == damege) {
				return i;
			}
		}

		return -1;
	}

	protected ItemStack getInventorySlotContainItemStack(int itemid) {
		// ����񂩂��H
		int j = getInventorySlotContainItem(itemid);
		return j > -1 ? mainInventory[j] : null;
	}

	protected ItemStack getInventorySlotContainItemStackAndDamege(int itemid,
			int damege) {
		// ����񂩂��H
		int j = getInventorySlotContainItemAndDamage(itemid, damege);
		return j > -1 ? mainInventory[j] : null;
	}

	public int getInventorySlotContainItemFood() {
		// �C���x���g���̍ŏ��̐H����Ԃ�
		for (int j = 0; j < mainInventory.length; j++) {
			ItemStack mi = mainInventory[j];
			if (mi != null && mi.getItem() instanceof ItemFood) {
				if (((ItemFood) mi.getItem()).getHealAmount() > 0) {
					return j;
				}
			}
		}
		return -1;
	}

	public int getSmeltingItem() {
		// �����\�A�C�e����Ԃ�
		for (int i = 0; i < mainInventory.length; i++) {
			if (isItemSmelting(i) && i != currentItem) {
				ItemStack mi = mainInventory[i];
				if (mi.getMaxDamage() > 0 && mi.getItemDamage() == 0) {
					// �C�����V�s�΍�
					continue;
				}
				// ���V�s�Ή��i
				return i;
			}
		}
		return -1;
	}

	public int getInventorySlotContainItemPotion(boolean flag, int potionID, boolean isUndead) {
		// �C���x���g���̍ŏ��̃|�[�V������Ԃ�
		// flag = true: �U���E�f�o�t�n�A false: �񕜁E�⏕�n
		// potionID: �v���|�[�V������ID
		for (int j = 0; j < mainInventory.length; j++) {
			if (mainInventory[j] != null
					&& mainInventory[j].getItem() instanceof ItemPotion) {
				ItemStack is = mainInventory[j];
				List list = ((ItemPotion) is.getItem()).getEffects(is);
				nextPotion: if (list != null) {
					PotionEffect potioneffect;
					for (Iterator iterator = list.iterator(); iterator
							.hasNext();) {
						potioneffect = (PotionEffect) iterator.next();
						if (potioneffect.getPotionID() == potionID) break;
						if (potioneffect.getPotionID() == Potion.heal.id) {
							if ((!flag && isUndead) || (flag && !isUndead)) {
								break nextPotion;
							}
						} else if (potioneffect.getPotionID() == Potion.harm.id) {
							if ((flag && isUndead) || (!flag && !isUndead)) {
								break nextPotion;
							}
						} else if (Potion.potionTypes[potioneffect.getPotionID()].isBadEffect() != flag) {
							break nextPotion;
						}
					}
					return j;
				}
			}
		}
		return -1;
	}

	public int getFirstEmptyStack() {
		for (int i = 0; i < mainInventory.length; i++) {
			if (mainInventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	public boolean isItemBurned(int index) {
		// �R����A�C�e����?
		return index > -1 && isItemBurned(getStackInSlot(index));
	}

	public static boolean isItemBurned(ItemStack pItemstack) {
		return (pItemstack != null && 
				TileEntityFurnace.getItemBurnTime(pItemstack) > 0);
	}

	public boolean isItemSmelting(int index) {
		// �R����A�C�e����?
		return isItemSmelting(getStackInSlot(index));
	}

	public static boolean isItemSmelting(ItemStack pItemstack) {
		return (pItemstack != null &&
				FurnaceRecipes.smelting().getSmeltingResult(pItemstack.itemID) != null);
	}

	public boolean isItemExplord(int index) {
		// �������H
		return (index >= 0) && isItemExplord(getStackInSlot(index));
	}

	public static boolean isItemExplord(ItemStack pItemstack) {
		if (pItemstack == null)
			return false;
		Item li = pItemstack.getItem();
		return (pItemstack != null &&
				li instanceof ItemBlock && Block.blocksList[li.itemID].blockMaterial == Material.tnt);
	}

	// �C���x���g���̓]���֘A
	public boolean isChanged(int pIndex) {
		// �ω������������̔���
		ItemStack lis = getStackInSlot(pIndex);
		return !ItemStack.areItemStacksEqual(lis, prevItems[pIndex]);
		// return (lis == null || prevItems[pIndex] == null) ?
		// (prevItems[pIndex] != lis) : !ItemStack.areItemStacksEqual(lis,
		// prevItems[pIndex]);
		// return prevItems[pIndex] != getStackInSlot(pIndex);
	}

	public void setChanged(int pIndex) {
		prevItems[pIndex] = new ItemStack(Item.sugar);
	}

	public void resetChanged(int pIndex) {
		// �����ς݂̃`�F�b�N
		ItemStack lis = getStackInSlot(pIndex);
		prevItems[pIndex] = (lis == null ? null : lis.copy());
	}

	public void clearChanged() {
		// ���������[�h�p�A�_�~�[��o�^���ċ����I�Ɉ��������
		ItemStack lis = new ItemStack(Item.sugar);
		for (int li = 0; li < prevItems.length; li++) {
			prevItems[li] = lis;
		}
	}
}
