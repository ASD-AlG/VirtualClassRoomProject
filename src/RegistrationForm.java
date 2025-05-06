import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Registration window for new users (Student or Teacher).
 */
public class RegistrationForm extends JFrame {

    private final JTextField nameField       = new JTextField();
    private final JTextField emailField      = new JTextField();
    private final JPasswordField passField   = new JPasswordField();
    private final JPasswordField confirmPass = new JPasswordField();
    private final JComboBox<String> roleBox  = new JComboBox<>(new String[]{ "Student", "Teacher" });

    private final DatabaseManager db = new DatabaseManager();

    
    public RegistrationForm() {
        setTitle("Virtual Classroom – Register");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUi();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUi() {
        JPanel panel = new JPanel(new GridLayout(6,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        panel.add(new JLabel("Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Password:"));
        panel.add(passField);

        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPass);

        panel.add(new JLabel("Role:"));
        panel.add(roleBox);

        JButton register = new JButton("Register");
        panel.add(register);

        add(panel);

        register.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                attemptRegistration();
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[^@]+@[^@]+\\.com$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void attemptRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String pass  = new String(passField.getPassword());
        String confirm = new String(confirmPass.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required", "Missing input", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format. Email must be in the format: name@domain.com", 
                                        "Invalid Email", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (db.registerUser(name, email, pass, role)) {
            JOptionPane.showMessageDialog(this, "Registration successful – you may log in now", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            
        } else {
            JOptionPane.showMessageDialog(this, "Email already exists", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}