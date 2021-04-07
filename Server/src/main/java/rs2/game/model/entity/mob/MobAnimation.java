package rs2.game.model.entity.mob;

public final class MobAnimation {

	public static final int PLAYER_STAND = 808;
	public static final int PLAYER_TURN = 823;
	public static final int PLAYER_WALK = 819;
	public static final int PLAYER_TURN_180 = 820;
	public static final int PLAYER_TURN_90_CW = 821;
	public static final int PLAYER_TURN_90_CCW = 822;
	public static final int PLAYER_RUN = 824;
	public static final int PLAYER_ATTACK = 422;
	public static final int PLAYER_BLOCK = 424;
	public static final int PLAYER_DEATH = 836;

	private int stand;
	private int turn;
	private int walk;
	private int turn180;
	private int turn90CW;
	private int turn90CCW;
	private int run;
	private int attack;
	private int block;
	private int death;
	
	public int getStand() {
		return stand;
	}

	public int getTurn() {
		return turn;
	}

	public int getWalk() {
		return walk;
	}

	public int getTurn180() {
		return turn180;
	}

	public int getTurn90CW() {
		return turn90CW;
	}

	public int getTurn90CCW() {
		return turn90CCW;
	}

	public int getRun() {
		return run;
	}

	public int getAttack() {
		return attack;
	}

	public int getBlock() {
		return block;
	}

	public int getDeath() {
		return death;
	}

	public void setStand(int stand) {
		this.stand = stand;
	}

	public void setTurn(int turn) {
		this.turn = turn;
	}

	public void setWalk(int walk) {
		this.walk = walk;
	}

	public void setTurn180(int turn180) {
		this.turn180 = turn180;
	}

	public void setTurn90CW(int turn90cw) {
		turn90CW = turn90cw;
	}

	public void setTurn90CCW(int turn90ccw) {
		turn90CCW = turn90ccw;
	}

	public void setRun(int run) {
		this.run = run;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setBlock(int block) {
		this.block = block;
	}

	public void setDeath(int death) {
		this.death = death;
	}
	
}