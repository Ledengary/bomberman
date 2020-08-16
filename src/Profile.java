import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

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

import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Profile extends JFrame {

	private JPanel contentPane;
	private final JLabel lblMainTheme = new JLabel("");
	private static BufferedReader in;
	private static PrintWriter out;
	private static Socket socket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					System.out.println("Profile Main");
					Profile frame = new Profile();
					FrameDragListener frameDragListener = new FrameDragListener(frame);
					frame.addMouseListener((MouseListener) frameDragListener);
					frame.addMouseMotionListener((MouseMotionListener) frameDragListener);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Profile() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 603, 627);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setUndecorated(true);
		setLocationRelativeTo(null);

		JLabel lblClose = new JLabel("");
		lblClose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		lblClose.setBounds(567, 2, 27, 27);
		contentPane.add(lblClose);

		JButton btnOffline = new JButton("PLAY OFFLINE");
		btnOffline.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOffline.setForeground(new Color(182, 182, 182));
		btnOffline.setBackground(new Color(30, 30, 30));
		btnOffline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIManager UI = new UIManager();
				UI.put("OptionPane.background", new ColorUIResource(30, 30, 30));
				UI.put("OptionPane.messageForeground", new ColorUIResource(182, 182, 182));
				UI.put("Panel.background", new ColorUIResource(30, 30, 30));
				UI.put("OptionPane.border", new LineBorder(new Color(180, 180, 180), 4));
				// Image imgMainTheme = new
				// ImageIcon(this.getClass().getResource("/SignInTheme.jpg")).getImage();
				// UI.put("OptionPane.informationIcon", new ImageIcon(imgMainTheme));

				Object[] options1 = { "OK" };

				JPanel panel = new JPanel();
				panel.setBackground(new Color(30, 30, 30));
				panel.setForeground(new Color(180, 180, 180));
				JLabel label = new JLabel("Enter Height Cell's number :    ");
				label.setForeground(new Color(180, 180, 180));
				panel.add(label);
				JTextField textField = new JTextField(10);
				textField.setForeground(new Color(180, 180, 180));
				textField.setBackground(new Color(30, 30, 30));
				panel.add(textField);
				int heightCellsNumber;
				int widthCellsNumber;
				int aliensNumber;
				try {
					JOptionPane.showOptionDialog(null, panel, "Height", JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options1, null);
					heightCellsNumber = Integer.parseInt(textField.getText());
					try {
						panel.remove(label);
						panel.remove(textField);
						label.setText("Enter Width Cell's number :    ");
						textField.setText("");
						panel.add(label);
						panel.add(textField);
						JOptionPane.showOptionDialog(null, panel, "Width", JOptionPane.YES_NO_CANCEL_OPTION,
								JOptionPane.PLAIN_MESSAGE, null, options1, null);
						widthCellsNumber = Integer.parseInt(textField.getText());
						try {
							panel.remove(label);
							panel.remove(textField);
							label.setText("Enter Aliens number :    ");
							textField.setText("");
							panel.add(label);
							panel.add(textField);
							JOptionPane.showOptionDialog(null, panel, "Width", JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.PLAIN_MESSAGE, null, options1, null);
							aliensNumber = Integer.parseInt(textField.getText());
							try {
								panel.remove(label);
								panel.remove(textField);
								label.setText("Start an Offline game with h(" + heightCellsNumber + ") , w("
										+ widthCellsNumber + ") and a(" + aliensNumber + ") ?         ");
								textField.setText("");
								panel.add(label);
								int dialogButton = 0;
								int dialogResult = JOptionPane.showConfirmDialog(null, label, "Warning", dialogButton);
								if (dialogResult == JOptionPane.YES_OPTION) {
									System.out.println("YES CLICKED !");
									OfflineGame offGame = new OfflineGame();
									dispose();
									offGame.setVisible(true);
								}
							} catch (Exception e3) {
								System.out.println("enter a number for Aliens");
							}
						} catch (Exception e3) {
							System.out.println("enter a number for Aliens");
						}
					} catch (Exception e2) {
						System.out.println("enter a number for widthCellsNumber");
					}
				} catch (Exception e2) {
					System.out.println("enter a number for heightCellsNumber");
				}

			}
		});
		btnOffline.setBounds(22, 523, 276, 80);
		contentPane.add(btnOffline);
		Image imgClose = new ImageIcon(this.getClass().getResource("/close.png")).getImage()
				.getScaledInstance(lblClose.getWidth(), lblClose.getHeight(), Image.SCALE_DEFAULT);
		lblClose.setIcon(new ImageIcon(imgClose));
		lblClose.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JButton btnOnline = new JButton("PLAY ONLNE");
		btnOnline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UIManager UI = new UIManager();
				UI.put("OptionPane.background", new ColorUIResource(30, 30, 30));
				UI.put("OptionPane.messageForeground", new ColorUIResource(182, 182, 182));
				UI.put("Panel.background", new ColorUIResource(30, 30, 30));
				UI.put("OptionPane.border", new LineBorder(new Color(180, 180, 180), 4));

				Object[] options1 = { "JOIN", "HOST" };
				JPanel panel = new JPanel();
				panel.setBackground(new Color(30, 30, 30));
				panel.setForeground(new Color(180, 180, 180));
				JLabel label = new JLabel("Choose Your Character");
				label.setHorizontalAlignment(JTextField.CENTER);
				label.setForeground(new Color(180, 180, 180));
				panel.add(label);
				int returnedValue = JOptionPane.showOptionDialog(null, panel, "Character",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options1, null);
				System.out.println(returnedValue + " returned...");
				if (returnedValue == 1) {
					/**
					 * karbar daraye do bakhsh e, ke agar ro host click kone adress va name karbar
					 * azash porside mishe va socket i ba moshakhasat dade shode sakhte mishe...
					 */
					// String serverAddress = JOptionPane.showInputDialog(btnOnline, "Enter server
					// address:", "Wellcome",
					// JOptionPane.QUESTION_MESSAGE);
					String serverAddress = "127.0.0.1";
					try {
						socket = new Socket(serverAddress, 9512);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream(), true);
						out.println("HOST");
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					while (true) {
						String line = null;
						String hostName = "";
						try {
							line = in.readLine();
						} catch (IOException e3) {
							e3.printStackTrace();
						}
						if (line.startsWith("SUBMIT-NAME")) {
							Object[] options3 = { "OK" };
							JPanel panel3 = new JPanel();
							panel3.setBackground(new Color(30, 30, 30));
							panel3.setForeground(new Color(180, 180, 180));
							JLabel label3 = new JLabel("Enter Your  Name :    ");
							label3.setForeground(new Color(180, 180, 180));
							panel3.add(label3);
							JTextField textField3 = new JTextField(10);
							textField3.setForeground(new Color(180, 180, 180));
							textField3.setBackground(new Color(30, 30, 30));
							panel3.add(textField3);
							JOptionPane.showOptionDialog(null, panel3, "Height", JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.PLAIN_MESSAGE, null, options3, null);
							hostName = textField3.getText();
							out.println(hostName);
							out.flush();
							setTitle(hostName);
						} else if (line.startsWith("NAME-ALREADY-EXISTS")) {
							JOptionPane.showMessageDialog(null, "Entered Name Already Exists !");
						} else if (line.startsWith("SUBMIT-ROOM-COUNT")) {
							boolean doneMainScope = false;
							while (!doneMainScope) {
								Object[] options3 = { "OK" };
								JPanel panel3 = new JPanel();
								panel3.setBackground(new Color(30, 30, 30));
								panel3.setForeground(new Color(180, 180, 180));
								JLabel label3 = new JLabel("Enter Room Capacity :    ");
								label3.setForeground(new Color(180, 180, 180));
								panel3.add(label3);
								JTextField textField3 = new JTextField(10);
								textField3.setForeground(new Color(180, 180, 180));
								textField3.setBackground(new Color(30, 30, 30));
								panel3.add(textField3);
								JOptionPane.showOptionDialog(null, panel3, "Room Capacity",
										JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options3,
										null);
								String roomCapacity = textField3.getText();
								if (Integer.parseInt(roomCapacity) >= 2 && Integer.parseInt(roomCapacity) <= 4) {
									boolean doneSecondScope = false;
									while (!doneSecondScope) {
										panel3.remove(label3);
										panel3.remove(textField3);
										label3.setText("Enter Height Cell's number :    ");
										textField3.setText("");
										panel3.add(label3);
										panel3.add(textField3);
										JOptionPane.showOptionDialog(null, panel3, "Height",
												JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null,
												options3, null);
										int height = Integer.parseInt(textField3.getText());
										if (height > 0) {
											while (true) {
												panel3.remove(label3);
												panel3.remove(textField3);
												label3.setText("Enter Width Cell's number :    ");
												textField3.setText("");
												panel3.add(label3);
												panel3.add(textField3);
												JOptionPane.showOptionDialog(null, panel3, "Width",
														JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
														null, options3, null);
												int width = Integer.parseInt(textField3.getText());
												if (width > 0) {
													out.println("RC" + " " + roomCapacity + " " + height + " " + width);
													doneSecondScope = true;
													doneMainScope = true;
													break;
												}
											}
										}
									}
								}
							}
						} else if (line.startsWith("SET")) {
							line = line.substring(4, line.length());
							System.out.println(line);
							String[] names = line.split("_");
							String[] ConnectedTo = names[1].split("@");
							String connectedToFinal = "";
							for (String others : ConnectedTo) {
								connectedToFinal += " and " + others;
							}
							connectedToFinal = connectedToFinal.substring(5);
							JOptionPane.showMessageDialog(null,
									"[" + names[0] + "]: You've Been Connected to " + connectedToFinal + " !    ");
							int roomId = Integer.parseInt(names[2]);
							int playerNumber = Integer.parseInt(names[3]);
							OnlineGame.main(socket, serverAddress, names[0], playerNumber, roomId);
							dispose();
							break;
						}
					}
				} else if (returnedValue == 0) {
					/**
					 * agar karbar rooye dokme join clickkonad baz ham socket i sakhte mishe ke
					 * neshani join ra darast be server darkhast join shodanash ra dade va server
					 * list i az host konande haye mojood ra barmigardanad va montazer host entekhab
					 * shode az karbar mimanad
					 */
					// String serverAddress = JOptionPane.showInputDialog(btnOnline, "Enter server
					// address:", "Wellcome",
					// JOptionPane.QUESTION_MESSAGE);
					String serverAddress = "127.0.0.1";
					try {
						socket = new Socket(serverAddress, 9512);
						in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out = new PrintWriter(socket.getOutputStream(), true);
						out.println("JOIN");
					} catch (IOException e2) {
						e2.printStackTrace();
					}

					JComboBox jcd = null;
					while (true) {
						// receive clients input
						String line = null;
						String[] betweenUnderlines;
						ArrayList<String> availableHostsToShow = new ArrayList<String>();
						String joinName = "";
						try {
							line = in.readLine();
						} catch (IOException e3) {
							e3.printStackTrace();
						}

						// example for text protocol
						if (line.startsWith("SUBMIT-NAME")) {
							Object[] options3 = { "OK" };
							JPanel panel3 = new JPanel();
							panel3.setBackground(new Color(30, 30, 30));
							panel3.setForeground(new Color(180, 180, 180));
							JLabel label3 = new JLabel("Enter Your  Name :    ");
							label3.setForeground(new Color(180, 180, 180));
							panel3.add(label3);
							JTextField textField3 = new JTextField(10);
							textField3.setForeground(new Color(180, 180, 180));
							textField3.setBackground(new Color(30, 30, 30));
							panel3.add(textField3);
							JOptionPane.showOptionDialog(null, panel3, "Height", JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.PLAIN_MESSAGE, null, options3, null);
							joinName = textField3.getText();
							out.println(joinName);
							out.flush();
							setTitle(joinName);

						} else if (line.startsWith("NAMES-ON-THE-WAY")) {
							line = line.substring(17, line.length());
							System.out.println(line + " came...");
							betweenUnderlines = line.split("_");
							for (int i = 0; i < betweenUnderlines.length; i++) {
								String[] betweenSign = betweenUnderlines[i].split("@");
								availableHostsToShow.add(betweenSign[0] + "   (" + (Integer.parseInt(betweenSign[1]))
										+ " Spots Available)");
							}
							jcd = new JComboBox(availableHostsToShow.toArray());

							String[] options = { "OK" };
							JPanel panel2 = new JPanel();
							JLabel lbl = new JLabel("Available Hosts : ");
							lbl.setForeground(new Color(182, 182, 182));
							JTextField txt = new JTextField(10);
							panel2.add(lbl);
							panel2.add(jcd);
							while (true) {
								int selectedOption = JOptionPane.showOptionDialog(null, panel2, "The Title",
										JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
								System.out.println(selectedOption);
								if (selectedOption == 0) {
									String[] parts = betweenUnderlines[jcd.getSelectedIndex()].split("@");
									if (Integer.parseInt(parts[1]) != 0) {
										out.println(jcd.getSelectedIndex());
										break;
									} else {
										JOptionPane.showMessageDialog(null, "Selected Room is full !");
									}
								} else if (selectedOption == -1) {
									break;
								}
							}
						} else if (line.startsWith("SET")) {
							line = line.substring(4, line.length());
							System.out.println(line);
							String[] names = line.split("_");
							String[] ConnectedTo = names[1].split("@");
							String connectedToFinal = "";
							for (String others : ConnectedTo) {
								connectedToFinal += " and " + others;
							}
							connectedToFinal = connectedToFinal.substring(5);
							JOptionPane.showMessageDialog(null,
									"[" + names[0] + "]: You've Been Connected to " + connectedToFinal + " !    ");
							int roomId = Integer.parseInt(names[2]);
							int playerNumber = Integer.parseInt(names[3]);
							OnlineGame.main(socket, serverAddress, names[0], playerNumber, roomId);
							dispose();
							break;
						} else {
							System.out.println(line + " :|");
						}
					}
				} else {
					return;
				}
			}
		});
		btnOnline.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOnline.setForeground(new Color(182, 182, 182));
		btnOnline.setBackground(new Color(30, 30, 30));
		btnOnline.setBounds(309, 523, 276, 80);
		contentPane.add(btnOnline);
		btnOffline.setFocusable(false);
		btnOnline.setFocusable(false);
		btnOffline.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnOnline.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JLabel lblProfilePicture = new JLabel("");
		lblProfilePicture.setBounds(199, 44, 210, 210);
		contentPane.add(lblProfilePicture);
		lblMainTheme.setBounds(0, 0, 604, 628);
		contentPane.add(lblMainTheme);
		Image imgProfilePicture = new ImageIcon(this.getClass().getResource("/ContactGrey.png")).getImage()
				.getScaledInstance(lblProfilePicture.getWidth(), lblProfilePicture.getHeight(), Image.SCALE_DEFAULT);
		lblProfilePicture.setIcon(new ImageIcon(imgProfilePicture));
		Image imgMainTheme = new ImageIcon(this.getClass().getResource("/ProfileTheme.jpg")).getImage()
				.getScaledInstance(lblMainTheme.getWidth(), lblMainTheme.getHeight(), Image.SCALE_DEFAULT);
		lblMainTheme.setIcon(new ImageIcon(imgMainTheme));
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
