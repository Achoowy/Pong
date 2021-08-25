package water.of.cup.pong;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.game.BoardItem;
import water.of.cup.boardgames.game.Button;
import water.of.cup.boardgames.game.Clock;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.GameConfig;
import water.of.cup.boardgames.game.GameImage;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.maps.MapData;
import water.of.cup.boardgames.game.maps.Screen;
import water.of.cup.boardgames.game.storage.GameStorage;

public class PongGame extends Game {
	protected Screen screen;
	private double[] ballPos;

	protected boolean singlePlayer;

	// protected double[] ballDirection;
	// protected double ballSpeed;
	private double[] ballVelocity;

	private Button paddle1;
	private Button paddle2;

	private int points1;
	private int points2;

	private Button ball;

	private int firstServe;

	private PongRunnable pongRunnable;

//	Collisions with
//	paddles are not elastic. To ensure that one side will eventually
//	win, the vertical component of the velocity is increased by two
//	percent every time the ball hits a paddle.

	public PongGame(int rotation) {
		super(rotation);
		ball = new Button(this, "PONG_BALL", new int[] { 128 - 3, 64 - 3 }, 0, "ball");
		paddle1 = new Button(this, "PONG_PADDLE", new int[] { 18, 64 - 8 }, 0, "paddle");
		paddle2 = new Button(this, "PONG_PADDLE", new int[] { 255 - 18 - 3, 64 - 8 }, 0, "paddle");
		ball.setScreen(screen);
		paddle1.setScreen(screen);
		paddle2.setScreen(screen);
		buttons.add(ball);
		buttons.add(paddle1);
		buttons.add(paddle2);
	}

	@Override
	protected void setMapInformation(int rotation) {
		this.mapStructure = new int[][] { { -1, -2 } };
		this.placedMapVal = -1;

		screen = new Screen(this, "PONG_BOARD", 2, new int[] { 0, 0 }, new int[][] { { 1, 2 } }, rotation);
		screens.add(screen);
	}

	@Override
	protected void startGame() {
		super.startGame();
		firstServe = 1;
		points1 = 0;
		points2 = 0;
		ballPos = new double[] { 128, 64 };

		singlePlayer = (teamManager.getGamePlayers().size() == 1);

		newRound();

	}

	private void newRound() {
		setBallPos(new double[] { 128, 64 });
		
		
		double ballAngle = (Math.random() * 2 - 1) * 5 * Math.PI / 12;
		double ballSpeed = 15;
		
		ballVelocity = new double[] { firstServe * ballSpeed * Math.cos(ballAngle), ballSpeed * Math.sin(ballAngle) };

		if (pongRunnable != null && !pongRunnable.isCancelled())
			pongRunnable.cancel();
		pongRunnable = new PongRunnable(this);
		pongRunnable.runTaskTimer(BoardGames.getInstance(), 60, 1);

	}

	protected void score() {

		if (singlePlayer) {
			teamManager.getTurnPlayer().getPlayer().sendMessage("You got " + points1 + " volleys.");
			this.endGame(null);
			return;
		}

		firstServe *= -1;
		if (ballVelocity[0] > 1)
			points1++;
		else
			points2++;

		for (GamePlayer gp : teamManager.getGamePlayers())
			gp.getPlayer().sendMessage(points1 + ":" + points2);
		newRound();
		
		if (points1 >= 11)
			this.endGame(teamManager.getGamePlayers().get(0));
		if (points2 >= 11)
			this.endGame(teamManager.getGamePlayers().get(1));
	}

	@Override
	protected void setGameName() {
		this.gameName = "Pong";

	}
	
	@Override
	public boolean canPlaceBoard(Location loc, int rotation) {
		int[] centerLoc = mapManager.getMapValsLocationOnRotatedBoard(placedMapVal);
		int[] mapDimensions = mapManager.getRotatedDimensions();

		// calculate map bounds
		int t1X = -centerLoc[0];
		int t2X = mapDimensions[0] + t1X;

		int t1Y = 0;
		int t2Y = 0; // for future changes

		int t1Z = -centerLoc[1];
		int t2Z = mapDimensions[1] + t1Z;

		// calculate min and max bounds
		int maxX = Math.max(t1X, t2X);
		int minX = Math.min(t1X, t2X);

		int maxY = Math.max(t1Y, t2Y);
		int minY = Math.min(t1Y, t2Y);

		int maxZ = Math.max(t1Z, t2Z);
		int minZ = Math.min(t1Z, t2Z);

		// check if blocks are empty
		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				for (int z = minZ; z <= maxZ; z++) {
					if (!loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z)
							.isEmpty())
						return false;

					// check that place on is not empty
					for (MapData mapData : mapManager.getMapDataAtLocationOnRotatedBoard(x - t1X, z - t1Z, y - t1Y)) {
						if (mapData.getMapVal() <= 0)
							continue;
						Location frameLoc = new Location(loc.getWorld(), loc.getBlockX() + x, loc.getBlockY() + y,
								loc.getBlockZ() + z);
						Block placedOn = frameLoc.getBlock().getRelative(mapData.getBlockFace().getOppositeFace());
						if (placedOn.getType() == Material.AIR)
							return false;
					}
				}
			}
		}

		// check that bottom blocks are not empty
//		for (int x = minX; x <= maxX; x++)
//			for (int z = minZ; z <= maxZ; z++) {
//				if (loc.getWorld().getBlockAt(loc.getBlockX() + x, loc.getBlockY() - 1, loc.getBlockZ() + z).isEmpty())
//					return false;
//			}

		return true;
	}

	@Override
	protected void setBoardImage() {
		this.gameImage = new GameImage("PONG_BOARD", 0);

	}

	@Override
	protected void clockOutOfTime() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Clock getClock() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GameInventory getGameInventory() {
		// TODO Auto-generated method stub
		return new PongInventory(this);
	}

	@Override
	protected GameStorage getGameStorage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getTeamNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected GameConfig getGameConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void click(Player player, double[] loc, ItemStack map) {
		GamePlayer gamePlayer = getGamePlayer(player);
		if (!teamManager.getGamePlayers().contains(gamePlayer))
			return;

		int y = mapManager.getClickLocation(loc, map)[1] - 8;
		if (singlePlayer) {
			paddle1.getLocation()[1] = y;
			paddle2.getLocation()[1] = y;
		} else {
			Button paddle = teamManager.getTeamByPlayer(gamePlayer).equals(teamManager.getTurnTeam()) ? paddle1
					: paddle2;
			paddle.getLocation()[1] = y;
		}
		mapManager.renderBoard();

	}

	@Override
	protected void gamePlayerOutOfTime(GamePlayer turn) {
		// TODO Auto-generated method stub

	}

	@Override
	public ItemStack getBoardItem() {
		return new BoardItem(gameName, new ItemStack(Material.DARK_OAK_TRAPDOOR, 1));
	}

	protected double[] getBallPos() {

		return new double[] { ballPos[0] + 2, ballPos[1] + 2 };
	}

	protected void setBallPos(double[] ballP) {
		ballPos[0] = ballP[0] - 2;
		ballPos[1] = ballP[1] - 2;
		int[] loc = ball.getLocation();
		loc[0] = (int) ballPos[0];
		loc[1] = (int) ballPos[1];
		mapManager.renderBoard();
	}

	protected double[] getBallVelocity() {
		return ballVelocity;
	}

	protected int getPaddleY(int paddleNum) {
		if (paddleNum == 1)
			return paddle1.getLocation()[1];
		return paddle2.getLocation()[1];
	}

	@Override
	public void endGame(GamePlayer winner) {
		if (pongRunnable != null && !pongRunnable.isCancelled())
			pongRunnable.cancel();
		super.endGame(winner);
	}

	protected void hit() {
		if (!singlePlayer)
			return;

		points1++;
		for (GamePlayer gp : teamManager.getGamePlayers())
			gp.getPlayer().sendMessage("volleys:" + points1);

	}

}