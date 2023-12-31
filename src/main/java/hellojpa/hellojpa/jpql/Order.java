package hellojpa.hellojpa.jpql;

import javax.persistence.*;

@Entity
@Table(name = "Orders")
public class Order {

    @Id @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
}
