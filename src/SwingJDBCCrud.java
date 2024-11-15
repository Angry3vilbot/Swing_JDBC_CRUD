import javax.swing.*;

public class SwingJDBCCrud {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Api ui = new UserInterface();
                ui.setVisible(true);
            }
        });

    }
}
