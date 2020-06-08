package simplebunsen.bootiful.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static simplebunsen.bootiful.main.BootifulMod.MOD_ID;


public class JumpBootsItem extends ArmorItem {
    public static final String REGISTRY_NAME = "jump_boots";
    public static ArmorItem instance;

    public static final int avgSecondsToDamage = 15;

    static {
        instance = new JumpBootsItem();
    }

    public JumpBootsItem() {
        super(JumpArmorMaterial.jumpArmor, EquipmentSlotType.FEET, new Properties().group(ItemGroup.COMBAT));
        this.setRegistryName(MOD_ID, REGISTRY_NAME);
    }


    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 0, 4, false, false, false));

        if (slot == EquipmentSlotType.FEET && stack.getItem() == this && !player.onGround) { //TODO: research ways to use isJumping or simply the jumpkey
            //TODO: hahhah this is utter bullshit but i hate attribute modifiers so it stays. Also FOV change bad
            //TODO: also it sucks donkey balls that speed is only applied in air and isn't there for the instant u land, need to use jump key holding
            //https://www.minecraftforge.net/forum/topic/47519-1102how-to-change-player-speed-without-changing-fov/
            player.addPotionEffect(new EffectInstance(Effects.SPEED, 2, 0, false, false, false));

            if (player instanceof ServerPlayerEntity && !world.isRemote && world.getRandom().nextInt(avgSecondsToDamage * 20) == 0) {
                stack.attemptDamageItem(1, world.getRandom(), (ServerPlayerEntity) player);
            }
        }

    }

    //TODO: Fancy effects
    // @SubscribeEvent
//    public static void onTickEvent(TickEvent.PlayerTickEvent event) {
//        LOGGER.fatal("Stopped being in the air etc, speed back to normal");
//    }

    public static class JumpArmorMaterial implements IArmorMaterial {

        public static final JumpArmorMaterial jumpArmor;
        private final SoundEvent equipSound;
        private final String name;
        private final int durability;
        private final int enchantability;
        private final int[] damageReduction;
        private final float toughness;

        static {
            jumpArmor = new JumpArmorMaterial("jump_armor", 320, new int[]{2, 5, 7, 2}, 12,
                    SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0F);
        }

        private JumpArmorMaterial(String name, int durability, int[] damageReduction, int enchantability, SoundEvent equipSound, float toughness) {
            this.name = name;
            this.equipSound = equipSound;
            this.durability = durability;
            this.enchantability = enchantability;
            this.damageReduction = damageReduction;
            this.toughness = toughness;
        }

        @Override
        public int getDurability(EquipmentSlotType slotIn) {
            return this.durability;
        }

        @Override
        public int getDamageReductionAmount(EquipmentSlotType slotIn) {
            return this.damageReduction[slotIn.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return this.enchantability;
        }

        @Override
        public SoundEvent getSoundEvent() {
            return this.equipSound;
        }

        @Override
        public Ingredient getRepairMaterial() {
            //TODO: change repair item
            return Ingredient.fromItems(Items.PUMPKIN);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName() {
            return MOD_ID + ":" + this.name;
        }

        @Override
        public float getToughness() {
            return this.toughness;
        }
    }
}
