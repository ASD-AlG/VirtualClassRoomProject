
import java.util.ArrayList;

/**
 * فئة الدورة التعليمية
 */
public class Course {
    private int id;
    private String title;
    private String description;
    private Teacher teacher;
    private ArrayList<Student> enrolledStudents;
    private ArrayList<Assignment> assignments;

    public Course(int id, String title, String description, Teacher teacher) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacher = teacher;
        this.enrolledStudents = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void enrollStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            student.enrollCourse(this);
        }
    }

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public ArrayList<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
}