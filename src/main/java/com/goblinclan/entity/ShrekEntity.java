package com.goblinclan.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * Shrek the ogre. Animations: idle, walk, run, punch_right, punch_left, smash,
 * roar, ground_slam, laugh, hurt, death.
 */
public class ShrekEntity extends Monster implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");

    private static final int SLAM_IMPACT_TICKS = 14;   // impact at 0.7s of ground_slam
    private static final double SLAM_RADIUS = 4.0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int slamCooldown = 100;
    private int slamDelay = -1;
    private int roarCooldown = 0;
    private int attackCounter = 0;

    public ShrekEntity(EntityType<? extends ShrekEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.FOLLOW_RANGE, 24.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    // ---- behaviour that drives the animations (server side) ----

    @Override
    public void setTarget(LivingEntity target) {
        boolean newTarget = target != null && this.getTarget() == null;
        super.setTarget(target);
        if (newTarget && !this.level().isClientSide && roarCooldown <= 0 && !isDeadOrDying()) {
            this.triggerAnim("action", "roar");
            this.playSound(SoundEvents.RAVAGER_ROAR, 1.2F, 0.7F);
            roarCooldown = 400;
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (roarCooldown > 0) roarCooldown--;
        if (slamCooldown > 0) slamCooldown--;

        if (slamDelay >= 0) {
            if (--slamDelay < 0) doSlamImpact();
            return;
        }

        LivingEntity target = this.getTarget();
        if (target != null) {
            if (slamCooldown <= 0 && this.distanceToSqr(target) < 16.0 && this.onGround()) {
                this.triggerAnim("action", "ground_slam");
                slamDelay = SLAM_IMPACT_TICKS;
                slamCooldown = 160 + this.random.nextInt(100);
            }
        } else if (!this.getNavigation().isInProgress() && this.random.nextInt(700) == 0) {
            this.triggerAnim("action", "laugh");
        }
    }

    private void doSlamImpact() {
        this.playSound(SoundEvents.ANVIL_LAND, 1.5F, 0.6F);
        for (LivingEntity e : this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(SLAM_RADIUS, 1.0, SLAM_RADIUS),
                x -> x != this && !(x instanceof ShrekEntity))) {
            e.hurt(this.damageSources().mobAttack(this), 6.0F);
            e.knockback(1.2, this.getX() - e.getX(), this.getZ() - e.getZ());
        }
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit && !this.level().isClientSide) {
            String[] moves = {"punch_right", "punch_left", "smash"};
            this.triggerAnim("action", moves[attackCounter++ % moves.length]);
        }
        return hit;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide && !isDeadOrDying() && slamDelay < 0) {
            this.triggerAnim("action", "hurt");
        }
        return result;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.4F, 0.9F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    // ---- GeckoLib ----
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<ShrekEntity>(this, "main", 5, this::mainPredicate));
        controllers.add(new AnimationController<ShrekEntity>(this, "action", 0, state -> PlayState.STOP)
                .triggerableAnim("punch_right", RawAnimation.begin().thenPlay("punch_right"))
                .triggerableAnim("punch_left", RawAnimation.begin().thenPlay("punch_left"))
                .triggerableAnim("smash", RawAnimation.begin().thenPlay("smash"))
                .triggerableAnim("roar", RawAnimation.begin().thenPlay("roar"))
                .triggerableAnim("ground_slam", RawAnimation.begin().thenPlay("ground_slam"))
                .triggerableAnim("laugh", RawAnimation.begin().thenPlay("laugh"))
                .triggerableAnim("hurt", RawAnimation.begin().thenPlay("hurt")));
    }

    private PlayState mainPredicate(AnimationState<ShrekEntity> state) {
        if (this.isDeadOrDying()) {
            return state.setAndContinue(DEATH);
        }
        if (state.isMoving()) {
            return state.setAndContinue(this.isAggressive() ? RUN : WALK);
        }
        return state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
