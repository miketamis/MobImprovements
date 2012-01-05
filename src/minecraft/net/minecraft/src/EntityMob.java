 

package net.minecraft.src;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;



public abstract class EntityMob extends EntityCreature
    implements IMob
{

    protected int attackStrength;
    //mike add
    public Map<Entity, Integer> Threat = new HashMap<Entity, Integer>();
    public Map<Double, Integer> toadd = new HashMap<Double, Integer>();
	private int checkenemystillthere;
//mike end
    public EntityMob(World world)
    {
        super(world);
        attackStrength = 2;
        field_35171_bJ = 5;
    }

    public void onLivingUpdate()
    {
        float f = getEntityBrightness(1.0F);
        if(f > 0.5F)
        {
            entityAge += 2;
        }
        super.onLivingUpdate();
    }

    public void onUpdate()
    {
    	
    	
        super.onUpdate();
        
        //mike add
     
    	  for(Entry<Double, Integer> entry : toadd.entrySet())
          {
    		  EntityLiving EL = (EntityLiving) getMobWithID(entry.getKey());
              if( EL != null)
              {
              	
           	Threat.put(EL, entry.getValue());
              attackhighestthreat();
              }
          }
          	
       	
          
      
        
        
        if(entityToAttack != null&&checkenemystillthere < 0)       
        {
        if(! canEntityBeSeen(entityToAttack))
        {
        	int numb;
        	Object t = Threat.remove(entityToAttack);
        	if(t != null)
        	{
     
        		numb = (Integer)t - 1;
        		
        	if(numb > 0)
        	Threat.put(entityToAttack, numb);
        	System.out.println(numb);
        	}
        	
        	Map.Entry<Entity, Integer> topthreat = null;
        	
        	if(Threat.size() != 0)
        	{
        		attackhighestthreat();
             
        }
        else
        {
        	System.out.println("empty");
        	entityToAttack = null;
        }
      
        }
        
        checkenemystillthere = 30;
        }
        else
        {
        	checkenemystillthere --;	
        }
        
        //mike end
        
        if(!worldObj.multiplayerWorld && worldObj.difficultySetting == 0)
        {
            setEntityDead();
        }
    }

    protected Entity findPlayerToAttack()
    {
        EntityPlayer entityplayer = worldObj.getClosestVulnerablePlayerToEntity(this, 16D);
        if(entityplayer != null && canEntityBeSeen(entityplayer))
        {
        	//mike add
        	 int i = entityplayer.inventory.getTotalArmorValue();
        	
         	if(!Threat.containsKey(entityplayer))
        	  Threat.put(entityplayer, i);
 
        	 //mike end
            return entityplayer;
          
        } else
        {
            return null;
        }
    }

    public boolean attackEntityFrom(DamageSource damagesource, int i)
    {
        if(super.attackEntityFrom(damagesource, i))
        {
            Entity entity = damagesource.getEntity();
            if(riddenByEntity == entity || ridingEntity == entity)
            {
                return true;
            }
            if(entity != this)
            {
            	
            	//mike add
            	int numb;
            	Object t = Threat.remove(entity);
            	if(t == null)
            	{
            	numb = i;
            		
            	}
            	else
            	{
            		numb = (Integer)t + i;
            		
            	}
            	Threat.put(entity, numb);
            	((EntityLiving) entity).getID();
            	System.out.println(numb);
            	attackhighestthreat();
            	
            	//mike end
            }
            return true;
        } else
        {
            return false;
        }
    }
//mike add
    public void attackhighestthreat()
    {
     	
    	Map.Entry<Entity, Integer> topthreat = null;
    	
    	
   for(Map.Entry<Entity, Integer> entry : Threat.entrySet())
   {
	   if(topthreat == null||  entry.getValue().compareTo(topthreat.getValue()) > 0)
		   topthreat = entry;
	   
   }
        entityToAttack = topthreat.getKey();
    }
    protected boolean attackEntityAsMob(Entity entity)
    {
        int i = attackStrength;
        if(isPotionActive(Potion.potionDamageBoost))
        {
            i += 3 << getActivePotionEffect(Potion.potionDamageBoost).getAmplifier();
        }
        if(isPotionActive(Potion.potionWeakness))
        {
            i -= 2 << getActivePotionEffect(Potion.potionWeakness).getAmplifier();
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }
//mike end
    protected void attackEntity(Entity entity, float f)
    {
        if(attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            attackEntityAsMob(entity);
        }
    }

    protected float getBlockPathWeight(int i, int j, int k)
    {
        return 0.5F - worldObj.getLightBrightness(i, j, k);
    }
    NBTTagCompound nbttagcompound1;
	
    public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        //mike add
        NBTTagList nbttaglist = new NBTTagList();
        for(Map.Entry<Entity, Integer> entry : Threat.entrySet())
        {
        	EntityLiving E = (EntityLiving) entry.getKey();
        	 nbttagcompound1 = new NBTTagCompound();
             nbttagcompound1.setDouble("Id", E.getID());
             nbttagcompound1.setInteger("threat", entry.getValue());
             nbttaglist.setTag(nbttagcompound1);
     	   
        }
        nbttagcompound.setTag("ThreatList",nbttaglist);
        //mike end
        
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        //mike add
        NBTTagList nbttaglist = nbttagcompound.getTagList("ThreatList");
        for(int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
           double e  = nbttagcompound1.getDouble("Id");
           
            int threat = nbttagcompound1.getInteger("threat");
            EntityLiving EL = (EntityLiving) getMobWithID(e);
            if( EL != null)
            {
            	
         	Threat.put(EL, threat);
            attackhighestthreat();
            }
            else
            {
            	toadd.put(e,threat);
            }
         	System.out.println(threat + " : "+e);
        	//mike end
           
        }

    }

    protected boolean func_40147_Y()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        if(worldObj.getSavedLightValue(EnumSkyBlock.Sky, i, j, k) > rand.nextInt(32))
        {
            return false;
        }
        int l = worldObj.getBlockLightValue(i, j, k);
        if(worldObj.getIsThundering())
        {
            int i1 = worldObj.skylightSubtracted;
            worldObj.skylightSubtracted = 10;
            l = worldObj.getBlockLightValue(i, j, k);
            worldObj.skylightSubtracted = i1;
        }
        return l <= rand.nextInt(8);
    }

    public boolean getCanSpawnHere()
    {
        return func_40147_Y() && super.getCanSpawnHere();
    }
}
