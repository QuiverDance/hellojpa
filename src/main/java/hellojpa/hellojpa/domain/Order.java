package hellojpa.hellojpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;
//    @Column(name = "MEMBER_ID")
//    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /*
    아래 매핑은 굳이 필요 없음, OrderItem에 OrderId가 있으므로 orderItems를 만들어 낼 수 있음.
     */
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
