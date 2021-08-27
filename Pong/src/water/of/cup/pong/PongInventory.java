package water.of.cup.pong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import water.of.cup.boardgames.config.ConfigUtil;
import water.of.cup.boardgames.game.GamePlayer;
import water.of.cup.boardgames.game.games.chess.Chess;
import water.of.cup.boardgames.game.inventories.GameInventory;
import water.of.cup.boardgames.game.inventories.GameOption;
import water.of.cup.boardgames.game.inventories.GameOptionType;

public class PongInventory extends GameInventory {

    private final PongGame game;

    public PongInventory(PongGame game) {
        super(game);
        this.game = game;
    }

    @Override
    protected ArrayList<GameOption> getOptions() {
        ArrayList<GameOption> options = new ArrayList<>();

        return options;
    }

    @Override
    protected int getMaxQueue() {
        return 3;
    }

    @Override
    protected int getMaxGame() {
        return 2;
    }

    @Override
    protected int getMinGame() {
        return 1;
    }

    @Override
    protected boolean hasTeamSelect() {
        return false;
    }

    @Override
    protected boolean hasGameWagers() {
        return false;
    }

    @Override
    protected boolean hasWagerScreen() {
        return false;
    }

    @Override
    protected boolean hasForfeitScreen() {
        return true;
    }

    @Override
    protected void onGameCreate(HashMap<String, Object> gameData, ArrayList<GamePlayer> players) {
        for(GamePlayer player : players) {
            player.getPlayer().sendMessage(ConfigUtil.CHAT_WELCOME_GAME.buildString(game.getAltName()));
        }

        game.startGame();
    }
}
