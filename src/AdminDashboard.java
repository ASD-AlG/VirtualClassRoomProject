import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

/**
 * واجهة المسؤول الرئيسية
 */
public class AdminDashboard extends JFrame {
    private String username;
    private String email;
    private DatabaseManager dbManager;

    private JTabbedPane tabbedPane;


    
    public AdminDashboard(String username,String email, DatabaseManager dbManager) {
        this.username = username;
        this.dbManager = dbManager;
        this.email = email;
        // إعداد النافذة
        App.UICHANGEMETHOD();
        setTitle("Admin -" + username);
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة التبويب
        tabbedPane = new JTabbedPane();
        tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // إضافة علامات التبويب
        tabbedPane.addTab("Users List", createUsersPanel());
        tabbedPane.addTab("courses List", createCoursesPanel());
        tabbedPane.addTab("System Information", createDashboardPanel());
        tabbedPane.addTab("Admin Information ", createSettingsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Users List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة البحث
        JPanel searchPanel = new JPanel();
        searchPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        searchPanel.add(new JLabel("Search:"));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JComboBox<String> roleFilter = new JComboBox<>(new String[]{"All", "Students", "Teachers", "Admins"});
        searchPanel.add(new JLabel("role:"));
        searchPanel.add(roleFilter);

        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // قائمة المستخدمين
        JTable userTable = new JTable();
        userTable.setDefaultEditor(Object.class, null);
        userTable.setModel(dbManager.getUsersTable());
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addUserButton = new JButton("Add User");
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationForm n = new RegistrationForm();
                n.setTitle("Add User");
                n.addWindowListener(new WindowListener() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        userTable.setModel(dbManager.getUsersTable());
                    }
                    @Override
                    public void windowActivated(WindowEvent e) {
                        
                    }
                    @Override
                    public void windowClosing(WindowEvent e) {
                        
                    }
                    @Override
                    public void windowDeactivated(WindowEvent e) {
                        
                    }
                    @Override
                    public void windowDeiconified(WindowEvent e) {
                        
                    }
                    @Override
                    public void windowIconified(WindowEvent e) {
                        
                    }
                    @Override
                    public void windowOpened(WindowEvent e) {
                        
                    }
                });
            }
        });

        JButton deleteUserButton = new JButton("Delete User");
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int userId = Integer.parseInt(userTable.getValueAt(selectedRow, 0).toString());
                    String userName = userTable.getValueAt(selectedRow, 1).toString();
                    
                    int confirm = JOptionPane.showConfirmDialog(
                        null, 
                        "Are you sure you want to delete user: " + userName + "?", 
                        "Confirm Deletion", 
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (dbManager.deleteUser(userId)) {
                            // Refresh the table
                            userTable.setModel(dbManager.getUsersTable());
                            JOptionPane.showMessageDialog(null, "User deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete user!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a user to delete!");
                }
            }
        });

        buttonPanel.add(addUserButton);
        buttonPanel.add(deleteUserButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
            
        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("courses List", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTable courseTable = new JTable();
        courseTable.setDefaultEditor(Object.class, null);
        courseTable.setModel(dbManager.getCoursesTable());
        JScrollPane scrollPane = new JScrollPane(courseTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addCourseButton = new JButton("Add Course");
        addCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new dialog for adding a course
                JDialog addCourseDialog = new JDialog(AdminDashboard.this, "Add New Course", true);
                addCourseDialog.setSize(400, 300);
                addCourseDialog.setLocationRelativeTo(AdminDashboard.this);
                
                JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
                formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                
                formPanel.add(new JLabel("Course Name:"));
                JTextField courseNameField = new JTextField();
                formPanel.add(courseNameField);
                
                formPanel.add(new JLabel("Credit Hours:"));
                JSpinner creditHoursSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
                formPanel.add(creditHoursSpinner);
                
                formPanel.add(new JLabel("Enrollment Date (MM/dd/yyyy):"));
                JTextField enrollmentDateField = new JTextField(new SimpleDateFormat("MM/dd/yyyy").format(new java.util.Date()));
                formPanel.add(enrollmentDateField);
                
                JButton submitButton = new JButton("Add Course");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String courseName = courseNameField.getText().trim();
                        int creditHours = (Integer) creditHoursSpinner.getValue();
                        String enrollmentDate = enrollmentDateField.getText().trim();
                        
                        if (courseName.isEmpty()) {
                            JOptionPane.showMessageDialog(addCourseDialog, "Please enter a course name!");
                            return;
                        }
                        
                        if (dbManager.addCourse(courseName, enrollmentDate, creditHours)) {
                            JOptionPane.showMessageDialog(addCourseDialog, "Course added successfully!");
                            // Refresh the course table
                            courseTable.setModel(dbManager.getCoursesTable());
                            addCourseDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(addCourseDialog, "Failed to add course!");
                        }
                    }
                });
                
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addCourseDialog.dispose();
                    }
                });
                
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(submitButton);
                buttonPanel.add(cancelButton);
                
                addCourseDialog.setLayout(new BorderLayout());
                addCourseDialog.add(formPanel, BorderLayout.CENTER);
                addCourseDialog.add(buttonPanel, BorderLayout.SOUTH);
                
                addCourseDialog.setVisible(true);
            }
        });

        JButton deleteCourseButton = new JButton("Delete Course");
        deleteCourseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int courseId = Integer.parseInt(courseTable.getValueAt(selectedRow, 0).toString());
                    String courseName = courseTable.getValueAt(selectedRow, 1).toString();
                    
                    int confirm = JOptionPane.showConfirmDialog(
                        null, 
                        "Are you sure you want to delete course: " + courseName + "?", 
                        "Confirm Deletion", 
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (dbManager.deleteCourse(courseId)) {
                            // Refresh the table
                            courseTable.setModel(dbManager.getCoursesTable());
                            JOptionPane.showMessageDialog(null, "Course deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete course!");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a course to delete!");
                }
            }
        });

        buttonPanel.add(addCourseButton);
        buttonPanel.add(deleteCourseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("System Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة المعلومات - عرض إحصائيات النظام
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        statsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // بطاقات الإحصائيات
        int num=dbManager.get_std_num();
        String std = String.valueOf(num);
        num=dbManager.get_tch_num();
        String tch = String.valueOf(num);
        num = dbManager.get_course_num();
        String crs = String.valueOf(num);
        addStatCard(statsPanel, " Number of Students", std);
        addStatCard(statsPanel, "Number of Teachers", tch);
        addStatCard(statsPanel, "Number of courses",crs);


        // زر تصدير التقارير
        JButton exportReportButton = new JButton("Export user information");
        exportReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportUsersToFile();
            }
        });
        
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.add(exportReportButton, BorderLayout.CENTER);
        statsPanel.add(exportPanel);

        panel.add(statsPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addStatCard(JPanel parent, String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        parent.add(card);
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Admin Information", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة الإعدادات
        JPanel settingsPanel = new JPanel(new GridLayout(6, 2, 10, 20));
        settingsPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        settingsPanel.add(new JLabel("Username:"));
        settingsPanel.add(new JLabel(dbManager.getName(email)));


        JButton logout = new JButton("Log out");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm newlogin = new LoginForm();
                newlogin.setLocationRelativeTo(null);
            }
        });
        
        settingsPanel.add(logout);

        panel.add(settingsPanel, BorderLayout.CENTER);

        return panel;
    }
    
    // Method to export users to a text file
    private void exportUsersToFile() {
        try {
            // Get all users from database
            ResultSet rs = dbManager.getAllUsers();
            if (rs == null) {
                JOptionPane.showMessageDialog(this, "Failed to retrieve user data", "Export Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create file chooser for save location
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Users Data");
            fileChooser.setSelectedFile(new java.io.File("users_data.txt"));
            
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                
                // Create a FileWriter
                java.io.FileWriter writer = new java.io.FileWriter(file);
                
                // Get column metadata
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                
                // Format column headers
                StringBuilder header = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    header.append(String.format("%-20s", metaData.getColumnName(i)));
                }
                writer.write(header.toString() + "\n");
                
                // Format separator line
                StringBuilder separator = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    separator.append(String.format("%-20s", "--------------------"));
                }
                writer.write(separator.toString() + "\n");
                
                // Format each row of data
                while (rs.next()) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        row.append(String.format("%-20s", rs.getString(i)));
                    }
                    writer.write(row.toString() + "\n");
                }
                
                writer.close();
                rs.close();
                
                JOptionPane.showMessageDialog(this, "User data exported successfully to: " + file.getAbsolutePath(), 
                                             "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error exporting user data: " + ex.getMessage(), 
                                         "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}