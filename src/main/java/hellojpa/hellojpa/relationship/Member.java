package hellojpa.hellojpa.relationship;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Embedded
    private Period period;

    @Embedded
    private Address address;

    /*
    값 타입 collection mapping
    값 타입 collection은 select box 선택처럼 아주 단순하고, 변경이 발생했을 때 추적이 필요없는 경우에만 사용하는게 좋다.
    그 외의 경우에는 값 타입을 wrapping 하는 entity 클래스를 생성하여 one to many 관계로 만든다.
     */
    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME")
    private Set<String> favoriteFoods = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "ADDRESSES", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    private List<Address> addressHistory = new ArrayList<>();

    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
