package org.lsh.jpademo.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.lsh.jpademo.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
@Log4j2
public class ItemRepositoryTest {
//    @Autowired
//    private ItemRepository itemRepository;
//    @Test
//    public void testInsert() {
//        Item item = new Item();
//        item.setItemNm("트윅스");
//        item.setPrice(1200L);
//        item.setStockNumber(11L);
//        item.setItemDetail("에너지바");
//        item.setRegTime(new Date());
//        item.setUpdateTime(new Date());
//        itemRepository.save(item);
//    }
//    @Test
//    public void getList(){
//        List<Item> items = itemRepository.findAll();
//        for(Item item : items){
//            log.info(item);
//        }
//    }
//    @Test
//    public void selectOne(){
//        Item item = itemRepository.findById(1L).get();
//        log.info(item);
//    }
//    @Test
//    public void update(){
//        Item item = itemRepository.findById(1L).get();
//        item.setItemNm("컵라면");
//        item.setPrice(1500L);
//        item.setItemDetail("간단한 한끼");
//        item.setStockNumber(7L);
//        item.setUpdateTime(new Date());
//        itemRepository.save(item);
//    }
//    @Test
//    public void deleteItem(){
//        itemRepository.deleteById(2L);
//    }
}
