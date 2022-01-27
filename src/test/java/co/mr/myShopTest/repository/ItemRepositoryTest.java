package co.mr.myShopTest.repository;

import co.mr.myShopTest.constant.ItemSellStatus;
import co.mr.myShopTest.entity.Item;
import co.mr.myShopTest.entity.QItem;
import com.mysql.cj.QueryResult;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    // 영속성 컨텍스트를 사용하기 위해
    // @PersistenceContext을 이용해서 EntityManager 빈을 주입 받는다.
    @PersistenceContext
    EntityManager em;

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
    public void findByPriceLessThanOrderByPriceDesc() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("Sample 상품 상세");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailNative() {
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemDetail("Sample 상품 상세");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    // Querydsl
    // @Query 어노테이션을 이용하는 방법에도 단점이 있다. @Query 어노테이션 안에 JPQL 문법으로 문자열을
    // 입력하기 때문에 잘못 입력하면 컴파일 시점에 에러를 발견할 수 없다.
    // 이를 보완할 수 있는 방법으로 Querydsl을 이용한다.

    // Querydsl 장점
    // 조건에 맞게 동적으로 쿼리를 생성
    // 비슷한 쿼리를 재사용할 수 있으며 제약조건 조립 및 가독성 향상
    // 문자열이 아닌 자바 소스코드로 작성하기 때문에 컴파일 시점에서 오류를 발견
    // IDE 도움을 받아서 자동 완성기능 이용할 수 있어 생산성 향상

    // Querydsl을 사용하기 위한 기본세팅
    // pom.xml 설정
    // 의존성 추가 querydsl-jpa, querydsl-apt

    // Qdomain이라는 자바코드를 생성하는 플러그인을 추가해준다.
    // 예를 들어, Item 테이블의 경우 QItem.java클래스가 자동으로 생성된다.
    // Querydsl을 통해서 쿼리를 생성할 때 Qdomain 객체를 사용한다.

    // maven Lifecycle 아래 compile을 더블 클릭한다. 빌드가 완료되면 target폴더가 생성되고
    // generated-sources 폴더에 QItem클래스가 생성된다.
    // QItem 클래스에는 Item 클래스의 모든 필드들에 대해서 사용 가능한 operation을 호출할 수 있는
    // 메소드가 정의돼 있다.


    @Test
    @DisplayName("Querydsl 조회테스트")
    public void queryDslTest() {
        this.createItemList();
        // JPAQueryFactory를 이용하여 쿼리를 동적으로생성한다.
        // 생성자의 파라미터로는 EntityManager 객체를 넣어준다.
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // Querydsl을 통해 쿼리를 생성하기 위해 플러그인을 통해 자동으로 생성된 QItem객체를 이용한다.
        QItem qItem = QItem.item;
        // SQL과 비슷하게 소스를 작성할 수 있다.
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "Sample 상품 상세" + "%"))
                .orderBy(qItem.price.desc());


        // JPAQuery 메소드 중 하나인 fetch를 이용해서 쿼리 결과를 리스트로 반환한다.
        // fetch()메소드 실행 시점에 쿼리문이 실행된다.

        // JPAQuery 데이터 반환 메소드
//        List<T> fetch() : 조회결과 리스트 반환;
//        T fetchOne : 조회대상이 1건인 경우 제네릭으로 지정한 타입반환;
//        T fetchFirst() :조회대상 중 1 건만 반환;
//        Long fetchCount() : 조회대상 개수 반환;
//        QueryResult<T> fetchResults() : 조회한 리스트와 전체 개수를 포함한 QueryResults반환

        List<Item> itemList = query.fetch();
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        // 판매중 셋팅
        for (int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        // 품절 상태 셋팅
        for (int i = 11; i <= 15; i++) {
            Item item = new Item();
            item.setItemName("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("Queryds 조회 테스트 2")
    public void queryDslTest2() {
        this.createItemList2();

        // 쿼리에 들어갈 조건을 만들어주는 빌더
        // Predicate를 구현하고 있으며 메소드 체인형식으로 사용할 수 있다.
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세";
        int price = 10003;
        String itemSellStat = "SELL";

        // 원하는 상품을 조회하는데 필요한 and 조건을 추가
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        // 10003보다 큰값
        booleanBuilder.and(item.price.gt(price));

        // 상품의 판매상태가 SELL일 때만 BooleanBuilder에 판매상태 조건을 동적으로 추가한다.
        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        // 데이터를 페이징해 조회하도록 PageRequest.of()메소드를 이용해 Pageable객체를 생성
        // 0부터 5개
        Pageable pageable = PageRequest.of(0, 5);
//        QuerydslPredicateExecutor인터페이스에서 정의한 findAll()메소드를 이용해 조건에 맞는 데이터를 Page객체로 받아온다.
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }



    }
}

// 실행 Spring Data JPA는 인터페이스만 작성하면 이렇게 런타임 시점에
// 자바의 Dynamic Proxy를 이용해서 객체를 동적으로 생성해준다.