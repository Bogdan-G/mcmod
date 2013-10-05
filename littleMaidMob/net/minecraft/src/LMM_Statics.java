package net.minecraft.src;

public class LMM_Statics {

	/** Absoption���ʂ��N���C�A���g���֓]������̂Ɏg�� */
	protected static final int dataWatch_Absoption		= 18;
	
	/** ���C�h�J���[(byte) */
	protected static final int dataWatch_Color			= 19;
	/**
	 * MSB|0x0000 0000|LSB<br>
	 *       |    |�{�̂̃e�N�X�`���C���f�b�N�X<br>
	 *       |�A�[�}�[�̃e�N�X�`���C���f�b�N�X<br>
	 */
	protected static final int dataWatch_Texture		= 20;
	/** ���f���p�[�c�̕\���t���O(Integer) */
	protected static final int dataWatch_Parts			= 21;
	/**
	 * �e��t���O����Z�߂ɂ������́B
	 */
	protected static final int dataWatch_Flags			= 22;
	protected static final int dataWatch_Flags_looksWithInterest		= 0x00000001;
	protected static final int dataWatch_Flags_looksWithInterestAXIS	= 0x00000002;
	protected static final int dataWatch_Flags_Aimebow					= 0x00000004;
	protected static final int dataWatch_Flags_Freedom					= 0x00000008;
	protected static final int dataWatch_Flags_Tracer					= 0x00000010;
	protected static final int dataWatch_Flags_remainsContract			= 0x00000020;
	protected static final int dataWatch_Flags_PlayingMode				= 0x00000040;
	protected static final int dataWatch_Flags_Working					= 0x00000080;
	protected static final int dataWatch_Flags_Wait						= 0x00000100;
	protected static final int dataWatch_Flags_WaitEx					= 0x00000200;
	protected static final int dataWatch_Flags_LooksSugar				= 0x00000400;
	protected static final int dataWatch_Flags_Bloodsuck				= 0x00000800;
	protected static final int dataWatch_Flags_OverDrive				= 0x00001000;
	/** �R�̎������EntityID�B */
	protected static final int dataWatch_Gotcha			= 23;
	
	/** ���C�h���[�h(Short) */
	protected static final int dataWatch_Mode			= 24;
	/** �����r(Byte) */
	protected static final int dataWatch_DominamtArm	= 25;
	/** �A�C�e���̎g�p����A�r��(Integer) */
	protected static final int dataWatch_ItemUse		= 26;
	/** �ێ��o���l�A���̂Ƃ���N���C�A���g���ł͕K�v�Ȃ��̂ŗv��Ȃ�(Integer) */
	protected static final int dataWatch_ExpValue		= 27;
	
	
	
	/**
	 * ���R�ݒ�l�B
	 */
	protected static final int dataWatch_Free			= 31;
	
	protected static final int dataFlags_ForceUpdateInventory	= 0x80000000;

// NetWork

	/*
	 * ����p�萔�A8bit�ڂ𗧂Ă��Entity�v��
	 */
	
	/*
	 * LMMPacet�̃t�H�[�}�b�g
	 * (Byte)
	 * 0	: ����(1byte)
	 * 1 - 4: EntityID(4Byte)�ꍇ�Ɋ���Ă͏ȗ� 
	 * 5 - 	: Data
	 * 
	 */
	/**
	 * �T�[�o�[���֑Ώۂ̃C���x���g���𑗐M����悤�Ɏw������B
	 * �X�|�[�����_�ł̓C���x���g����񂪖������߁B
	 * [0]		: 0x00;
	 * [1..4]	: EntityID(int);
	 */
	public static final byte LMN_Server_UpdateSlots		= (byte)0x80;
	/**
	 * �N���C�A���g���֘r�U����w������B
	 * �U�������̍Đ��������w�肷��B
	 * [0]		: 0x81;
	 * [1..4]	: EntityID(int);
	 * [5]		: ArmIndex(byte);
	 * [6..9]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_SwingArm		= (byte)0x81;
	/**
	 * �T�[�o�[���֐����̎g�p��ʒm����B
	 * GUISelect�p�B
	 * [0]		: 0x02;
	 * [1]		: color(byte);
	 */
	public static final byte LMN_Server_DecDyePowder	= (byte)0x02;
	/**
	 * �T�[�o�[��IFF�̐ݒ�l���ύX���ꂽ���Ƃ�ʒm����B
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 * [6..]	: TargetName(str);
	 */
	public static final byte LMN_Server_SetIFFValue		= (byte)0x04;
	/**
	 * �N���C�A���g��IFF�̐ݒ�l��ʒm����B
	 * [0]		: 0x04;
	 * [1]		: IFFValue(byte);
	 * [2..5]	: Index(int);
	 */
	public static final byte LMN_Client_SetIFFValue		= (byte)0x04;
	/**
	 * �T�[�o�[�֌��݂�IFF�̐ݒ�l��v������B
	 * �v�����͈�ӂȎ��ʔԍ���t�^���邱�ƁB
	 * [0]		: 0x05;
	 * [1..4]	: Index(int);
	 * [5..]	: TargetName(str);
	 */
	public static final byte LMN_Server_GetIFFValue		= (byte)0x05;
	/**
	 * �T�[�o�[��IFF�̐ݒ�l��ۑ�����悤�Ɏw������B
	 * [0]		: 0x06;
	 */
	public static final byte LMN_Server_SaveIFF			= (byte)0x06;
	/**
	 * �N���C�A���g���։����𔭐�������悤�Ɏw������B
	 * �����̎��̂̓N���C�A���g���̓o�^�������g�p���邽�ߕW���̍Đ��菇���Ɖ����łȂ����߁B
	 * [0]		: 0x07;
	 * [1..4]	: EntityID(int);
	 * [5..8]	: SoundIndex(int);
	 */
	public static final byte LMN_Client_PlaySound		= (byte)0x89;


}
