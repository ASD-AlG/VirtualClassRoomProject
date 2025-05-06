import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.*;

/**
 * واجهة المعلم الرئيسية
 */
public class TeacherDashboard extends JFrame {
    private String username;
    private String email;
    private DatabaseManager dbManager;

    private JTabbedPane tabbedPane;

    public TeacherDashboard(String username, String email, DatabaseManager dbManager) {
        this.username = username;
        this.email = email;
        this.dbManager = dbManager;

        // إعداد النافذة
        App.UICHANGEMETHOD();
        setTitle("Teacher Dashboard: " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة التبويب
        tabbedPane = new JTabbedPane();
        tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // إضافة علامات التبويب
        tabbedPane.addTab("Courses", createCoursesPanel());
        tabbedPane.addTab("Announcements", createAssignmentsPanel());
        tabbedPane.addTab("Student grades", createGradingPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("your courses", JLabel.CENTER);
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
                JDialog addCourseDialog = new JDialog(TeacherDashboard.this, "Add New Course", true);
                addCourseDialog.setSize(400, 300);
                addCourseDialog.setLocationRelativeTo(TeacherDashboard.this);
                
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
                            JTable courseTable = (JTable) ((JScrollPane) panel.getComponent(1)).getViewport().getView();
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

        buttonPanel.add(addCourseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // Create assignments table and populate it with data from the database
        JTable asstable = new JTable();
        asstable.setDefaultEditor(Object.class, null);
        asstable.setModel(dbManager.getAssignmentTable());
        JScrollPane scrollPane = new JScrollPane(asstable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addAssignmentButton = new JButton("New Announcement");
        addAssignmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new dialog for adding an assignment
                JDialog addAssignmentDialog = new JDialog(TeacherDashboard.this, "Add New Assignment", true);
                addAssignmentDialog.setSize(500, 350);
                addAssignmentDialog.setLocationRelativeTo(TeacherDashboard.this);
                
                // Use GridBagLayout for more flexible layout
                JPanel formPanel = new JPanel(new GridBagLayout());
                formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 5, 5, 5);
                
                // Course field
                gbc.gridx = 0;
                gbc.gridy = 0;
                formPanel.add(new JLabel("Course:"), gbc);
                
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                JTextField courseField = new JTextField(20);
                formPanel.add(courseField, gbc);
                
                // Assignment description/question field
                gbc.gridx = 0;
                gbc.gridy = 1;
                gbc.weightx = 0.0;
                formPanel.add(new JLabel("Assignment Description:"), gbc);
                
                gbc.gridx = 0;
                gbc.gridy = 2;
                gbc.gridwidth = 2;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;
                JTextArea questionArea = new JTextArea(5, 30);
                questionArea.setLineWrap(true);
                questionArea.setWrapStyleWord(true);
                JScrollPane questionScrollPane = new JScrollPane(questionArea);
                formPanel.add(questionScrollPane, gbc);
                
                // Submit button
                JButton submitButton = new JButton("Add Assignment");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String course = courseField.getText().trim();
                        String question = questionArea.getText().trim();
                        
                        // Input validation
                        if (course.isEmpty()) {
                            JOptionPane.showMessageDialog(addAssignmentDialog, 
                                "Please enter a course name!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        if (question.isEmpty()) {
                            JOptionPane.showMessageDialog(addAssignmentDialog, 
                                "Please enter an assignment description!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        // Current date for submission date
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
                        
                        // End date is 7 days from now
                        java.util.Calendar cal = java.util.Calendar.getInstance();
                        cal.add(java.util.Calendar.DATE, 7);
                        String endDate = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
                        
                        // Save to database
                        if (dbManager.insertAssignment(course, currentDate, endDate, question)) {
                            JOptionPane.showMessageDialog(addAssignmentDialog, 
                                "Assignment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            
                            // Refresh the assignment table
                            asstable.setModel(dbManager.getAssignmentTable());
                            addAssignmentDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(addAssignmentDialog, 
                                "Failed to add assignment!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                
                // Cancel button
                JButton cancelButton = new JButton("Cancel");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addAssignmentDialog.dispose();
                    }
                });
                
                // Button panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                buttonPanel.add(submitButton);
                buttonPanel.add(cancelButton);
                
                // Assemble the dialog
                addAssignmentDialog.setLayout(new BorderLayout());
                addAssignmentDialog.add(formPanel, BorderLayout.CENTER);
                addAssignmentDialog.add(buttonPanel, BorderLayout.SOUTH);
                
                addAssignmentDialog.setVisible(true);
            }
        });

        buttonPanel.add(addAssignmentButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Student grades", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة التسليمات للتقييم (بيانات وهمية للعرض)
        String[] columnNames = {"Student", "Course", "Assignment", "Submission Date", "Grade"};
        Object[][] data = {
                {"ahmed", "object oriented programing", "Q1", "6/5/2025", "10/10"},
                {"ahmed", "object oriented programing", "Q2", "6/5/2025", "10/10"},
                {"ahmed", "object oriented programing", "Q3", "6/5/2025", "10/10"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JButton gradeButton = new JButton("Grade assignment");
        panel.add(gradeButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Reports", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة التقارير
        JPanel reportsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        reportsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton courseStatisticsButton = new JButton("courses");
        JButton studentPerformanceButton = new JButton("Student performance");
        JButton assignmentStatisticsButton = new JButton("Assignments");
        JButton exportReportButton = new JButton("Print report as PDF");

        reportsPanel.add(courseStatisticsButton);
        reportsPanel.add(studentPerformanceButton);
        reportsPanel.add(assignmentStatisticsButton);
        reportsPanel.add(exportReportButton);

        panel.add(reportsPanel, BorderLayout.CENTER);

        return panel;
    }
}