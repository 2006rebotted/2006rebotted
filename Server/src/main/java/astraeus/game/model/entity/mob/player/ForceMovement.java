package astraeus.game.model.entity.mob.player;

import astraeus.game.model.Direction;
import astraeus.game.model.Position;

/*
  Copyright (c) 2010-2011 Graham Edgecombe
  Copyright (c) 2011-2016 Major <major.emrs@gmail.com> and other apollo contributors
  
  Permission to use, copy, modify, and/or distribute this software for any
  purpose with or without fee is hereby granted, provided that the above
  copyright notice and this permission notice appear in all copies.

  THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
  WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
  ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
  WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
  ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
   OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

/**
 * The Force Movement {@link main.astraeus.game.model.entity.mob.update.UpdateBlock}. Only players can utilise
 * this block.
 * <p>
 * Note: This block is used to force a player to walk to a set location. The
 * player can then perform an action (e.g. an animation), as used in the Agility
 * skill, hence this block earning the name 'Asynchronous Animation/Walking',
 * although the action is not restricted to animations.
 *
 * @author Major | modified by Seven
 */
public final class ForceMovement {
      
	 /**
   * The initial {@link Position} of the player.
   */
  private final Position startLocation; 
	
	/**
	 * The {@link Position} the player is being moved to.
	 */
	private final Position endLocation;

	/**
	 * The length of time (in game pulses) the player's movement along the
	 * X-axis will last.
	 */
	private final int durationX;

	/**
	 * The length of time (in game pulses) the player's movement along the
	 * Y-axis will last.
	 */
	private final int durationY;	
	
	 /**
   * The direction the player is moving.
   */
  private final Direction direction;

	/**
	 * Creates a new force movement block.
	 *
	 * @param initialLocation
	 *            The initial {@link Position} of the player.
	 * @param finalLocation
	 *            The final {@link Position} of the player
	 * @param travelDurationX
	 *            The length of time (in game pulses) the player's movement
	 *            along the X-axis will last.
	 * @param travelDurationY
	 *            The length of time (in game pulses) the player's movement
	 *            along the Y-axis will last.
	 * @param direction
	 *            The direction the player should move.
	 */
	ForceMovement(Position initialLocation, Position finalLocation, int travelDurationX, int travelDurationY, Direction direction) {
		this.startLocation = initialLocation;
		this.endLocation = finalLocation;
		this.durationX = travelDurationX;
		this.durationY = travelDurationY;
		this.direction = direction;
	}
	
  /**
  * Gets the coordinate of the initial {@link Position}.
  *
  * @return The coordinate.
  */
  public Position getStartLocation() {
        return startLocation;
  }
  
  /**
   * Gets the X coordinate of the initial {@link Position}.
   *
   * @return The X coordinate.
   */
  public int getStartX() {
    return startLocation.getX();
  }
	
  /**
   * Gets the Y coordinate of the initial {@link Position}.
   *
   * @return The Y coordinate.
   */
  public int getStartY() {
    return startLocation.getY();
  }
  
  /**
  * Gets the coordinate of the final {@link Position}.
  *
  * @return The coordinate.
  */
	public Position getEndLocation() {
	      return endLocation;
	}
	
	/**
	 * Gets the X coordinate of the final {@link Position}.
	 *
	 * @return The X coordinate.
	 */
	public int getEndX() {
		return endLocation.getX();
	}

	/**
	 * Gets the Y coordinate of the final {@link Position}.
	 *
	 * @return The Y coordinate.
	 */
	public int getEndY() {
		return endLocation.getY();
	}
	
	/**
	 * Gets the length of time (in game pulses) the player's movement along the
	 * X-axis will last.
	 *
	 * @return The time period.
	 */
	public int getDurationX() {
		return durationX;
	}

	/**
	 * Gets the length of time (in game pulses) the player's movement along the
	 * Y-axis will last.
	 *
	 * @return The time period.
	 */
	public int getDurationY() {
		return durationY;
	}
	
	 /**
   * Gets the {@link Direction} the player should move.
   *
   * @return The direction.
   */
  public Direction getDirection() {
    return direction;
  }

}