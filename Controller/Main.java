package Controller;

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        //Set UIManager for cross-platform that way all variations of the game see the same image.
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // handle exception
        }

        GameController controller = new GameController();
    }
}
