package water.of.cup.pong;

import water.of.cup.boardgames.game.Game;
import water.of.cup.boardgames.game.storage.BoardGamesStorageType;
import water.of.cup.boardgames.game.storage.GameStorage;
import water.of.cup.boardgames.game.storage.StorageType;

public class PongStorage extends GameStorage {

    public PongStorage(Game game) {
        super(game);
    }

    @Override
    protected String getTableName() {
        return "pong";
    }

    @Override
    protected StorageType[] getGameStores() {
        return new StorageType[] {
        		BoardGamesStorageType.WINS,
        		BoardGamesStorageType.LOSSES,
        		BoardGamesStorageType.POINTS
        };
    }
}
