package simplebunsen.bootiful.main;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BootifulMod.MOD_ID)
public class BootifulMod
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "bootiful";
    public static IEventBus MOD_EVENT_BUS;
    public static BootifulMod instance;

    public BootifulMod() {

        instance = this;
        MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();

        registerCommonEvents();
        DistExecutor.runWhenOn(Dist.CLIENT, () -> BootifulMod::registerClientOnlyEvents);

        MinecraftForge.EVENT_BUS.register(simplebunsen.bootiful.items.JumpBootsItem.class);

    }


    private void registerCommonEvents() {
        MOD_EVENT_BUS.register(simplebunsen.bootiful.init.StartupCommon.class);
    }

    private static void registerClientOnlyEvents() {
        MOD_EVENT_BUS.register(simplebunsen.bootiful.init.StartupClientOnly.class);
    }
}
