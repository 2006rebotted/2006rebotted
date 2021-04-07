package com.runescape;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import com.runescape.cache.graphics.Widget;
import com.runescape.draw.ProducingGraphicsBuffer;

public class GameApplet extends Applet implements Runnable, MouseListener,
		MouseMotionListener, MouseWheelListener, KeyListener, FocusListener,
		WindowListener {

	private static final long serialVersionUID = 1L;

	private int anInt4;
	private int delayTime;
	int minDelay;
	private final long aLongArray7[] = new long[10];
	int fps;
	boolean shouldDebug;
	int myWidth;
	int myHeight;
	Graphics graphics;
	ProducingGraphicsBuffer fullGameScreen;
	GameFrame gameFrame;
	private boolean shouldClearScreen;
	boolean awtFocus;
	int idleTime;
	int clickMode2;
	public int mouseX;
	public int mouseY;
	int clickMode1;
	private int clickX;
	private int clickY;
	public long aLong29;
	public int clickMode3;
	public int saveClickX;
	public int saveClickY;
	final int keyArray[] = new int[128];
	private final int charQueue[] = new int[128];
	public boolean isLoading;
	private int readIndex;
	private int writeIndex;
	public static int anInt34;
	public boolean isApplet;
	protected int rotationGliding;
	public boolean resized;

	public void refreshFrameSize(boolean undecorated, int width, int height,
			boolean resizable, boolean full) {
		boolean createdByApplet = (isApplet && !full);
		myWidth = width;
		myHeight = height;
		if (gameFrame != null) {
			gameFrame.dispose();
		}
		if (!createdByApplet) {
			gameFrame = new GameFrame(this, width, height, resizable,
					undecorated);
			gameFrame.addWindowListener(this);
		}
		graphics = (createdByApplet ? this : gameFrame).getGraphics();
		if (!createdByApplet) {
			getGameComponent().addMouseWheelListener(this);
			getGameComponent().addMouseListener(this);
			getGameComponent().addMouseMotionListener(this);
			getGameComponent().addKeyListener(this);
			getGameComponent().addFocusListener(this);
		}
	}

	public boolean appletClient() {
		return gameFrame == null && isApplet == true;
	}

	final void createClientFrame(int w, int h) {
		isApplet = false;
		myWidth = w;
		myHeight = h;
		gameFrame = new GameFrame(this, myWidth, myHeight,
				Client.frameMode == Client.ScreenMode.RESIZABLE,
				Client.frameMode == Client.ScreenMode.FULLSCREEN);
		gameFrame.setFocusTraversalKeysEnabled(false);
		graphics = getGameComponent().getGraphics();
		fullGameScreen = new ProducingGraphicsBuffer(myWidth, myHeight);
		startRunnable(this, 1);
	}

	final void initClientFrame(int w, int h) {
		isApplet = true;
		myWidth = w;
		myHeight = h;
		graphics = getGameComponent().getGraphics();
		fullGameScreen = new ProducingGraphicsBuffer(myWidth, myHeight);
		startRunnable(this, 1);
	}

	public void run() {
		getGameComponent().addMouseListener(this);
		getGameComponent().addMouseMotionListener(this);
		getGameComponent().addKeyListener(this);
		getGameComponent().addFocusListener(this);
		getGameComponent().addMouseWheelListener(this);
		if (gameFrame != null) {
			gameFrame.addWindowListener(this);
		}
		drawLoadingText(0, "Loading...");
		startUp();
		int i = 0;
		int j = 256;
		int k = 1;
		int l = 0;
		int i1 = 0;
		for (int j1 = 0; j1 < 10; j1++) {
			aLongArray7[j1] = System.currentTimeMillis();
		}
		do {
			if (anInt4 < 0) {
				break;
			}
			if (anInt4 > 0) {
				anInt4--;
				if (anInt4 == 0) {
					exit();
					return;
				}
			}
			int k1 = j;
			int i2 = k;
			j = 300;
			k = 1;
			long l2 = System.currentTimeMillis();
			if (aLongArray7[i] == 0L) {
				j = k1;
				k = i2;
			} else if (l2 > aLongArray7[i]) {
				j = (int) ((long) (2560 * delayTime) / (l2 - aLongArray7[i]));
			}
			if (j < 25) {
				j = 25;
			}
			if (j > 256) {
				j = 256;
				k = (int) ((long) delayTime - (l2 - aLongArray7[i]) / 10L);
			}
			if (k > delayTime) {
				k = delayTime;
			}
			aLongArray7[i] = l2;
			i = (i + 1) % 10;
			if (k > 1) {
				for (int j2 = 0; j2 < 10; j2++) {
					if (aLongArray7[j2] != 0L) {
						aLongArray7[j2] += k;
					}
				}

			}
			if (k < minDelay) {
				k = minDelay;
			}
			try {
				Thread.sleep(k);
			} catch (InterruptedException interruptedexception) {
				i1++;
			}
			for (; l < 256; l += j) {
				clickMode3 = clickMode1;
				saveClickX = clickX;
				saveClickY = clickY;
				clickMode1 = 0;
				processGameLoop();
				readIndex = writeIndex;
			}

			l &= 0xff;
			if (delayTime > 0) {
				fps = (1000 * j) / (delayTime * 256);
			}
			processDrawing();
			if (shouldDebug) {
				System.out.println((new StringBuilder()).append("ntime:")
						.append(l2).toString());
				for (int k2 = 0; k2 < 10; k2++) {
					int i3 = ((i - k2 - 1) + 20) % 10;
					System.out.println((new StringBuilder()).append("otim")
							.append(i3).append(":").append(aLongArray7[i3])
							.toString());
				}

				System.out.println((new StringBuilder()).append("fps:")
						.append(fps).append(" ratio:").append(j)
						.append(" count:").append(l).toString());
				System.out.println((new StringBuilder()).append("del:")
						.append(k).append(" deltime:").append(delayTime)
						.append(" mindel:").append(minDelay).toString());
				System.out.println((new StringBuilder()).append("intex:")
						.append(i1).append(" opos:").append(i).toString());
				shouldDebug = false;
				i1 = 0;
			}
		} while (true);
		if (anInt4 == -1) {
			exit();
		}
	}

	private void exit() {
		anInt4 = -2;
		cleanUpForQuit();
		if (gameFrame != null) {
			try {
				Thread.sleep(1000L);
			} catch (Exception exception) {
			}
			try {
				System.exit(0);
			} catch (Throwable throwable) {
			}
		}
	}

	final void method4(int i) {
		delayTime = 1000 / i;
	}

	public final void start() {
		if (anInt4 >= 0) {
			anInt4 = 0;
		}
	}

	public final void stop() {
		if (anInt4 >= 0) {
			anInt4 = 4000 / delayTime;
		}
	}

	public final void destroy() {
		anInt4 = -1;
		try {
			Thread.sleep(5000L);
		} catch (Exception exception) {
		}
		if (anInt4 == -1) {
			exit();
		}
	}

	public final void update(Graphics g) {
		if (graphics == null) {
			graphics = g;
		}
		shouldClearScreen = true;
		raiseWelcomeScreen();
	}

	public final void paint(Graphics g) {
		if (graphics == null) {
			graphics = g;
		}
		shouldClearScreen = true;
		raiseWelcomeScreen();
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		int rotation = event.getWheelRotation();
		handleInterfaceScrolling(event);
		if (mouseX > 0 && mouseX < 512 && mouseY > Client.frameHeight - 165
				&& mouseY < Client.frameHeight - 25) {
			int scrollPos = Client.anInt1089;
			scrollPos -= rotation * 30;
			if (scrollPos < 0)
				scrollPos = 0;
			if (scrollPos > Client.anInt1211 - 110)
				scrollPos = Client.anInt1211 - 110;
			if (Client.anInt1089 != scrollPos) {
				Client.anInt1089 = scrollPos;
				Client.updateChatbox = true;
			}
		}
	}

	public void handleInterfaceScrolling(MouseWheelEvent event) {
		int rotation = event.getWheelRotation();
		int positionX = 0;
		int positionY = 0;
		int width = 0;
		int height = 0;
		int offsetX = 0;
		int offsetY = 0;
		int childID = 0;
		int tabInterfaceID = Client.tabInterfaceIDs[Client.tabId];
		if (tabInterfaceID != -1) {
			Widget tab = Widget.interfaceCache[tabInterfaceID];
			offsetX = Client.frameMode == Client.ScreenMode.FIXED ? Client.frameWidth - 218
					: (Client.frameMode == Client.ScreenMode.FIXED ? 28
							: Client.frameWidth - 197);
			offsetY = Client.frameMode == Client.ScreenMode.FIXED ? Client.frameHeight - 298
					: (Client.frameMode == Client.ScreenMode.FIXED ? 37
							: Client.frameHeight
									- (Client.frameWidth >= 1000 ? 37 : 74) - 267);
			for (int index = 0; index < tab.children.length; index++) {
				if (Widget.interfaceCache[tab.children[index]].scrollMax > 0) {
					childID = index;
					positionX = tab.childX[index];
					positionY = tab.childY[index];
					width = Widget.interfaceCache[tab.children[index]].width;
					height = Widget.interfaceCache[tab.children[index]].height;
					break;
				}
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY
					&& mouseX < offsetX + positionX + width
					&& mouseY < offsetY + positionY + height) {
				Widget.interfaceCache[tab.children[childID]].scrollPosition += rotation * 30;
			}
		}
		if (Client.openInterfaceId != -1) {
			Widget rsi = Widget.interfaceCache[Client.openInterfaceId];
			offsetX = Client.frameMode == Client.ScreenMode.FIXED ? 4
					: (Client.frameWidth / 2) - 356;
			offsetY = Client.frameMode == Client.ScreenMode.FIXED ? 4
					: (Client.frameHeight / 2) - 230;
			for (int index = 0; index < rsi.children.length; index++) {
				if (Widget.interfaceCache[rsi.children[index]].scrollMax > 0) {
					childID = index;
					positionX = rsi.childX[index];
					positionY = rsi.childY[index];
					width = Widget.interfaceCache[rsi.children[index]].width;
					height = Widget.interfaceCache[rsi.children[index]].height;
					break;
				}
			}
			if (mouseX > offsetX + positionX && mouseY > offsetY + positionY
					&& mouseX < offsetX + positionX + width
					&& mouseY < offsetY + positionY + height) {
				Widget.interfaceCache[rsi.children[childID]].scrollPosition += rotation * 30;
			}
		}
	}

	public int clickType;
	public final int LEFT = 0;
	public final int RIGHT = 1;
	public final int DRAG = 2;
	public final int RELEASED = 3;
	public final int MOVE = 4;
	public int releasedX;
	public int releasedY;
	public boolean mouseWheelDown;

	public final void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int type = e.getButton();
		if (gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;// 4
			y -= insets.top;// 22
		}
		idleTime = 0;
		clickX = x;
		clickY = y;
		aLong29 = System.currentTimeMillis();
		if (type == 2) {
			mouseWheelDown = true;
			mouseWheelX = x;
			mouseWheelY = y;
			return;
		}
		if (e.isMetaDown()) {
			clickType = RIGHT;
			clickMode1 = 2;
			clickMode2 = 2;
		} else {
			clickType = LEFT;
			clickMode1 = 1;
			clickMode2 = 1;
		}
	}

	public final void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;// 4
			y -= insets.top;// 22
		}
		releasedX = x;
		releasedY = y;
		idleTime = 0;
		clickMode2 = 0;
		clickType = RELEASED;
		mouseWheelDown = false;
	}

	public final void mouseClicked(MouseEvent mouseevent) {
	}

	public final void mouseEntered(MouseEvent mouseevent) {
	}

	public final void mouseExited(MouseEvent mouseevent) {
		idleTime = 0;
		mouseX = -1;
		mouseY = -1;
	}

	public int mouseWheelX;
	public int mouseWheelY;

	public final void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;// 4
			y -= insets.top;// 22
		}
		if (mouseWheelDown) {
			y = mouseWheelX - e.getX();
			int k = mouseWheelY - e.getY();
			mouseWheelDragged(y, -k);
			mouseWheelX = e.getX();
			mouseWheelY = e.getY();
			return;
		}
		idleTime = 0;
		mouseX = x;
		mouseY = y;
		clickType = DRAG;
	}

	void mouseWheelDragged(int param1, int param2) {

	}

	public final void mouseMoved(MouseEvent mouseevent) {
		int x = mouseevent.getX();
		int y = mouseevent.getY();
		if (gameFrame != null) {
			Insets insets = gameFrame.getInsets();
			x -= insets.left;// 4
			y -= insets.top;// 22
		}
		idleTime = 0;
		mouseX = x;
		mouseY = y;
		clickType = MOVE;
	}

	public final void keyPressed(KeyEvent keyevent) {
		idleTime = 0;
		int i = keyevent.getKeyCode();
		int j = keyevent.getKeyChar();
		if (i == KeyEvent.VK_F1) {
			Client.setTab(3);
		} else if (i == KeyEvent.VK_ESCAPE) {
			Client.setTab(10);
		} else if (i == KeyEvent.VK_F2) {
			Client.setTab(4);
		} else if (i == KeyEvent.VK_F3) {
			Client.setTab(5);
		} else if (i == KeyEvent.VK_F4) {
			Client.setTab(6);
		} else if (i == KeyEvent.VK_F5) {
			Client.setTab(0);
		}
		if (j < 30)
			j = 0;
		if (i == 37)
			j = 1;
		if (i == 39)
			j = 2;
		if (i == 38)
			j = 3;
		if (i == 40)
			j = 4;
		if (i == 17)
			j = 5;
		if (i == 8)
			j = 8;
		if (i == 127)
			j = 8;
		if (i == 9)
			j = 9;
		if (i == 10)
			j = 10;
		if (i >= 112 && i <= 123)
			j = (1008 + i) - 112;
		if (i == 36)
			j = 1000;
		if (i == 35)
			j = 1001;
		if (i == 33)
			j = 1002;
		if (i == 34)
			j = 1003;
		if (j > 0 && j < 128)
			keyArray[j] = 1;
		if (j > 4) {
			charQueue[writeIndex] = j;
			writeIndex = writeIndex + 1 & 0x7f;
		}
	}

	public final void keyReleased(KeyEvent keyevent) {
		idleTime = 0;
		int i = keyevent.getKeyCode();
		char c = keyevent.getKeyChar();
		if (c < '\036')
			c = '\0';
		if (i == 37)
			c = '\001';
		if (i == 39)
			c = '\002';
		if (i == 38)
			c = '\003';
		if (i == 40)
			c = '\004';
		if (i == 17)
			c = '\005';
		if (i == 8)
			c = '\b';
		if (i == 127)
			c = '\b';
		if (i == 9)
			c = '\t';
		if (i == 10)
			c = '\n';
		if (c > 0 && c < '\200')
			keyArray[c] = 0;
	}

	public final void keyTyped(KeyEvent keyevent) {
	}

	final int readChar(int dummy) {
		while (dummy >= 0) {
			for (int j = 1; j > 0; j++)
				;
		}
		int k = -1;
		if (writeIndex != readIndex) {
			k = charQueue[readIndex];
			readIndex = readIndex + 1 & 0x7f;
		}
		return k;
	}

	public final void focusGained(FocusEvent focusevent) {
		awtFocus = true;
		shouldClearScreen = true;
		raiseWelcomeScreen();
	}

	public final void focusLost(FocusEvent focusevent) {
		awtFocus = false;
		for (int i = 0; i < 128; i++) {
			keyArray[i] = 0;
		}

	}

	public final void windowActivated(WindowEvent windowevent) {
	}

	public final void windowClosed(WindowEvent windowevent) {
	}

	public final void windowClosing(WindowEvent windowevent) {
		destroy();

	}

	public final void windowDeactivated(WindowEvent windowevent) {
	}

	public final void windowDeiconified(WindowEvent windowevent) {
	}

	public final void windowIconified(WindowEvent windowevent) {
	}

	public final void windowOpened(WindowEvent windowevent) {
	}

	void startUp() {
	}

	void processGameLoop() {
	}

	void cleanUpForQuit() {
	}

	void processDrawing() {
	}

	void raiseWelcomeScreen() {
	}

	Component getGameComponent() {
		if (gameFrame != null && !isApplet) {
			return gameFrame;
		} else {
			return this;
		}
	}

	public void startRunnable(Runnable runnable, int i) {
		Thread thread = new Thread(runnable);
		thread.start();
		thread.setPriority(i);
	}

	void drawLoadingText(int percentage, String loadingText) {
		while (graphics == null) {
			graphics = (isApplet ? this : gameFrame).getGraphics();
			try {
				getGameComponent().repaint();
			} catch (Exception _ex) {
			}
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		}
		Font font = new Font("Helvetica", 1, 13);
		FontMetrics fontmetrics = getGameComponent().getFontMetrics(font);
		Font font1 = new Font("Helvetica", 0, 13);
		FontMetrics fontmetrics1 = getGameComponent().getFontMetrics(font1);
		if (shouldClearScreen) {
			graphics.setColor(Color.black);
			graphics.fillRect(0, 0, Client.frameWidth, Client.frameHeight);
			shouldClearScreen = false;
		}
		Color color = new Color(140, 17, 17);
		int y = Client.frameHeight / 2 - 18;
		graphics.setColor(color);
		graphics.drawRect(Client.frameWidth / 2 - 152, y, 304, 34);
		graphics.fillRect(Client.frameWidth / 2 - 150, y + 2, percentage * 3, 30);
		graphics.setColor(Color.black);
		graphics.fillRect((Client.frameWidth / 2 - 150) + percentage * 3, y + 2,
				300 - percentage * 3, 30);
		graphics.setFont(font);
		graphics.setColor(Color.white);
		graphics.drawString(loadingText,
				(Client.frameWidth - fontmetrics.stringWidth(loadingText)) / 2,
				y + 22);
		graphics.drawString("",
				(Client.frameWidth - fontmetrics1.stringWidth("")) / 2, y - 8);
	}

	GameApplet() {
		delayTime = 20;
		minDelay = 1;
		shouldDebug = false;
		shouldClearScreen = true;
		awtFocus = true;
	}

}
