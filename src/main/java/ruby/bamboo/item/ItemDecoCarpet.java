package ruby.bamboo.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemDecoCarpet extends ItemBlock {
    public ItemDecoCarpet(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        int i = itemstack.getItemDamage();
        return field_150939_a.getUnlocalizedName() + "." + i;
    }
}
