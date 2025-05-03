

import java.util.ArrayList;

/**
 * فئة الواجب
 */
public class Assignment {
    private int id;
    private Course course;
    private String title;
    private String description;
    private String deadline;
    private ArrayList<Submission> submissions;

    public Assignment(int id, Course course, String title, String description, String deadline) {
        this.id = id;
        this.course = course;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.submissions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void addSubmission(Submission submission) {
        submissions.add(submission);
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }
}