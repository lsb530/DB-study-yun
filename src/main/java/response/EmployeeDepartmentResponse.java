package response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class EmployeeDepartmentResponse {
    private Long id;
    private String name;
    private int age;
    private String position;
    private int salary;
    private String departmentName;
}
