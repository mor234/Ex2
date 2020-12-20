package gameClient;

import com.google.gson.JsonObject;

/**
 * This class represent information about the game.
 * @author mor234
 */
public class GameInfo {
    int id;
    int pokemonNumber;
    boolean isLoggedIn;
    int moves;
    double grade;
    int gameLevel;
    int maxUserLevel;
    String graphFileLoc;
    int numberOfAgent;

    /**
     * get Json object containing information about the game,
     * and load the information from it.
     * @param jsonObject
     */

    public void load(JsonObject jsonObject) {

        jsonObject = jsonObject.get("GameServer").getAsJsonObject();

        id = jsonObject.get("id").getAsInt();
        pokemonNumber = jsonObject.get("pokemons").getAsInt();
        isLoggedIn = jsonObject.get("is_logged_in").getAsBoolean();
        moves = jsonObject.get("moves").getAsInt();

        grade = jsonObject.get("grade").getAsDouble();
        gameLevel = jsonObject.get("game_level").getAsInt();
        maxUserLevel = jsonObject.get("max_user_level").getAsInt();
        graphFileLoc = jsonObject.get("graph").getAsString();
        numberOfAgent = jsonObject.get("agents").getAsInt();

    }


    /*
     * Automatically generated set and get functions
     */

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getPokemonNumber() {
        return pokemonNumber;
    }

    public void setPokemonNumber(int pokemonNumber) {
        this.pokemonNumber = pokemonNumber;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public int getMoves() {
        return moves;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getMaxUserLevel() {
        return maxUserLevel;
    }

    public void setMaxUserLevel(int maxUserLevel) {
        this.maxUserLevel = maxUserLevel;
    }

    public String getGraphLoc() {
        return graphFileLoc;
    }

    public void setGraphLoc(String graphLoc) {
        this.graphFileLoc = graphLoc;
    }

    public int getNumberOfAgent() {
        return numberOfAgent;
    }

    public void setNumberOfAgent(int numberOfAgent) {
        this.numberOfAgent = numberOfAgent;
    }

    /**
     * Automatically generated toString function
     * @return String of the game
     */

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", pokemonNumber=" + pokemonNumber +
                ", isLoggedIn=" + isLoggedIn +
                ", moves=" + moves +
                ", grade=" + grade +
                ", gameLevel=" + gameLevel +
                ", maxUserLevel=" + maxUserLevel +
                ", graphFileLoc='" + graphFileLoc + '\'' +
                ", numberOfAgent=" + numberOfAgent +
                '}';
    }
}
