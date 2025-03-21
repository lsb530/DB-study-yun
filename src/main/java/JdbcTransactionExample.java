import lombok.extern.slf4j.Slf4j;
import request.Department;
import request.EmployeeCreateRequest;
import response.EmployeeResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcTransactionExample {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, InterruptedException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            log.debug("직원 전체 조회");
            List<EmployeeResponse> employees = getEmployees(connection);
            for (EmployeeResponse employee : employees) {
                log.info("result: {}", employee);
            }

            // log.debug("직원 단건 조회");
            // Optional<EmployeeResponse> findEmployee = getEmployeeById(connection, 1L);
            // findEmployee.ifPresentOrElse(response -> log.info("result: {}", response), () -> log.warn("데이터 없음"));
            //
            // log.debug("직원 추가");
            // insertEmployee(connection, new EmployeeCreateRequest("Hello", 30, "Something Engineer", 12345, Department.IT));
            // log.debug("데이터 삽입 후 직원 전체 조회");
            // for (EmployeeResponse employee : getEmployees(connection)) {
            //     log.info("result: {}", employee);
            // }

            Thread.sleep(5000);
        } catch (SQLException e) {
            assert connection != null;
            connection.rollback();
            log.error(e.getMessage());
            throw e;
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    public static List<EmployeeResponse> getEmployees(Connection connection) throws SQLException {
        final String SQL = "SELECT id, name, position, salary FROM employee";
        List<EmployeeResponse> employees = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String position = rs.getString(3);
                int salary = rs.getInt(4);
                employees.add(new EmployeeResponse(id, name, position, salary));
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
        return employees;
    }

    public static Optional<EmployeeResponse> getEmployeeById(Connection connection, Long employeeId) throws SQLException {
        final String sql = "SELECT id, name, position, salary FROM employee WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setLong(1, employeeId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String position = rs.getString(3);
                int salary = rs.getInt(4);
                return Optional.of(new EmployeeResponse(id, name, position, salary));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
        return Optional.empty();
    }

    private static void insertEmployee(Connection connection, EmployeeCreateRequest request) throws SQLException {
        final String sql = "INSERT INTO employee (name, age, position, salary, department_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, request.getName());
            pstmt.setInt(2, request.getAge());
            pstmt.setString(3, request.getPosition());
            pstmt.setInt(4, request.getSalary());
            pstmt.setInt(5, request.getDepartment().ordinal());

            System.out.println(pstmt);
            // pstmt.execute(); 전체
            // pstmt.executeQuery(); // 조회
            pstmt.executeUpdate(); // 조회 제외(추가, 수정, 삭제)
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/rdb_study_test", "root", "password");
    }

}
