import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class UserInterface extends Api {
    private JButton updateButton;
    private JButton readButton;
    private JButton deleteButton;
    private JButton createButton;
    private JTable dataTable;
    private JPanel contentPane;

    private String[][] data = null;
    private final String[] columnNames = {"username", "age", "balance"};

    public UserInterface() {
        setTitle("CRUD");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set the Content Pane (the JPanel that acts as the main container)
        setContentPane(contentPane);
        // Resize the window to fit all components
        pack();
        // Set the location of the window to the center of the screen
        setLocationRelativeTo(null);
        // Create button action listener
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createHandler();
            }
        });
        // Read button action listener
        readButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                readHandler();
            }
        });
        // Update button action listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateHandler();
            }
        });
        // Delete button action listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteHandler();
            }
        });
    }

    private void createHandler() {
        String username = JOptionPane.showInputDialog("Enter the username");
        if(username == null || username.isBlank()) return; //Check if the input is empty/user pressed Cancel
        int age;
        double balance;
        try {
            age = Integer.parseInt(JOptionPane.showInputDialog("Enter a new age"));
            balance = Double.parseDouble(JOptionPane.showInputDialog("Enter a new balance"));
        } catch (Exception e) {
            return;
        }

        try {
            createItem(username, age, balance);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + e.getCause());
        }
    }
    private void readHandler() {
        try {
            data = readItems();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + e.getCause());
        }

        if(data != null) {
            SwingUtilities.invokeLater(() -> {
                tableModel = new CustomTableModel();
                dataTable.setModel(tableModel);
            });
        }
    }
    private void updateHandler () {
        try {
            String queryUsername = JOptionPane.showInputDialog("Enter the username from the record you wish to update");
            if(queryUsername == null || queryUsername.isBlank()) return; //Check if the input is empty/user pressed Cancel
            String newUsername = JOptionPane.showInputDialog("Enter a new username");
            if(newUsername == null || newUsername.isBlank()) return;
            int newAge;
            double newBalance;
            try {
                newAge = Integer.parseInt(JOptionPane.showInputDialog("Enter a new age"));
                newBalance = Double.parseDouble(JOptionPane.showInputDialog("Enter a new balance"));
            } catch (Exception e) {
                return;
            }
            updateItem(queryUsername, newUsername, newAge, newBalance);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + e.getCause());
        }
    }
    private void deleteHandler () {
        try {
            String username = JOptionPane.showInputDialog("Enter the username from the record you wish to delete");
            if(username == null || username.isBlank()) return; //Check if the input is empty/user pressed Cancel
            deleteItem(username);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + e.getCause());
        }
    }

    class CustomTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            for (int i = 0; i < data[0].length; i++) {
                if(data[0][i] == null) {
                    return i;
                }
            }
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnNames[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[columnIndex][rowIndex];
        }
        @Override
        public void addTableModelListener(TableModelListener l) {}
        @Override
        public void removeTableModelListener(TableModelListener l) {}
    };

    CustomTableModel tableModel = new CustomTableModel();
}
