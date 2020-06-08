package simplebunsen.bootiful.events;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplebunsen.bootiful.items.enchants.StompingEnchantment;
import simplebunsen.bootiful.main.BootifulMod;

import java.util.HashMap;
import java.util.UUID;

public class StompEntityEvents {
    public static HashMap<UUID, Boolean> uuidHasStompedMap = new HashMap<>();

    @SubscribeEvent
//    public static void onTickEvent(TickEvent.PlayerTickEvent event) {
//        LOGGER.fatal("Stopped being in the air etc, speed back to normal");
//    }
    public void onTickEvent(TickEvent.PlayerTickEvent event)
    {
        PlayerEntity entity = event.player;

        if (uuidHasStompedMap.containsKey(entity.getUniqueID()))
        {
            float enchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(StompingEnchantment.STOMPING, entity);
            boolean usedMidAirStomp = uuidHasStompedMap.get(entity.getUniqueID());
            boolean playerSneaking = entity.isSneaking();
            boolean canStomp = !entity.onGround && !usedMidAirStomp && !entity.isActualySwimming() && !entity.isElytraFlying();

            // Reset stomp
            if (entity.onGround && uuidHasStompedMap.get(entity.getUniqueID()))
            {
                entity.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 0.5F);
                createParicleCloud(entity, ParticleTypes.EXPLOSION);
                BootifulMod.LOGGER.fatal("ground contact, stomp");
                uuidHasStompedMap.put(entity.getUniqueID(), false);
            }

            if (!(enchantmentLevel > 0))
                return;

            // initiate stomping
            if (canStomp && !entity.abilities.isFlying)
            {
                if (playerSneaking && getFloorDistance(event.player.getEntityWorld(), entity) >= 3)// && entity.getMotion().getY() < 0)
                {
                    entity.setMotion(entity.getMotion().getX(), -0.9D, entity.getMotion().getZ());
                    uuidHasStompedMap.put(entity.getUniqueID(), true);

                    entity.playSound(SoundEvents.ENTITY_ENDER_DRAGON_FLAP, 0.3F, 2.0F);
                    createParicleCloud(entity, ParticleTypes.POOF);
                }

            }
        }
        else if (entity instanceof PlayerEntity)
        {
            uuidHasStompedMap.put(entity.getUniqueID(), false);
        }
    }


    private int getFloorDistance(World entityWorld, PlayerEntity entity) {

        BlockPos entPos = entity.getPosition();
        int firstBlockY = 0;
        for (int i = entPos.getY(); i >= 0; i--) {
            if (entityWorld.getBlockState(new BlockPos(entPos.getX(), i, entPos.getZ())).getBlock() != Blocks.AIR){
                System.out.println("I think that the distance is " + (entPos.getY() - i));
                return entPos.getY() - i;
            }
        }
        System.out.println("Something wong");
        return -1;
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event)
    {
        float enchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(StompingEnchantment.STOMPING, event.getEntityLiving());

        //cancelling fall damage?
        if (enchantmentLevel > 0 && uuidHasStompedMap.containsKey(event.getEntityLiving().getUniqueID()) && uuidHasStompedMap.get(event.getEntityLiving().getUniqueID()))
        {
            int i = MathHelper.ceil((event.getDistance() - 3.0F - enchantmentLevel) * event.getDamageMultiplier());
            event.setDistance(i);
        }
    }

    private void createParicleCloud(LivingEntity entity, BasicParticleType poof) {
        for (int i = 0; i < 20; ++i) {
            double d0 = entity.world.rand.nextGaussian() * 0.02D;
            double d1 = entity.world.rand.nextGaussian() * 0.02D;
            double d2 = entity.world.rand.nextGaussian() * 0.02D;

            entity.world.addParticle(poof, entity.getPosX() + (double) (entity.world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth() - d0 * 10.0D, entity.getPosY() - d1 * 10.0D, entity.getPosZ() + (double) (entity.world.rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity.getWidth() - d2 * 10.0D, d0, d1, d2);
        }
    }

}
