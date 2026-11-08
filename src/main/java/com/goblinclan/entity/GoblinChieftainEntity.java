package com.goblinclan.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

/**
 * Big boss goblin. Animations: idle, walk, run, attack, slam, roar and their *_enraged variants.
 * Enrages below 50% health (faster, uses the *_enraged animations).
 */
public class GoblinChieftainEntity extends AbstractGoblinEntity {
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation IDLE_ENRAGED = RawAnimation.begin().thenLoop("idle_enraged");
    private static final RawAnimation SLAM = RawAnimation.begin().thenPlay("slam");
    private static final RawAnimation ROAR = RawAnimation.begin().thenPlay("roar");
    private static final RawAnimation ATTACK_ENRAGED = RawAnimation.begin().thenPlay("attack_enraged");
    private static final RawAnimation SLAM_ENRAGED = RawAnimation.begin().thenPlay("slam_enraged");
    private static final RawAnimation ROAR_ENRAGED = RawAnimation.begin().thenPlay("roar_enraged");

    private static final double SPEED_NORMAL = 0.26;
    private static final double SPEED_ENRAGED = 0.33;

    private boolean wasEnraged = false;
    private boolean lastAttackWasSlam = false;

    public GoblinChieftainEntity(EntityType<? extends GoblinChieftainEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.MOVEMENT_SPEED, SPEED_NORMAL)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ARMOR, 6.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7);
    }

    public boolean isEnraged() {
        return this.getHealth() <= this.getMaxHealth() * 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isAlive()) {
            boolean enraged = isEnraged();
            if (enraged && !wasEnraged) {
                wasEnraged = true;
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(SPEED_ENRAGED);
                this.triggerAnim("action", "roar_enraged");
                this.level().playSound(null, this.blockPosition(), SoundEvents.RAVAGER_ROAR,
                        SoundSource.HOSTILE, 1.5F, 0.7F);
            }
        }
    }

    @Override
    public void setTarget(LivingEntity target) {
        boolean newTarget = target != null && this.getTarget() == null;
        super.setTarget(target);
        if (newTarget && !this.level().isClientSide) {
            this.triggerAnim("action", isEnraged() ? "roar_enraged" : "roar");
            this.level().playSound(null, this.blockPosition(), SoundEvents.RAVAGER_ROAR,
                    SoundSource.HOSTILE, 1.5F, 0.8F);
        }
    }

    @Override
    protected String pickAttackAnim() {
        lastAttackWasSlam = this.getRandom().nextInt(4) == 0;
        String base = lastAttackWasSlam ? "slam" : "attack";
        return isEnraged() ? base + "_enraged" : base;
    }

    @Override
    protected void onAttackHit(Entity target) {
        if (lastAttackWasSlam) {
            slamShockwave();
        }
    }

    /** Ground slam: damages and shoves nearby non-goblin creatures. */
    private void slamShockwave() {
        this.playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.5F, 0.6F);
        for (LivingEntity e : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(3.5, 1.0, 3.5),
                x -> x != this && !(x instanceof AbstractGoblinEntity))) {
            e.hurt(this.damageSources().mobAttack(this), 4.0F);
            double dx = e.getX() - this.getX();
            double dz = e.getZ() - this.getZ();
            double len = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
            e.setDeltaMovement(e.getDeltaMovement().add(dx / len * 0.6, 0.4, dz / len * 0.6));
            e.hurtMarked = true;
        }
    }

    @Override
    protected void registerActions(AnimationController<AbstractGoblinEntity> action) {
        action.triggerableAnim("attack", ATTACK)
                .triggerableAnim("slam", SLAM)
                .triggerableAnim("roar", ROAR)
                .triggerableAnim("attack_enraged", ATTACK_ENRAGED)
                .triggerableAnim("slam_enraged", SLAM_ENRAGED)
                .triggerableAnim("roar_enraged", ROAR_ENRAGED);
    }

    @Override
    protected PlayState mainPredicate(AnimationState<AbstractGoblinEntity> state) {
        if (state.isMoving()) {
            return state.setAndContinue(this.isAggressive() ? RUN : WALK);
        }
        return state.setAndContinue(isEnraged() ? IDLE_ENRAGED : IDLE);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VINDICATOR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VINDICATOR_DEATH;
    }
}
