import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class OfflineGame extends JFrame implements Runnable {

	private static JPanel contentPane;
	private JLabel lblMainCharacterLives;
	public static int n = 11;
	public static int m = 11;
	public static int tempN;
	public static int tempM;
	public static int aliensNumber = 2;
	public static JLabel labels[][];
	private static String metalImage = "Metal";
	private static String boxImage = "box.png";
	private static String mainCharacterImage = "MainCharacter.png";
	private static String alienImage = "Alien.png";
	private static String bombImage = "bomb.png";
	private static String heartImage = "heart.png";
	private static String diamondImage = "diamond.png";
	private static int mainCharacterLives = 10;
	public static JPanel gamePanel;
	private static int currentI;
	private static int currentJ;
	private static int tempCurrentI;
	private static int tempCurrentJ;
	public static ArrayList<int[]> stoneObstaclesIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> boxObstaclesIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> aliensIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> bombsIndexes = new ArrayList<int[]>();
	public static ArrayList<int[]> heartIndexes = new ArrayList<int[]>();
	public static ArrayList<Integer> bombingRangeDivision = new ArrayList<Integer>();
	private static int alienInteraction = 0;
	private static boolean gameResult = false;
	private static int usedBombs = 0;
	private static int finalTime = 0;
	public static int[] nextDiamondIndexes = { -1, -1, -1 };
	private static int bombingRange = 1;
	private static int stoneIconAddress = 0;
	private static boolean bombsAreAllSet = true;
	private static int[] bombToSetIndexes = { 0, 0 };
	public static boolean aNewHeartIsBeingMade = false;
	public static boolean aNewDiamondIsReserved = false;
	public static int nextDiamondArrival = 10;
	private static boolean gameOver = false;
	private static JLabel lblTimer;
	public static int secondsPassed = 0;
	public static Timer gameTimer = new Timer();
	public static TimerTask gameTimerTask = new TimerTask() {
		public void run() {
			secondsPassed++;
			lblTimer.setText(Integer.toString(secondsPassed));
		}
	};
	private static JLabel lblHeart2;
	private static JLabel lblHeart1;
	private static JLabel lblHeart3;
	private static JLabel lblHeart4;
	private static JLabel lblHeart5;
	private static JLabel lblHeart6;
	private static JLabel lblHeart7;
	private static JLabel lblHeart8;
	private static JLabel lblHeart9;
	private static JLabel lblHeart10;
	private static JLabel lblFinalScore;
	private static JButton btnProfile;

	/**
	 * Launch the application.
	 */
	public void run() {
		while (true) {
			if (!gameOver) {
				updateHeartIcons();
				checkForAlienInteraction();
				explosion();
				moveAliensMadafakinAsses();
				checkToMakeANewHeart();
				checktoDeleteAHeart();
				checkToMakeANewDiamond();
				checktoDeleteADiamond();
				checktoDivideBombingRange();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			} else {
				break;
			}
		}
	}

	public static void main(String[] args) {
		OfflineGame frame = new OfflineGame();
		new Thread(frame).start();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public OfflineGame() {
		gameTimer.scheduleAtFixedRate(gameTimerTask, 1000, 1000);
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
		if (n > 17 || m > 17) {
			metalImage = "Metal_2";
			boxImage = "box2.png";
			mainCharacterImage = "MainCharacter2.png";
			alienImage = "Alien2.png";
			bombImage = "bomb2.png";
			heartImage = "heart2.png";
			heartImage = "diamond2.png";
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 603, 627);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblClose = new JLabel("");
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		lblClose.setBounds(567, 2, 27, 27);
		lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		contentPane.add(lblClose);

		gamePanel = new JPanel();
		gamePanel.setBackground(new Color(30, 30, 30));
		gamePanel.setBounds(84, 175, 438, 387);
		contentPane.add(gamePanel);
		gamePanel.setLayout(new GridLayout(n, m, 0, 0));
		gamePanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent evt) {
				System.out.println("KEY PRESSED !");
				moveIt(evt);
			}
		});

		buildGameInterface();
		stoneIconAddress = labels[0][0].getIcon().getIconWidth();

		lblMainCharacterLives = new JLabel("Lives : " + mainCharacterLives);
		lblMainCharacterLives.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblMainCharacterLives.setBounds(84, 139, 77, 35);
		lblMainCharacterLives.setForeground(new Color(180, 180, 180));
		lblMainCharacterLives.setBackground(new Color(180, 180, 180));
		contentPane.add(lblMainCharacterLives);

		lblTimer = new JLabel("");
		lblTimer.setHorizontalAlignment(JTextField.CENTER);
		lblTimer.setFont(new Font("Tahoma", Font.PLAIN, 70));
		lblTimer.setForeground(new Color(182, 182, 182));
		lblTimer.setBounds(84, 64, 438, 65);
		contentPane.add(lblTimer);

		JLabel lblTime = new JLabel("Timer");
		lblTime.setHorizontalAlignment(JTextField.CENTER);
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblTime.setBounds(84, 38, 438, 27);
		lblTime.setForeground(new Color(182, 182, 182));
		contentPane.add(lblTime);
		setLocationRelativeTo(null);
		setUndecorated(true);

		Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage()
				.getScaledInstance(lblClose.getWidth(), lblClose.getHeight(), Image.SCALE_DEFAULT);
		lblClose.setIcon(new ImageIcon(imgClose));

		Image imgHeart = new ImageIcon(this.getClass().getResource("/heart.png")).getImage()
				.getScaledInstance(lblClose.getWidth(), lblClose.getHeight(), Image.SCALE_DEFAULT);
		lblHeart1 = new JLabel("");
		lblHeart1.setBounds(172, 139, 27, 35);
		lblHeart1.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart1);

		lblHeart2 = new JLabel("");
		lblHeart2.setBounds(197, 139, 27, 35);
		lblHeart2.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart2);

		lblHeart3 = new JLabel("");
		lblHeart3.setBounds(223, 139, 27, 35);
		lblHeart3.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart3);

		lblHeart4 = new JLabel("");
		lblHeart4.setBounds(249, 139, 27, 35);
		lblHeart4.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart4);

		lblHeart5 = new JLabel("");
		lblHeart5.setBounds(274, 139, 27, 35);
		lblHeart5.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart5);

		lblHeart6 = new JLabel("");
		lblHeart6.setBounds(299, 139, 27, 35);
		lblHeart6.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart6);

		lblHeart7 = new JLabel("");
		lblHeart7.setBounds(323, 139, 27, 35);
		lblHeart7.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart7);

		lblHeart8 = new JLabel("");
		lblHeart8.setBounds(349, 139, 27, 35);
		lblHeart8.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart8);

		lblHeart9 = new JLabel("");
		lblHeart9.setBounds(375, 139, 27, 35);
		lblHeart9.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart9);

		lblHeart10 = new JLabel("");
		lblHeart10.setBounds(401, 139, 27, 35);
		lblHeart10.setIcon(new ImageIcon(imgHeart));
		contentPane.add(lblHeart10);

		lblFinalScore = new JLabel("");
		lblFinalScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblFinalScore.setForeground(new Color(182, 182, 182));
		lblFinalScore.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblFinalScore.setBounds(84, 572, 221, 35);
		contentPane.add(lblFinalScore);

		btnProfile = new JButton("Profile");
		btnProfile.setFont(new Font("Tahoma", Font.PLAIN, 17));
		btnProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Profile profile = new Profile();
				dispose();
				profile.setVisible(true);
			}
		});
		btnProfile.setBounds(383, 570, 88, 39);
		btnProfile.setBackground(new Color(30, 30, 30));
		btnProfile.setForeground(new Color(182, 182, 182));
		btnProfile.setFocusable(false);
		btnProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnProfile.setVisible(false);
		contentPane.add(btnProfile);

		JLabel lblMainTheme = new JLabel("");
		lblMainTheme.setBounds(0, 0, 604, 628);
		contentPane.add(lblMainTheme);
		Image imgMainTheme = new ImageIcon(this.getClass().getResource("/ProfileTheme.jpg")).getImage()
				.getScaledInstance(lblMainTheme.getWidth(), lblMainTheme.getHeight(), Image.SCALE_DEFAULT);
		lblMainTheme.setIcon(new ImageIcon(imgMainTheme));

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
			currentI--;
			reDrawMatris();
		} else {
			if (checkforHeartAcievement(1)) {
				System.out.println("HEART ACHIEVED !");
			} else {
				if (checkforDiamondAchievement(1)) {
					System.out.println("DIAMOND ACHIEVED !");
				} else {
					System.out.println("UP MISSED !");
				}
			}
		}
	}

	private void moveDown() {
		if (!checkIfNewDimensionsHaveProblem(2)) {
			backUpMainCharacterDimensions();
			currentI++;
			reDrawMatris();
			// checkforHeartAcievement();
		} else {
			if (checkforHeartAcievement(2)) {
				System.out.println("HEART ACHIEVED !");
			} else {
				if (checkforDiamondAchievement(2)) {
					System.out.println("DIAMOND ACHIEVED !");
				} else {
					System.out.println("UP MISSED !");
				}
			}
		}
	}

	private void moveRight() {
		if (!checkIfNewDimensionsHaveProblem(3)) {
			backUpMainCharacterDimensions();
			currentJ++;
			reDrawMatris();
			// checkforHeartAcievement();
		} else {
			if (checkforHeartAcievement(3)) {
				System.out.println("HEART ACHIEVED !");
			} else {
				if (checkforDiamondAchievement(3)) {
					System.out.println("DIAMOND ACHIEVED !");
				} else {
					System.out.println("UP MISSED !");
				}
			}
		}
	}

	private void moveLeft() {
		if (!checkIfNewDimensionsHaveProblem(4)) {
			backUpMainCharacterDimensions();
			currentJ--;
			reDrawMatris();
			// checkforHeartAcievement();
		} else {
			if (checkforHeartAcievement(4)) {
				System.out.println("HEART ACHIEVED !");
			} else {
				if (checkforDiamondAchievement(4)) {
					System.out.println("DIAMOND ACHIEVED !");
				} else {
					System.out.println("UP MISSED !");
				}
			}
		}
	}

	private void spacePressed() {
		usedBombs++;
		int[] bombIndex = { currentI, currentJ, (secondsPassed + 3), bombingRange };
		bombsIndexes.add(bombIndex);
		bombsAreAllSet = false;
		bombToSetIndexes[0] = currentI;
		bombToSetIndexes[1] = currentJ;
		System.out.println("secondsPassed : " + secondsPassed + " / explosion at : " + (secondsPassed + 3));
		int counter = 0;
		int tempI = currentI;
		int tempJ = currentJ;
		while (counter <= 4) {
			tempI = currentI;
			tempJ = currentJ;
			switch (counter) {
			case 1:
				for (int i = 0; i < bombingRange; i++) {
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
				for (int i = 0; i < bombingRange; i++) {
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
				for (int i = 0; i < bombingRange; i++) {
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
				for (int i = 0; i < bombingRange; i++) {
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
	}

	private boolean checkforHeartAcievement(int arrow) {
		int tempCurrentI = currentI;
		int tempCurrentJ = currentJ;
		if (arrow == 1) {
			tempCurrentI--;
		} else if (arrow == 2) {
			tempCurrentI++;
		} else if (arrow == 3) {
			tempCurrentJ++;
		} else if (arrow == 4) {
			tempCurrentJ--;
		}
		for (int[] heart : heartIndexes) {
			if (tempCurrentI == heart[0] && tempCurrentJ == heart[1]) {
				backUpMainCharacterDimensions();
				currentI = tempCurrentI;
				currentJ = tempCurrentJ;
				reDrawMatris();
				mainCharacterLives = 10;
				updateHeartIcons();
				lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
				return true;
			}
		}
		return false;
	}

	private boolean checkforDiamondAchievement(int arrow) {
		int tempCurrentI = currentI;
		int tempCurrentJ = currentJ;
		if (arrow == 1) {
			tempCurrentI--;
		} else if (arrow == 2) {
			tempCurrentI++;
		} else if (arrow == 3) {
			tempCurrentJ++;
		} else if (arrow == 4) {
			tempCurrentJ--;
		}
		if (tempCurrentI == nextDiamondIndexes[0] && tempCurrentJ == nextDiamondIndexes[1]) {
			backUpMainCharacterDimensions();
			currentI = tempCurrentI;
			currentJ = tempCurrentJ;
			reDrawMatris();
			if (bombingRange != 8) {
				bombingRange *= 2;
				bombingRangeDivision.add(secondsPassed + 10);
			}
			System.out.println("BOOOOOOOOOOOOOOOOST !!!!!!!!!!!!!!");
			return true;
		}
		return false;
	}

	private void multiplyBombingRange() {

	}

	private void checkToMakeANewDiamond() {
		Random randLast = new Random();
		if (!aNewDiamondIsReserved) {
			nextDiamondArrival = randLast.nextInt(15) + 10 + secondsPassed;
			System.out.println("STAR AT " + nextDiamondArrival);
			aNewDiamondIsReserved = true;
		}
		if (secondsPassed == nextDiamondArrival) {
			if (nextDiamondIndexes[0] == -1 && nextDiamondIndexes[1] == -1 && nextDiamondIndexes[2] == -1) {
				int newStarI;
				int newStarJ;
				while (true) {
					newStarI = randLast.nextInt(10) + 1;
					newStarJ = randLast.nextInt(10) + 1;
					if (labels[newStarI][newStarJ].getIcon() == null) {
						nextDiamondIndexes[0] = newStarI;
						nextDiamondIndexes[1] = newStarJ;
						nextDiamondIndexes[2] = (secondsPassed + 5);
						Image imgDiamond = new ImageIcon(this.getClass().getResource(diamondImage)).getImage();
						labels[newStarI][newStarJ].setIcon(new ImageIcon(imgDiamond));
						break;
					}
				}
			}
		}

	}

	private void checktoDivideBombingRange() {
		for (int bombingDivisionTime : bombingRangeDivision) {
			if (secondsPassed == bombingDivisionTime) {
				if (bombingRange != 1) {
					bombingRange /= 2;
				}
			}
		}
	}

	private void checktoDeleteADiamond() {
		if (secondsPassed == nextDiamondIndexes[2]) {
			System.out.println("STAR TO BE DELETED !");
			labels[nextDiamondIndexes[0]][nextDiamondIndexes[1]].setIcon(null);
			nextDiamondIndexes[0] = -1;
			nextDiamondIndexes[1] = -1;
			nextDiamondIndexes[2] = -1;
			aNewDiamondIsReserved = false;
		}
	}

	private void checkToMakeANewHeart() {
		if (secondsPassed != 0) {
			if (secondsPassed % 10 == 0 && aNewHeartIsBeingMade == false) {
				aNewHeartIsBeingMade = true;
				System.out.println("NEW HEART iS IN THE HOUSE !");
				Random randLast = new Random();
				int newHeartI;
				int newHeartJ;
				while (true) {
					newHeartI = randLast.nextInt(10) + 1;
					newHeartJ = randLast.nextInt(10) + 1;
					if (labels[newHeartI][newHeartJ].getIcon() == null) {
						int[] newHeart = { newHeartI, newHeartJ, (secondsPassed + 4) };
						heartIndexes.add(newHeart);
						Image imgHeart = new ImageIcon(this.getClass().getResource(heartImage)).getImage();
						labels[newHeartI][newHeartJ].setIcon(new ImageIcon(imgHeart));
						break;
					}
				}
			} else {
				aNewHeartIsBeingMade = false;
			}
		}
	}

	private void checktoDeleteAHeart() {
		for (int i = 0; i < heartIndexes.size(); i++) {
			if (secondsPassed == heartIndexes.get(i)[2]) {
				labels[heartIndexes.get(i)[0]][heartIndexes.get(i)[1]].setIcon(null);
				heartIndexes.remove(i);
				break;
			}
		}
	}

	private void explosion() {
		for (int[] bomb : bombsIndexes) {
			int bombI = bomb[0];
			int bombJ = bomb[1];
			int range = bomb[3];
			int newBombI = bombI;
			int newBombJ = bombJ;
			if (secondsPassed == bomb[2]) {
				mainCharacterLives--;
				lblMainCharacterLives.setText("Lives : " + Integer.toString(mainCharacterLives));
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
											for (int i = 0; i < aliensIndexes.size(); i++) {
												if (newBombI == aliensIndexes.get(i)[0]
														&& newBombJ == aliensIndexes.get(i)[1]) {
													aliensIndexes.remove(i);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("currentI : " + currentI + " currentJ " + currentJ);
									System.out.println("newBombI : " + newBombI + " newBombJ : " + newBombJ);
									mainCharacterLives--;
									System.out.println("case 1");
									lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
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
											for (int i = 0; i < aliensIndexes.size(); i++) {
												if (newBombI == aliensIndexes.get(i)[0]
														&& newBombJ == aliensIndexes.get(i)[1]) {
													aliensIndexes.remove(i);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("currentI : " + currentI + " currentJ " + currentJ);
									System.out.println("newBombI : " + newBombI + " newBombJ : " + newBombJ);
									mainCharacterLives--;
									System.out.println("case 2");
									lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
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
											for (int i = 0; i < aliensIndexes.size(); i++) {
												if (newBombI == aliensIndexes.get(i)[0]
														&& newBombJ == aliensIndexes.get(i)[1]) {
													aliensIndexes.remove(i);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("currentI : " + currentI + " currentJ " + currentJ);
									System.out.println("newBombI : " + newBombI + " newBombJ : " + newBombJ);
									mainCharacterLives--;
									System.out.println("case 2");
									lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
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
											for (int i = 0; i < aliensIndexes.size(); i++) {
												if (newBombI == aliensIndexes.get(i)[0]
														&& newBombJ == aliensIndexes.get(i)[1]) {
													aliensIndexes.remove(i);
													System.out.println("KILLED ONE MOTHERFCKER");
													break;
												}
											}
										} else {
											break;
										}
									}
								} else {
									System.out.println("currentI : " + currentI + " currentJ " + currentJ);
									System.out.println("newBombI : " + newBombI + " newBombJ : " + newBombJ);
									mainCharacterLives--;
									System.out.println("case 4");
									lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
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
		if (!bombsAreAllSet) {
			Image imgBomb = new ImageIcon(this.getClass().getResource("/" + bombImage)).getImage();
			labels[bombToSetIndexes[0]][bombToSetIndexes[1]].setIcon(new ImageIcon(imgBomb));
			bombsAreAllSet = true;
		}
	}

	private void checkForAlienInteraction() {
		if (alienInteraction == 2) {
			System.out.println("INTERACTION");
			endGame(1);
		}
		if (aliensIndexes.size() == 0) {
			System.out.println("KILLED EM ALL");
			gameResult = true;
			endGame(2);
		}
		if (mainCharacterLives <= 0) {
			System.out.println("OUTTA AMO");
			endGame(3);
		}
	}

	private void moveAliensMadafakinAsses() {
		for (int k = 0; k < aliensIndexes.size(); k++) {
			int alienI = aliensIndexes.get(k)[0];
			int alienJ = aliensIndexes.get(k)[1];
			int alienLastMove = aliensIndexes.get(k)[2];
			int newAlienI = alienI;
			int newAlienJ = alienJ;
			Random randLast = new Random();
			boolean moved = false;
			boolean getOut = false;
			while (!moved && !getOut) {
				int direction = randLast.nextInt(10) + 1;
				if (direction > 4) {
					direction = alienLastMove;
				}
				newAlienI = alienI;
				newAlienJ = alienJ;
				switch (direction) {
				case 1:
					newAlienI--;
					if (!checkIfNewDimensionsHaveProblemForGhosts(alienI, alienJ, 1)) {
						int[] saveAlienIndex = { newAlienI, newAlienJ, 1 };
						aliensIndexes.remove(k);
						aliensIndexes.add(k, saveAlienIndex);
						labels[alienI][alienJ].setIcon(null);
						Image imgAlien = new ImageIcon(this.getClass().getResource(alienImage)).getImage();
						labels[newAlienI][newAlienJ].setIcon(new ImageIcon(imgAlien));
						moved = true;
					} else {
						if (currentI == newAlienI && currentJ == newAlienJ) {
							mainCharacterLives--;
							System.out.println("case 1 / 2");
							alienInteraction++;
							lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
							recreateAnAlien(alienI, alienJ);
							System.out.println("BLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCK");
							getOut = true;
						}
					}
					break;
				case 2:
					newAlienI++;
					if (!checkIfNewDimensionsHaveProblemForGhosts(alienI, alienJ, 2)) {
						int[] saveAlienIndex = { newAlienI, newAlienJ, 2 };
						aliensIndexes.remove(k);
						aliensIndexes.add(k, saveAlienIndex);
						labels[alienI][alienJ].setIcon(null);
						Image imgAlien = new ImageIcon(this.getClass().getResource(alienImage)).getImage();
						labels[newAlienI][newAlienJ].setIcon(new ImageIcon(imgAlien));
						moved = true;
					} else {
						if (currentI == newAlienI && currentJ == newAlienJ) {
							mainCharacterLives--;
							System.out.println("case 2 / 2");
							alienInteraction++;
							lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
							recreateAnAlien(alienI, alienJ);
							System.out.println("BLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCK");
							getOut = true;
						}
					}
					break;
				case 3:
					newAlienJ++;
					if (!checkIfNewDimensionsHaveProblemForGhosts(alienI, alienJ, 3)) {
						int[] saveAlienIndex = { newAlienI, newAlienJ, 3 };
						aliensIndexes.remove(k);
						aliensIndexes.add(k, saveAlienIndex);
						labels[alienI][alienJ].setIcon(null);
						Image imgAlien = new ImageIcon(this.getClass().getResource(alienImage)).getImage();
						labels[newAlienI][newAlienJ].setIcon(new ImageIcon(imgAlien));
						moved = true;
					} else {
						if (currentI == newAlienI && currentJ == newAlienJ) {
							mainCharacterLives--;
							System.out.println("case 3 / 2");
							alienInteraction++;
							lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
							recreateAnAlien(alienI, alienJ);
							System.out.println("BLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCK");
							getOut = true;
						}
					}
					break;
				case 4:
					newAlienJ--;
					if (!checkIfNewDimensionsHaveProblemForGhosts(alienI, alienJ, 4)) {
						int[] saveAlienIndex = { newAlienI, newAlienJ, 4 };
						aliensIndexes.remove(k);
						aliensIndexes.add(k, saveAlienIndex);
						labels[alienI][alienJ].setIcon(null);
						Image imgAlien = new ImageIcon(this.getClass().getResource(alienImage)).getImage();
						labels[newAlienI][newAlienJ].setIcon(new ImageIcon(imgAlien));
						moved = true;
					} else {
						if (currentI == newAlienI && currentJ == newAlienJ) {
							mainCharacterLives--;
							System.out.println("case 4 / 2");
							alienInteraction++;
							lblMainCharacterLives.setText("Lives : " + mainCharacterLives);
							recreateAnAlien(alienI, alienJ);
							System.out.println("BLOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOCK");
							getOut = true;
						}
					}
					break;
				}
			}
		}
	}

	private void updateHeartIcons() {
		int difference = 10 - mainCharacterLives;
		switch (difference) {
		case 1:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(true);
			lblHeart6.setVisible(true);
			lblHeart7.setVisible(true);
			lblHeart8.setVisible(true);
			lblHeart9.setVisible(true);
			lblHeart10.setVisible(false);
			break;
		case 2:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(true);
			lblHeart6.setVisible(true);
			lblHeart7.setVisible(true);
			lblHeart8.setVisible(true);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 3:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(true);
			lblHeart6.setVisible(true);
			lblHeart7.setVisible(true);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 4:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(true);
			lblHeart6.setVisible(true);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 5:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(true);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 6:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(true);
			lblHeart5.setVisible(false);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 7:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(true);
			lblHeart4.setVisible(false);
			lblHeart5.setVisible(false);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 8:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(true);
			lblHeart3.setVisible(false);
			lblHeart4.setVisible(false);
			lblHeart5.setVisible(false);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 9:
			lblHeart1.setVisible(true);
			lblHeart2.setVisible(false);
			lblHeart3.setVisible(false);
			lblHeart4.setVisible(false);
			lblHeart5.setVisible(false);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		case 10:
			lblHeart1.setVisible(false);
			lblHeart2.setVisible(false);
			lblHeart3.setVisible(false);
			lblHeart4.setVisible(false);
			lblHeart5.setVisible(false);
			lblHeart6.setVisible(false);
			lblHeart7.setVisible(false);
			lblHeart8.setVisible(false);
			lblHeart9.setVisible(false);
			lblHeart10.setVisible(false);
			break;
		}
	}

	private static void recreateAnAlien(int alienI, int alienJ) {
		labels[alienI][alienJ].setIcon(null);
		Random randLast = new Random();
		int counter = 0;
		for (int[] indexes : aliensIndexes) {
			if (indexes[0] == alienI && indexes[1] == alienJ) {
				aliensIndexes.remove(counter);
				boolean found = false;
				while (!found) {
					int newI = randLast.nextInt(tempN) + 1;
					int newJ = randLast.nextInt(tempM) + 1;
					if (labels[newI][newJ].getIcon() == null) {
						int rndLastMove = randLast.nextInt(4) + 1;
						int[] newIndexes = { newI, newJ, rndLastMove };
						aliensIndexes.add(counter, newIndexes);
						System.out.println(
								"Indexes " + alienI + " / " + alienJ + " have been expired to " + newI + " / " + newJ);
						found = true;
					}
				}
				break;
			}
			counter++;
		}
	}

	private static boolean checkIfNewDimensionsHaveProblemForGhosts(int currentI, int currentJ, int arrow) {
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

	private static void endGame(int order) {
		gameOver = true;
		finalTime = secondsPassed;
		contentPane.remove(lblTimer);
		String finalRecord = calculateRecord();
		
		UIManager UI = new UIManager();
		UI.put("OptionPane.background", new ColorUIResource(30, 30, 30));
		UI.put("OptionPane.messageForeground", new ColorUIResource(182, 182, 182));
		UI.put("Panel.background", new ColorUIResource(30, 30, 30));
		UI.put("OptionPane.border", new LineBorder(new Color(180, 180, 180), 4));
		
		if (gameResult == true) {
			JOptionPane.showMessageDialog(null, "YOU WON ! Final Score : " + finalRecord);
		} else {
			if (alienInteraction == 2) {
				JOptionPane.showMessageDialog(null,
						"YOU LOST ! Final Score : " + finalRecord + "   (Alien Interaction)");
			} else {
				JOptionPane.showMessageDialog(null, "YOU LOST ! Final Score : " + finalRecord);
			}
		}
		lblFinalScore.setText("Final Score : " + finalRecord);
		btnProfile.setVisible(true);
	}

	private static String calculateRecord() {
		double finalScore;
		double top = Math.pow(aliensNumber, (gameResult) ? 4 : 1);
		double bottom = (usedBombs != 0) ? finalTime + logarithmBasedTwo(usedBombs, 2) : finalTime;
		finalScore = top / bottom;
		return cleanFinalScore(finalScore);
	}

	private static String cleanFinalScore(double givenFinalScore) {
		String givenFinalScoreString = Double.toString(givenFinalScore);
		if (givenFinalScoreString.charAt(0) != '0') {
			return Character.toString(givenFinalScoreString.charAt(0))
					+ Character.toString(givenFinalScoreString.charAt(2))
					+ Character.toString(givenFinalScoreString.charAt(3));
		} else {
			if (givenFinalScoreString.charAt(2) == '0') {
				return Character.toString(givenFinalScoreString.charAt(3));
			} else {
				return Character.toString(givenFinalScoreString.charAt(2))
						+ Character.toString(givenFinalScoreString.charAt(3));
			}
		}
	}

	private static int logarithmBasedTwo(int x, int base) {
		return (int) (Math.log(x) / Math.log(base));
	}

	private void buildGameInterface() {
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
					Image imgLbl = new ImageIcon(this.getClass().getResource(imgAddress)).getImage();
					labels[i][j].setIcon(new ImageIcon(imgLbl));
				} else {
					labels[i][j].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					if (i == 1 && j == 1) {
						Image imgLbl = new ImageIcon(this.getClass().getResource("/" + mainCharacterImage)).getImage();
						labels[i][j].setIcon(new ImageIcon(imgLbl));
						currentI = 1;
						currentJ = 1;
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
								Image imgLbl = new ImageIcon(this.getClass().getResource(imgAddress)).getImage();
								labels[i][j].setIcon(new ImageIcon(imgLbl));
								oneAfterOneJ = false;
							} else {
								int rnd = randLast.nextInt(10) + 1;
								String imgAddress = "";
								if (rnd == 1 || rnd == 2) {
									imgAddress = boxImage;
									Image imgLbl = new ImageIcon(this.getClass().getResource(imgAddress)).getImage();
									labels[i][j].setIcon(new ImageIcon(imgLbl));
								}
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
		for (int k = 0; k < aliensNumber; k++) {
			while (true) {
				int randomI = randLast.nextInt(tempN) + 1;
				int randomJ = randLast.nextInt(tempM) + 1;
				if (labels[randomI][randomJ].getIcon() == null) {
					int rndLastMove = randLast.nextInt(4) + 1;
					int[] saveAlienIndex = { randomI, randomJ, rndLastMove };
					aliensIndexes.add(saveAlienIndex);
					Image imgAlien = new ImageIcon(this.getClass().getResource(alienImage)).getImage();
					labels[randomI][randomJ].setIcon(new ImageIcon(imgAlien));
					break;
				}
			}
		}
	}
}
