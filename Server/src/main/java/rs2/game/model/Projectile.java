package rs2.game.model;

public class Projectile {
	private short id;
	private byte size;
	private byte delay;
	private byte duration;
	private byte startHeight;
	private byte endHeight;
	private byte curve;

	public Projectile(int id) {
		this.id = ((short) id);
		size = 1;
		delay = 50;
		duration = 75;
		startHeight = 43;
		endHeight = 31;
		curve = 16;
	}

	public Projectile(int id, boolean ranged) {
		this(id);
		curve = ((byte) (ranged ? 10 : 16));
	}

	public Projectile(int id, int size, int delay, int duration, int startHeight, int endHeight, int curve) {
		this.id = ((short) id);
		this.size = ((byte) size);
		this.delay = ((byte) delay);
		this.duration = ((byte) duration);
		this.startHeight = ((byte) startHeight);
		this.endHeight = ((byte) endHeight);
		this.curve = ((byte) curve);
	}

	public Projectile(Projectile p) {
		id = p.id;
		size = p.size;
		delay = p.delay;
		duration = p.duration;
		startHeight = p.startHeight;
		endHeight = p.endHeight;
		curve = p.curve;
	}

	public int getCurve() {
		return curve;
	}

	public int getDelay() {
		return delay;
	}

	public int getDuration() {
		return duration;
	}

	public int getEndHeight() {
		return endHeight;
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public int getStartHeight() {
		return startHeight;
	}

	public void setCurve(int curve) {
		this.curve = ((byte) curve);
	}

	public void setDelay(int delay) {
		this.delay = ((byte) delay);
	}

	public void setDuration(int duration) {
		this.duration = ((byte) duration);
	}

	public void setEndHeight(int endHeight) {
		this.endHeight = ((byte) endHeight);
	}

	public void setId(int id) {
		this.id = ((short) id);
	}

	public void setSize(int size) {
		this.size = ((byte) size);
	}

	public void setStartHeight(int startHeight) {
		this.startHeight = ((byte) startHeight);
	}
}
