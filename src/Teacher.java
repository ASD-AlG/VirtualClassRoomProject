
import java.util.ArrayList;

/**
 * فئة المعلم (وراثة من User)
 */
public class Teacher extends User {
    private ArrayList<Course> taughtCourses;

    public Teacher(int id, String name, String email, String password) {
        super(id, name, email, password, "Teacher");
        taughtCourses = new ArrayList<>();
    }

    public void createCourse(String title, String description) {
        // تنفيذ منطق إنشاء دورة
    }

    public void createAssignment(Course course, String title, String description, String deadline) {
        // تنفيذ منطق إنشاء واجب
    }

    public void gradeSubmission(int submissionId, double grade, String feedback) {
        // تنفيذ منطق وضع الدرجة
    }

    @Override
    public void displayInfo() {
        System.out.println("Teacher: " + name);
        System.out.println("Email: " + email);
        System.out.println("Courses: " + taughtCourses.size());
    }
}