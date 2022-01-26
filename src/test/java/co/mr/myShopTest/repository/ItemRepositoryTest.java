package co.mr.myShopTest.repository;

import co.mr.myShopTest.constant.ItemSellStatus;
import co.mr.myShopTest.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// 통합테스트를 위해 스프링 부트에서 제공하는 어노테이션
// 실제 애플리케이션을 구동할 때처럼 모든 Bean을 IoC 컨테이너에 등록한다.
// 애플리케이션 규모가 크면 속도가 느려질 수 있다.
@SpringBootTest
// application.properties보다 먼저 application-test.properties 설정을 사용하겠다는 의미(h2DB 사용)
// 없으면 application.properties를 그대로 사용하게됨
@TestPropertySource(locations = "classpath:application-test.properties")
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
}

// 실행 Spring Data JPA는 인터페이스만 작성하면 이렇게 런타임 시점에 자바의 Dynamic Proxy를 이용해서 객체를 동적으로 생성해준다.