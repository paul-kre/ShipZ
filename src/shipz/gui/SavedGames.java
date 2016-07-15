package shipz.gui;

import javafx.beans.property.SimpleStringProperty;

public class SavedGames {

    private SimpleStringProperty gamename;
    private SimpleStringProperty name1;
    private SimpleStringProperty name2;
    private SimpleStringProperty mode;
    private SimpleStringProperty date;


    public SavedGames () {
    }

    public SavedGames (String s1, String s2, String s3, String s4, String s5) {

        gamename = new SimpleStringProperty(s1);
        name1 = new SimpleStringProperty(s2);
        name2 = new SimpleStringProperty(s3);
        mode = new SimpleStringProperty(s4);
        date = new SimpleStringProperty(s5);
    }

    public String getGamename() {

        return gamename.get();
    }
    public void setGamename(String s) {

        gamename.set(s);
    }

    public String getName1() {

        return name1.get();
    }
    public void setName1(String s) {

        name1.set(s);
    }

    public String getName2() {

        return name2.get();
    }
    public void setName2(String s) {

        name2.set(s);
    }

    public String getMode() {

        return mode.get();
    }
    public void setMode(String s) {

        mode.set(s);
    }

    public String getDate() {

        return date.get();
    }
    public void setDate(String s) {

        date.set(s);
    }

    @Override
    public String toString() {

        return ("1");
    }
}
