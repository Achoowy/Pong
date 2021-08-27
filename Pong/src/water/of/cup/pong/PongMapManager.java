package water.of.cup.pong;

import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import water.of.cup.boardgames.BoardGames;
import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.maps.GameMap;
import water.of.cup.boardgames.game.maps.GameRenderer;
import water.of.cup.boardgames.game.maps.MapManager;
import water.of.cup.boardgames.game.maps.Screen;

public class PongMapManager extends MapManager {
	private Game game;
	public PongMapManager(int[][] mapStructure, int rotation, Game game) {
		super(mapStructure, rotation, game);
		this.game = game;
	}
	
	@Override
	public void renderBoard() {
		for (Screen screen : game.getScreens())
			for (int mapVal : screen.getMapVals()) {
				GameMap map = game.getGameMapByMapVal(mapVal);
				MapMeta mapMeta = map.getMapMeta();
				MapView view = mapMeta.getMapView();

				for (MapRenderer renderer : view.getRenderers())
					if (renderer instanceof GameRenderer)
						((GameRenderer) renderer).rerender();
				Bukkit.getServer().getScheduler().runTaskAsynchronously(BoardGames.getInstance(), new Runnable() {
					@Override
					public void run() {
						view.getWorld().getPlayers().forEach(player -> player.sendMap(view));
					}
				});
			}
	}
	
	public void renderBoard(int mapVal) {
		GameMap map = game.getGameMapByMapVal(mapVal);
		MapMeta mapMeta = map.getMapMeta();
		MapView view = mapMeta.getMapView();

		for (MapRenderer renderer : view.getRenderers())
			if (renderer instanceof GameRenderer)
				((GameRenderer) renderer).rerender();
		Bukkit.getServer().getScheduler().runTaskAsynchronously(BoardGames.getInstance(), new Runnable() {
			@Override
			public void run() {
				view.getWorld().getPlayers().forEach(player -> player.sendMap(view));
			}
		});
	}

}
