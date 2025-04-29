package Student;
import javax.swing.*;

public class StudentDashboard extends JFrame {
    public StudentDashboard() {
        setTitle("Student Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("Welcome, Student!", SwingConstants.CENTER);
        add(welcomeLabel);

        setVisible(true);
    }
}
