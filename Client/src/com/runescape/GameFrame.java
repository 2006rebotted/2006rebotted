package com.runescape;
import java.awt.*;

public final class GameFrame extends Frame {

	private final GameApplet applet;
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension screenSize = toolkit.getScreenSize();
	public int screenWidth = (int) screenSize.getWidth();
	public int screenHeight = (int) screenSize.getHeight();
	protected final Insets insets;
	private static final long serialVersionUID = 1L;

	public GameFrame(GameApplet applet, int width, int height, boolean resizable, boolean fullscreen) {
		this.applet = applet;
		setTitle(Configuration.CLIENT_NAME);
		setResizable(resizable);
		setUndecorated(fullscreen);
		setVisible(true); 
		insets = getInsets();
		if (resizable) {
			setMinimumSize(new Dimension(766 + insets.left + insets.right, 536 + insets.top + insets.bottom));
		}
		setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
		setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		requestFocus();
		toFront();
	}

	public Graphics getGraphics() {
		final Graphics graphics = super.getGraphics();
		Insets insets = this.getInsets();
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.translate(insets != null ? insets.left : 0, insets != null ? insets.top : 0);
		return graphics;
	}

	public int getFrameWidth() {
		Insets insets = this.getInsets();
		return getWidth() - (insets.left + insets.right);
	}

	public int getFrameHeight() {
		Insets insets = this.getInsets();
		return getHeight() - (insets.top + insets.bottom);
	}

	public void update(Graphics graphics) {
		applet.update(graphics);
	}

	public void paint(Graphics graphics) {
		applet.paint(graphics);
	}
}