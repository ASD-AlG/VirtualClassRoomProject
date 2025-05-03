
import java.awt.*;
import javax.swing.*;

/**
 * This class is the student dashboard after they login
 */
public class StudentDashboard extends JFrame {
    private String username;
    private DatabaseManager dbManager;
    private JTabbedPane tabbedPane;

    public StudentDashboard(String username, DatabaseManager dbManager) {
        this.username = username;
        this.dbManager = dbManager;

        // إعداد النافذة
        setTitle("لوحة تحكم الطالب: " + username);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // إنشاء لوحة التبويب
        tabbedPane = new JTabbedPane();
        tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // إضافة علامات التبويب
        tabbedPane.addTab("الدورات المسجلة", createCoursesPanel());
        tabbedPane.addTab("الواجبات", createAssignmentsPanel());
        tabbedPane.addTab("الدرجات", createGradesPanel());
        tabbedPane.addTab("الملف الشخصي", createProfilePanel());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("الدورات المسجلة", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة الدورات المسجلة (بيانات وهمية للعرض)
        String[] columnNames = {"رقم الدورة", "اسم الدورة", "المعلم", "الوصف"};
        Object[][] data = {
                {"101", "البرمجة بلغة جافا", "د. محمد", "مقدمة في البرمجة بلغة جافا"},
                {"102", "قواعد البيانات", "د. أحمد", "تصميم وإدارة قواعد البيانات"},
                {"103", "هندسة البرمجيات", "د. علي", "مبادئ تطوير البرمجيات"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // زر للتسجيل في دورة جديدة
        JButton registerCourseButton = new JButton("التسجيل في دورة جديدة");
        panel.add(registerCourseButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAssignmentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("الواجبات", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة الواجبات (بيانات وهمية للعرض)
        String[] columnNames = {"رقم الواجب", "الدورة", "العنوان", "الموعد النهائي", "الحالة"};
        Object[][] data = {
                {"201", "البرمجة بلغة جافا", "واجب #1: برمجة آلة حاسبة", "2023-12-15", "تم التسليم"},
                {"202", "قواعد البيانات", "واجب #1: تصميم قاعدة بيانات", "2023-12-20", "قيد التنفيذ"},
                {"203", "هندسة البرمجيات", "واجب #1: متطلبات المشروع", "2023-12-25", "لم يبدأ بعد"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // زر لتسليم واجب
        JButton submitAssignmentButton = new JButton("تسليم واجب");
        panel.add(submitAssignmentButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        // عنوان
        JLabel titleLabel = new JLabel("الدرجات", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);

        // قائمة الدرجات (بيانات وهمية للعرض)
        String[] columnNames = {"الدورة", "الواجب", "الدرجة", "ملاحظات"};
        Object[][] data = {
                {"البرمجة بلغة جافا", "واجب #1", "90/100", "عمل ممتاز!"},
                {"قواعد البيانات", "واجب #1", "لم يتم التقييم بعد", ""},
                {"هندسة البرمجيات", "واجب #1", "لم يتم التسليم", ""}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        panel.add(new JLabel("اسم المستخدم:"));
        panel.add(new JLabel(username));

        panel.add(new JLabel("البريد الإلكتروني:"));
        panel.add(new JLabel(username + "@example.com")); // بيانات وهمية للعرض

        panel.add(new JLabel("نوع المستخدم:"));
        panel.add(new JLabel("طالب"));

        panel.add(new JLabel("تاريخ التسجيل:"));
        panel.add(new JLabel("2023-11-01")); // بيانات وهمية للعرض

        JButton changePasswordButton = new JButton("تغيير كلمة المرور");
        panel.add(changePasswordButton);

        return panel;
    }
}