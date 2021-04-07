package astraeus.game.model.widget.dialog;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.NpcDefinition;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.out.DisplayNpcHeadModelOnWidgetPacket;
import astraeus.net.packet.out.DisplayPlayerHeadModelOnWidgetPacket;
import astraeus.net.packet.out.RemoveWidgetPacket;
import astraeus.net.packet.out.SetWidgetAnimationPacket;
import astraeus.net.packet.out.SetWidgetStringPacket;
import astraeus.util.LoggerUtils;
import astraeus.util.StringUtils;

/**
 * Represents a factory class that contains important functions for building dialogues.
 * 
 * @author Vult-R <https://github.com/Vult-R>
 */
public final class DialogueFactory {
	
	/**
	 * The single logger for this class.
	 */
	private static final Logger logger = LoggerUtils.getLogger(DialogueFactory.class);	

	/**
	 * The queue of dialogues in this factory.
	 */
	private final Queue<Chainable> chain = new ArrayDeque<>();	

	/**
	 * The maximum length of a single line of dialogue.
	 */
	private static final int MAXIMUM_LENGTH = 100;

	/**
	 * The player who owns this factory.
	 */
	private final Player player;
	
	/**
	 * The flag that denotes dialogue is active.
	 */
	private boolean active;

	/**
	 * The next action in the dialogue chain.
	 */
	private Optional<Runnable> nextAction = Optional.empty();

	/**
	 * Creates a new {@link DialogueFactory}.
	 *
	 * @param player
	 * 		The player who owns this factory.
	 */
	public DialogueFactory(Player player) {
		this.player = player;
	}

	/**
	 * Sends a player a dialogue.
	 *
	 * @param dialogue
	 *            The dialogue to sent.
	 */
	public final DialogueFactory sendDialogue(Dialogue dialogue) {
		player.setDialogue(Optional.of(dialogue));
		dialogue.sendDialogues(this);
		return this;
	}

	/**
	 * Sets an {@code action} so this action can be executed after dialogues are done.
	 *
	 * @param action
	 * 		The action to set.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory onAction(Runnable action) {
		setNextAction(Optional.of(action));
		return this;
	}

	/**
	 * Accepts the next dialogue in the chain.
	 *
	 * @return The instance of this factory.
	 */
	public DialogueFactory onNext() {
		if (getChain().peek() != null) {
			Chainable chain = getChain().poll();

			chain.accept(this);
		} else {
			player.queuePacket(new RemoveWidgetPacket());
		}
		return this;
	}

	/**
	 * Executes an {@code option} for a {@code player}.
	 *
	 * @param type
	 * 		The type of option.
	 *
	 * 	@param option
	 * 		The option to execute.
	 */
	public final void executeOption(int type, Optional<OptionDialogue> option) {
		option.ifPresent($it -> $it.getActions().get(type).run());
		execute();
	}

	/**
	 * Clears the current dialogue {@code chain}.
	 *
	 * @return The instance of this factory.
	 */
	public void clear() {
		chain.clear();
		nextAction = Optional.empty();
		player.setDialogue(Optional.empty());
		player.setOptionDialogue(Optional.empty());
		setActive(false);
	}

	/**
	 * Appends a {@code chain} to this factory.
	 *
	 * @return The instance of this factory.
	 */
	private final DialogueFactory append(Chainable chain) {
		this.chain.add(chain);
		return this;
	}

	/**
	 * Gets the current chain.
	 * 
	 * @return The queue of dialogues.
	 */
	public final Queue<Chainable> getChain() {
		return chain;
	}

	/**
	 * Retrieves the next dialogue in the chain and executes it.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory execute() {
		// check to see if there are anymore dialogues.
		if (getChain().peek() != null) {

			// there is so, grab the next dialogue.
			Chainable entry = getChain().poll();

			// is this an option dialogue?
			if (entry instanceof OptionDialogue) {
				OptionDialogue option = (OptionDialogue) entry;
				player.setOptionDialogue(Optional.of(option));
			}
			setActive(true);
			// whatever dialogue it is, accept it.
			entry.accept(this);
		} else {
			// there are no dialogues in this chain.

				// is there an action?
				if (getNextAction().isPresent()) {
					// there is so, execute it.
					getNextAction().ifPresent($it -> $it.run());
					// we just used this action so empty it so it can't be used again.
					setNextAction(Optional.empty());
					return this;
			}
				setActive(false);
			// there are no more dialogues, so clear the screen.
				player.queuePacket(new RemoveWidgetPacket());
				
				if (player.getInteractingEntity() != null) {
					player.getInteractingEntity().setInteractingEntity(null);					
				}
				
				player.setInteractingEntity(null);
		}
		return this;
	}
	
	/**
	 * Retrieves the next dialogue in the chain and executes it.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory executeNoClose() {
		// check to see if there are anymore dialogues.
		if (getChain().peek() != null) {

			// there is so, grab the next dialogue.
			Chainable entry = getChain().poll();

			// is this an option dialogue?
			if (entry instanceof OptionDialogue) {
				OptionDialogue option = (OptionDialogue) entry;
				player.setOptionDialogue(Optional.of(option));
			}
			setActive(true);
			// whatever dialogue it is, accept it.
			entry.accept(this);
		} else {
			// there are no dialogues in this chain.

				// is there an action?
				if (getNextAction().isPresent()) {
					// there is so, execute it.
					getNextAction().ifPresent($it -> $it.run());
					// we just used this action so empty it so it can't be used again.
					setNextAction(Optional.empty());
					return this;
			}
				setActive(false);
				player.setInteractingEntity(null);
		}
		return this;
	}

	/**
	 * Appends keywords to an existing dialogue text.
	 *
	 * @param line
	 *            The line to check for a keyword.
	 */
	private final String appendKeywords(String line) {
		if (line.contains("#username")) {
			line = line.replaceAll("#username", StringUtils.formatName((player.getUsername())));
		}

		if (player.getInteractingEntity() != null && player.getInteractingEntity() instanceof Npc) {

			final Npc npc = World.world.getMobs().get(player.getInteractingEntity().getSlot());			

			if (line.contains("#name")) {
				line = line.replaceAll("#name", npc.getName());
			}
		}
		return line;
	}

	/**
	 * Appends a {@link PlayerDialogue} to the current dialogue chain.
	 *
	 * @param lines
	 * 		The dialogue of the player talking.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendPlayerChat(String... lines) {
		return append(new PlayerDialogue(lines));
	}

	/**
	 * Appends a {@link PlayerDialogue} to the current dialogue chain.
	 *
	 * @param lines
	 * 		The dialogue of the player talking.
	 *
	 * @param expression
	 * 		The expression of this dialogue.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendPlayerChat(Expression expression, String... lines) {
		return append(new PlayerDialogue(expression, lines));
	}

	/**
	 * Sends a dialogue with a player talking.
	 *
	 * @param dialogue
	 * 		The player dialogue.
	 *
	 * 	@return The instance of this factory.
	 */
	final DialogueFactory sendPlayerChat(PlayerDialogue dialogue) {
		Expression expression = dialogue.getExpression();
		String[] lines = dialogue.getLines();

		validateLength(lines);
		switch (lines.length) {
			case 1:
				player.queuePacket(new SetWidgetAnimationPacket(969, expression.getId()));
				player.queuePacket(new SetWidgetStringPacket(StringUtils.formatName(player.getUsername()), 970));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 971));
				player.queuePacket(new DisplayPlayerHeadModelOnWidgetPacket(969));
				player.getWidgets().openChatBoxWidget(968);
				break;

			case 2:
				player.queuePacket(new SetWidgetAnimationPacket(974, expression.getId()));
				player.queuePacket(new SetWidgetStringPacket(StringUtils.formatName(player.getUsername()), 975));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 976));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 977));
				player.queuePacket(new DisplayPlayerHeadModelOnWidgetPacket(974));
				player.getWidgets().openChatBoxWidget(973);
				break;

			case 3:
				player.queuePacket(new SetWidgetAnimationPacket(980, expression.getId()));
				player.queuePacket(new SetWidgetStringPacket(StringUtils.formatName(player.getUsername()), 981));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 982));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 983));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[2]), 984));
				player.queuePacket(new DisplayPlayerHeadModelOnWidgetPacket(980));
				player.getWidgets().openChatBoxWidget(979);
				break;

			case 4:
				player.queuePacket(new SetWidgetAnimationPacket(987, expression.getId()));
				player.queuePacket(new SetWidgetStringPacket(StringUtils.formatName(player.getUsername()), 988));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 989));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 990));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[2]), 991));
				player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[3]), 992));
				player.queuePacket(new DisplayPlayerHeadModelOnWidgetPacket(987));
				player.getWidgets().openChatBoxWidget(986);
				break;

			default:
				logger.log(Level.SEVERE, String.format("Invalid player dialogue line length: %s", lines.length));
				break;
		}
		return this;
	}

	/**
	 * Appends an npc dialogue.
	 *
	 * @param lines
	 * 		The text of this dialogue.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendNpcChat(String... lines) {
		return append(new NpcDialogue(lines));
	}

	/**
	 * Appends an {@link NpcDialogue} to the current dialogue chain.
	 *
	 * @param expression
	 * 		The expression of this npc.
	 *
	 * @param lines
	 * 		The text of this dialogue.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendNpcChat(Expression expression, String... lines) {
		if (player.getInteractingEntity() != null) {
			int id = player.getInteractingEntity().getId();			
			return append(new NpcDialogue(id, expression, lines));
		}
		return append(new NpcDialogue(expression, lines));
	}

	/**
	 * Appends an {@link NpcDialogue} to the current dialogue chain.
	 *
	 * @param id
	 * 		The id of this npc.
	 *
	 * @param lines
	 * 		The text of this dialogue.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendNpcChat(int id, String... lines) {
		return append(new NpcDialogue(id, Expression.DEFAULT, lines));
	}

	/**
	 * Appends an {@link NpcDialogue} to the current dialogue chain.
	 *
	 * @param id
	 * 		The id of this npc.
	 *
	 * @param expression
	 * 		The expression of this npc.
	 *
	 * @param lines
	 * 		The text of this dialogue.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendNpcChat(int id, Expression expression, String... lines) {
		return append(new NpcDialogue(id, expression, lines));
	}

	/**
	 * Sends a dialogue with a npc talking.
	 *
	 * @param dialogue
	 * 		The dialogue.
	 *
	 * @return The instance of this factory.
	 */
	final DialogueFactory sendNpcChat(NpcDialogue dialogue) {
		Expression expression = dialogue.getExpression();
		String[] lines = dialogue.getLines();
		validateLength(lines);

		int npcId = 0;

		if (dialogue.getId() == -1) {
			if (player.getInteractingEntity() != null) {
				if (player.getInteractingEntity().isMob()) {
					npcId = player.getInteractingEntity().getMob().getId();
				}
			}
		} else {
			npcId = dialogue.getId();
		}

		final NpcDefinition mob = NpcDefinition.get(npcId);
		
		if (mob == null) {
			return this;
		}
		
		switch (lines.length) {
		case 1:
			player.queuePacket(new SetWidgetAnimationPacket(4883, expression.getId()));
			player.queuePacket(new SetWidgetStringPacket(mob.getName(), 4884));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 4885));
			player.queuePacket(new DisplayNpcHeadModelOnWidgetPacket(mob.getId(), 4883));
			player.getWidgets().openChatBoxWidget(4882);
			break;

		case 2:
			player.queuePacket(new SetWidgetAnimationPacket(4888, expression.getId()));
			player.queuePacket(new SetWidgetStringPacket(mob.getName(), 4889));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 4890));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 4891));
			player.queuePacket(new DisplayNpcHeadModelOnWidgetPacket(mob.getId(), 4888));
			player.getWidgets().openChatBoxWidget(4887);
			break;

		case 3:
			player.queuePacket(new SetWidgetAnimationPacket(4894, expression.getId()));
			player.queuePacket(new SetWidgetStringPacket(mob.getName(), 4895));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 4896));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 4897));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[2]), 4898));
			player.queuePacket(new DisplayNpcHeadModelOnWidgetPacket(mob.getId(), 4894));
			player.getWidgets().openChatBoxWidget(4893);
			break;

		case 4:
			player.queuePacket(new SetWidgetAnimationPacket(4901, expression.getId()));
			player.queuePacket(new SetWidgetStringPacket(mob.getName(), 4902));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[0]), 4903));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[1]), 4904));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[2]), 4905));
			player.queuePacket(new SetWidgetStringPacket(appendKeywords(lines[3]), 4906));
			player.queuePacket(new DisplayNpcHeadModelOnWidgetPacket(mob.getId(), 4901));
			player.getWidgets().openChatBoxWidget(4900);
			break;

		default:
			logger.log(Level.SEVERE, String.format("Invalid npc dialogue line length: %s", lines.length));
			break;
		}
		return this;
	}

	/**
	 * Appends the {@link OptionDialogue} onto the current dialogue chain.
	 *
	 * @param option1
	 * 		The text for the first option.
	 *
	 * @param action1
	 * 		The action for the first action.
	 *
	 * @param option2
	 * 		The text for the second option.
	 *
	 * @param action2
	 * 		The action for the second action.
	 */
	public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2) {
		return append(new OptionDialogue(option1, action1, option2, action2));
	}

	/**
	 * Appends the {@link OptionDialogue} onto the current dialogue chain.
	 *
	 * @param option1
	 * 		The text for the first option.
	 *
	 * @param action1
	 * 		The action for the first action.
	 *
	 * @param option2
	 * 		The text for the second option.
	 *
	 * @param action2
	 * 		The action for the second action.
	 *
	 * @param option3
	 * 		The text for the third option.
	 *
	 * @param action3
	 * 		The action for the third action.
	 */
	public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3) {
		return append(new OptionDialogue(option1, action1, option2, action2, option3, action3));
	}

	/**
	 * Appends the {@link OptionDialogue} onto the current dialogue chain.
	 *
	 * @param option1
	 * 		The text for the first option.
	 *
	 * @param action1
	 * 		The action for the first action.
	 *
	 * @param option2
	 * 		The text for the second option.
	 *
	 * @param action2
	 * 		The action for the second action.
	 *
	 * @param option3
	 * 		The text for the third option.
	 *
	 * @param action3
	 * 		The action for the third action.
	 *
	 * @param option4
	 * 		The text for the four option.
	 *
	 * @param action4
	 * 		The action for the four action.
	 */
	public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4) {
		return append(new OptionDialogue(option1, action1, option2, action2, option3, action3, option4, action4));
	}

	/**
	 * Appends the {@link OptionDialogue} onto the current dialogue chain.
	 *
	 * @param option1
	 * 		The text for the first option.
	 *
	 * @param action1
	 * 		The action for the first action.
	 *
	 * @param option2
	 * 		The text for the second option.
	 *
	 * @param action2
	 * 		The action for the second action.
	 *
	 * @param option3
	 * 		The text for the third option.
	 *
	 * @param action3
	 * 		The action for the third action.
	 *
	 * @param option4
	 * 		The text for the four option.
	 *
	 * @param action4
	 * 		The action for the four action.
	 *
	 * @param option5
	 * 		The text for the fifth option.
	 *
	 * @param action5
	 * 		The action for the fifth action.
	 */
	public final DialogueFactory sendOption(String option1, Runnable action1, String option2, Runnable action2, String option3, Runnable action3, String option4, Runnable action4, String option5, Runnable action5) {
		return append(new OptionDialogue(option1, action1, option2, action2, option3, action3, option4, action4, option5, action5));
	}

	/**
	 * Sends a dialogue with options.
	 *
	 * @param dialogue
	 * 		The dialogue.
	 *
	 * @return The instance of this factory.
	 */
	final DialogueFactory sendOption(OptionDialogue dialogue) {
		String[] options = dialogue.getLines();
		validateLength(options);
		switch (options.length) {
		case 2:
			player.queuePacket(new SetWidgetStringPacket("Select an Option", 2460));
			player.queuePacket(new SetWidgetStringPacket(options[0], 2461));
			player.queuePacket(new SetWidgetStringPacket(options[1], 2462));
			player.getWidgets().openChatBoxWidget(2459);
			return this;
			
		case 3:
			player.queuePacket(new SetWidgetStringPacket("Select an Option", 2470));
			player.queuePacket(new SetWidgetStringPacket(options[0], 2471));
			player.queuePacket(new SetWidgetStringPacket(options[1], 2472));
			player.queuePacket(new SetWidgetStringPacket(options[2], 2473));
			player.getWidgets().openChatBoxWidget(2469);
			return this;
			
		case 4:
			player.queuePacket(new SetWidgetStringPacket("Select an Option", 2481));
			player.queuePacket(new SetWidgetStringPacket(options[0], 2482));
			player.queuePacket(new SetWidgetStringPacket(options[1], 2483));
			player.queuePacket(new SetWidgetStringPacket(options[2], 2484));
			player.queuePacket(new SetWidgetStringPacket(options[3], 2485));
			player.getWidgets().openChatBoxWidget(2480);
			return this;
			
		case 5:
			player.queuePacket(new SetWidgetStringPacket("Select an Option", 2493));
			player.queuePacket(new SetWidgetStringPacket(options[0], 2494));
			player.queuePacket(new SetWidgetStringPacket(options[1], 2495));
			player.queuePacket(new SetWidgetStringPacket(options[2], 2496));
			player.queuePacket(new SetWidgetStringPacket(options[3], 2497));
			player.queuePacket(new SetWidgetStringPacket(options[4], 2498));
			player.getWidgets().openChatBoxWidget(2492);
			return this;
		}
		return this;
	}

	/**
	 * Appends a {@link StatementDialogue} to the current dialogue chain.
	 *
	 * @param lines
	 * 		The text for this statement.
	 *
	 * @return The instance of this factory.
	 */
	public final DialogueFactory sendStatement(String... lines) {
		validateLength(lines);
		append(new StatementDialogue(lines));
		return this;
	}

	/**
	 * Sends a player a statement dialogue.
	 *
	 * @param dialogue
	 *            The statement dialogue.
	 */
	final DialogueFactory sendStatement(StatementDialogue dialogue) {
		validateLength(dialogue.getLines());
		switch (dialogue.getLines().length) {		

		case 1:
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[0], 357));
			player.getWidgets().openChatBoxWidget(356);
			break;
		case 2:
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[0], 360));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[1], 361));
			player.getWidgets().openChatBoxWidget(359);
			break;
		case 3:
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[0], 364));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[1], 365));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[2], 366));
			player.getWidgets().openChatBoxWidget(363);
			break;
		case 4:
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[0], 369));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[1], 370));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[2], 371));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[3], 372));
			player.getWidgets().openChatBoxWidget(368);
			break;
		case 5:
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[0], 375));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[1], 376));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[2], 377));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[3], 378));
			player.queuePacket(new SetWidgetStringPacket(dialogue.getLines()[4], 379));
			player.getWidgets().openChatBoxWidget(374);

		default:
			logger.log(Level.SEVERE, String.format("Invalid statement dialogue line length: %s", dialogue.getLines().length));
			break;
		}
		return this;
	}
	
	/**
	 * The method that validates the length of {@code text}.
	 *
	 * @param text
	 *            the text that will be validated.
	 * @throws IllegalStateException
	 *             if any lines of the text exceed a certain length.
	 */
	private final void validateLength(String... text) {
		if (Arrays.stream(text).filter(Objects::nonNull).anyMatch(s -> s.length() > MAXIMUM_LENGTH)) {
			throw new IllegalStateException("Dialogue length too long, maximum length is: " + MAXIMUM_LENGTH);
		}
	}

	/**
	 * The player that owns this factory.
	 *
	 * @return The player.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the {@link Optional} describing the next action in the dialogue chain.
	 *
	 * @return The optional describing the next action.
	 */
	public Optional<Runnable> getNextAction() {
		return nextAction;
	}

	/**
	 * Sets the next action in  the dialogue chain.
	 *
	 * @param nextAction
	 *		The action to set.
	 */
	public void setNextAction(Optional<Runnable> nextAction) {
		this.nextAction = nextAction;
	}

	/**
	 * Determines if dialogues are currently open.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets the flag that indicates dialogues are present.
	 * 
	 * @param active
	 * 		The flag to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}	
	
}

