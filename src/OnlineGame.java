import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.print.attribute.standard.Severity;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class OnlineGame extends JFrame implements Runnable {

	private static JPanel contentPane;
	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;
	public static int n = 11;
	public static int m = 11;
	public static int tempN;
	public static int tempM;
	public static JLabel labels[][];
	private static String metalImage = "Metal";
	private static String boxImage = "box.png";
	private static String mainCharacterImage;
	private static String bombImage = "bomb.png";
	private static String heartImage = "heart.png";
	private static String diamondImage = "diamond.png";
	private static int stoneIconAddress = 0;
	private static int currentI;
	private static int currentJ;
	private static int tempCurrentI;
	private static int tempCurrentJ;
	public static ArrayList<int[]> playersLives = new ArrayList<int[]>();
	public static ArrayList<int[]> playersIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> tempPlayersIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> boxObstaclesIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> bombsIndexes = new ArrayList<int[]>();
	public static ArrayList<Integer> waitingBombs = new ArrayList<Integer>();
	public static ArrayList<int[]> heartIndexes = new ArrayList<int[]>();
	public static ArrayList<Integer> exceptedPlayerNumber = new ArrayList<Integer>();
	public static ArrayList<Integer> exceptedPlayerTimer = new ArrayList<Integer>();
	public static String playerName;
	public static int playerNumber;
	public static String serverAddress;
	public static Socket sentSocket;
	public static int roomId;
	public static int roomCapacity2To4;;
	private static int bombingRange = 1;
	private static JLabel lblTimer;
	public static int secondsPassed = 0;
	public static Timer gameTimer = new Timer();
	public static TimerTask gameTimerTask = new TimerTask() {
		public void run() {
			secondsPassed++;
			lblTimer.setText(Integer.toString(secondsPassed));
		}
	};
	public static JPanel gamePanel;
	public static JLabel lblMainCharacterLives;

	/**
	 * Launch the application.
	 */
	public void run() {
		while (true) {
			explosion();
			checkTheThreeSecondsRule();
			if (playerNumber == 0) {
				checkToMakeANewHeart();
				checktoDeleteAHeart();
				checkToMakeANewDiamond();
				checktoDeleteADiamond();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}

	public static void main(Socket sentSocket, String serverAddress, String name, int playerNumber, int roomId) {
		OnlineGame frame = new OnlineGame(sentSocket, serverAddress, name, playerNumber, roomId);
		Thread mainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String line = null;
					try {
						line = in.readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					System.out.println("Line : " + line);
					if (line.startsWith("GET-NAME")) {
						playerName = line.substring(9, line.length());
						System.out.println("Name is : " + playerName);
						frame.setTitle(playerName);
					} else if (line.startsWith("TIMER-TASK")) {
						gameTimer.scheduleAtFixedRate(gameTimerTask, 1000, 1000);
						out.println("GIVE-ME-BOXES" + " " + roomId);
					} else if (line.startsWith("SET-UP-LIVES")) {
						int capacity = line.charAt(13) - '0';
						switch (capacity) {
						case 2:
							int[] lives2 = { 10, 10 };
							playersLives.add(lives2);
							break;
						case 3:
							int[] lives3 = { 10, 10, 10 };
							playersLives.add(lives3);
							break;
						case 4:
							int[] lives4 = { 10, 10, 10, 10 };
							playersLives.add(lives4);
							break;
						}
					} else if (line.startsWith("ROOM-CAPACITY")) {
						roomCapacity2To4 = line.charAt(14) - '0';
						System.out.println("ROOM-CAPACITY set to " + roomCapacity2To4);
					} else if (line.startsWith("RECIEVE-INDEXES")) {
						line = line.substring(16);
						String[] playersIndexesString = line.split(" ");
						for (int i = 0; i < playersIndexesString.length; i++) {
							String[] parts = playersIndexesString[i].split("@");
							int[] indexes = { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
							playersIndexes.add(indexes);
							System.out.println(
									(i + 1) + " : " + Integer.parseInt(parts[0]) + " , " + Integer.parseInt(parts[1]));
						}
						System.out.println("Recieved Indexes : " + line);
						buildGameInterface();
						stoneIconAddress = labels[0][0].getIcon().getIconWidth();
					} else if (line.startsWith("UPDATE-INDEXES")) {
						backUpPlayerIndexes();
						line = line.substring(15);
						String[] partsSpaces = line.split(" ");
						for (int i = 0; i < partsSpaces.length; i++) {
							if (!exceptedPlayerNumber.contains(i)) {
								String[] partsSigns = partsSpaces[i].split("@");
								int newIndexI = Integer.parseInt(partsSigns[0]);
								int newIndexJ = Integer.parseInt(partsSigns[1]);
								int[] newIndexesToInsert = { newIndexI, newIndexJ };
								playersIndexes.set(i, newIndexesToInsert);
								if (i == playerNumber) {
									currentI = newIndexI;
									currentJ = newIndexJ;
								}
							} else {
								System.out.println("DENIED");
							}
						}
						reDrawNewIndexes();
					} else if (line.startsWith("BOMB-SET")) {
						line = line.substring(9);
						String[] partsSpaces = line.split(" ");
						int bombI = Integer.parseInt(partsSpaces[0]);
						int bombJ = Integer.parseInt(partsSpaces[1]);
						int bombOwner = Integer.parseInt(partsSpaces[2]);
						int explosionAt = Integer.parseInt(partsSpaces[3]);
						int bombRange = Integer.parseInt(partsSpaces[4]);
						int[] bombIndex = { bombI, bombJ, explosionAt, bombRange, bombOwner };
						bombsIndexes.add(bombIndex);
						waitingBombs.add(bombsIndexes.size() - 1);
						int counter = 0;
						int tempI = currentI;
						int tempJ = currentJ;
						while (counter <= 4) {
							tempI = bombI;
							tempJ = bombJ;
							switch (counter) {
							case 1:
								for (int i = 0; i < bombRange; i++) {
									tempI--;
									if (tempI >= 1 && labels[tempI][tempJ].getIcon() == null) {
										labels[tempI][tempJ].setForeground(new Color(245, 143, 11));
										labels[tempI][tempJ].setText("@");
									} else {
										if (labels[tempI][tempJ].getIcon().getIconWidth() == stoneIconAddress) {
											break;
										}
									}
								}
								break;
							case 2:
								for (int i = 0; i < bombRange; i++) {
									tempI++;
									if (tempI <= tempN && labels[tempI][tempJ].getIcon() == null) {
										labels[tempI][tempJ].setForeground(new Color(245, 143, 11));
										labels[tempI][tempJ].setText("@");
									} else {
										if (labels[tempI][tempJ].getIcon().getIconWidth() == stoneIconAddress) {
											break;
										}
									}
								}
								break;
							case 3:
								for (int i = 0; i < bombRange; i++) {
									tempJ++;
									if (tempI <= tempM && labels[tempI][tempJ].getIcon() == null) {
										labels[tempI][tempJ].setForeground(new Color(245, 143, 11));
										labels[tempI][tempJ].setText("@");
									} else {
										if (labels[tempI][tempJ].getIcon().getIconWidth() == stoneIconAddress) {
											break;
										}
									}
								}
								break;
							case 4:
								for (int i = 0; i < bombRange; i++) {
									tempJ--;
									if (tempJ >= 1 && labels[tempI][tempJ].getIcon() == null) {
										labels[tempI][tempJ].setForeground(new Color(245, 143, 11));
										labels[tempI][tempJ].setText("@");
									} else {
										if (labels[tempI][tempJ].getIcon().getIconWidth() == stoneIconAddress) {
											break;
										}
									}
								}
								break;
							}
							counter++;
						}
					} else if (line.startsWith("HOLD-ON-INSERTING")) {
						exceptedPlayerNumber.add(line.charAt(18) - '0');
						exceptedPlayerTimer.add(secondsPassed + 3);
						int playerI = playersIndexes.get(line.charAt(18) - '0')[0];
						int playerJ = playersIndexes.get(line.charAt(18) - '0')[1];
						System.out.println(playerI + " , " + playerJ);
						labels[playerI][playerJ].setIcon(null);
						System.out.println("SET TO NULL");
					} else if (line.startsWith("LIVES")) {
						line = line.substring(6);
						String[] CharacterLives = line.split(" ");
						for (int i = 0; i < CharacterLives.length; i++) {
							playersLives.get(roomId)[i] = Integer.parseInt(CharacterLives[i]);
						}
					} else if (line.startsWith("EMPTY-OR-NOT")) {
						int checkI = -1;
						int checkJ = -1;
						int position = line.charAt(13) - '0';
						switch (position) {
						case 1:
							checkI = 1;
							checkJ = 1;
							break;
						case 2:
							checkI = 1;
							checkJ = tempM;

							break;
						case 3:
							checkI = tempN;
							checkJ = 1;

							break;
						case 4:
							checkI = tempN;
							checkJ = tempM;

							break;
						}
						if (labels[checkI][checkJ].getIcon() == null) {
							out.println("ANSWER-YES" + " " + Integer.toString(checkI) + "@" + Integer.toString(checkJ));
						} else {
							out.println("ANSWER-NO");
						}
					} else if (line.startsWith("ALL-BOXES")) {
						System.out.println("ALL-BOXES came for " + playerName);
						line = line.substring(10);
						String[] parts = line.split(" ");
						for (String part : parts) {
							String[] indexes = part.split("@");
							int indexI = Integer.parseInt(indexes[0]);
							int indexJ = Integer.parseInt(indexes[1]);
							if (indexI >= 1 && indexI <= tempN && indexJ >= 1 && indexJ <= tempM) {
								System.out.println(" > " + indexI + " " + indexJ);
								if (labels[indexI][indexJ].getIcon() == null) {
									int[] indexesToInsert = { indexI, indexJ };
									boxObstaclesIndexes.add(indexesToInsert);
								}
							}
						}
						drawBoxes();
					} else if (line.startsWith("NOPE")) {
						System.out.println("NEW HEART VALIDATION CAME");
						Random randLast = new Random();
						int newHeartI;
						int newHeartJ;
						while (true) {
							newHeartI = randLast.nextInt(10) + 1;
							newHeartJ = randLast.nextInt(10) + 1;
							if (labels[newHeartI][newHeartJ].getIcon() == null) {
								out.println("GIMME-NEW-HEART" + " " + newHeartI + " " + newHeartJ + " " + secondsPassed
										+ " " + roomId);
								break;
							}
						}
					} else if (line.startsWith("MAKE-A-HEART")) {
						System.out.println("NEW HEART MADE !");
						String[] parts = line.split(" ");
						int newHeartI = Integer.parseInt(parts[1]);
						int newHeartJ = Integer.parseInt(parts[2]);
						Image imgHeart = new ImageIcon(this.getClass().getResource(heartImage)).getImage();
						labels[newHeartI][newHeartJ].setIcon(new ImageIcon(imgHeart));
						out.println("HEART-FALSE" + " " + roomId);
					} else if (line.startsWith("RESERVED-NOPE")) {

					} else if (line.startsWith("NDIAMOND")) {
						Random randLast = new Random();
						String[] parts = line.split(" ");
						int nextDiamondArrival = Integer.parseInt(parts[1]);
						int nextDiamondIndexesZero = Integer.parseInt(parts[2]);
						int nextDiamondIndexesOne = Integer.parseInt(parts[3]);
						int nextDiamondIndexesTwo = Integer.parseInt(parts[4]);
						if (secondsPassed == nextDiamondArrival) {
							if (nextDiamondIndexesZero == -1 && nextDiamondIndexesOne == -1
									&& nextDiamondIndexesTwo == -1) {
								int newStarI;
								int newStarJ;
								while (true) {
									newStarI = randLast.nextInt(10) + 1;
									newStarJ = randLast.nextInt(10) + 1;
									if (labels[newStarI][newStarJ].getIcon() == null) {
										System.out.println(" > " + newStarI + " " + newStarJ);
										nextDiamondIndexesZero = newStarI;
										nextDiamondIndexesOne = newStarJ;
										nextDiamondIndexesTwo = (secondsPassed + 5);
										out.println("NEW-DIAMOND-IS-MADE" + " " + newStarI + " " + newStarJ + " "
												+ nextDiamondIndexesTwo + " " + roomId);
										break;
									}
								}
							}
						}
					} else if (line.startsWith("HERE-ARE-THE-DIAMONDS")) {
						String[] parts = line.split(" ");
						int newStarI = Integer.parseInt(parts[1]);
						int newStarJ = Integer.parseInt(parts[2]);
						Image imgDiamond = new ImageIcon(this.getClass().getResource(diamondImage)).getImage();
						labels[newStarI][newStarJ].setIcon(new ImageIcon(imgDiamond));
					} else if (line.startsWith("MAKE-A-DIAMOND")) {
						String[] parts = line.split(" ");
						int newStarI = Integer.parseInt(parts[1]);
						int newStarJ = Integer.parseInt(parts[2]);
						Image imgDiamond = new ImageIcon(this.getClass().getResource(diamondImage)).getImage();
						labels[newStarI][newStarJ].setIcon(new ImageIcon(imgDiamond));
					} else if (line.startsWith("DELETE-THIS-DIAMOND")) {
						String[] parts = line.split(" ");
						int newStarI = Integer.parseInt(parts[1]);
						int newStarJ = Integer.parseInt(parts[1]);
						labels[newStarI][newStarJ].setIcon(null);
					}
				}
			}
		});
		FrameDragListener frameDragListener = new FrameDragListener(frame);
		frame.addMouseListener((MouseListener) frameDragListener);
		frame.addMouseMotionListener((MouseMotionListener) frameDragListener);
		frame.setVisible(true);
		new Thread(frame).start();
		mainThread.start();
	}

	/**
	 * Create the frame.
	 */
	public OnlineGame(Socket sentSocket, String serverAddress, String name, int playerNumber, int roomId) {
		socket = sentSocket;
		playerName = name;
		this.serverAddress = serverAddress;
		this.playerNumber = playerNumber;

		try {
			this.socket = new Socket(serverAddress, 9512);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				moveIt(e);
			}
		});
		tempN = n;
		tempM = m;
		n += 2;
		m += 2;
		switch (playerNumber) {
		case 0:
			currentI = 1;
			currentJ = 1;
			mainCharacterImage = characterProfilePictureAddress(0);
			break;
		case 1:
			currentI = 1;
			currentJ = tempM;
			mainCharacterImage = characterProfilePictureAddress(1);
			break;
		case 2:
			currentI = tempN;
			currentJ = 1;
			mainCharacterImage = characterProfilePictureAddress(2);
			break;
		case 3:
			currentI = tempN;
			currentJ = tempM;
			mainCharacterImage = characterProfilePictureAddress(1);
			break;
		}
		if (n > 17 || m > 17) {
			metalImage = "Metal_2";
			boxImage = "box2.png";
			bombImage = "bomb2.png";
			heartImage = "heart2.png";
			heartImage = "diamond2.png";
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 603, 627);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTime = new JLabel("Timer");
		lblTime.setHorizontalAlignment(JTextField.CENTER);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTime.setBounds(84, 38, 438, 27);
		lblTime.setForeground(new Color(182, 182, 182));
		contentPane.add(lblTime);

		lblTimer = new JLabel("");
		lblTimer.setHorizontalAlignment(JTextField.CENTER);
		lblTimer.setFont(new Font("Tahoma", Font.PLAIN, 70));
		lblTimer.setForeground(new Color(182, 182, 182));
		lblTimer.setBounds(84, 64, 438, 65);
		contentPane.add(lblTimer);

		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(30, 30, 30));
		gamePanel.setBounds(84, 175, 438, 387);
		contentPane.add(gamePanel);
		gamePanel.setLayout(new GridLayout(n, m, 0, 0));
		gamePanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				moveIt(evt);
			}
		});

		JLabel lblMainTheme = new JLabel("");
		lblMainTheme.setBounds(0, 0, 604, 628);
		Image imgMainTheme = new ImageIcon(this.getClass().getResource("/ProfileTheme.jpg")).getImage()
				.getScaledInstance(lblMainTheme.getWidth(), lblMainTheme.getHeight(), Image.SCALE_DEFAULT);

		JLabel lblClose = new JLabel("");
		lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClose.setBounds(567, 2, 27, 27);
		Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage()
				.getScaledInstance(lblClose.getWidth(), lblClose.getHeight(), Image.SCALE_DEFAULT);
		lblClose.setIcon(new ImageIcon(imgClose));
		contentPane.add(lblClose);

		lblMainCharacterLives = new JLabel("lives : 10");
		lblMainCharacterLives.setBounds(84, 137, 180, 28);
		lblMainCharacterLives.setForeground(new Color(182, 182, 182));
		contentPane.add(lblMainCharacterLives);
		lblMainTheme.setIcon(new ImageIcon(imgMainTheme));
		contentPane.add(lblMainTheme);
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		out.println("SETTING-UP-GAME-PAGE");
		out.println("UPDATE-SOCKET" + " _" + roomId + "_" + playerNumber + "_" + socket);
		out.println("GAME-PAGE-REQUEST-NAME" + " " + playerNumber + " " + roomId);
		out.println("SYNC-INTERFACES" + " " + roomId);
	}

	private static void buildGameInterface() {
		boolean oneAfterOneI = true;
		boolean oneAfterOneJ = false;
		Random randLast = new Random();
		labels = new JLabel[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				labels[i][j] = new JLabel();
				labels[i][j].setBackground(new Color(180, 180, 180));
				labels[i][j].setForeground(new Color(180, 180, 180));
				labels[i][j].setFont(new Font("Tahoma", Font.PLAIN, 28));
				labels[i][j].setName(Integer.toString(i) + "_" + Integer.toString(j));
				if (i == 0 || j == 0 || i == n - 1 || j == m - 1) {
					int rnd = randLast.nextInt(3) + 1;
					String imgAddress = "";
					if (rnd == 1) {
						imgAddress = metalImage + "" + ".jpg";
					} else if (rnd == 2) {
						imgAddress = metalImage + "2" + ".jpg";
					} else if (rnd == 3) {
						imgAddress = metalImage + "3" + ".jpg";
					}
					Image imgLbl = new ImageIcon(OnlineGame.class.getResource(imgAddress)).getImage();
					labels[i][j].setIcon(new ImageIcon(imgLbl));
				} else {
					labels[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					if (i == 1 && j == 1) {
						String address = characterProfilePictureAddress(0);
						Image imgLbl = new ImageIcon(OnlineGame.class.getResource("/" + address)).getImage();
						labels[i][j].setIcon(new ImageIcon(imgLbl));
					} else if (i == 1 && j == tempM && roomCapacity2To4 >= 2) {
						String address = characterProfilePictureAddress(1);
						Image imgLbl = new ImageIcon(OnlineGame.class.getResource("/" + address)).getImage();
						labels[i][j].setIcon(new ImageIcon(imgLbl));
					} else if (i == tempN && j == 1 && roomCapacity2To4 >= 3) {
						String address = characterProfilePictureAddress(2);
						Image imgLbl = new ImageIcon(OnlineGame.class.getResource("/" + address)).getImage();
						labels[i][j].setIcon(new ImageIcon(imgLbl));
					} else if (i == tempN && j == tempM && roomCapacity2To4 == 4) {
						String address = characterProfilePictureAddress(3);
						Image imgLbl = new ImageIcon(OnlineGame.class.getResource("/" + address)).getImage();
						labels[i][j].setIcon(new ImageIcon(imgLbl));
					} else {
						if (oneAfterOneI) {
							if (oneAfterOneJ) {
								int rnd = randLast.nextInt(3) + 1;
								String imgAddress = "";
								if (rnd == 1) {
									imgAddress = metalImage + "" + ".jpg";
								} else if (rnd == 2) {
									imgAddress = metalImage + "2" + ".jpg";
								} else if (rnd == 3) {
									imgAddress = metalImage + "3" + ".jpg";
								}
								Image imgLbl = new ImageIcon(OnlineGame.class.getResource(imgAddress)).getImage();
								labels[i][j].setIcon(new ImageIcon(imgLbl));
								oneAfterOneJ = false;
							} else {
								oneAfterOneJ = true;
							}
						}
					}
				}
				gamePanel.add(labels[i][j]);
			}
			oneAfterOneJ = false;
			if (oneAfterOneI) {
				oneAfterOneI = false;
			} else {
				oneAfterOneI = true;
			}
		}
	}

	private static String characterProfilePictureAddress(int givenPlayerNumber) {
		String address = "";
		switch (givenPlayerNumber) {
		case 0:
			if (n > 17 || m > 17) {
				address = "MainCharacter2.png";
			} else {
				address = "MainCharacter.png";
			}
			break;
		case 1:
			if (n > 17 || m > 17) {
				address = "MainCharacter_22.png";
			} else {
				address = "MainCharacter_2.png";
			}
			break;
		case 2:
			if (n > 17 || m > 17) {
				address = "MainCharacter_32.png";
			} else {
				address = "MainCharacter_3.png";
			}
			break;
		case 3:
			if (n > 17 || m > 17) {
				address = "MainCharacter_42.png";
			} else {
				address = "MainCharacter_4.png";
			}
			break;
		}
		return address;
	}

	private void explosion() {
		for (int[] bomb : bombsIndexes) {
			int bombI = bomb[0];
			int bombJ = bomb[1];
			int range = bomb[3];
			int newBombI = bombI;
			int newBombJ = bombJ;
			if (secondsPassed == bomb[2]) {
				// playersLives.get(roomId)[playerNumber] =
				// playersLives.get(roomId)[playerNumber] - 1;
				lblMainCharacterLives.setText("Lives : " + playersLives.get(roomId)[playerNumber]);
				labels[bomb[0]][bomb[1]].setIcon(null);
				int counter = 1;
				while (counter <= 4) {
					newBombI = bombI;
					newBombJ = bombJ;
					switch (counter) {
					case 1:
						for (int k = 0; k < range; k++) {
							newBombI--;
							if (newBombI >= 1) {
								if (newBombI != currentI || newBombJ != currentJ) {
									if (labels[newBombI][newBombJ].getIcon() != null) {
										if (labels[newBombI][newBombJ].getIcon().getIconWidth() != stoneIconAddress) {
											labels[newBombI][newBombJ].setIcon(null);
											for (int i = 0; i < playersIndexes.size(); i++) {
												if (newBombI == playersIndexes.get(i)[0]
														&& newBombJ == playersIndexes.get(i)[1]) {
													out.println("DEAD-BODY" + " " + i + " " + roomId);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
											for (int i = 0; i < boxObstaclesIndexes.size(); i++) {
												if (newBombI == boxObstaclesIndexes.get(i)[0]
														&& newBombJ == boxObstaclesIndexes.get(i)[1]) {
													out.println(
															"DBOX" + " " + newBombI + "@" + newBombJ + " " + roomId);
													System.out.println("KILLED ONE BOX");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("case 1");
								}
							} else {
								break;
							}
						}
						break;
					case 2:
						for (int k = 0; k < range; k++) {
							newBombI++;
							if (newBombI <= tempN) {
								if (newBombI != currentI || newBombJ != currentJ) {
									if (labels[newBombI][newBombJ].getIcon() != null) {
										if (labels[newBombI][newBombJ].getIcon().getIconWidth() != stoneIconAddress) {
											labels[newBombI][newBombJ].setIcon(null);
											for (int i = 0; i < playersIndexes.size(); i++) {
												if (newBombI == playersIndexes.get(i)[0]
														&& newBombJ == playersIndexes.get(i)[1]) {
													out.println("DEAD-BODY" + " " + i + " " + roomId);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("case 2");
								}
							} else {
								break;
							}
						}
						break;
					case 3:
						for (int k = 0; k < range; k++) {
							newBombJ++;
							if (newBombJ <= tempM) {
								if (newBombI != currentI || newBombJ != currentJ) {
									if (labels[newBombI][newBombJ].getIcon() != null) {
										if (labels[newBombI][newBombJ].getIcon().getIconWidth() != stoneIconAddress) {
											labels[newBombI][newBombJ].setIcon(null);
											for (int i = 0; i < playersIndexes.size(); i++) {
												if (newBombI == playersIndexes.get(i)[0]
														&& newBombJ == playersIndexes.get(i)[1]) {
													out.println("DEAD-BODY" + " " + i + " " + roomId);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("case 3");
								}
							} else {
								break;
							}
						}
						break;
					case 4:
						for (int k = 0; k < range; k++) {
							newBombJ--;
							if (newBombJ >= 1) {
								if (newBombI != currentI || newBombJ != currentJ) {
									if (labels[newBombI][newBombJ].getIcon() != null) {
										if (labels[newBombI][newBombJ].getIcon().getIconWidth() != stoneIconAddress) {
											labels[newBombI][newBombJ].setIcon(null);
											for (int i = 0; i < playersIndexes.size(); i++) {
												if (newBombI == playersIndexes.get(i)[0]
														&& newBombJ == playersIndexes.get(i)[1]) {
													out.println("DEAD-BODY" + " " + i + " " + roomId);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("case 4");
								}
							} else {
								break;
							}
						}
						break;
					}
					counter++;
				}
				eraseAllAtSigns(bombI, bombJ, range);
			}
		}
	}

	private void checkToMakeANewHeart() {
		if (secondsPassed != 0) {
			if (secondsPassed % 10 == 0) {
				System.out.println("NEW HEART IS IN THE PROCESS");
				out.println("BEING-MADE" + " " + roomId);
			}
		}
	}
	
	private void checktoDeleteAHeart() {
		
	}

	private void checktoDeleteADiamond() {
		out.println("DELETE-DIAMOND-IF-NEEDED" + " " + roomId + " " + secondsPassed);
	}

	private void checkToMakeANewDiamond() {
		out.println("DIAMOND-MADE" + " " + roomId + " " + secondsPassed);
		out.println("WHEN-DIAMOND" + " " + roomId);
	}

	private void checkTheThreeSecondsRule() {
		for (int j = 0; j < exceptedPlayerNumber.size(); j++) {
			if (secondsPassed == exceptedPlayerTimer.get(j)) {
				exceptedPlayerNumber.remove(j);
				exceptedPlayerTimer.remove(j);
				out.println("SEND-EM-ALL-TO-ME" + " " + roomId);
				System.out.println("SEND-EM-ALL-TO-ME");
			}
		}
	}

	private static void eraseAllAtSigns(int bombI, int bombJ, int range) {
		int counter = 1;
		int tempI = bombI;
		int tempJ = bombJ;
		while (counter <= 4) {
			tempI = bombI;
			tempJ = bombJ;
			switch (counter) {
			case 1:
				for (int i = 0; i < range; i++) {
					tempI--;
					if (tempI >= 1) {
						labels[tempI][tempJ].setText("");
					} else {
						break;
					}
				}
				break;
			case 2:
				for (int i = 0; i < range; i++) {
					tempI++;
					if (tempI <= tempN) {
						labels[tempI][tempJ].setText("");
					} else {
						break;
					}
				}
				break;
			case 3:
				for (int i = 0; i < range; i++) {
					tempJ++;
					if (tempJ <= tempM) {
						labels[tempI][tempJ].setText("");
					} else {
						break;
					}
				}
				break;
			case 4:
				for (int i = 0; i < range; i++) {
					tempJ--;
					if (tempJ >= 1) {
						labels[tempI][tempJ].setText("");
					} else {
						break;
					}
				}
				break;
			}
			counter++;
		}
	}

	public static void backUpPlayerIndexes() {
		tempPlayersIndexes.clear();
		for (int[] indexes : playersIndexes) {
			tempPlayersIndexes.add(indexes);
		}
	}

	public static void reDrawNewIndexes() {
		for (int i = 0; i < tempPlayersIndexes.size(); i++) {
			int lastI = tempPlayersIndexes.get(i)[0];
			int lastJ = tempPlayersIndexes.get(i)[1];
			int newI = playersIndexes.get(i)[0];
			int newJ = playersIndexes.get(i)[1];
			labels[lastI][lastJ].setIcon(null);
			String address = characterProfilePictureAddress(i);
			Image imgLbl = new ImageIcon(OnlineGame.class.getResource("/" + address)).getImage();
			if (!exceptedPlayerNumber.contains(i)) {
				labels[newI][newJ].setIcon(new ImageIcon(imgLbl));
			}
			ArrayList<Integer> indexesToDeleteFromWaitingBombs = new ArrayList<Integer>();
			if (waitingBombs.size() != 0) {
				int foreachCounter = 0;
				for (int counter : waitingBombs) {
					if (lastI == bombsIndexes.get(counter)[0] && lastJ == bombsIndexes.get(counter)[1]) {
						Image imgBomb = new ImageIcon(OnlineGame.class.getResource("/" + bombImage)).getImage();
						labels[lastI][lastJ].setIcon(new ImageIcon(imgBomb));
						indexesToDeleteFromWaitingBombs.add(foreachCounter);
					}
					foreachCounter++;
				}
				deleteWaitingBombs(indexesToDeleteFromWaitingBombs);
			}
		}
	}

	private static void drawBoxes() {
		for (int i = 0; i < boxObstaclesIndexes.size(); i++) {
			if (labels[boxObstaclesIndexes.get(i)[0]][boxObstaclesIndexes.get(i)[1]].getIcon() == null) {
				Image imgBox = new ImageIcon(OnlineGame.class.getResource("/" + boxImage)).getImage();
				labels[boxObstaclesIndexes.get(i)[0]][boxObstaclesIndexes.get(i)[1]].setIcon(new ImageIcon(imgBox));
			}
		}
	}

	private static void deleteWaitingBombs(ArrayList<Integer> indexesToDeleteFromWaitingBombs) {
		for (int indexes : indexesToDeleteFromWaitingBombs) {
			waitingBombs.remove(indexes);
		}
	}

	public void moveIt(KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_UP:
			moveUp();
			break;
		case KeyEvent.VK_DOWN:
			moveDown();
			break;
		case KeyEvent.VK_RIGHT:
			moveRight();
			break;
		case KeyEvent.VK_LEFT:
			moveLeft();
			break;
		case KeyEvent.VK_SPACE:
			spacePressed();
			break;
		}
	}

	private void moveUp() {
		if (!checkIfNewDimensionsHaveProblem(1)) {
			backUpMainCharacterDimensions();
			out.println("MOVED" + " " + 1 + " " + playerNumber + " " + roomId);
			currentI--;
		} else {
			System.out.println("UP MISSED !");
		}
	}

	private void moveDown() {
		if (!checkIfNewDimensionsHaveProblem(2)) {
			backUpMainCharacterDimensions();
			out.println("MOVED" + " " + 2 + " " + playerNumber + " " + roomId);
			currentI++;
			// checkforHeartAcievement();
		} else {
			System.out.println("DOWN MISSED !");
		}
	}

	private void moveRight() {
		if (!checkIfNewDimensionsHaveProblem(3)) {
			backUpMainCharacterDimensions();
			out.println("MOVED" + " " + 3 + " " + playerNumber + " " + roomId);
			currentJ++;
			// checkforHeartAcievement();
		} else {
			System.out.println("RIGHT MISSED !");
		}
	}

	private void moveLeft() {
		if (!checkIfNewDimensionsHaveProblem(4)) {
			backUpMainCharacterDimensions();
			out.println("MOVED" + " " + 4 + " " + playerNumber + " " + roomId);
			currentJ--;
			// checkforHeartAcievement();
		} else {
			System.out.println("LEFT MISSED !");
		}
	}

	private void spacePressed() {
		System.out.println("secondsPassed : " + secondsPassed + " / explosion at : " + (secondsPassed + 3));
		out.println("NEW-BOMB" + " " + currentI + " " + currentJ + " " + playerNumber + " " + roomId + " "
				+ (secondsPassed + 3) + " " + bombingRange);
	}

	private static boolean checkIfNewDimensionsHaveProblem(int arrow) {
		int newI = currentI;
		int newJ = currentJ;
		switch (arrow) {
		case 1:
			newI--;
			if (newI < 1 || labels[newI][newJ].getIcon() != null) {
				return true;
			}
			break;
		case 2:
			newI++;
			if (newI > tempN || labels[newI][newJ].getIcon() != null) {
				return true;
			}
			break;
		case 3:
			newJ++;
			if (newJ > tempM || labels[newI][newJ].getIcon() != null) {
				return true;
			}
			break;
		case 4:
			newJ--;
			if (newJ < 1 || labels[newI][newJ].getIcon() != null) {
				return true;
			}
			break;
		}
		return false;
	}

	private static void backUpMainCharacterDimensions() {
		tempCurrentI = currentI;
		tempCurrentJ = currentJ;
	}

	private void reDrawMatris() {
		labels[tempCurrentI][tempCurrentJ].setIcon(null);
		Image imgLbl = new ImageIcon(this.getClass().getResource("/" + mainCharacterImage)).getImage();
		labels[currentI][currentJ].setIcon(new ImageIcon(imgLbl));
	}

	public static class FrameDragListener extends MouseAdapter {

		private final JFrame frame;
		private Point mouseDownCompCoords = null;

		public FrameDragListener(JFrame frame) {
			this.frame = frame;
		}

		public void mouseReleased(MouseEvent e) {
			mouseDownCompCoords = null;
		}

		public void mousePressed(MouseEvent e) {
			mouseDownCompCoords = e.getPoint();
		}

		public void mouseDragged(MouseEvent e) {
			Point currCoords = e.getLocationOnScreen();
			frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
}
