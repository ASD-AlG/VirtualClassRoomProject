
/**
 * فئة المسؤول (وراثة من User)
 */
public class Admin extends User {
    public Admin(int id, String name, String email, String password) {
        super(id, name, email, password, "Admin");
    }

    public void manageUsers() {
        // تنفيذ منطق إدارة المستخدمين
    }

    public void manageCourses() {
        // تنفيذ منطق إدارة الدورات
    }

    public void generateReports() {
        // تنفيذ منطق إنشاء التقارير
    }

    @Override
    public void displayInfo() {
        System.out.println("مسؤول: " + name);
        System.out.println("البريد الإلكتروني: " + email);
    }
}