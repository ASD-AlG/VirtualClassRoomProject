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

    public TeacherDashboard(String username,String email, DatabaseManager dbManager) {
        this.username = username;
        this.email = email;
        this.dbManager = dbManager;

        // إعداد النافذة
        App.UICHANGEMETHOD();
        setTitle("لوحة تحكم المعلم: " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة التبويب
        tabbedPane = new JTabbedPane();
        tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // إضافة علامات التبويب
        tabbedPane.addTab("الدورات", createCoursesPanel());
        tabbedPane.addTab("الواجبات", createAssignmentsPanel());
        tabbedPane.addTab("تقييم الطلاب", createGradingPanel());
        tabbedPane.addTab("التقارير", createReportsPanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("الدورات الخاصة بك", JLabel.CENTER);
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

        JButton addCourseButton = new JButton("إضافة دورة جديدة");
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
        JButton manageCourseButton = new JButton("إدارة الدورة المحددة");

        buttonPanel.add(addCourseButton);
        buttonPanel.add(manageCourseButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("إدارة الواجبات", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة الواجبات (بيانات وهمية للعرض)
        String[] columnNames = {"رقم الواجب", "الدورة", "العنوان", "الموعد النهائي", "عدد التسليمات"};
        Object[][] data = {
                {"201", "البرمجة بلغة جافا", "واجب #1: برمجة آلة حاسبة", "2023-12-15", "20/25"},
                {"202", "قواعد البيانات", "واجب #1: تصميم قاعدة بيانات", "2023-12-20", "15/20"},
                {"203", "هندسة البرمجيات", "واجب #1: متطلبات المشروع", "2023-12-25", "5/15"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addAssignmentButton = new JButton("إضافة واجب جديد");
        JButton editAssignmentButton = new JButton("تعديل الواجب المحدد");
        JButton viewSubmissionsButton = new JButton("عرض التسليمات");

        buttonPanel.add(addAssignmentButton);
        buttonPanel.add(editAssignmentButton);
        buttonPanel.add(viewSubmissionsButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("تقييم الطلاب", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة التسليمات للتقييم (بيانات وهمية للعرض)
        String[] columnNames = {"الطالب", "الدورة", "الواجب", "تاريخ التسليم", "الدرجة"};
        Object[][] data = {
                {"أحمد محمد", "البرمجة بلغة جافا", "واجب #1", "2023-12-10", "لم يتم التقييم"},
                {"سارة علي", "البرمجة بلغة جافا", "واجب #1", "2023-12-11", "90/100"},
                {"خالد أحمد", "قواعد البيانات", "واجب #1", "2023-12-12", "لم يتم التقييم"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JButton gradeButton = new JButton("وضع درجة للتسليم المحدد");
        panel.add(gradeButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("التقارير والإحصائيات", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // لوحة التقارير
        JPanel reportsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        reportsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton courseStatisticsButton = new JButton("إحصائيات الدورات");
        JButton studentPerformanceButton = new JButton("أداء الطلاب");
        JButton assignmentStatisticsButton = new JButton("إحصائيات الواجبات");
        JButton exportReportButton = new JButton("تصدير التقارير إلى PDF");

        reportsPanel.add(courseStatisticsButton);
        reportsPanel.add(studentPerformanceButton);
        reportsPanel.add(assignmentStatisticsButton);
        reportsPanel.add(exportReportButton);

        panel.add(reportsPanel, BorderLayout.CENTER);

        return panel;
    }
}