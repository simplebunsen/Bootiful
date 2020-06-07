package simplebunsen.bootiful.events;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import simplebunsen.bootiful.items.enchants.StompingEnchantment;

import java.util.HashMap;
import java.util.UUID;

public class StompEntityEvents {
    public static HashMap<UUID, Boolean> uuidHasStompedMap = new HashMap<>();


    @SubscribeEvent
    public void onLivingUpdate(LivingUpdateEvent event)
    {
        LivingEntity entity = event.getEntityLiving();

        if (entity instanceof PlayerEntity && uuidHasStompedMap.containsKey(entity.getUniqueID()))
        {
            float enchantmentLevel = EnchantmentHelper.getMaxEnchantmentLevel(StompingEnchantment.STOMPING, entity);
            boolean usedMidAirStomp = uuidHasStompedMap.get(entity.getUniqueID());
            boolean playerSneaking = entity.isSneaking();
            boolean canStomp = !entity.onGround && !usedMidAirStomp;

            // Reset stomp
            if (entity.onGround && uuidHasStompedMap.get(entity.getUniqueID()) != false)
            {
                entity.playSound(SoundEvents.BLOCK_ANVIL_LAND, 0.3F, 2.0F);
                createParicleCloud(entity, ParticleTypes.EXPLOSION);
                uuidHasStompedMap.put(entity.getUniqueID(), false);
            }

            if (!(enchantmentLevel > 0))
                return;

            // initiate stomping
            if (canStomp && !entity.isElytraFlying() && !((PlayerEntity) entity).abilities.isFlying)
            {
                if (playerSneaking && entity.getMotion().getY() < 0)
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
