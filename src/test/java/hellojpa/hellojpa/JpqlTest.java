package hellojpa.hellojpa;

import hellojpa.hellojpa.jpql.Member;
import hellojpa.hellojpa.jpql.MemberDTO;
import hellojpa.hellojpa.jpql.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@SpringBootTest
class JpqlTest {

	@Autowired
	private EntityManager em;

	@Test
	void basic() {
		//기본
		TypedQuery<Member> query = em.createQuery("select m from Member as m", Member.class);

		//복수 조회(결과가 없으면 빈 리스트 반환)
		List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
		//단건 조회(결과가 없거나 둘 이상이면 예외 발생)
		Member singleResult = em.createQuery("select m from Member as m", Member.class).getSingleResult();

		//이름 기준 파라미터 바인딩
		Member singleResult2 = em.createQuery("select m from Member as m where m.username = :username", Member.class)
				.setParameter("username", "user1")
				.getSingleResult();

	}

	@Test
	void projection(){
		//Entity project, 반환된 entity 객체들은 영속성 컨텍스트에 속함.
		List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();

		//Embedded type projection
		List<Order> resultList2 = em.createQuery("select o.address from Order as o", Order.class).getResultList();

		//scalar type projection
		List<String> resultList3= em.createQuery("select m.username from Member as m", String.class).getResultList();

		//multi value projection
		List<MemberDTO> resultList4 = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member as m", MemberDTO.class).getResultList();
	}

	@Test
	void paging(){
		List<Member> resultList = em.createQuery("select m from member m order by m.age desc", Member.class)
				.setFirstResult(0)
				.setMaxResults(10)
				.getResultList();
	}
}
