import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcExample {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/rdb_study", "root", "rootroot"
        );
        PreparedStatement pstmt = connection.prepareStatement("SELECT id, name, position, salary FROM employee");
        ResultSet rs = pstmt.executeQuery();
        System.out.println();
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String position = rs.getString(3);
            int salary = rs.getInt(4);
            System.out.print("id = " + id + ", ");
            System.out.print("name = " + name + ", ");
            System.out.print("position = " + position + ", ");
            System.out.println("salary = " + salary);
        }
    }
}
