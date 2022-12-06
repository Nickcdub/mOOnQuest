package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;

public class UserInput implements ActionListener {

    public void actionPerformed(ActionEvent e) {

        String name = e.getActionCommand();
        ByteArrayInputStream bais = null;
        switch (name) {
            case "KNIGHT" -> bais = new ByteArrayInputStream("k".getBytes());
            case "MENDER" -> bais = new ByteArrayInputStream("m".getBytes());
            case "ASSASSIN" -> bais = new ByteArrayInputStream("a".getBytes());
        }
        System.setIn(bais);

    }
}
