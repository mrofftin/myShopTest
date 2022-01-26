package co.mr.myShopTest.repository;

import co.mr.myShopTest.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// 첫번째는 entity타입, 두번째는 기본키 타입
//JpaRepository에서는 기본적인 CRUD/ 페이징처리를 위한 메소드가 정의되어 있다.

// 지원하는 메소드 예
// save : 엔티티저장 및 수정
// delete : 엔티티 삭제
// count : 엔티티 총 개수 반환
// findAll : 모든 엔티티 조회


public interface ItemRepository extends JpaRepository<Item, Long> {
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

}
