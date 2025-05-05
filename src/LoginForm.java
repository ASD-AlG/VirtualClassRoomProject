
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple login window for the Virtual Classroom system.
 */
public class LoginForm extends JFrame {

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JComboBox<String> roleBox = new JComboBox<>(new String[] { "Student", "Teacher", "Admin" });

    private final DatabaseManager db = new DatabaseManager();

    public LoginForm() {
        setTitle("Virtual Classroom – Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initUi();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
    }
    
    private void initUi() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        panel.add(login);
        panel.add(register);

        add(panel);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });

        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegistrationForm(); // open registration form
            }
        });
    }

    private void attemptLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password", "Missing input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.validateLogin(email, password, role)) {
            JOptionPane.showMessageDialog(this, "Login successful", "Welcome " + role, JOptionPane.INFORMATION_MESSAGE);
            switch (role) {
                case "Student":
                    new StudentDashboard(role, db);
                    setVisible(false);
                    break;
                case "Teacher":
                    new TeacherDashboard(role, db);
                    setVisible(false);
                    break;
                case "Admin":
                    new AdminDashboard(role, db);
                    setVisible(false);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown Role Error");
                    break;
            }

            
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new LoginForm();
        
    }
}