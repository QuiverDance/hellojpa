package hellojpa.hellojpa;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class JpaConnectionTest {

    @Autowired
    EntityManager em;

    @Test
    void test(){
//        Member member = new Member();
//        member.setId(1L); member.setName("userA");
//
//        em.persist(member);
//
//        Member findMember = em.find(Member.class, 1L);
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getName()).isEqualTo("userA");
    }
}
