import lombok.extern.slf4j.Slf4j;
import response.EmployeeDepartmentResponse;
import response.EmployeeResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JdbcJoinExample {

    private enum JOIN_TYPE {
        LEFT, RIGHT
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
            connection = getConnection();
            log.debug("Inner Join");
            List<EmployeeDepartmentResponse> employeeDepartmentResponse = getEmployeesWithDepartment(connection);
            log.info("size: {}", employeeDepartmentResponse.size());
            for (EmployeeDepartmentResponse employeeWithDepartment : employeeDepartmentResponse) {
                log.info("result: {}", employeeWithDepartment);
            }

            log.debug("Left Join");
            List<EmployeeDepartmentResponse> employeeDepartmentLeftResponse = getEmployeesWithDepartment(connection, JOIN_TYPE.LEFT);
            log.info("size: {}", employeeDepartmentLeftResponse.size());
            for (EmployeeDepartmentResponse employeeWithDepartment : employeeDepartmentLeftResponse) {
                log.info("result: {}", employeeWithDepartment);
            }

            log.debug("Right Join");
            List<EmployeeDepartmentResponse> employeeDepartmentRightResponse = getEmployeesWithDepartment(connection, JOIN_TYPE.RIGHT);
            log.info("size: {}", employeeDepartmentRightResponse.size());
            for (EmployeeDepartmentResponse employeeWithDepartment : employeeDepartmentRightResponse) {
                log.info("result: {}", employeeWithDepartment);
            }
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    public static List<EmployeeDepartmentResponse> getEmployeesWithDepartment(Connection connection) throws SQLException {
        final String sql = """
            SELECT e.id, e.name, e.age, e.position, e.salary, d.name AS department FROM employee e
             JOIN department d ON e.department_id = d.id
            """.trim();
        List<EmployeeDepartmentResponse> employeesWithDepartment = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                int age = rs.getInt(3);
                String position = rs.getString(4);
                int salary = rs.getInt(5);
                String departmentName = rs.getString(6);
                employeesWithDepartment.add(new EmployeeDepartmentResponse(id, name, age, position, salary, departmentName));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            if (pstmt != null)
                pstmt.close();
            if (rs != null)
                rs.close();
        }
        return employeesWithDepartment;
    }

    public static List<EmployeeDepartmentResponse> getEmployeesWithDepartment(Connection connection, JOIN_TYPE type) throws SQLException {
        String sql = """
            SELECT e.id, e.name, e.age, e.position, e.salary, d.name AS department FROM employee e
             %s JOIN department d ON e.department_id = d.id
            """.trim();
        sql = switch (type) {
            case LEFT -> sql.replace("%s", "LEFT");
            case RIGHT -> sql.replace("%s", "RIGHT");
        };
        List<EmployeeDepartmentResponse> employeesWithDepartment = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                int age = rs.getInt(3);
                String position = rs.getString(4);
                int salary = rs.getInt(5);
                String departmentName = rs.getString(6);
                employeesWithDepartment.add(new EmployeeDepartmentResponse(id, name, age, position, salary, departmentName));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } finally {
            if (pstmt != null)
                pstmt.close();
            if (rs != null)
                rs.close();
        }
        return employeesWithDepartment;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb_study_test", "root", "password");
    }
}
