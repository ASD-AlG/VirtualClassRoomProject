
import java.util.ArrayList;

/**
 * فئة الطالب (وراثة من User)
 */
public class Student extends User {
    private ArrayList<Course> enrolledCourses;
    private ArrayList<Assignment> assignments;

    public Student(int id, String name, String email, String password) {
        super(id, name, email, password, "Student");
        enrolledCourses = new ArrayList<>();
        assignments = new ArrayList<>();
    }

    public void enrollCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void submitAssignment(Assignment assignment, String filePath) {
        // تنفيذ منطق تسليم الواجب
    }

    @Override
    public void displayInfo() {
        System.out.println("الطالب: " + name);
        System.out.println("البريد الإلكتروني: " + email);
        System.out.println("الدورات المسجلة: " + enrolledCourses.size());
    }
}