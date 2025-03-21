package jdbc_template.v3;

import jdbc_template.dto.Item;
import jdbc_template.dto.ItemCreateDto;
import jdbc_template.dto.ItemSearchCond;
import jdbc_template.dto.ItemUpdateDto;
import jdbc_template.v2.JdbcTemplateRepositoryV2;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class JdbcTemplateV3Example {

    public static void main(String[] args) {
        JdbcTemplateRepositoryV3 repository = new JdbcTemplateRepositoryV3();

        Item createdItem = repository.save(new ItemCreateDto("달력", 2000, 20));
        log.info("createdItem: {}", createdItem);

        ItemSearchCond searchCondition = new ItemSearchCond();
        // // searchCondition.setName("감");
        // // searchCondition.setMaxPrice(6000);
        List<Item> items = repository.findAll(searchCondition);
        log.info("item count: {}", items.size());
        for (Item item : items) {
            log.info("item: {}", item);
        }
    }
}
