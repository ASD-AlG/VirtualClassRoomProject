
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

import org.w3c.dom.events.MouseEvent;

/**
 * واجهة المسؤول الرئيسية
 */
public class AdminDashboard extends JFrame {
    private String username;
    private DatabaseManager dbManager;

    private JTabbedPane tabbedPane;


    
    public AdminDashboard(String username, DatabaseManager dbManager) {
        this.username = username;
        this.dbManager = dbManager;

        // إعداد النافذة
        App.UICHANGEMETHOD();
        setTitle("لوحة تحكم المسؤول: " + username);
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
        
        String[] columnNames = {"المعرف", "الاسم", "البريد الإلكتروني", "نوع المستخدم", "تاريخ التسجيل"};
        Object[][] data = {
            
                {"1", "أحمد محمد", "ahmed@example.com", "طالب", "2023-10-01"},
                {"2", "سارة علي", "sarah@example.com", "طالب", "2023-10-05"},
                {"3", "د. محمد", "dr.mohamed@example.com", "معلم", "2023-09-15"},
                {"4", "د. أحمد", "dr.ahmed@example.com", "معلم", "2023-09-20"},
                {"5", "Admin", "admin@example.com", "مسؤول", "2023-09-01"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addUserButton = new JButton("إضافة مستخدم");
        JButton editUserButton = new JButton("تعديل المستخدم المحدد");
        JButton deleteUserButton = new JButton("حذف المستخدم المحدد");

        buttonPanel.add(addUserButton);
        buttonPanel.add(editUserButton);
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

        // قائمة الدورات (بيانات وهمية للعرض)
        String[] columnNames = {"رقم الدورة", "اسم الدورة", "المعلم", "عدد الطلاب", "تاريخ الإنشاء"};
        Object[][] data = {
                {"101", "البرمجة بلغة جافا", "د. محمد", "25", "2023-10-01"},
                {"102", "قواعد البيانات", "د. أحمد", "20", "2023-10-05"},
                {"103", "هندسة البرمجيات", "د. علي", "15", "2023-10-10"}
        };

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // أزرار للتحكم
        JPanel buttonPanel = new JPanel();
        buttonPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton addCourseButton = new JButton("إضافة دورة");
        JButton editCourseButton = new JButton("تعديل الدورة المحددة");
        JButton deleteCourseButton = new JButton("حذف الدورة المحددة");
        JButton enrollStudentsButton = new JButton("تسجيل الطلاب");

        buttonPanel.add(addCourseButton);
        buttonPanel.add(editCourseButton);
        buttonPanel.add(deleteCourseButton);
        buttonPanel.add(enrollStudentsButton);

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
        JButton exportReportButton = new JButton("تصدير التقارير");
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
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        settingsPanel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        settingsPanel.add(new JLabel("اسم النظام:"));
        settingsPanel.add(new JTextField("نظام إدارة الفصول الافتراضية"));

        settingsPanel.add(new JLabel("البريد الإلكتروني للنظام:"));
        settingsPanel.add(new JTextField("system@example.com"));

        settingsPanel.add(new JLabel("الحد الأقصى لحجم الملفات (MB):"));
        settingsPanel.add(new JTextField("20"));

        settingsPanel.add(new JLabel("عدد المحاولات المسموح بها لتسجيل الدخول:"));
        settingsPanel.add(new JTextField("3"));

        settingsPanel.add(new JLabel("التنبيهات عبر البريد الإلكتروني:"));
        JCheckBox emailAlertsCheckBox = new JCheckBox("تفعيل");
        emailAlertsCheckBox.setSelected(true);
        settingsPanel.add(emailAlertsCheckBox);

        JButton saveSettingsButton = new JButton("حفظ الإعدادات");
        settingsPanel.add(saveSettingsButton);

        panel.add(settingsPanel, BorderLayout.CENTER);

        return panel;
    }
}