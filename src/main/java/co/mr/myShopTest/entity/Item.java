package co.mr.myShopTest.entity;

import co.mr.myShopTest.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter @Setter
@ToString
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id; // 상품코드

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; // 가격

    @Column(nullable=false)
    private int stockNumber; // 재고수량
    // CLOB: 사이즈가 큰 데이터를 외부파일로 저장하기 위한 데이터 타입, 문자형 대용량 파일을 저장하는데 사용하는 데이터타입,
    // BLOB: 바이너리 데이터를 DB외부에 저장하기 위한 타입, 이미지, 사운드, 비디오 같은 멀티미디어데이터를 다룰 때 사용
    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING) // enum 타입 매핑
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; //수정시간
}
