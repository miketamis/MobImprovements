// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import java.util.*;

// Referenced classes of package net.minecraft.src:
//            Entity, DataWatcher, Vec3D, World, 
//            Profiler, DamageSource, Material, Potion, 
//            EntityXPOrb, MathHelper, EntityPlayer, EntityWolf, 
//            PotionEffect, EnchantmentHelper, Block, StepSound, 
//            AxisAlignedBB, NBTTagCompound, NBTTagList, PotionHelper, 
//            EnumCreatureAttribute, ItemStack, Item

public abstract class EntityLiving extends Entity
{
	//mike add
		private double EntitySavedID = -1;
		
		public double getID()
		{
			if(EntitySavedID ==-1)
			{
				EntitySavedID = posX + posY + posZ; //TODO: beter generation
				
			}
			return EntitySavedID;
		}
		
		public double getIDNogen()
		{
		
			return EntitySavedID;
		}
		
		public Entity getMobWithID(double d)
		{
			Iterator moblist = worldObj.loadedEntityList.iterator();
			while(moblist.hasNext())
			{
				Entity e = (Entity) moblist.next();
			if(e instanceof EntityLiving && ((EntityLiving) e).getIDNogen() == d)
			{
				return e;
				
			}
			}
			return null;
		}
		//mike end
    public int heartsHalvesLife;
    public float field_9098_aw;
    public float field_9096_ay;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    protected float field_9124_aB;
    protected float field_9123_aC;
    protected float field_9122_aD;
    protected float field_9121_aE;
    protected boolean field_9120_aF;
    protected String texture;
    protected boolean field_9118_aH;
    protected float field_9117_aI;
    protected String entityType;
    protected float field_9115_aK;
    protected int scoreValue;
    protected float field_9113_aM;
    public boolean isMultiplayerEntity;
    public float field_35194_aj;
    public float field_35193_ak;
    public float prevSwingProgress;
    public float swingProgress;
    protected int health;
    public int prevHealth;
    protected int field_40105_ap;
    private int livingSoundTime;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public int attackTime;
    public float prevCameraPitch;
    public float cameraPitch;
    protected boolean unused_flag;
    protected int experienceValue;
    public int field_9144_ba;
    public float field_9143_bb;
    public float field_9142_bc;
    public float field_9141_bd;
    public float field_386_ba;
    protected EntityPlayer field_34903_b;
    protected int field_34904_c;
    public int field_35189_aD;
    public int field_35190_aE;
    protected HashMap field_35191_aF;
    private boolean field_39002_b;
    private int field_39003_c;
    protected int newPosRotationIncrements;
    protected double newPosX;
    protected double newPosY;
    protected double newPosZ;
    protected double newRotationYaw;
    protected double newRotationPitch;
    float field_9134_bl;
    protected int naturalArmorRating;
    protected int entityAge;
    protected float moveStrafing;
    protected float moveForward;
    protected float randomYawVelocity;
    protected boolean isJumping;
    protected float defaultPitch;
    protected float moveSpeed;
    private int field_39004_d;
    private Entity currentTarget;
    protected int numTicksToChaseTarget;

    public EntityLiving(World world)
    {
        super(world);
        heartsHalvesLife = 20;
        renderYawOffset = 0.0F;
        prevRenderYawOffset = 0.0F;
        field_9120_aF = true;
        texture = "/mob/char.png";
        field_9118_aH = true;
        field_9117_aI = 0.0F;
        entityType = null;
        field_9115_aK = 1.0F;
        scoreValue = 0;
        field_9113_aM = 0.0F;
        isMultiplayerEntity = false;
        field_35194_aj = 0.1F;
        field_35193_ak = 0.02F;
        attackedAtYaw = 0.0F;
        deathTime = 0;
        attackTime = 0;
        unused_flag = false;
        field_9144_ba = -1;
        field_9143_bb = (float)(Math.random() * 0.89999997615814209D + 0.10000000149011612D);
        field_34903_b = null;
        field_34904_c = 0;
        field_35189_aD = 0;
        field_35190_aE = 0;
        field_35191_aF = new HashMap();
        field_39002_b = true;
        field_9134_bl = 0.0F;
        naturalArmorRating = 0;
        entityAge = 0;
        isJumping = false;
        defaultPitch = 0.0F;
        moveSpeed = 0.7F;
        field_39004_d = 0;
        numTicksToChaseTarget = 0;
        health = getMaxHealth();
        preventEntitySpawning = true;
        field_9096_ay = (float)(Math.random() + 1.0D) * 0.01F;
        setPosition(posX, posY, posZ);
        field_9098_aw = (float)Math.random() * 12398F;
        rotationYaw = (float)(Math.random() * 3.1415927410125732D * 2D);
        stepHeight = 0.5F;
    }

    protected void entityInit()
    {
        dataWatcher.addObject(8, Integer.valueOf(field_39003_c));
    }

    public boolean canEntityBeSeen(Entity entity)
    {
        return worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(entity.posX, entity.posY + (double)entity.getEyeHeight(), entity.posZ)) == null;
    }

    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    public boolean canBePushed()
    {
        return !isDead;
    }

    public float getEyeHeight()
    {
        return height * 0.85F;
    }

    public int getTalkInterval()
    {
        return 80;
    }

    public void playLivingSound()
    {
        String s = getLivingSound();
        if(s != null)
        {
            worldObj.playSoundAtEntity(this, s, getSoundVolume(), func_40090_w());
        }
    }

    public void onEntityUpdate()
    {
        prevSwingProgress = swingProgress;
        super.onEntityUpdate();
        Profiler.startSection("mobBaseTick");
        if(rand.nextInt(1000) < livingSoundTime++)
        {
            livingSoundTime = -getTalkInterval();
            playLivingSound();
        }
        if(isEntityAlive() && isEntityInsideOpaqueBlock())
        {
            if(!attackEntityFrom(DamageSource.inWall, 1));
        }
        if(func_40033_ax() || worldObj.singleplayerWorld)
        {
            func_40036_aw();
        }
        if(isEntityAlive() && isInsideOfMaterial(Material.water) && !canBreatheUnderwater() && !field_35191_aF.containsKey(Integer.valueOf(Potion.waterBreathingPotion.potionId)))
        {
            func_41008_j(func_40094_f(func_41009_al()));
            if(func_41009_al() == -20)
            {
                func_41008_j(0);
                for(int i = 0; i < 8; i++)
                {
                    float f = rand.nextFloat() - rand.nextFloat();
                    float f1 = rand.nextFloat() - rand.nextFloat();
                    float f2 = rand.nextFloat() - rand.nextFloat();
                    worldObj.spawnParticle("bubble", posX + (double)f, posY + (double)f1, posZ + (double)f2, motionX, motionY, motionZ);
                }

                attackEntityFrom(DamageSource.drown, 2);
            }
            func_40036_aw();
        } else
        {
            func_41008_j(300);
        }
        prevCameraPitch = cameraPitch;
        if(attackTime > 0)
        {
            attackTime--;
        }
        if(hurtTime > 0)
        {
            hurtTime--;
        }
        if(heartsLife > 0)
        {
            heartsLife--;
        }
        if(health <= 0)
        {
            func_40102_ag();
        }
        if(field_34904_c > 0)
        {
            field_34904_c--;
        } else
        {
            field_34903_b = null;
        }
        func_35186_aj();
        field_9121_aE = field_9122_aD;
        prevRenderYawOffset = renderYawOffset;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        Profiler.endSection();
    }

    protected void func_40102_ag()
    {
        deathTime++;
        if(deathTime == 20)
        {
            if(!worldObj.singleplayerWorld && (field_34904_c > 0 || func_35188_X()) && !func_40104_l())
            {
                for(int i = getExperiencePoints(field_34903_b); i > 0;)
                {
                    int k = EntityXPOrb.getMore(i);
                    i -= k;
                    worldObj.entityJoinedWorld(new EntityXPOrb(worldObj, posX, posY, posZ, k));
                }

            }
            onEntityDeath();
            setEntityDead();
            for(int j = 0; j < 20; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }

        }
    }

    protected int func_40094_f(int i)
    {
        return i - 1;
    }

    protected int getExperiencePoints(EntityPlayer entityplayer)
    {
        return experienceValue;
    }

    protected boolean func_35188_X()
    {
        return false;
    }

    public void spawnExplosionParticle()
    {
        for(int i = 0; i < 20; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d3 = 10D;
            worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d * d3, (posY + (double)(rand.nextFloat() * height)) - d1 * d3, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d2 * d3, d, d1, d2);
        }

    }

    public void updateRidden()
    {
        super.updateRidden();
        field_9124_aB = field_9123_aC;
        field_9123_aC = 0.0F;
        fallDistance = 0.0F;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if(field_35189_aD > 0)
        {
            if(field_35190_aE <= 0)
            {
                field_35190_aE = 60;
            }
            field_35190_aE--;
            if(field_35190_aE <= 0)
            {
                field_35189_aD--;
            }
        }
        onLivingUpdate();
        double d = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = renderYawOffset;
        float f2 = 0.0F;
        field_9124_aB = field_9123_aC;
        float f3 = 0.0F;
        if(f > 0.05F)
        {
            f3 = 1.0F;
            f2 = f * 3F;
            f1 = ((float)Math.atan2(d1, d) * 180F) / 3.141593F - 90F;
        }
        if(swingProgress > 0.0F)
        {
            f1 = rotationYaw;
        }
        if(!onGround)
        {
            f3 = 0.0F;
        }
        field_9123_aC = field_9123_aC + (f3 - field_9123_aC) * 0.3F;
        float f4;
        for(f4 = f1 - renderYawOffset; f4 < -180F; f4 += 360F) { }
        for(; f4 >= 180F; f4 -= 360F) { }
        renderYawOffset += f4 * 0.3F;
        float f5;
        for(f5 = rotationYaw - renderYawOffset; f5 < -180F; f5 += 360F) { }
        for(; f5 >= 180F; f5 -= 360F) { }
        boolean flag = f5 < -90F || f5 >= 90F;
        if(f5 < -75F)
        {
            f5 = -75F;
        }
        if(f5 >= 75F)
        {
            f5 = 75F;
        }
        renderYawOffset = rotationYaw - f5;
        if(f5 * f5 > 2500F)
        {
            renderYawOffset += f5 * 0.2F;
        }
        if(flag)
        {
            f2 *= -1F;
        }
        for(; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }
        for(; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }
        for(; renderYawOffset - prevRenderYawOffset < -180F; prevRenderYawOffset -= 360F) { }
        for(; renderYawOffset - prevRenderYawOffset >= 180F; prevRenderYawOffset += 360F) { }
        for(; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }
        for(; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }
        field_9122_aD += f2;
    }

    protected void setSize(float f, float f1)
    {
        super.setSize(f, f1);
    }

    public void heal(int i)
    {
        if(health <= 0)
        {
            return;
        }
        health += i;
        if(health > getMaxHealth())
        {
            health = getMaxHealth();
        }
        heartsLife = heartsHalvesLife / 2;
    }

    public abstract int getMaxHealth();

    public int getEntityHealth()
    {
        return health;
    }

    public void setEntityHealth(int i)
    {
        health = i;
        if(i > getMaxHealth())
        {
            i = getMaxHealth();
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if(worldObj.singleplayerWorld)
        {
            return false;
        }
        entityAge = 0;
        if(health <= 0)
        {
            return false;
        }
        if(damagesource.func_40272_k() && func_35184_a(Potion.fireResistancePotion))
        {
            return false;
        }
        field_9141_bd = 1.5F;
        boolean flag = true;
        if((float)heartsLife > (float)heartsHalvesLife / 2.0F)
        {
            if(i <= naturalArmorRating)
            {
                return false;
            }
            damageEntity(damagesource, i - naturalArmorRating);
            naturalArmorRating = i;
            flag = false;
        } else
        {
            naturalArmorRating = i;
            prevHealth = health;
            heartsLife = heartsHalvesLife;
            damageEntity(damagesource, i);
            hurtTime = maxHurtTime = 10;
        }
        attackedAtYaw = 0.0F;
        Entity entity = damagesource.getEntity();
        if(entity != null)
        {
            if(entity instanceof EntityPlayer)
            {
                field_34904_c = 60;
                field_34903_b = (EntityPlayer)entity;
            } else
            if(entity instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)entity;
                if(entitywolf.isTamed())
                {
                    field_34904_c = 60;
                    field_34903_b = null;
                }
            }
        }
        if(flag)
        {
            worldObj.sendTrackedEntityStatusUpdatePacket(this, (byte)2);
            setBeenAttacked();
            if(entity != null)
            {
                double d = entity.posX - posX;
                double d1;
                for(d1 = entity.posZ - posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - rotationYaw;
                knockBack(entity, i, d, d1);
            } else
            {
                attackedAtYaw = (int)(Math.random() * 2D) * 180;
            }
        }
        if(health <= 0)
        {
            if(flag)
            {
                worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume(), func_40090_w());
            }
            onDeath(damagesource);
        } else
        if(flag)
        {
            worldObj.playSoundAtEntity(this, getHurtSound(), getSoundVolume(), func_40090_w());
        }
        return true;
    }

    private float func_40090_w()
    {
        if(func_40104_l())
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F;
        } else
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
        }
    }

    protected int func_40092_O()
    {
        return 0;
    }

    protected void func_40101_g(int i)
    {
    }

    protected int func_40091_d(DamageSource damagesource, int i)
    {
        if(!damagesource.unblockable())
        {
            int j = 25 - func_40092_O();
            int k = i * j + field_40105_ap;
            func_40101_g(i);
            i = k / 25;
            field_40105_ap = k % 25;
        }
        return i;
    }

    protected int func_40099_b(DamageSource damagesource, int i)
    {
        if(func_35184_a(Potion.resistance))
        {
            int j = (func_35187_b(Potion.resistance).getAmplifier() + 1) * 5;
            int k = 25 - j;
            int l = i * k + field_40105_ap;
            i = l / 25;
            field_40105_ap = l % 25;
        }
        return i;
    }

    protected void damageEntity(DamageSource damagesource, int i)
    {
        i = func_40091_d(damagesource, i);
        i = func_40099_b(damagesource, i);
        health -= i;
    }

    protected float getSoundVolume()
    {
        return 1.0F;
    }

    protected String getLivingSound()
    {
        return null;
    }

    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    protected String getDeathSound()
    {
        return "damage.hurtflesh";
    }

    public void knockBack(Entity entity, int i, double d, double d1)
    {
        isAirBorne = true;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = 0.4F;
        motionX /= 2D;
        motionY /= 2D;
        motionZ /= 2D;
        motionX -= (d / (double)f) * (double)f1;
        motionY += 0.40000000596046448D;
        motionZ -= (d1 / (double)f) * (double)f1;
        if(motionY > 0.40000000596046448D)
        {
            motionY = 0.40000000596046448D;
        }
    }

    public void onDeath(DamageSource damagesource)
    {
        Entity entity = damagesource.getEntity();
        if(scoreValue >= 0 && entity != null)
        {
            entity.addToPlayerScore(this, scoreValue);
        }
        if(entity != null)
        {
            entity.onKillEntity(this);
        }
        unused_flag = true;
        if(!worldObj.singleplayerWorld)
        {
            int i = 0;
            if(entity instanceof EntityPlayer)
            {
                i = EnchantmentHelper.func_40633_f(((EntityPlayer)entity).inventory);
            }
            if(!func_40104_l())
            {
                dropFewItems(field_34904_c > 0, i);
            }
        }
        worldObj.sendTrackedEntityStatusUpdatePacket(this, (byte)3);
    }

    protected void dropFewItems(boolean flag, int i)
    {
        int j = getDropItemId();
        if(j > 0)
        {
            int k = rand.nextInt(3);
            if(i > 0)
            {
                k += rand.nextInt(i + 1);
            }
            for(int l = 0; l < k; l++)
            {
                dropItem(j, 1);
            }

        }
    }

    protected int getDropItemId()
    {
        return 0;
    }

    protected void fall(float f)
    {
        super.fall(f);
        int i = (int)Math.ceil(f - 3F);
        if(i > 0)
        {
            if(i > 4)
            {
                worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
            } else
            {
                worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
            }
            attackEntityFrom(DamageSource.fall, i);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));
            if(j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.stepSoundDir(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }

    public void moveEntityWithHeading(float f, float f1)
    {
        if(isInWater())
        {
            double d = posY;
            moveFlying(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.80000001192092896D;
            motionY *= 0.80000001192092896D;
            motionZ *= 0.80000001192092896D;
            motionY -= 0.02D;
            if(isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        } else
        if(handleLavaMovement())
        {
            double d1 = posY;
            moveFlying(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
            motionY -= 0.02D;
            if(isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d1, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        } else
        {
            float f2 = 0.91F;
            if(onGround)
            {
                f2 = 0.5460001F;
                int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if(i > 0)
                {
                    f2 = Block.blocksList[i].slipperiness * 0.91F;
                }
            }
            float f3 = 0.1627714F / (f2 * f2 * f2);
            float f4 = onGround ? field_35194_aj * f3 : field_35193_ak;
            moveFlying(f, f1, f4);
            f2 = 0.91F;
            if(onGround)
            {
                f2 = 0.5460001F;
                int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if(j > 0)
                {
                    f2 = Block.blocksList[j].slipperiness * 0.91F;
                }
            }
            if(isOnLadder())
            {
                float f5 = 0.15F;
                if(motionX < (double)(-f5))
                {
                    motionX = -f5;
                }
                if(motionX > (double)f5)
                {
                    motionX = f5;
                }
                if(motionZ < (double)(-f5))
                {
                    motionZ = -f5;
                }
                if(motionZ > (double)f5)
                {
                    motionZ = f5;
                }
                fallDistance = 0.0F;
                if(motionY < -0.14999999999999999D)
                {
                    motionY = -0.14999999999999999D;
                }
                if(isSneaking() && motionY < 0.0D)
                {
                    motionY = 0.0D;
                }
            }
            moveEntity(motionX, motionY, motionZ);
            if(isCollidedHorizontally && isOnLadder())
            {
                motionY = 0.20000000000000001D;
            }
            motionY -= 0.080000000000000002D;
            motionY *= 0.98000001907348633D;
            motionX *= f2;
            motionZ *= f2;
        }
        field_9142_bc = field_9141_bd;
        double d2 = posX - prevPosX;
        double d3 = posZ - prevPosZ;
        float f6 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;
        if(f6 > 1.0F)
        {
            f6 = 1.0F;
        }
        field_9141_bd += (f6 - field_9141_bd) * 0.4F;
        field_386_ba += field_9141_bd;
    }

    public boolean isOnLadder()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        return worldObj.getBlockId(i, j, k) == Block.ladder.blockID;
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        nbttagcompound.setShort("Health", (short)health);
        nbttagcompound.setShort("HurtTime", (short)hurtTime);
        nbttagcompound.setShort("DeathTime", (short)deathTime);
        nbttagcompound.setShort("AttackTime", (short)attackTime);
        //mike add
        if( EntitySavedID != -1)
        {
     	   nbttagcompound.setDouble("EntitySavedID", EntitySavedID);
            
        }
        //mike end
        if(!field_35191_aF.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound nbttagcompound1;
            for(Iterator iterator = field_35191_aF.values().iterator(); iterator.hasNext(); nbttaglist.setTag(nbttagcompound1))
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Id", (byte)potioneffect.getPotionID());
                nbttagcompound1.setByte("Amplifier", (byte)potioneffect.getAmplifier());
                nbttagcompound1.setInteger("Duration", potioneffect.getDuration());
            }

            nbttagcompound.setTag("ActiveEffects", nbttaglist);
        }
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
    	   //mike add
        if(nbttagcompound.hasKey("EntitySavedID"))
        {
        	EntitySavedID = nbttagcompound.getDouble("EntitySavedID");
        }
        //mike end
        health = nbttagcompound.getShort("Health");
        if(!nbttagcompound.hasKey("Health"))
        {
            health = getMaxHealth();
        }
        hurtTime = nbttagcompound.getShort("HurtTime");
        deathTime = nbttagcompound.getShort("DeathTime");
        attackTime = nbttagcompound.getShort("AttackTime");
        if(nbttagcompound.hasKey("ActiveEffects"))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("ActiveEffects");
            for(int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
                byte byte0 = nbttagcompound1.getByte("Id");
                byte byte1 = nbttagcompound1.getByte("Amplifier");
                int j = nbttagcompound1.getInteger("Duration");
                field_35191_aF.put(Integer.valueOf(byte0), new PotionEffect(byte0, j, byte1));
            }

        }
    }

    public boolean isEntityAlive()
    {
        return !isDead && health > 0;
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    public void onLivingUpdate()
    {
        if(field_39004_d > 0)
        {
            field_39004_d--;
        }
        if(newPosRotationIncrements > 0)
        {
            double d = posX + (newPosX - posX) / (double)newPosRotationIncrements;
            double d1 = posY + (newPosY - posY) / (double)newPosRotationIncrements;
            double d2 = posZ + (newPosZ - posZ) / (double)newPosRotationIncrements;
            double d3;
            for(d3 = newRotationYaw - (double)rotationYaw; d3 < -180D; d3 += 360D) { }
            for(; d3 >= 180D; d3 -= 360D) { }
            rotationYaw += d3 / (double)newPosRotationIncrements;
            rotationPitch += (newRotationPitch - (double)rotationPitch) / (double)newPosRotationIncrements;
            newPosRotationIncrements--;
            setPosition(d, d1, d2);
            setRotation(rotationYaw, rotationPitch);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.contract(0.03125D, 0.0D, 0.03125D));
            if(list1.size() > 0)
            {
                double d4 = 0.0D;
                for(int j = 0; j < list1.size(); j++)
                {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB)list1.get(j);
                    if(axisalignedbb.maxY > d4)
                    {
                        d4 = axisalignedbb.maxY;
                    }
                }

                d1 += d4 - boundingBox.minY;
                setPosition(d, d1, d2);
            }
        }
        Profiler.startSection("ai");
        if(isMovementBlocked())
        {
            isJumping = false;
            moveStrafing = 0.0F;
            moveForward = 0.0F;
            randomYawVelocity = 0.0F;
        } else
        if(!isMultiplayerEntity)
        {
            updateEntityActionState();
        }
        Profiler.endSection();
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        if(isJumping)
        {
            if(flag)
            {
                motionY += 0.039999999105930328D;
            } else
            if(flag1)
            {
                motionY += 0.039999999105930328D;
            } else
            if(onGround && field_39004_d == 0)
            {
                jump();
                field_39004_d = 10;
            }
        } else
        {
            field_39004_d = 0;
        }
        moveStrafing *= 0.98F;
        moveForward *= 0.98F;
        randomYawVelocity *= 0.9F;
        float f = field_35194_aj;
        field_35194_aj *= func_35178_D();
        moveEntityWithHeading(moveStrafing, moveForward);
        field_35194_aj = f;
        Profiler.startSection("push");
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if(list != null && list.size() > 0)
        {
            for(int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
                if(entity.canBePushed())
                {
                    entity.applyEntityCollision(this);
                }
            }

        }
        Profiler.endSection();
    }

    protected boolean isMovementBlocked()
    {
        return health <= 0;
    }

    public boolean func_35180_G()
    {
        return false;
    }

    protected void jump()
    {
        motionY = 0.41999998688697815D;
        if(func_35184_a(Potion.jump))
        {
            motionY += (float)(func_35187_b(Potion.jump).getAmplifier() + 1) * 0.1F;
        }
        if(getSprinting())
        {
            float f = rotationYaw * 0.01745329F;
            motionX -= MathHelper.sin(f) * 0.2F;
            motionZ += MathHelper.cos(f) * 0.2F;
        }
        isAirBorne = true;
    }

    protected boolean canDespawn()
    {
        return true;
    }

    protected void despawnEntity()
    {
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1D);
        if(entityplayer != null)
        {
            double d = ((Entity) (entityplayer)).posX - posX;
            double d1 = ((Entity) (entityplayer)).posY - posY;
            double d2 = ((Entity) (entityplayer)).posZ - posZ;
            double d3 = d * d + d1 * d1 + d2 * d2;
            if(canDespawn() && d3 > 16384D)
            {
                setEntityDead();
            }
            if(entityAge > 600 && rand.nextInt(800) == 0 && d3 > 1024D && canDespawn())
            {
                setEntityDead();
            } else
            if(d3 < 1024D)
            {
                entityAge = 0;
            }
        }
    }

    protected void updateEntityActionState()
    {
        entityAge++;
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1D);
        despawnEntity();
        moveStrafing = 0.0F;
        moveForward = 0.0F;
        float f = 8F;
        if(rand.nextFloat() < 0.02F)
        {
            EntityPlayer entityplayer1 = worldObj.getClosestPlayerToEntity(this, f);
            if(entityplayer1 != null)
            {
                currentTarget = entityplayer1;
                numTicksToChaseTarget = 10 + rand.nextInt(20);
            } else
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
        }
        if(currentTarget != null)
        {
            faceEntity(currentTarget, 10F, getVerticalFaceSpeed());
            if(numTicksToChaseTarget-- <= 0 || currentTarget.isDead || currentTarget.getDistanceSqToEntity(this) > (double)(f * f))
            {
                currentTarget = null;
            }
        } else
        {
            if(rand.nextFloat() < 0.05F)
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
            rotationYaw += randomYawVelocity;
            rotationPitch = defaultPitch;
        }
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        if(flag || flag1)
        {
            isJumping = rand.nextFloat() < 0.8F;
        }
    }

    protected int getVerticalFaceSpeed()
    {
        return 40;
    }

    public void faceEntity(Entity entity, float f, float f1)
    {
        double d = entity.posX - posX;
        double d2 = entity.posZ - posZ;
        double d1;
        if(entity instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)entity;
            d1 = (posY + (double)getEyeHeight()) - (entityliving.posY + (double)entityliving.getEyeHeight());
        } else
        {
            d1 = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2D - (posY + (double)getEyeHeight());
        }
        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f2 = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
        float f3 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
        rotationPitch = -updateRotation(rotationPitch, f3, f1);
        rotationYaw = updateRotation(rotationYaw, f2, f);
    }

    public boolean hasCurrentTarget()
    {
        return currentTarget != null;
    }

    public Entity getCurrentTarget()
    {
        return currentTarget;
    }

    private float updateRotation(float f, float f1, float f2)
    {
        float f3;
        for(f3 = f1 - f; f3 < -180F; f3 += 360F) { }
        for(; f3 >= 180F; f3 -= 360F) { }
        if(f3 > f2)
        {
            f3 = f2;
        }
        if(f3 < -f2)
        {
            f3 = -f2;
        }
        return f + f3;
    }

    public void onEntityDeath()
    {
    }

    public boolean getCanSpawnHere()
    {
        return worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.getIsAnyLiquid(boundingBox);
    }

    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4);
    }

    public Vec3D getLookVec()
    {
        return getLook(1.0F);
    }

    public Vec3D getLook(float f)
    {
        if(f == 1.0F)
        {
            float f1 = MathHelper.cos(-rotationYaw * 0.01745329F - 3.141593F);
            float f3 = MathHelper.sin(-rotationYaw * 0.01745329F - 3.141593F);
            float f5 = -MathHelper.cos(-rotationPitch * 0.01745329F);
            float f7 = MathHelper.sin(-rotationPitch * 0.01745329F);
            return Vec3D.createVector(f3 * f5, f7, f1 * f5);
        } else
        {
            float f2 = prevRotationPitch + (rotationPitch - prevRotationPitch) * f;
            float f4 = prevRotationYaw + (rotationYaw - prevRotationYaw) * f;
            float f6 = MathHelper.cos(-f4 * 0.01745329F - 3.141593F);
            float f8 = MathHelper.sin(-f4 * 0.01745329F - 3.141593F);
            float f9 = -MathHelper.cos(-f2 * 0.01745329F);
            float f10 = MathHelper.sin(-f2 * 0.01745329F);
            return Vec3D.createVector(f8 * f9, f10, f6 * f9);
        }
    }

    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    public boolean isPlayerSleeping()
    {
        return false;
    }

    protected void func_35186_aj()
    {
        Iterator iterator = field_35191_aF.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)field_35191_aF.get(integer);
            if(!potioneffect.onUpdate(this) && !worldObj.singleplayerWorld)
            {
                iterator.remove();
                func_35185_c(potioneffect);
            }
        } while(true);
        if(field_39002_b)
        {
            if(!worldObj.singleplayerWorld)
            {
                if(!field_35191_aF.isEmpty())
                {
                    int i = PotionHelper.func_40553_a(field_35191_aF.values());
                    dataWatcher.updateObject(8, Integer.valueOf(i));
                } else
                {
                    dataWatcher.updateObject(8, Integer.valueOf(0));
                }
            }
            field_39002_b = false;
        }
        if(rand.nextBoolean())
        {
            int j = dataWatcher.getWatchableObjectInt(8);
            if(j > 0)
            {
                double d = (double)(j >> 16 & 0xff) / 255D;
                double d1 = (double)(j >> 8 & 0xff) / 255D;
                double d2 = (double)(j >> 0 & 0xff) / 255D;
                worldObj.spawnParticle("mobSpell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, d, d1, d2);
            }
        }
    }

    public void func_40089_ar()
    {
        Iterator iterator = field_35191_aF.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
            {
                break;
            }
            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)field_35191_aF.get(integer);
            if(!worldObj.singleplayerWorld)
            {
                iterator.remove();
                func_35185_c(potioneffect);
            }
        } while(true);
    }

    public Collection func_35183_ak()
    {
        return field_35191_aF.values();
    }

    public boolean func_35184_a(Potion potion)
    {
        return field_35191_aF.containsKey(Integer.valueOf(potion.potionId));
    }

    public PotionEffect func_35187_b(Potion potion)
    {
        return (PotionEffect)field_35191_aF.get(Integer.valueOf(potion.potionId));
    }

    public void func_35182_d(PotionEffect potioneffect)
    {
        if(!func_40096_a(potioneffect))
        {
            return;
        }
        if(field_35191_aF.containsKey(Integer.valueOf(potioneffect.getPotionID())))
        {
            ((PotionEffect)field_35191_aF.get(Integer.valueOf(potioneffect.getPotionID()))).combine(potioneffect);
            func_35179_b((PotionEffect)field_35191_aF.get(Integer.valueOf(potioneffect.getPotionID())));
        } else
        {
            field_35191_aF.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
            func_35181_a(potioneffect);
        }
    }

    public boolean func_40096_a(PotionEffect potioneffect)
    {
        if(func_40093_t() == EnumCreatureAttribute.UNDEAD)
        {
            int i = potioneffect.getPotionID();
            if(i == Potion.regenerationPotion.potionId || i == Potion.poisonPotion.potionId)
            {
                return false;
            }
        }
        return true;
    }

    public boolean func_40100_at()
    {
        return func_40093_t() == EnumCreatureAttribute.UNDEAD;
    }

    protected void func_35181_a(PotionEffect potioneffect)
    {
        field_39002_b = true;
    }

    protected void func_35179_b(PotionEffect potioneffect)
    {
        field_39002_b = true;
    }

    protected void func_35185_c(PotionEffect potioneffect)
    {
        field_39002_b = true;
    }

    protected float func_35178_D()
    {
        float f = 1.0F;
        if(func_35184_a(Potion.moveSpeed))
        {
            f *= 1.0F + 0.2F * (float)(func_35187_b(Potion.moveSpeed).getAmplifier() + 1);
        }
        if(func_35184_a(Potion.moveSlowdown))
        {
            f *= 1.0F - 0.15F * (float)(func_35187_b(Potion.moveSlowdown).getAmplifier() + 1);
        }
        return f;
    }

    public void func_40098_a_(double d, double d1, double d2)
    {
        setLocationAndAngles(d, d1, d2, rotationYaw, rotationPitch);
    }

    public boolean func_40104_l()
    {
        return false;
    }

    public EnumCreatureAttribute func_40093_t()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public void func_41030_c(ItemStack itemstack)
    {
        worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);
        for(int i = 0; i < 5; i++)
        {
            Vec3D vec3d = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
            vec3d.rotateAroundX((-rotationPitch * 3.141593F) / 180F);
            vec3d.rotateAroundY((-rotationYaw * 3.141593F) / 180F);
            Vec3D vec3d1 = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
            vec3d1.rotateAroundX((-rotationPitch * 3.141593F) / 180F);
            vec3d1.rotateAroundY((-rotationYaw * 3.141593F) / 180F);
            vec3d1 = vec3d1.addVector(posX, posY + (double)getEyeHeight(), posZ);
            worldObj.spawnParticle((new StringBuilder()).append("iconcrack_").append(itemstack.getItem().shiftedIndex).toString(), vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.050000000000000003D, vec3d.zCoord);
        }

    }
}
