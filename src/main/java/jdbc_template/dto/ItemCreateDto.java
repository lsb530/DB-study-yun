package jdbc_template.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ItemCreateDto {
    private String name; // getName() <-> name
    private Integer price; // getPrice() <-> price
    private Integer quantity; // getQu.. <-> qu..
}
