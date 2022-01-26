package co.mr.myShopTest.repository;

import co.mr.myShopTest.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

// 첫번째는 entity타입, 두번째는 기본키 타입
//JpaRepository에서는 기본적인 CRUD/ 페이징처리를 위한 메소드가 정의되어 있다.

// 지원하는 메소드 예
// save : 엔티티저장 및 수정
// delete : 엔티티 삭제
// count : 엔티티 총 개수 반환
// findAll : 모든 엔티티 조회


public interface ItemRepository extends JpaRepository<Item, Long> {

}
