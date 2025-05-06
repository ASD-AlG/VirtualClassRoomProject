import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

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
        tabbedPane.addTab("إدارة المستخدمين", createUsersPanel());
        tabbedPane.addTab("إدارة الدورات", createCoursesPanel());
        tabbedPane.addTab("لوحة المعلومات", createDashboardPanel());
        tabbedPane.addTab("إعدادات النظام", createSettingsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("إدارة المستخدمين", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة البحث
        JPanel searchPanel = new JPanel();
        searchPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        searchPanel.add(new JLabel("البحث:"));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);

        JComboBox<String> roleFilter = new JComboBox<>(new String[]{"الكل", "طالب", "معلم", "مسؤول"});
        searchPanel.add(new JLabel("الفئة:"));
        searchPanel.add(roleFilter);

        JButton searchButton = new JButton("بحث");
        searchPanel.add(searchButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // قائمة المستخدمين (بيانات وهمية للعرض)
        


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
            }
        });


        JButton deleteUserButton = new JButton("Delete User");

        buttonPanel.add(addUserButton);
        buttonPanel.add(deleteUserButton);




        panel.add(buttonPanel, BorderLayout.SOUTH);
            
        return panel;
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("إدارة الدورات", JLabel.CENTER);
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
        JButton deleteCourseButton = new JButton("Delete Course");

        buttonPanel.add(addCourseButton);
        buttonPanel.add(deleteCourseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("لوحة المعلومات", JLabel.CENTER);
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
        addStatCard(statsPanel, "إجمالي الطلاب", std);
        addStatCard(statsPanel, "إجمالي المعلمين", tch);
        addStatCard(statsPanel, "إجمالي الدورات", "15");
        addStatCard(statsPanel, "الواجبات النشطة", "25");
        addStatCard(statsPanel, "تسليمات هذا الأسبوع", "120");

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
        JLabel titleLabel = new JLabel("إعدادات النظام", JLabel.CENTER);
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
    
    // Added method to export users to a text file
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