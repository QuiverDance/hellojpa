package hellojpa.hellojpa.jpql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Getter @Setter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
