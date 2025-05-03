
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Assignment Creation Form for teachers
 */
public class AddAssignmentForm extends JFrame {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextField deadlineField;
    private JComboBox<String> courseComboBox;
    private JButton submitButton;
    private DatabaseManager dbManager;
 

    
    public AddAssignmentForm(DatabaseManager dbManager) {
        this.dbManager = dbManager;

        // Window setup
        setTitle("Add New Assignment");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Add New Assignment", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 2, 10, 15));

        formPanel.add(new JLabel("Course:"));
        String[] courses = {"Introduction to Computer Science", "Database Systems", "Computer Networks"};
        courseComboBox = new JComboBox<>(courses);
        formPanel.add(courseComboBox);

        formPanel.add(new JLabel("Assignment Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane);

        formPanel.add(new JLabel("Deadline (YYYY-MM-DD):"));
        deadlineField = new JTextField();
        formPanel.add(deadlineField);

        formPanel.add(new JLabel("Attach Files:"));
        JButton attachFileButton = new JButton("Attach File");
        formPanel.add(attachFileButton);

        // Submit button
        submitButton = new JButton("Create Assignment");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAssignment();
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(submitButton, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void addAssignment() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String deadline = deadlineField.getText();
        String course = (String) courseComboBox.getSelectedItem();

        // Field validation
        if (title.isEmpty() || description.isEmpty() || deadline.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Success message - in a real app, this would save to the database
        JOptionPane.showMessageDialog(this, "Assignment created!\n" +
                        "Course: " + course + "\n" +
                        "Title: " + title + "\n" +
                        "Deadline: " + deadline,
                "Success", JOptionPane.INFORMATION_MESSAGE);

        dispose(); // Close the form after submitting
    }
}