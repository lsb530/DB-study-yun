import response.EmployeeResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JdbcConnectionControlExample {
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<EmployeeResponse> employees = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("입력하세요: ");
            System.out.println("c - DB 연결");
            System.out.println("q - DB 연결 종료");
            System.out.println("종료하려면 'exit' 입력");

            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("c")) {
                try {
                    if (connection == null || connection.isClosed()) {
                        // DB 연결
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/rdb_study", "root", "password"
                        );
                        System.out.println("DB 연결 성공!");
                    } else {
                        System.out.println("이미 DB에 연결되어 있습니다.");
                    }
                } catch (Exception e) {
                    System.out.println("DB 연결 오류");
                    e.printStackTrace();
                }
            } else if (input.equalsIgnoreCase("q")) {
                // try {
                //     if (connection != null && !connection.isClosed()) {
                //         // DB 연결 종료
                //         connection.close();
                //         System.out.println("DB 연결 종료!");
                //     } else {
                //         System.out.println("DB에 연결되지 않았습니다.");
                //     }
                // } catch (SQLException e) {
                //     System.out.println("DB 연결 종료 오류");
                //     e.printStackTrace();
                // }
            } else if (input.equalsIgnoreCase("exit")) {
                break;  // 프로그램 종료
            }

            // DB 연결 후 데이터 조회
            if (connection != null && !connection.isClosed()) {
                try {
                    final String SQL = "SELECT id, name, position, salary FROM employee";
                    pstmt = connection.prepareStatement(SQL);
                    rs = pstmt.executeQuery();
                    employees.clear();
                    while (rs.next()) {
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String position = rs.getString(3);
                        int salary = rs.getInt(4);
                        employees.add(new EmployeeResponse(id, name, position, salary));
                    }
                    for (EmployeeResponse employee : employees) {
                        System.out.println(employee);
                    }
                } catch (SQLException e) {
                    System.out.println("쿼리 실행 오류");
                    e.printStackTrace();
                }
            }
        }

        // 종료 시 자원 해제
        if (scanner != null) {
            scanner.close();
        }
        // if (connection != null && !connection.isClosed()) {
        //     connection.close();
        // }
    }
}
