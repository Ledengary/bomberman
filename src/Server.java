import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Server {
	private static int port = 9512;
	public static ServerSocket listener;
	private static ArrayList<String> hostUsersName = new ArrayList<String>();
	public static ArrayList<Integer> hostCapacity = new ArrayList<Integer>();
	public static ArrayList<Integer> hostCapacityAvailable = new ArrayList<Integer>();
	private static ArrayList<Socket> hostUsersSocket = new ArrayList<Socket>();
	private static ArrayList<String> joinUsersName = new ArrayList<String>();
	private static ArrayList<Socket> joinUsersSocket = new ArrayList<Socket>();
	public static ArrayList<Socket[]> rooms = new ArrayList<Socket[]>();
	public static ArrayList<String[]> roomsNames = new ArrayList<String[]>();
	public static ArrayList<Integer> roomsCapacity = new ArrayList<Integer>();
	public static ArrayList<int[]> roomSize = new ArrayList<int[]>();
	public static ArrayList<String[]> roomsPlayersIndexes = new ArrayList<String[]>();
	public static ArrayList<boolean[]> roomsEveryOnePresentOrNot = new ArrayList<boolean[]>();
	public static ArrayList<Integer> roomsAvailability = new ArrayList<Integer>();
	public static ArrayList<int[]> roomsMainCharacterLives = new ArrayList<int[]>();
	public static ArrayList<String[]> roomsBoxes = new ArrayList<String[]>();
	public static ArrayList<int[]> heartIndexes = new ArrayList<int[]>();
	public static ArrayList<Boolean> aNewHeartIsBeingMade = new ArrayList<Boolean>();
	public static ArrayList<Boolean> aNewDiamondIsReserved = new ArrayList<Boolean>();
	public static ArrayList<Integer> nextDiamondArrival = new ArrayList<Integer>();
	public static ArrayList<int[]> nextDiamondIndexes = new ArrayList<int[]>();

	public Server(int port) throws IOException {
		this.port = port;
		listener = new ServerSocket(port);
		System.out.println("the server is running :D (Constructor)");
		try {
			// waiting for clients to connect
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("CAME 2");
		listener = new ServerSocket(port);
		System.out.println("the server is running :D (MAIN)");
		try {
			// waiting for clients to connect
			while (true) {
				new Handler(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class Handler extends Thread {
		private String name;
		private Socket socket;
		private BufferedReader in;
		private PrintWriter out;

		// set socket
		public Handler(Socket socket) throws IOException {
			this.socket = socket;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		}

		public void run() {
			try {
				String type = in.readLine();
				System.out.println("Type : " + type);
				if (type.startsWith("HOST")) {
					out.println("SUBMIT-NAME");
					while (true) {
						name = in.readLine();
						if (!hostUsersName.contains(name)) {
							hostUsersName.add(name);
							hostUsersSocket.add(socket);
							out.println("SUBMIT-ROOM-COUNT");
							String roomCapacity = in.readLine();
							if (roomCapacity.startsWith("RC")) {
								String[] parts = roomCapacity.split(" ");
								roomCapacity = parts[1];
								int height = Integer.parseInt(parts[2]);
								int width = Integer.parseInt(parts[3]);
								hostCapacity.add(Integer.parseInt(roomCapacity));
								hostCapacityAvailable.add(Integer.parseInt(roomCapacity) - 1);
								roomsAvailability.add(Integer.parseInt(roomCapacity) - 1);
								int[] size = { height, width };
								roomSize.add(size);
								System.out.println("Room Capacity of " + roomCapacity + " added for " + name + " !");
								switch (Integer.parseInt(roomCapacity)) {
								case 2:
									Socket[] newRoom2 = { socket, null };
									String[] newRoomNames2 = { name, null };
									String[] newRoomIndexes2 = { "1@1", "1" + "@" + Integer.toString(width) };
									int[] newLives2 = { 10, 10 };
									int[] diamond2 = { -1, -1, -1 };
									boolean[] everyOnePresentOrNot2 = { true, false };
									rooms.add(newRoom2);
									roomsNames.add(newRoomNames2);
									roomsPlayersIndexes.add(newRoomIndexes2);
									roomsCapacity.add(Integer.parseInt(roomCapacity));
									roomsAvailability.add(Integer.parseInt(roomCapacity));
									roomsEveryOnePresentOrNot.add(everyOnePresentOrNot2);
									roomsMainCharacterLives.add(newLives2);
									roomsBoxes.add(boxMaker(height, width));
									aNewHeartIsBeingMade.add(false);
									aNewDiamondIsReserved.add(false);
									nextDiamondArrival.add(10);
									nextDiamondIndexes.add(diamond2);
									break;
								case 3:
									Socket[] newRoom3 = { socket, null, null };
									String[] newRoomNames3 = { name, null, null };
									String[] newRoomIndexes3 = { "1@1", "1" + "@" + Integer.toString(width),
											Integer.toString(height) + "@" + "1" };
									int[] newLives3 = { 10, 10, 10 };
									int[] diamond3 = { -1, -1, -1 };
									boolean[] everyOnePresentOrNot3 = { true, false, false };
									rooms.add(newRoom3);
									roomsNames.add(newRoomNames3);
									roomsPlayersIndexes.add(newRoomIndexes3);
									roomsCapacity.add(Integer.parseInt(roomCapacity));
									roomsAvailability.add(Integer.parseInt(roomCapacity));
									roomsEveryOnePresentOrNot.add(everyOnePresentOrNot3);
									roomsMainCharacterLives.add(newLives3);
									roomsBoxes.add(boxMaker(height, width));
									aNewHeartIsBeingMade.add(false);
									aNewDiamondIsReserved.add(false);
									nextDiamondArrival.add(10);
									nextDiamondIndexes.add(diamond3);
									break;
								case 4:
									Socket[] newRoom4 = { socket, null, null, null };
									String[] newRoomNames4 = { name, null, null, null };
									String[] newRoomIndexes4 = { "1@1", "1" + "@" + Integer.toString(width),
											Integer.toString(height) + "@" + "1",
											Integer.toString(height) + "@" + Integer.toString(width) };
									int[] newLives4 = { 10, 10, 10, 10 };
									int[] diamond4 = { -1, -1, -1 };
									boolean[] everyOnePresentOrNot4 = { true, false, false, false };
									rooms.add(newRoom4);
									roomsNames.add(newRoomNames4);
									roomsPlayersIndexes.add(newRoomIndexes4);
									roomsCapacity.add(Integer.parseInt(roomCapacity));
									roomsAvailability.add(Integer.parseInt(roomCapacity));
									roomsEveryOnePresentOrNot.add(everyOnePresentOrNot4);
									roomsMainCharacterLives.add(newLives4);
									roomsBoxes.add(boxMaker(height, width));
									aNewHeartIsBeingMade.add(false);
									aNewDiamondIsReserved.add(false);
									nextDiamondArrival.add(10);
									nextDiamondIndexes.add(diamond4);
									break;
								}
							}
						} else {
							out.println("NAME-ALREADY-EXISTS");
						}
					}
				} else if (type.startsWith("JOIN")) {
					while (true) {
						out.println("SUBMIT-NAME");
						name = in.readLine();
						if (!hostUsersName.contains(name)) {
							joinUsersName.add(name);
							joinUsersSocket.add(socket);
							String hostUsersNames = null;
							for (int i = 0; i < hostUsersName.size(); i++) {
								hostUsersNames += "_" + hostUsersName.get(i) + "@" + hostCapacityAvailable.get(i);
							}
							hostUsersNames = hostUsersNames.substring(5, hostUsersNames.length());
							out.println("NAMES-ON-THE-WAY" + " " + hostUsersNames);
							int getSelectedRowOfHostSockets = Integer.parseInt(in.readLine());
							System.out.println(name + " and " + hostUsersName.get(getSelectedRowOfHostSockets)
									+ " got selected... (Server)");
							rooms.get(getSelectedRowOfHostSockets)[rooms.get(getSelectedRowOfHostSockets).length
									- hostCapacityAvailable.get(getSelectedRowOfHostSockets)] = socket;
							roomsNames
									.get(getSelectedRowOfHostSockets)[roomsNames.get(getSelectedRowOfHostSockets).length
											- hostCapacityAvailable.get(getSelectedRowOfHostSockets)] = name;
							hostCapacityAvailable.set(getSelectedRowOfHostSockets,
									hostCapacityAvailable.get(getSelectedRowOfHostSockets) - 1);
							System.out.println("ROOM UPDATED !");
							System.out.println("---------------------------------------------------0");
							for (int i = 0; i < roomsNames.size(); i++) {
								for (int j = 0; j < roomsNames.get(i).length; j++) {
									System.out.println(roomsNames.get(i)[j] + " => " + rooms.get(i)[j]);
								}
								System.out.println("");
							}
							System.out.println("---------------------------------------------------1");
							System.out.println("---------------------------------------------------0");
							for (int i = 0; i < roomsEveryOnePresentOrNot.size(); i++) {
								for (int j = 0; j < roomsEveryOnePresentOrNot.get(i).length; j++) {
									System.out.println(
											roomsNames.get(i)[j] + " => " + roomsEveryOnePresentOrNot.get(i)[j]);
								}
								System.out.println("");
							}
							System.out.println("---------------------------------------------------1");
							if (hostCapacityAvailable.get(getSelectedRowOfHostSockets) == 0) {
								int roomId = getSelectedRowOfHostSockets;
								for (int i = 0; i < rooms.get(getSelectedRowOfHostSockets).length; i++) {
									String playerName = roomsNames.get(getSelectedRowOfHostSockets)[i];
									Socket hostSocket = rooms.get(getSelectedRowOfHostSockets)[i];
									PrintWriter outToRoomPlayer = new PrintWriter(hostSocket.getOutputStream(), true);
									String othersName = "";
									for (int j = 0; j < roomsNames.get(getSelectedRowOfHostSockets).length; j++) {
										if (i != j) {
											othersName += "@" + roomsNames.get(getSelectedRowOfHostSockets)[j];
										}
									}
									othersName = othersName.substring(1);
									outToRoomPlayer.println(
											"SET" + " " + playerName + "_" + othersName + "_" + roomId + "_" + i);
								}
							}
							break;
						} else {
							out.println("NAME-ALREADY-EXISTS");
						}
					}
				} else if (type.startsWith("SETTING-UP-GAME-PAGE")) {
					System.out.println("SERVER : SETTING-UP-GAME-PAGE");
					/**
					 * hala omadim too safhe bazi va darim sohbat mikonimba client haye dar hale
					 * bazi kardan
					 */
					while (true) {
						String order = in.readLine();
						System.out.println("Order : " + order);
						if (order.startsWith("UPDATE-SOCKET")) {
							String line = order.substring(15, order.length());
							System.out.println("Line : " + line);
							String[] parts = line.split("_");
							rooms.get(Integer.parseInt(parts[0]))[Integer.parseInt(parts[1])] = socket;
							System.out.println("Room updated ...");
						} else if (order.startsWith("GAME-PAGE-REQUEST-NAME")) {
							String line = order;
							int roomNamesId = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1));
							out.println("GET-NAME" + " " + roomsNames.get(roomNamesId)[line.charAt(23) - '0']);
							System.out.println("roomId : " + roomNamesId + " and "
									+ roomsEveryOnePresentOrNot.get(roomNamesId).length + " and "
									+ roomsAvailability.get(roomNamesId));
							roomsEveryOnePresentOrNot
									.get(roomNamesId)[(roomsEveryOnePresentOrNot.get(roomNamesId).length - 1)
											- roomsAvailability.get(roomNamesId)] = true;
							roomsAvailability.set(roomNamesId, roomsAvailability.get(roomNamesId) - 1);
							checkIfEveryOneIsPresent(roomNamesId);
							out.println("SET-UP-LIVES" + " " + hostCapacity.get(roomNamesId));
							break;
						}
					}
					while (true) {
						String order = in.readLine();
						if (order.startsWith("SYNC-INTERFACES")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							out.println("ROOM-CAPACITY" + " " + roomsCapacity.get(roomId));
							switch (roomsCapacity.get(roomId)) {
							case 2:
								out.println("RECIEVE-INDEXES" + " " + roomsPlayersIndexes.get(roomId)[0] + " "
										+ roomsPlayersIndexes.get(roomId)[1]);
								break;
							case 3:
								out.println("RECIEVE-INDEXES" + " " + roomsPlayersIndexes.get(roomId)[0] + " "
										+ roomsPlayersIndexes.get(roomId)[1] + " "
										+ roomsPlayersIndexes.get(roomId)[2]);
								break;

							case 4:
								out.println("RECIEVE-INDEXES" + " " + roomsPlayersIndexes.get(roomId)[0] + " "
										+ roomsPlayersIndexes.get(roomId)[1] + " " + roomsPlayersIndexes.get(roomId)[2]
										+ " " + roomsPlayersIndexes.get(roomId)[3]);
								break;
							}
						} else if (order.startsWith("MOVED")) {
							order = order.substring(6);
							String[] parts = order.split(" ");
							int movedTo = Integer.parseInt(parts[0]);
							int playerNumber = Integer.parseInt(parts[1]);
							int roomId = Integer.parseInt(parts[2]);
							String oldIndexes = roomsPlayersIndexes.get(roomId)[playerNumber];
							String[] oldIndexesParts = oldIndexes.split("@");
							int playerIndexI = Integer.parseInt(oldIndexesParts[0]);
							int playerIndexJ = Integer.parseInt(oldIndexesParts[1]);
							switch (movedTo) {
							case 1:
								playerIndexI--;
								break;
							case 2:
								playerIndexI++;
								break;
							case 3:
								playerIndexJ++;
								break;
							case 4:
								playerIndexJ--;
								break;
							}
							String newIndexes = Integer.toString(playerIndexI) + "@" + Integer.toString(playerIndexJ);
							roomsPlayersIndexes.get(roomId)[playerNumber] = newIndexes;
							sendNewIndexesToAll(roomId);
						} else if (order.startsWith("NEW-BOMB")) {
							order = order.substring(9);
							String[] parts = order.split(" ");
							int bombI = Integer.parseInt(parts[0]);
							int bombJ = Integer.parseInt(parts[1]);
							int playerNumber = Integer.parseInt(parts[2]);
							int roomId = Integer.parseInt(parts[3]);
							int explosionAtTime = Integer.parseInt(parts[4]);
							int bombingRange = Integer.parseInt(parts[5]);
							sendNewBombIndexToAll(bombI, bombJ, playerNumber, roomId, explosionAtTime, bombingRange);
						} else if (order.startsWith("SEND-EM-ALL-TO-ME")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							sendNewIndexesToAll(roomId);
						} else if (order.startsWith("DBOX")) {
							String[] parts = order.split(" ");
							String sentIndexes = parts[1];
							int roomId = Integer.parseInt(parts[2]);
							String storedIndexes = "";
							String newStoredIndexes = "";
							for (int i = 0; i < roomsBoxes.get(roomId).length; i++) {
								storedIndexes += " " + roomsBoxes.get(roomId)[i];
							}
							storedIndexes = storedIndexes.substring(1);
							String[] storedIndexesParts = storedIndexes.split(" ");
							for (int i = 0; i < storedIndexesParts.length; i++) {
								if (!sentIndexes.equals(storedIndexesParts[i])) {
									newStoredIndexes += " " + storedIndexesParts[i];
								}
							}
							String[] newStoredIndexesParts = newStoredIndexes.split(" ");
							roomsBoxes.set(roomId, newStoredIndexesParts);
							sendToAllBoxIndexes(roomId);
						} else if (order.startsWith("DEAD-BODY")) {
							String[] parts = order.split(" ");
							int killedPlayer = Integer.parseInt(parts[1]);
							int roomId = Integer.parseInt(parts[2]);
							sendToAllHoldOn(roomId, killedPlayer);
							roomsMainCharacterLives
									.get(roomId)[killedPlayer] = roomsMainCharacterLives.get(roomId)[killedPlayer] - 1;
							Random randLast = new Random();
							boolean switchCaseFlagged = false;
							while (!switchCaseFlagged) {
								String answer = "";
								int position = randLast.nextInt(4) + 1;
								switch (position) {
								case 1:
									out.println("EMPTY-OR-NOT" + " " + 1);
									answer = in.readLine();
									if (answer.startsWith("ANSWER-YES")) {
										roomsPlayersIndexes.get(roomId)[killedPlayer] = answer
												.substring(answer.lastIndexOf(' ') + 1);
										switchCaseFlagged = true;
									}
									break;
								case 2:
									out.println("EMPTY-OR-NOT" + " " + 2);
									answer = in.readLine();
									if (answer.startsWith("ANSWER-YES")) {
										roomsPlayersIndexes.get(roomId)[killedPlayer] = answer
												.substring(answer.lastIndexOf(' ') + 1);
										switchCaseFlagged = true;
									}
									break;
								case 3:
									out.println("EMPTY-OR-NOT" + " " + 3);
									answer = in.readLine();
									if (answer.startsWith("ANSWER-YES")) {
										roomsPlayersIndexes.get(roomId)[killedPlayer] = answer
												.substring(answer.lastIndexOf(' ') + 1);
										switchCaseFlagged = true;
									}
									break;
								case 4:
									out.println("EMPTY-OR-NOT" + " " + 4);
									answer = in.readLine();
									if (answer.startsWith("ANSWER-YES")) {
										roomsPlayersIndexes.get(roomId)[killedPlayer] = answer
												.substring(answer.lastIndexOf(' ') + 1);
										switchCaseFlagged = true;
									}
									break;
								}
							}
							sendToAllKilledPlayer(roomId);
							sendNewIndexesToAll(roomId);
						} else if (order.startsWith("GIVE-ME-BOXES")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							sendToAllBoxIndexes(roomId);
							System.out.println("sendToAllBoxIndexes used " + roomsBoxes.size());
						} else if (order.startsWith("BEING-MADE")) {
							int roomId = order.charAt(11) - '0';
							if (aNewHeartIsBeingMade.get(roomId) == false) {
								aNewHeartIsBeingMade.set(roomId, true);
								out.println("NOPE");
							} else {
								out.println("YOPE");
							}
						} else if (order.startsWith("DIAMOND-MADE")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							int secondsPassed = Integer.parseInt(parts[2]);
							if (aNewDiamondIsReserved.get(roomId) == false) {
								aNewDiamondIsReserved.set(roomId, true);
								Random randLast = new Random();
								nextDiamondArrival.set(roomId, randLast.nextInt(15) + 10 + secondsPassed);
							}
						} else if (order.startsWith("GIMME-NEW-HEART")) {
							String[] parts = order.split(" ");
							int newHeartI = Integer.parseInt(parts[1]);
							int newHeartJ = Integer.parseInt(parts[2]);
							int secondsPassed = Integer.parseInt(parts[3]);
							int roomId = Integer.parseInt(parts[4]);
							int[] newHeart = { newHeartI, newHeartJ, (secondsPassed + 4) };
							heartIndexes.add(newHeart);
							sendToAllNewHeart(newHeartI, newHeartJ, roomId);
						} else if (order.startsWith("HEART-FALSE")) {
							int roomId = order.charAt(12) - '0';
						} else if (order.startsWith("WHEN-DIAMOND")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							out.println("NDIAMOND" + " " + nextDiamondArrival.get(roomId) + " "
									+ nextDiamondIndexes.get(roomId)[0] + " " + nextDiamondIndexes.get(roomId)[1] + " "
									+ nextDiamondIndexes.get(roomId)[1]);
						} else if (order.startsWith("NEW-DIAMOND-IS-MADE")) {
							System.out.println(order);
							String[] parts = order.split(" ");
							int newStarI = Integer.parseInt(parts[1]);
							int newStarJ = Integer.parseInt(parts[2]);
							int nextTime = Integer.parseInt(parts[3]);
							int[] insert = { newStarI, newStarJ, nextTime };
							int roomId = Integer.parseInt(parts[4]);
							nextDiamondIndexes.set(roomId, insert);
							sendToAllDiamond(newStarI, newStarJ, roomId);
						} else if (order.startsWith("DELETE-DIAMOND-IF-NEEDED")) {
							String[] parts = order.split(" ");
							int roomId = Integer.parseInt(parts[1]);
							int secondsPassed = Integer.parseInt(parts[2]);
							if (secondsPassed == nextDiamondIndexes.get(roomId)[2]) {
								int diamondI = nextDiamondIndexes.get(roomId)[0];
								int diamondJ = nextDiamondIndexes.get(roomId)[1];
								System.out.println("STAR TO BE DELETED !");
								int[] diamond = { -1, -1, -1 };
								nextDiamondIndexes.set(roomId, diamond);
								aNewDiamondIsReserved.set(roomId, false);
								sendToAllToDeleteDiamond(diamondI, diamondJ, roomId);
							}
						}
					}
				} else {
					System.out.println("(SERVER) else came...");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void sendToAllToDeleteDiamond(int diamondI, int diamondJ, int roomId) throws IOException {
			for (Socket socket : rooms.get(roomId)) {
				Socket playerSocket = socket;
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("DELETE-THIS-DIAMOND" + " " + diamondI + " " + diamondJ);
			}
		}

		private void sendToAllDiamond(int newStarI, int newStarJ, int roomId) throws IOException {
			for (Socket socket : rooms.get(roomId)) {
				Socket playerSocket = socket;
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				System.out.println("HERE-ARE-THE-DIAMONDS" + " " + newStarI + " " + newStarJ);
				playerOut.println("HERE-ARE-THE-DIAMONDS" + " " + newStarI + " " + newStarJ);
			}
		}

		private void sendToAllNewHeart(int newHeartI, int newHeartJ, int roomId) throws IOException {
			for (Socket socket : rooms.get(roomId)) {
				Socket playerSocket = socket;
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("MAKE-A-HEART" + " " + newHeartI + " " + newHeartJ);
			}
		}

		private String[] boxMaker(int height, int width) throws IOException {
			System.out.println("BOX-MAKER");
			Random randLast = new Random();
			String answers = "";
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int rnd = randLast.nextInt(10) + 1;
					if (rnd == 1) {
						answers += " " + Integer.toString(i) + "@" + Integer.toString(j);
					}
				}
			}
			answers = answers.substring(1);
			String[] finalAnswer = answers.split(" ");
			return finalAnswer;
		}

		private void sendToAllHoldOn(int roomId, int killedPlayer) throws IOException {
			for (int i = 0; i < roomsCapacity.get(roomId); i++) {
				Socket playerSocket = rooms.get(roomId)[i];
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("HOLD-ON-INSERTING" + " " + killedPlayer);
			}
		}

		private void sendToAllKilledPlayer(int roomId) throws IOException {
			String allTheLives = "";
			for (int live : roomsMainCharacterLives.get(roomId)) {
				allTheLives += " " + live;
			}
			allTheLives = allTheLives.substring(1);
			for (int i = 0; i < roomsCapacity.get(roomId); i++) {
				Socket playerSocket = rooms.get(roomId)[i];
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("LIVES" + " " + allTheLives);
			}
		}

		public void checkIfEveryOneIsPresent(int roomId) throws IOException {
			for (int i = 0; i < roomsEveryOnePresentOrNot.get(roomId).length; i++) {
				if (roomsEveryOnePresentOrNot.get(roomId)[i]) {
					if (i == roomsEveryOnePresentOrNot.get(roomId).length - 1) {
						sendToAllLetsGo(roomId);
					}
				} else {
					break;
				}
			}
		}

		private void sendToAllBoxIndexes(int roomId) throws IOException {
			String boxes = "";
			for (String str : roomsBoxes.get(roomId)) {
				boxes += " " + str;
			}
			boxes = boxes.substring(1);
			System.out.println("Boxes : " + boxes);
			for (int i = 0; i < roomsCapacity.get(roomId); i++) {
				Socket playerSocket = rooms.get(roomId)[i];
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("ALL-BOXES" + " " + boxes);
			}
		}

		private void sendToAllLetsGo(int roomId) throws IOException {
			for (Socket socket : rooms.get(roomId)) {
				Socket playerSocket = socket;
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("TIMER-TASK");
			}
		}

		private void sendNewIndexesToAll(int roomId) throws IOException {
			String allTheIndexes = "";
			for (String index : roomsPlayersIndexes.get(roomId)) {
				allTheIndexes += " " + index;
			}
			allTheIndexes = allTheIndexes.substring(1);
			for (int i = 0; i < roomsCapacity.get(roomId); i++) {
				Socket playerSocket = rooms.get(roomId)[i];
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("UPDATE-INDEXES" + " " + allTheIndexes);
			}
		}

		private void sendNewBombIndexToAll(int bombI, int bombJ, int playerNumber, int roomId, int explosionAt,
				int bombRange) throws IOException {
			String allTheIndexes = "";
			for (String index : roomsPlayersIndexes.get(roomId)) {
				allTheIndexes += " " + index;
			}
			allTheIndexes = allTheIndexes.substring(1);
			for (int i = 0; i < roomsCapacity.get(roomId); i++) {
				Socket playerSocket = rooms.get(roomId)[i];
				PrintWriter playerOut = new PrintWriter(playerSocket.getOutputStream(), true);
				playerOut.println("BOMB-SET" + " " + bombI + " " + bombJ + " " + playerNumber + " " + explosionAt + " "
						+ bombRange);
			}
		}
	}
}
