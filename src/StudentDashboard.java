
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * This class is the student dashboard after they login
 */
public class StudentDashboard extends JFrame {
    private String username;
    private String email;
    private DatabaseManager dbManager;
    private JTabbedPane tabbedPane;

    public StudentDashboard(String username,String email, DatabaseManager dbManager) {
        this.username = username;
        this.dbManager = dbManager;
        this.email = email;
        // إعداد النافذة
        App.UICHANGEMETHOD();
        setTitle("Student Dashboard: " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة التبويب
        tabbedPane = new JTabbedPane();
        tabbedPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        // إضافة علامات التبويب
        tabbedPane.addTab("Enrolled Courses ", createCoursesPanel());
        tabbedPane.addTab("Announcement", createAssignmentsPanel());
        tabbedPane.addTab("Grades", createGradesPanel());
        tabbedPane.addTab("Personal Profile", createProfilePanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Enrolled Courses", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTable StudentCourses = new JTable();
        StudentCourses.setModel(dbManager.getCoursesTable());
        StudentCourses.setDefaultEditor(Object.class, null);
        JScrollPane scrollPane = new JScrollPane(StudentCourses);
        panel.add(scrollPane, BorderLayout.CENTER);

        // زر للتسجيل في دورة جديدة

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Announcement", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة الواجبات (بيانات وهمية للعرض)
        JTable StudentAssignments = new JTable();
        StudentAssignments.setDefaultEditor(Object.class, null);
        StudentAssignments.setModel(dbManager.getAssignmentTable());
        JScrollPane scrollPane = new JScrollPane(StudentAssignments);
        panel.add(scrollPane, BorderLayout.CENTER);

        // زر لتسليم واجب
        JButton submitAssignmentButton = new JButton("Submet Assignment");
        panel.add(submitAssignmentButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("Grades", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);



        JTable gradestable = new JTable();
        gradestable.setModel(dbManager.getGradesTable());
        gradestable.setDefaultEditor(Object.class, null);

        JScrollPane scrollPane = new JScrollPane(gradestable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        panel.add(new JLabel("Username"));
        panel.add(new JLabel(dbManager.getName(email)));

        panel.add(new JLabel("Email:"));
        panel.add(new JLabel(email));

        panel.add(new JLabel("Role:"));
        panel.add(new JLabel("Student"));

        JButton logout = new JButton("Log out");
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm newlogin = new LoginForm();
                newlogin.setLocationRelativeTo(null);
            }
        });
        panel.add(logout);

        return panel;
    }
}