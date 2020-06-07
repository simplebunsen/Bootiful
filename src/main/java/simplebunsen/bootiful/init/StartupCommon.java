package simplebunsen.bootiful.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplebunsen.bootiful.items.JumpBootsItem;

public class StartupCommon {

    @SubscribeEvent
    public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
        itemRegisterEvent.getRegistry().register(JumpBootsItem.instance);
    }
}
