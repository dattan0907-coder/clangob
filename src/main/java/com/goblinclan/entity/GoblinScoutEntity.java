package com.goblinclan.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

/** Small, fast goblin with dagger and shield. Animations: idle, walk, attack, cackle. */
public class GoblinScoutEntity extends AbstractGoblinEntity {
    private static final RawAnimation CACKLE = RawAnimation.begin().thenPlay("cackle");

    public GoblinScoutEntity(EntityType<? extends GoblinScoutEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 0.36)
                .add(Attributes.ATTACK_DAMAGE, 2.0)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerActions(AnimationController<AbstractGoblinEntity> action) {
        action.triggerableAnim("attack", ATTACK).triggerableAnim("cackle", CACKLE);
    }

    @Override
    public void tick() {
        super.tick();
        // Occasional cackle while standing around with no target.
        if (!this.level().isClientSide && this.isAlive() && this.getTarget() == null
                && this.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4
                && this.getRandom().nextInt(500) == 0) {
            this.triggerAnim("action", "cackle");
            this.playSound(SoundEvents.VEX_AMBIENT, 1.0F, 1.3F);
        }
    }

    @Override
    public void setTarget(LivingEntity target) {
        boolean newTarget = target != null && this.getTarget() == null;
        super.setTarget(target);
        if (newTarget && !this.level().isClientSide) {
            this.triggerAnim("action", "cackle");
            this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.2F);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VEX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VEX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VEX_DEATH;
    }
}
