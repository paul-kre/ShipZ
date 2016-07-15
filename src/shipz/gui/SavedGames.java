import javafx.beans.property.SimpleStringProperty;

public class SavedGames {

    private SimpleStringProperty position;
    private SimpleStringProperty name;
    private SimpleStringProperty points;
    private SimpleStringProperty date;


    public SavedGames () {
    }

    public SavedGames (String s1, String s2, String s3, String s4) {

        position = new SimpleStringProperty(s1);
        name = new SimpleStringProperty(s2);
        points = new SimpleStringProperty(s3);
        date = new SimpleStringProperty(s4);
    }

    public String getPosition() {

        return position.get();
    }
    public void setPosition(String s) {

        position.set(s);
    }

    public String getName() {

        return name.get();
    }
    public void setName(String s) {

        name.set(s);
    }

    public String getPoints() {

        return points.get();
    }
    public void setPoints(String s) {

        points.set(s);
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
