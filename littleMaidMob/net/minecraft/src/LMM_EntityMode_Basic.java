package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class LMM_EntityMode_Basic extends LMM_EntityModeBase {

	public static final int mmode_Wild		= 0x0000;
	public static final int mmode_Escorter	= 0x0001;
	
	private IInventory myTile;
	private IInventory myChest;
	private List<IInventory> fusedTiles;
	private boolean isWorking;
	private double lastdistance;
	private int maidSearchCount;

	
	/**
	 * Wild, Escorter 
	 */
	public LMM_EntityMode_Basic(LMM_EntityLittleMaid pEntity) {
		super(pEntity);
		fusedTiles = new ArrayList<IInventory>();
		myTile = null;
	}

	@Override
	public int priority() {
		// TODO Auto-generated method stub
		return 9000;
	}
	
	@Override
	public void init() {
		ModLoader.addLocalization("littleMaidMob.mode.Strike", "Strike");
		ModLoader.addLocalization("littleMaidMob.mode.Wait", "Wait");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "Wild");
		ModLoader.addLocalization("littleMaidMob.mode.Wild", "ja_JP", "�쐶��");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.Escorter", "ja_JP", "�]��");
		ModLoader.addLocalization("littleMaidMob.mode.F-Escorter", "Freedom");
		ModLoader.addLocalization("littleMaidMob.mode.D-Escorter", "D-Escorter");
		ModLoader.addLocalization("littleMaidMob.mode.T-Escorter", "Taracer");
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		// Wild
		EntityAITasks[] ltasks = new EntityAITasks[2];
		ltasks[0] = new EntityAITasks(null);
		ltasks[1] = new EntityAITasks(null);

		ltasks[0].addTask(1, owner.aiSwiming);
		ltasks[0].addTask(2, new LMM_EntityAIAttackOnCollide(owner, 0.23F, true));
		ltasks[0].addTask(3, owner.aiPanic);
		ltasks[0].addTask(4, owner.aiBegMove);
		ltasks[0].addTask(4, owner.aiBeg);
		ltasks[0].addTask(5, owner.aiRestrictRain);
		ltasks[0].addTask(6, owner.aiFreeRain);
//        ltasks[0].addTask(4, new EntityAIMoveIndoors(this));
//		ltasks[0].addTask(7, owner.aiCloseDoor);
//		ltasks[0].addTask(8, owner.aiOpenDoor);
		ltasks[0].addTask(9, new LMM_EntityAICollectItem(owner, 0.23F));
		ltasks[0].addTask(10, new EntityAILeapAtTarget(owner, 0.3F));
		ltasks[0].addTask(11, owner.aiWander);
		ltasks[0].addTask(12, new EntityAIWatchClosest2(owner, net.minecraft.src.EntityLiving.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAIWatchClosest2(owner, net.minecraft.src.LMM_EntityLittleMaid.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAIWatchClosest2(owner, net.minecraft.src.EntityPlayer.class, 10F, 0.02F));
		ltasks[0].addTask(13, new EntityAILookIdle(owner));

		ltasks[1].addTask(1, new LMM_EntityAIHurtByTarget(owner, false));

		owner.addMaidMode(ltasks, "Wild", mmode_Wild);

		// Escorter:0x0001
		ltasks = new EntityAITasks[2];
		ltasks[0] = pDefaultMove;
		ltasks[1] = pDefaultTargeting;
		owner.addMaidMode(ltasks, "Escorter", mmode_Escorter);
		
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		// �����I�Ɋ��蓖�Ă�
		owner.setMaidMode("Escorter");
		return true;
	}
	
	@Override
	public boolean setMode(int pMode) {
		switch (pMode) {
		case mmode_Wild :
			owner.setFreedom(true);
//			owner.aiWander.setEnable(true);
			return true;
		case mmode_Escorter :
			owner.aiAvoidPlayer.setEnable(false);
			for (int li = 0; li < owner.mstatSwingStatus.length; li++) {
				owner.setEquipItem(li, -1);
			}
			return true;
		}
//		owner.getNavigator().clearPathEntity()
		return false;
	}
	
	@Override
	public int getNextEquipItem(int pMode) {
		return pMode == mmode_Wild ? 0 : -1;
	}
	
	@Override
	public boolean checkItemStack(ItemStack pItemStack) {
		return true;
	}

	@Override
	public boolean isSearchBlock() {
		if (owner.getMaidModeInt() == mmode_Escorter && owner.isFreedom() && owner.maidInventory.getFirstEmptyStack() == -1) {
/*
			// �`�F�X�g�J�[�g�̌���
			List<Entity> list = owner.worldObj.getEntitiesWithinAABB(IInventory.class, owner.boundingBox.expand(8D, 2D, 8D));
			double cartl = 256D;
			for (Entity lentity : list) {
				if (!fusedTiles.contains(lentity)) {
					if (lentity instanceof EntityMinecart && ((EntityMinecart)lentity).minecartType != 1) {
						continue;
					}
					double l = lentity.getDistanceSqToEntity(owner);
					// ������ʒu�ɂ���ł��߂����ׂĂ��Ȃ��J�[�g�`�F�X�g
					
					if (cartl > l && owner.getEntitySenses().canSee(lentity)) {
						myTile = (IInventory)lentity;
						cartl = l;
					}
				}
			}
*/
			// Entity�n��Inventory��������Ȃ���Βʏ�̃T�[�`�����s
			return myTile == null;
		}
		return false;
	}

	@Override
	public boolean shouldBlock(int pMode) {
		return myTile != null;
	}

	@Override
	public boolean checkBlock(int pMode, int px, int py, int pz) {
		TileEntity ltile = owner.worldObj.getBlockTileEntity(px, py, pz);
		if (!(ltile instanceof IInventory)) {
			return false;
		}
		
		// ���E�̃��C�h����
		for (Object lo : owner.worldObj.getLoadedEntityList()) {
			if (lo instanceof LMM_EntityLittleMaid) {
				LMM_EntityLittleMaid lem = (LMM_EntityLittleMaid)lo;
//				if (lem.isUsingTile(ltile)) {
//					// �N�����g���Ă���
//					return false;
//				}
			}
		}
		
		if (fusedTiles.contains(ltile)) {
			// ���ɒʂ�߂����ꏊ��b�I
			return false;
		}
		myTile = (IInventory)ltile;
		return true;
	}

	@Override
	public void startBlock(int pMode) {
		lastdistance = -1;
		myChest = null;
		maidSearchCount = 0;
	}

	@Override
	public void resetBlock(int pMode) {
		myTile = null;
		if (myChest != null) {
			myChest.closeChest();
			myChest = null;
		}
	}

	@Override
	public boolean executeBlock(int pMode, int px, int py, int pz) {
//		isMaidChaseWait = true;
		if (myTile instanceof TileEntityChest) {
			// �u���b�N�n�̃`�F�X�g
			if (!((TileEntityChest) myTile).isInvalid()) {
				// �T�[�`�����`�F�X�g
				TileEntityChest lchest = (TileEntityChest)myTile;
				// �g�p���O�ɉ�����
				if (MMM_Helper.canBlockBeSeen(owner, lchest.xCoord, lchest.yCoord, lchest.zCoord, false, true, false)) {
					if (myChest == null) {
						getChest();
						if (myChest != null) {
							myChest.openChest();
						} else {
							// �J���Ȃ��`�F�X�g
							myTile = null;
						}
					}
					// �`�F�X�g�Ɏ��[
					owner.setWorking(true);
					putChest();
					return true;
				} else {
					// ��������
					myTile = null;
					if (myChest != null) {
						myChest.closeChest();
						myChest = null;
					}
				}
			} else {
				// Tile�̏���
				myTile = null;
				if (myChest != null) {
					myChest.closeChest();
					myChest = null;
				}
			}
		} else if (myTile instanceof Entity) {
			// �`�F�X�g�t�J�[�g�Ƃ�
			/*
			Entity lentity = (Entity)myTile;
			if (!lentity.isDead) {
				if (owner.getDistanceSqToEntity(lentity) < 5D)	{
//				if (!hasPath() && getDistanceSqToEntity(myEntity) < 5D)	{
					setPathToEntity(null);
					if (myChest == null) {
						myChest = (EntityMinecart)myEntity;
						serchedChest.add((EntityMinecart)myEntity);
							myChest.openChest();
					}
					if (myChest != null) {
						faceEntity(myEntity, 30F, 40F);
					}
					// �`�F�X�g�Ɏ��[
					putChest();
				} else {
					// �`�F�X�g�܂ł̃p�X�����
					if (!isMaidWaitEx()) {
						double distance = getDistanceSqToEntity(myEntity);
						if (distance == lastdistance) {
							mod_littleMaidMob.Debug("Assert.");
							updateWanderPath();
						} else {
							setPathToEntity(worldObj.getPathEntityToEntity(this, myEntity, 16F, true, false, false, true));
						}
						lastdistance = distance;
//						mod_littleMaidMob.Debug(String.format("Rerute:%b", hasPath()));
						if (myChest != null) {
							myChest.closeChest();
							myChest = null;
						}
					}
				}
				
			} else {
				// Entity�̎��S
				myTile = null;
				if (myChest != null) {
					myChest.closeChest();
					myChest = null;
				}
			}
			*/
		}
		return false;
	}

	@Override
	public boolean outrangeBlock(int pMode, int pX, int pY, int pZ) {
		// �`�F�X�g�܂ł̃p�X�����
		boolean lf = false;
		if (!owner.isMaidWaitEx()) {
			double distance;
			if (myTile instanceof TileEntity) {
				distance = ((TileEntity)myTile).getDistanceFrom(owner.posX, owner.posY, owner.posZ);
				if (distance == lastdistance) {
					// �ړ����ł܂�Ȃ��悤�ɗ�������
					mod_LMM_littleMaidMob.Debug("Assert.");
					owner.updateWanderPath();
					lf = true;
				} else {
					lf = MMM_Helper.setPathToTile(owner, (TileEntity)myTile, false);
				}
			} else {
				distance = 0;
			}
			lastdistance = distance;
			// �����W�O�̃`�F�X�g�͕���
			if (myChest != null) {
				myChest.closeChest();
				myChest = null;
			}
		}
		return lf;
	}

	@Override
	public void farrangeBlock() {
		// TODO Auto-generated method stub
		super.farrangeBlock();
	}


	protected boolean getChest() {
		// �`�F�X�g���l��
		if (myTile == null) {
			return false;
		}
		World world = owner.worldObj;
		int i = ((TileEntity)myTile).xCoord;
		int j = ((TileEntity)myTile).yCoord;
		int k = ((TileEntity)myTile).zCoord;
		IInventory obj = (TileEntityChest)world.getBlockTileEntity(i, j, k);
		IInventory obj2 = null;
		if(obj == null || world.isRemote) {
			return false;
		}
		// �����ς�
		fusedTiles.add(obj);
		
		// �J�����ǂ����̔���
		if(world.isBlockNormalCube(i, j + 1, k)) {
			return false;
		}
		if(world.getBlockId(i - 1, j, k) == Block.chest.blockID && world.isBlockNormalCube(i - 1, j + 1, k)) {
			return false;
		}
		if(world.getBlockId(i + 1, j, k) == Block.chest.blockID && world.isBlockNormalCube(i + 1, j + 1, k)) {
			return false;
		}
		if(world.getBlockId(i, j, k - 1) == Block.chest.blockID && world.isBlockNormalCube(i, j + 1, k - 1)) {
			return false;
		}
		if(world.getBlockId(i, j, k + 1) == Block.chest.blockID && world.isBlockNormalCube(i, j + 1, k + 1)) {
			return false;
		}
		if(world.getBlockId(i - 1, j, k) == Block.chest.blockID) {
			obj2 = (TileEntityChest)world.getBlockTileEntity(i - 1, j, k);
			obj = new InventoryLargeChest("Large chest", obj2, ((IInventory) (obj)));
		}
		if(world.getBlockId(i + 1, j, k) == Block.chest.blockID) {
			obj2 = (TileEntityChest)world.getBlockTileEntity(i + 1, j, k);
			obj = new InventoryLargeChest("Large chest", ((IInventory) (obj)), obj2);
		}
		if(world.getBlockId(i, j, k - 1) == Block.chest.blockID) {
			obj2 = (TileEntityChest)world.getBlockTileEntity(i, j, k - 1);
			obj = new InventoryLargeChest("Large chest", obj2, ((IInventory) (obj)));
		}
		if(world.getBlockId(i, j, k + 1) == Block.chest.blockID) {
			obj2 = (TileEntityChest)world.getBlockTileEntity(i, j, k + 1);
			obj = new InventoryLargeChest("Large chest", ((IInventory) (obj)), obj2);
		}
		if (obj2 != null) {
			fusedTiles.add(obj2);
		}
		
		myChest = (IInventory)obj;
		return true;
	}

	protected void putChest() {
		// �`�F�X�g�ɋߐ�
		if (owner.getSwingStatusDominant().canAttack() && myChest != null) {
			// �����A���v�A����Ă���w�����ȊO�̃A�C�e����˂�����
			ItemStack is;
			mod_LMM_littleMaidMob.Debug(String.format("getChest:%d", maidSearchCount));
			while ((is = owner.maidInventory.getStackInSlot(maidSearchCount)) == null && maidSearchCount < owner.maidInventory.mainInventory.length) {
				maidSearchCount++;
			}
			if (is != null && !(
					   is.getItem().itemID == Item.sugar.itemID
					|| is.getItem().itemID == Item.pocketSundial.itemID
					|| (is == owner.maidInventory.armorItemInSlot(3))
//					|| (is.getItem() instanceof ItemArmor && ((ItemArmor)is.getItem()).armorType == 0)
				))
			{
//				mod_littleMaidMob.Debug("getchest2.");
				boolean f = false;
				for (int j = 0; j < myChest.getSizeInventory() && is.stackSize > 0; j++)
				{
					ItemStack isc = myChest.getStackInSlot(j);
					if (isc == null)
					{
//						mod_littleMaidMob.Debug(String.format("%s -> NULL", is.getItemName()));
						myChest.setInventorySlotContents(j, is.copy());
						is.stackSize = 0;
						f = true;
						break;
					}
					else if (isc.isStackable() && isc.isItemEqual(is))
					{
//						mod_littleMaidMob.Debug(String.format("%s -> %s", is.getItemName(), isc.getItemName()));
						f = true;
						isc.stackSize += is.stackSize;
						if (isc.stackSize > isc.getMaxStackSize())
						{
							is.stackSize = isc.stackSize - isc.getMaxStackSize();
							isc.stackSize = isc.getMaxStackSize();
						} else {
							is.stackSize = 0; 
							break;
						}
					}
				}
				if (is.stackSize <= 0) {
					owner.maidInventory.setInventorySlotContents(maidSearchCount, null);
				}
				if (f) {
					owner.worldObj.playSoundAtEntity(owner, "random.pop", 0.5F, (owner.rand.nextFloat() - owner.rand.nextFloat()) * 0.2F + 1.0F);
					owner.setSwing(2, LMM_EnumSound.Null);
				}
			}
//			mod_littleMaidMob.Debug(String.format("getchest3:%d", maidSearchCount));
			if (++maidSearchCount >= owner.maidInventory.mainInventory.length) {
				// �����ς݂̑Ώۂ��X�^�b�N
//				serchedChest.add(myChest);
				myTile = null;
				myChest.closeChest();
				myChest = null;
				lastdistance = 0D;
				mod_LMM_littleMaidMob.Debug("endChest.");
				// �󂫂��ł�����{���I��
				if (owner.maidInventory.getFirstEmptyStack() > -1) {
					mod_LMM_littleMaidMob.Debug("Search clear.");
					fusedTiles.clear();
				}
			}
		}
	}



}
