package baguchan.mcmod.tofucraft.entity;

import baguchan.mcmod.tofucraft.init.TofuCreatureAttribute;
import baguchan.mcmod.tofucraft.init.TofuItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TofuMindEntity extends MonsterEntity {
    public TofuMindEntity(EntityType<? extends TofuMindEntity> type, World p_i48553_2_) {
        super(type, p_i48553_2_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.9D));
        this.goalSelector.addGoal(6, new LookAtWithoutMovingGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(8, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractIllagerEntity.class, false));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.28F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(24.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    public void livingTick() {
        this.updateArmSwingProgress();
        super.livingTick();
    }

    @Override
    protected void updateAITasks() {

        Vec3d vec3d = this.getMotion();
        if (!this.onGround && vec3d.y < 0.0D) {
            this.setMotion(vec3d.mul(1.0D, 0.6D, 1.0D));
        }


        super.updateAITasks();
    }

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEnchantmentBasedOnDifficulty(difficultyIn);

        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(TofuItems.METALSWORD));


        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }


    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return true;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity) entityIn).getCreatureAttribute() == TofuCreatureAttribute.TOFUGUARIAN) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    public CreatureAttribute getCreatureAttribute() {
        return TofuCreatureAttribute.TOFUGUARIAN;
    }
}
