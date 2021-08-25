package water.of.cup.pong;
import java.util.ArrayList;

import water.of.cup.boardgames.extension.BoardGamesConfigOption;
import water.of.cup.boardgames.extension.BoardGamesExtension;
import water.of.cup.boardgames.game.Game;

public class Pong extends BoardGamesExtension {

	@Override
	public ArrayList<Class<? extends Game>> getGames() {
		ArrayList<Class<? extends Game>> games = new ArrayList<Class<? extends Game>>();
		games.add(PongGame.class);
		return games;
	}

	@Override
	public String getExtensionName() {
		// TODO Auto-generated method stub
		return "Pong";
	}

	@Override
	public ArrayList<BoardGamesConfigOption> getExtensionConfig() {
		ArrayList<BoardGamesConfigOption> configOptions = new ArrayList<>();
//		for(ConfigUtil configUtil : ConfigUtil.values()) {
//			configOptions.add(new BoardGamesConfigOption(configUtil.getPath(), configUtil.getDefaultValue()));
//		}
		return configOptions;
	}
}
