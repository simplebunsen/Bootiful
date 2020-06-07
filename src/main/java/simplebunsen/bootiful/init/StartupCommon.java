package simplebunsen.bootiful.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplebunsen.bootiful.items.JumpBootsItem;
import simplebunsen.bootiful.items.enchants.StompingEnchantment;

public class StartupCommon {

    @SubscribeEvent
    public static void onItemsRegistration(final Register<Item> itemRegisterEvent) {
        itemRegisterEvent.getRegistry().register(JumpBootsItem.instance);
    }

    @SubscribeEvent
    public static void onEnchantmentRegistration(final Register<Enchantment> enchantmentRegisterEvent)
    {
        enchantmentRegisterEvent.getRegistry().register(StompingEnchantment.STOMPING);
    }

}
