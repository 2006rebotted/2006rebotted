package astraeus.game.model.entity.mob.player.skill.impl;

import java.util.Optional;

import com.google.common.collect.ImmutableList;

import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.combat.def.SpellDefinition;
import astraeus.game.model.entity.mob.player.Player;

public class MagicSkill {

	@SuppressWarnings("unused")
	private final Player player;

	public MagicSkill(Player player) {
		this.player = player;
	}

	public enum SpellBookTypes {
		MODERN,
		ANCIENTS,
		LUNARS;
	}

	public enum TeleportTypes {
		SPELL_BOOK,
		MODERN,
		LUNAR,
		ANCIENT,
		TABLET,
		TELEOTHER,
		OBELISK,
		LEVER;
	}

	/**
	 * Current player spell book type.
	 */
	private SpellBookTypes spellBookType = SpellBookTypes.MODERN;
	
	private Optional<SpellDefinition> castingSpell = Optional.empty();

	private boolean isTeleporting = false;

	private boolean vengeanceActive = false;

	private long lastVengeance = 0L;

	public final static ImmutableList<Integer> SAFE_SPELLS = ImmutableList.of(30298);

	public boolean canTeleport(boolean override) {
		return false;
	}

	public boolean handleButtons(int button) {
		return false;
	}

	public void teleport(final Position position, TeleportTypes teleportType, boolean override) {

	}

	public void teleport(final Position position) {

	}

	public boolean isTeleporting() {
		return isTeleporting;
	}

	public boolean isVengeanceActive() {
		return vengeanceActive;
	}

	public long getLastVengeance() {
		return lastVengeance;
	}

	public SpellBookTypes getSpellBookType() {
		return spellBookType;
	}

	public void setLastVengeance(long lastVengeance) {
		this.lastVengeance = lastVengeance;
	}

	public void setSpellBookType(SpellBookTypes spellBookType) {
		this.spellBookType = spellBookType;
	}

	public void setTeleporting(boolean isTeleporting) {
		this.isTeleporting = isTeleporting;
	}

	public void setVengeanceActive(boolean vengeanceActive) {
		this.vengeanceActive = vengeanceActive;
	}

	public Optional<SpellDefinition> getCastingSpell() {
		return castingSpell;
	}

	public void setCastingSpell(Optional<SpellDefinition> castingSpell) {
		this.castingSpell = castingSpell;
	}
	
}
