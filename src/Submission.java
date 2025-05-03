
/**
 * فئة التسليم (للواجبات)
 */
public class Submission {
    private int id;
    private Assignment assignment;
    private Student student;
    private String filePath;
    private double grade;
    private String feedback;
    private String submissionDate;

    public Submission(int id, Assignment assignment, Student student, String filePath, String submissionDate) {
        this.id = id;
        this.assignment = assignment;
        this.student = student;
        this.filePath = filePath;
        this.submissionDate = submissionDate;
        this.grade = -1; // لم يتم وضع درجة بعد
        this.feedback = "";
    }

    public int getId() {
        return id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public Student getStudent() {
        return student;
    }

    public String getFilePath() {
        return filePath;
    }

    public double getGrade() {
        return grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getSubmissionDate() {
        return submissionDate;
    }

    public void setGrade(double grade, String feedback) {
        this.grade = grade;
        this.feedback = feedback;
    }
}