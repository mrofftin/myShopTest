package co.mr.myShopTest.repository;

import co.mr.myShopTest.constant.ItemSellStatus;
import co.mr.myShopTest.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 통합테스트를 위해 스프링 부트에서 제공하는 어노테이션
// 실제 애플리케이션을 구동할 때처럼 모든 Bean을 IoC 컨테이너에 등록한다.
// 애플리케이션 규모가 크면 속도가 느려질 수 있다.
@SpringBootTest
// application.properties보다 먼저 application-test.properties 설정을 사용하겠다는 의미(h2DB 사용)
// 없으면 application.properties를 그대로 사용하게됨
//@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {
    // ItemRepository를 사용하기 위해서는 주입을 받아야 함.
    @Autowired
    ItemRepository itemRepository;

    @Test // 테스트할 메소드위에 선언하면 해당 메소드를 테스트 대상으로 지정한다.
    @DisplayName("상품 저장 테스트") // Junit5에 추가된 어노테이션으로 테스트 코드 실행시 지정된 테스트명이 노출된다.
    public void createItemTest() {
        Item item = new Item();
        item.setItemName("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    // 데이터베이스에 상품데이터가 없으므로 테스트 데이터 생성을 위한 10개의 상품을 저장
    public void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("Sample 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("Sample 상품 상세" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    // 쿼리메소드

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        this.createItemList(); // 테스트 상품을 만드는 메소드 호출
        List<Item> itemList = itemRepository.findByItemName("Sample 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    // 상품을 상품명과 상품상세설명을 OR 조건을 이용하여 조회하는 쿼리메소드
    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        this.createItemList(); // 테스트 상품을 만드는 메소드 호출
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("Sample 상품1", "Sample 상품 상세3");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(10006);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("Sample 상품 상세");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("Sample 상품 상세");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }

}

// 실행 Spring Data JPA는 인터페이스만 작성하면 이렇게 런타임 시점에
// 자바의 Dynamic Proxy를 이용해서 객체를 동적으로 생성해준다.