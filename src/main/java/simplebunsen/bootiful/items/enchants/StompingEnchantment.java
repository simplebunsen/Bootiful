package simplebunsen.bootiful.items.enchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import simplebunsen.bootiful.items.JumpBootsItem;
import simplebunsen.bootiful.main.BootifulMod;

public class StompingEnchantment extends Enchantment {

    public StompingEnchantment(Rarity rarityIn, EquipmentSlotType... slots) {
        super(rarityIn, EnchantmentType.ARMOR_FEET, slots);
        //TODO figure out how to add Enchanment Type for just cloud boots
    }

    public static final Enchantment STOMPING;

    static {
        STOMPING = new StompingEnchantment(Rarity.RARE, EquipmentSlotType.FEET);
        STOMPING.setRegistryName(BootifulMod.MOD_ID, "stomping");
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        //TODO: might not work well with jump boots, balance!
        return enchantmentLevel * 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    protected boolean canApplyTogether(Enchantment ench) {
        return super.canApplyTogether(ench);
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if(stack.getItem() == JumpBootsItem.instance){
            return true;
        }
        return false;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }
}
