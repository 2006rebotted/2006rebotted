package rs2.game.event.impl;

import rs2.game.event.Event;

/**
 * An event which manages button actions.
 *
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 */
public final class ButtonActionEvent implements Event {

	/**
	 * The id of the button.
	 */
	private final int button;	

	/**
	 * Constructs a new {@link ButtonActionEvent} with the specified button id.
	 *
	 * @param button The buttons id.
	 */
	public ButtonActionEvent(int button) {		
		this.button = button;
	}

	/**
	 * Returns the buttons id.
	 */
	public int getButton() {
		return button;
	}

}