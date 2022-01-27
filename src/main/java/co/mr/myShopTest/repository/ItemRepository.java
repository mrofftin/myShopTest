package co.mr.myShopTest.repository;

import co.mr.myShopTest.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 첫번째는 entity타입, 두번째는 기본키 타입
//JpaRepository에서는 기본적인 CRUD/ 페이징처리를 위한 메소드가 정의되어 있다.

// 지원하는 메소드 예
// save : 엔티티저장 및 수정
// delete : 엔티티 삭제
// count : 엔티티 총 개수 반환
// findAll : 모든 엔티티 조회


public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item> {
    // 쿼리메소드는 Spring Data JPA의 핵심 기능 중 하나
    // Repository 인터페이스에 간단한 네이밍 규칙을 이용하여 메소드를 작성하면 쿼리를 실행할 수 있다.
    // find를 가장많이 사용
    // find+(엔티티이름)+By+변수명(필드명) : 엔티티이름은 보통 생략한다. findItemByItemName --> findByItemName
    // 상품명으로 데이터를 조회하기 위해 By뒤에 itemName 필드명을 붙여준다.
    List<Item> findByItemName(String itemName);

    // Or 조건
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    // LessThan 조건
    List<Item> findByPriceLessThan(Integer price);

    // OrderBy desc : OrderBy+속성명+Desc
    // findByPriceLessThan 출력결과를 OrderBy를 이용하여 정렬 조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // 쿼리메소드 단점
    // 조건이 많아지면 메소드명이 길어지게되고, 오히려 이름을 보고
    // 어떻게 동작되는지 해석하기가 힘들어지는 단점이 있다.

    // 간단한 처리시 유용하게 사용
    // 복잡한 쿼리를 다루기에는 부적합

    // 이를 보완하기 위해 Spring Data JPA에서는 @Query 어노테이션 제공
    // @Query 이용하면 SQL과 유사한 JPQL(Java Persistence Query Language)이라는
    // 객체지향쿼리 언어를 통해서 복잡한 쿼리도 처리 가능

    // SQL은 데이터베이스의 테이블을 대상으로 쿼리 수행
    // JPQL은 엔티티 객체를 대상으로 쿼리 수행

    // JPQL로 작성시 데이터베이스가 변경되어도 애플리케이션이 영향을 받지 않게 된다.

    // *** @Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어준다.
    // from 뒤에는 엔티티 클래스명, Item을 i 별칭으로 사용
    // Item 객체로 부터 Item데이터를 select하겠다는 의미

    // @Param는 넘어오는 파라미터값("Sample 상품 상세)을
    // 지정한 변수(itemDetail)로 JPQL(:itemDetail)에 전달
    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);


    // 네이티브 쿼리
    // @Query의 nativeQuery 속성을 사용하면 기존의 데이터베이스에서 사용하던
    // 쿼리를 그대로 사용 가능, 이경우 특정 데이터베이스에 종속된다.(독립성 상실)
    // 통계용 쿼리처럼 복잡한 쿼리를 그대로 사용해야하는 경우 활용할 수 있음

    // value 안에 네이티브 쿼리를 작성하고, nativeQuery=true로 지정
    @Query(value = "select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailNative(@Param("itemDetail") String itemDetail);

    // *** QuerydslPredicateExecutor 상속 추가
    // long count(Predicate) :조건에 맞는 데이터의 총 개수 반환
    // boolean exists(Predicate) :조건에 맞는 데이터 존재 여부 반환
    // Iterable findAll(Predicate) :조건에 맞는 모든 데이터 반환
    // Page<T> findAll(Predicate, Pageable) :조건에 맞는 페이지 데이터 반환
    // Iterable findAll(Predicate, Sort) : 조건에 맞는 정렬된 데이터 반환
    // T findOne(Predicate) : 조건에 맞는 데이터 1개 반환










}
