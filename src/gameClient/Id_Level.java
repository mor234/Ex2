package gameClient;

/**
 * This class contains the id and level needed inorder to connect to the serer and start a game.
 *  @author mor234
 */

public class Id_Level {
    private int id = -1;
    private int level = -1;

    /*
    Automatically generated get functions
    */

    public int getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }


    /**
     * get the id and level as string ang load the in int format
     *
     * @param idStr    id as a String
     * @param levelStr level as a String
     * @return true if converted both to int successfully, false if not
     */
    public boolean setID_Level(String idStr, String levelStr) {
        try {
            this.id = Integer.parseInt(idStr);
            this.level = Integer.parseInt(levelStr);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;

    }

}
