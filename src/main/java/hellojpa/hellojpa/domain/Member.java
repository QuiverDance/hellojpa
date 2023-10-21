package hellojpa.hellojpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    /*
    아래 정보는 굳이 필요없음. Order 만으로도 충분히 Member의 주문 내역을 확인할 수 있음
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
