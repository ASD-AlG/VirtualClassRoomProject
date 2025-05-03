
/**
 * Base User class for the Virtual Classroom System
 */
public abstract class User {
    protected int id;
    protected String name;
    protected String email;
    protected String password;
    protected String role;

    public User(int id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void updateProfile(String name, String email, String password) {
        this.name = name;
        this.email = email;
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    // Abstract method to be implemented by subclasses
    public abstract void displayInfo();
}