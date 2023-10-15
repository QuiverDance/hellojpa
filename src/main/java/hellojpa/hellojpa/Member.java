package hellojpa.hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "name", insertable = true, updatable = true, nullable = false)
    private String name;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    private LocalDate createdDate;
    private LocalDate lastModifiedDate;

    @Lob
    private String description;
}
