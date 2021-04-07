package astraeus.game.model.entity.object;

import com.google.gson.annotations.SerializedName;

import java.util.Optional;

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
 * The type of an in-game object.
 *
 * @author Seven | Apollo Development team.
 */
public enum GameObjectType {
	@SerializedName("STRAIGHT")
	STRAIGHT(0, ObjectGroup.WALL),
	TRIANGULAR_CORNER(1, ObjectGroup.WALL),
	CORNER(2, ObjectGroup.WALL),
	RECTANGULAR_CORNER(3, ObjectGroup.WALL),
	STRAIGHT_INSIDE(4, ObjectGroup.WALL_DECORATION),
	STRAIGHT_OUTSIDE(5, ObjectGroup.WALL_DECORATION),
	DIAGONAL_OUTSIDE(6, ObjectGroup.WALL_DECORATION),
	DIAGONAL_INSIDE(7, ObjectGroup.WALL_DECORATION),
	DIAGONAL_IN(8, ObjectGroup.WALL_DECORATION),
	@SerializedName("DIAGONAL")
	DIAGONAL(9, ObjectGroup.INTERACTABLE),
	INTERACTABLE(10, ObjectGroup.INTERACTABLE),
	GROUND_OBJECT(11, ObjectGroup.INTERACTABLE),
	STRAIGHT_SLOPED(12, ObjectGroup.ROOF),
	DIAGONAL_SLOPED(13, ObjectGroup.ROOF),
	DIAGONAL_SLOPED_CONNECTING(14, ObjectGroup.ROOF),
	STRAIGHT_SLOPED_CORNER_CONNECTING(15, ObjectGroup.ROOF),
	STRAIGHT_SLOPED_CORNER(16, ObjectGroup.ROOF),
	STRAIGHT_FLAT(17, ObjectGroup.ROOF),
	STRAIGHT_BOTTOM_EDGE(18, ObjectGroup.ROOF),
	DIAGONAL_BOTTOM_EDGE_CONNECTING(19, ObjectGroup.ROOF),
	STRAIGHT_BOTTOM_EDGE_CONNECTING(20, ObjectGroup.ROOF),
	STRAIGHT_BOTTOM_EDGE_CONNECTING_CORNER(21, ObjectGroup.ROOF),
	GROUND_DECORATION(22, ObjectGroup.GROUND_DECORATION);

	/**
	 * The integer value of this ObjectType.
	 */
	private final int value;

	private final ObjectGroup group;

	/**
	 * Creates the ObjectType.
	 *
	 * @param value
	 *            The integer value of this ObjectType.
	 */
	GameObjectType(int value, ObjectGroup group) {
		this.value = value;
		this.group = group;
	}

	/**
	 * Gets a {@link Optional} of a {@link GameObjectType} by a specified {@code value}.
	 *
	 * @param value
	 * 		The packed value.
	 */
	public static Optional<GameObjectType> lookup(int value) {
		for(GameObjectType type : GameObjectType.values()) {
			if (type.getValue() == value) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets the integer value of this ObjectType.
	 *
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets the group this type belongs to.
	 *
	 * @return The group.
	 */
	public ObjectGroup getGroup() {
		return group;
	}

}
