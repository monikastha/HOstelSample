package Admin;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create the content panel and CardLayout for dynamic content switching
        JPanel contentPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Create the AdminRoomManager panel
        AdminRoomManager roomManagerPanel = new AdminRoomManager(); // AdminRoomManager panel
        contentPanel.add(roomManagerPanel, "rooms");

         // Create the AdminStudentManager panel
        AdminStudentManager studentManagerPanel = new AdminStudentManager(); // AdminRoomManager panel
        contentPanel.add(studentManagerPanel, "students");
        // Create the AdminStudentManager panel
        AdminFeeManager feeManagerPanel = new AdminFeeManager(); // AdminRoomManager panel
        contentPanel.add(feeManagerPanel, "fees");

        // Add the sidebar to the frame, passing the CardLayout and content panel
        AdminSidebar sidebar = new AdminSidebar(cardLayout, contentPanel);
        add(sidebar, BorderLayout.WEST);

        // Add the content panel to the center of the frame
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Create a basic panel with a label to represent each section
    private JPanel createPanel(String name) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(name));
        return panel;
    }

    // Main method to run the application
    public static void main(String[] args) {
        // Run the dashboard in the event dispatch thread for thread-safety
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard();
        });
    }
}
