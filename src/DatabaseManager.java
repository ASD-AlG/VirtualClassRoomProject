
import java.sql.*;
import java.text.SimpleDateFormat;

import net.proteanit.sql.DbUtils;
import javax.swing.table.TableModel;
public class DatabaseManager {

    private static final String DB_URL      =
            "jdbc:mysql://localhost:3306/VirtualClassroom?serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "1234";

    private Connection connection;

    // -------- constructor --------------------------------------------------
    public DatabaseManager() {
        try {
            // load the MySQL driver so we CAN catch ClassNotFoundException
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTablesIfNotExist();
            System.out.println("connection succesful");
        } catch (SQLException ex) {          // connection‑related problems
            ex.printStackTrace();
            connection = null;
        } catch (ClassNotFoundException ex) { // driver not on class‑path
            System.err.println("❌ MySQL JDBC driver not found ‑ add mysql‑connector‑java‑8.x.x.jar to the class‑path.");
            connection = null;
        }
    }

    // -------- schema -------------------------------------------------------
    private void createTablesIfNotExist() {
        if (connection == null) return;

        final String usersTable =
            "CREATE TABLE IF NOT EXISTS Users ("
          + " user_id  INT PRIMARY KEY AUTO_INCREMENT,"
          + " name     VARCHAR(100) NOT NULL,"
          + " email    VARCHAR(100) NOT NULL UNIQUE,"
          + " password VARCHAR(100) NOT NULL,"
          + " role     ENUM('Student','Teacher','Admin') NOT NULL"
          + ")";

          final String AssignmentTable =
          "CREATE TABLE IF NOT EXISTS Assignment ("
          + " assignment_id INT PRIMARY KEY AUTO_INCREMENT,"
          + " course VARCHAR(100) NOT NULL,"
          + " submissionDate DATE NOT NULL,"
            + " End_date DATE NOT NULL"
        + ")";


        final String CourseTable =
          "CREATE TABLE IF NOT EXISTS courses ("
          + " course_id INT PRIMARY KEY AUTO_INCREMENT,"
          + " course_name VARCHAR(100) NOT NULL,"
          + " enrollmentDate DATE NOT NULL,"
            + " credit_hour INT NOT NULL"
        + ")";


        try (Statement st = connection.createStatement()) {
            st.execute(usersTable);
            st.execute(AssignmentTable);
            st.execute(CourseTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // -------- CRUD helpers (unchanged) ------------------------------------
    public boolean registerUser(String name, String email,
                                String password, String role) {
        if (connection == null) return false;

        final String sql = "INSERT INTO Users(name,email,password,role)"
                         + " VALUES(?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);   // 👉 hash in production
            pst.setString(4, role);
            pst.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            return false;                 // duplicate email
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean insertAssignmen(String course, String submissionDate,
                                String End_date) {
        if (connection == null) return false;

        final String sql = "INSERT INTO Assignment(course,submissionDate,End_date)"
                         + " VALUES(?,?,?)";
                    
                         
        

        try (PreparedStatement pst = connection.prepareStatement(sql)) {

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            try {
                java.util.Date dt = sdf.parse(submissionDate);//add catch bc i suck
                java.sql.Date submissionDate1 = new java.sql.Date(dt.getTime());
                
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                java.util.Date dt1 = sdf1.parse(End_date);//add catch bc i suck
                java.sql.Date End_date1 = new java.sql.Date(dt.getTime());
                
            } catch (Exception e) {
                System.err.println("Parse Exception Thrown");
                e.printStackTrace();
            }


            pst.setString(1, course);
            pst.setDate(2, submissionDate1);
            pst.setDate(3, End_date1);
           
            pst.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            return false;                 // duplicate email
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean validateLogin(String email, String password, String role) {
       
        if (connection == null) return false;

        final String sql =
            "SELECT user_id FROM Users WHERE email=? AND password=? AND role=?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, role);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    public int get_std_num(){
        int count=0;
        
        final String sql =
            "SELECT count(user_id) from Users where role='Student'";
        try (Statement stmt = connection.createStatement();ResultSet rs = stmt.executeQuery(sql);) {
            rs.next();
            count = rs.getInt(1);
            return count;
        
        

        }catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

    }
    public int get_tch_num(){
        int count=0;
        
        final String sql =
            "SELECT count(user_id) from Users where role='Teacher'";
        try (Statement stmt = connection.createStatement();ResultSet rs = stmt.executeQuery(sql);) {
            rs.next();
            count = rs.getInt(1);
            return count;
        
        

        }catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    public TableModel getAssignmentTable()
    {
        final String sql = "select * from assignment";
        try (Statement stmt = connection.createStatement();ResultSet rs = stmt.executeQuery(sql);) {
            return DbUtils.resultSetToTableModel(rs);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                return null;
            }
    }

    public TableModel getCoursesTable()
    {
        final String sql = "select * from courses";
        try (Statement stmt = connection.createStatement();ResultSet rs = stmt.executeQuery(sql);) {
            return DbUtils.resultSetToTableModel(rs);
        }
        catch (SQLException ex) {
            ex.printStackTrace();
                return null;
            }
    }



    public int getUserId(String email) {
        if (connection == null) return -1;

        try (PreparedStatement pst =
                 connection.prepareStatement("SELECT user_id FROM Users WHERE email=?")) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }




    public void close() {
        try { if (connection != null) connection.close(); }
        catch (SQLException ignored) {}
    }
}
