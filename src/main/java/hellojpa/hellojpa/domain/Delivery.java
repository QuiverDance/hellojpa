package hellojpa.hellojpa.domain;

import javax.persistence.*;

@Entity
public class Delivery {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    private String city;
    private String street;
    private String zipcode;
    private DeliveryStatus status;
}
