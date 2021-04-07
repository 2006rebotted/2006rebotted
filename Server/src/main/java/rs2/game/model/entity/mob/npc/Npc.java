package rs2.game.model.entity.mob.npc;

import rs2.game.GameConstants;
import rs2.game.model.Direction;
import rs2.game.model.Position;
import rs2.game.model.entity.EntityType;
import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.dmg.Hit;
import rs2.game.model.entity.mob.combat.type.CombatType;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.game.model.location.Area;
import rs2.util.Stopwatch;

import java.util.Objects;

public class Npc extends Mob {

      private Direction facingDirection = Direction.SOUTH;

      private Position createdLocation;

      private int maximumHealth;

      private int currentHealth;

      private boolean randomWalk;

      private int respawnTimer;

      private final Stopwatch randomWalkTimer = new Stopwatch();

      private boolean following;

      private Area walkingArea;

      private Mob owner;

      private boolean lockFollow;

      public Npc(int id) {
            super(GameConstants.DEFAULT_LOCATION);
            setId(id);
            size = NpcDefinition.get(id).getSize();
      }
      
      @Override
      public void onDeath() {
            
      }

      @Override
      public void onMovement() {

      }

      @Override
      public int size() {            
            return NpcDefinition.get(getId()).getSize();
      }

      @Override
      public void postUpdate() {
            getUpdateFlags().clear();
            getAnimations().clear();
            getGraphics().clear();
      }

      @Override
      public void decrementHealth(int HP) {
            this.currentHealth -= HP;

            if (currentHealth <= 0) {
                  currentHealth = 100;
                  // setDead(true);
                  return;
            }
      }

      /**
       * @return the createdLocation
       */
      public Position getCreatedLocation() {
            return createdLocation;
      }

      @Override
      public int getCurrentHealth() {
            return currentHealth;
      }

      public Direction getFacingDirection() {
            return facingDirection;
      }

      @Override
      public int getMaximumHealth() {
            return maximumHealth;
      }

      public String getName() {
            return NpcDefinition.getDefinitions()[getId()].getName();
      }

      public String getName(int npcId) {
            if (NpcDefinition.getDefinitions()[npcId] == null || npcId < 0
                        || npcId >= NpcDefinition.MOB_LIMIT) {
                  return "None";
            }
            return NpcDefinition.getDefinitions()[npcId].getName();
      }

      public int getRespawnTimer() {
            return respawnTimer;
      }

      /**
       * @return the following
       */
      public boolean isFollowing() {
            return following;
      }

      public boolean isRandomWalk() {
            return randomWalk;
      }

      @Override
      public void preUpdate() {
    	  
            movement.handleEntityMovement();
            
            tick();
            
			if (!isRandomWalk() && getInteractingEntity() == null && tick % 5 == 4) {
				Npcs.resetFacingDirection(this);
			}

			if (!isDead() && getInteractingEntity() == null) {
				resetEntityInteraction();
			}

			if (isRandomWalk() && getInteractingEntity() == null && tick % 5 == 4) {
				Npcs.handleRandomWalk(this);
			}
      }

      /**
       * @param createdLocation the createdLocation to set
       */
      public void setCreatedLocation(Position createdLocation) {
            this.createdLocation = createdLocation;
      }

      public void setCurrentHealth(int HP) {
            this.currentHealth = HP;
      }

      public void setFacingDirection(Direction facingDirection) {
            this.facingDirection = facingDirection;
            getUpdateFlags().add(UpdateFlag.FACE_COORDINATE);
      }

      /**
       * @param following the following to set
       */
      public void setFollowing(boolean following) {
            this.following = following;
      }

      public void setMaximumHealth(int maximumHealth) {
            this.maximumHealth = maximumHealth;
      }

      public void setRandomWalk(boolean randomWalk) {
            this.randomWalk = randomWalk;
      }

      public void setRespawnTimer(int respawnTimer) {
            this.respawnTimer = respawnTimer;
      }

      public void setLockFollow(boolean lockFollow) {
            this.lockFollow = lockFollow;
      }

      @Override
      public int hashCode() {
            return Objects.hash(getId(), getSlot());
      }

      @Override
      public boolean equals(Object obj) {
            if (obj == null) {
                  return false;
            }

            if (obj instanceof Npc) {
                  Npc other = (Npc) obj;
                  return other.hashCode() == hashCode();
            }

            return false;
      }

      /**
       * The mob should walk to home
       * 
       * @return If the mob can walk to home or not
       */
      public boolean isWalkToHome() {
            if (Area.inWilderness(this)) {
                  return Math.abs(getPosition().getX() - createdLocation.getX()) + Math.abs(
                              getPosition().getY() - createdLocation.getY()) > getSize() * 1 + 2;
            }

            if (isNpc()) { // TODO: isAttackable
                  return Math.abs(getPosition().getX() - createdLocation.getX()) + Math.abs(
                              getPosition().getY() - createdLocation.getY()) > getSize() * 2 + 6;
            }

            return Position.getManhattanDistance(createdLocation, getPosition()) > 2;
      }

      public Mob getOwner() {
            return owner;
      }

      public boolean isLockFollow() {
            return lockFollow;
      }

      /**
       * @return the walkingArea
       */
      public Area getWalkingArea() {
            return walkingArea;
      }

      /**
       * @param walkingArea the walkingArea to set
       */
      public void setWalkingArea(Area walkingArea) {
            this.walkingArea = walkingArea;
      }

      /**
       * @return the randomWalkTimer
       */
      public Stopwatch getRandomWalkTimer() {
            return randomWalkTimer;
      }

      @Override
      public int getHashCode() {
            return Objects.hash(getId(), getSlot());
      }

      public String toString() {
            return String.format("[MOB] - [name= %s] [id= %d] [slot= %d] [location= %s]", getName(), getId(), getSlot(), getPosition().toString());
      }

	@Override
	public EntityType type() {
		return EntityType.NPC;
	}

	@Override
	public void hit(Mob attacker, Hit hit) {

	}

	@Override
	public void onTick() {
		
	}

	@Override
	public void onDamage(Mob attacker, Hit hit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canAttack(Mob defender, CombatType type) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void buildAttack(CombatType type) {
		// TODO Auto-generated method stub
		
	}

}
