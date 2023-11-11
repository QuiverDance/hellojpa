package hellojpa.hellojpa.jpql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

    @Id @GeneratedValue
    private long id;
    private String name;
    private int price;
    private int stockAmount;
}
