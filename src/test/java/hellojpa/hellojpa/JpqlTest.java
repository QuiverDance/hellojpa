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
	
	@Test
	void join(){
		//inner join
		List<Member> resultList = em.createQuery("select m from Member as m inner join m.team t", Member.class).getResultList();

		//left (outer) join, outer는 생략가능
		List<Member> resultList2 = em.createQuery("select m from Member as m left outer join m.team t", Member.class).getResultList();

		//product(theta) join
		List<Member> resultList3 = em.createQuery("select m from Member as m, Team as t", Member.class).getResultList();

		//join result filtering
		List<Member> resultList4 = em.createQuery("select m from Member as m left outer join m.team t on t.name = 'teamA'", Member.class).getResultList();

		//filtering
		List<Member> resultList5 = em.createQuery("select m from Member as m left outer join m.team t on m.name = t.name", Member.class).getResultList();
	}

	@Test
	void subQuery(){
		//나이가 평균보다 많은 회원
		List<Member> resultList = em.createQuery("select m from Member m where m.age (select avg(m2.age) from Member m2", Member.class).getResultList();

		//JPA는 WHERE와 HAVING 절에서 서브쿼리 사용 가능
		//하이버네이트에서는 SELECT 절에서도 가능
		//FROM 절의 JPQL에서 서브쿼리는
		// 불가능 -> JOIN으로 할 수 있으면 하고 JOIN으로 불가능한 경우에는 native 쿼리를 쓰거나, 퀴리 분리 해서 여러번 보내기, 애플리케이션에서 따로 해결의 방법을 사용한다
	}

	@Test
	void typeExpression(){
		//문자 = '여기에 내용'. 문자열 내에 따옴표는 ''로 표현
		//숫자 = 10L 10D 10F와 같은 형식으로 표현
		//Boolean = TRUE, FALSE
		//ENUM = jpabook.MemberType.ADMIN (패키지명 포함), 파라미터 바인딩으로 사용할 때에는 별 상관 없음.
		//엔티티 타입 = TYPE(m) = Member (상속 관계에서 사용)
		//ex) createQuery("select i from Item i where type(i) = Book", Item.class)
	}

	@Test
	void condition(){
		//case
		em.createQuery("select " +
				"case when m.age <= 10 then '학생 요금' " +
				"when m.age >= 60 then '경로 요금' " +
				"else '일반요금' " +
				"end " +
				"from member m");

		//COALESCE : 하나씩 조회해서 null이 아니면 반환
		//username이 null인 회원을 '이름 없는 회원으로 반환'
		em.createQuery("select coalesce(m.username, '이름 없는 회원' from Member m");

		//NULLIF : 두 값이 같으면 null 반환, 다르면 첫번째 값 반환
		//username이 '관리자'이면 null 반환'
		em.createQuery("select NULLIF(m.username, '관리자' from Member m");
	}

	@Test
	void jpqlBasicFunction(){
		//CONCAT select concat('a', 'b') from member m
		//SUBSTRING select substring(m.username, 2, 3) from member m
		//TRIM : 공백 제거
		//LOWER, UPPER : 대소문자 병경
		//LENGTH : 문자 길이
		//LOCATE : 문자 위치 select locate('de', 'abcdef') from member m
		//ABS, SQRT, MOD
		//SIZE : collection의 크기 select size(t.members0 from Team t
		//INDEX(JPA 용도) , @ORDERCOLUMN에서 컬렉션의 위치값을 구할 때 쓸 수 있음, @ORDERCOLUMN은 안 쓰는게 좋기 때문에 잘 안씀
	}

	@Test
	void customFunction(){
		//Dialect를 따로 확장하여 미리 등록해야함.
		//select function('function_name', i.name) from Item i
	}

	@Test
	void pathExpression(){
		//상태 필드 : 경로 탐색의 끝, 더이상 탐색하지 않음. select m.username From Member m
		//단일 값 연관 경로 : 묵시적 내부 조인 발생, 탐색이 더 진행 될 수 있음. -> 묵시적 조인은 잘 사용하지 않는 것이 좋음.
		//select m.team.name From Member m
		//컬렉션 값 연관 경로 : 묵시적 내부 조인 발생, 탐색이 더 진행되지 않음, From 절에서 명시적 조인을 통해 별칭을 얻으면 별칭으로 탐색가능
		//select t.members From Team
	}

	@Test
	void fetchJoin(){
		//many to one fetch join
		//select m from Member m join fetch m.team -> SELECT M.*, T.* FROM MEMBER M INNER JOIN TEAM T ON M.TEAM_ID=T.ID

		//one to many fetch join
		//select t from Team t join fetch t.members where t.name = '팀A'
		//-> SELECT T.*. M.* FROM TEAM T INNER JOIN MEMBER M ON T.ID=M.TEAM_ID WHERE T.NAME = '팀A'

		//JPQL의 DISTINCT는 SQL의 DISTINCT의 기능 + 중복 엔티티를 제거한다.
		//select distinct t from Team t join fetch t.members

		//fetch join의 대상에는 별칭을 줄 수 없다.
		//둘 이상의 컬렉션은 페치조인 할 수 없다.
		//컬렉션을 페치조인하면 페이징 API를 사용할 수 없다.
		//-일대일, 일대다의 페치 조인의 경우 사용할 수 있지만, 일대다에서는 불가능(데이터의 수가 늘어날 수 있기 때문)
	}

	@Test
	void typeQuery(){
		//select i from Item i where type(i) IN (Book, Movie)

		//select i from Item i where treat(i as Book).author = 'Kim'
	}

	@Test
	void entityUsage(){
		//select count(m) from Member m -> 엔티티를 직접 사용
		//JPQL에서 엔티티를 직접 사용하면 SQL에서 해당 엔티티의 기본키 값을 사용
		//== select count(m.id) from Member
		//엔티티를 파라미터로 넘겨도 동일
		//select m from Member m where m.id = :member에서 member를 엔티티로 넘기면 sql에서는 id로 변환된다.

		//외래키 값 사용
		//select m from Member m where m.team = :team"
		//역시 SQL에서 id로 변환된다.
	}

	@Test
	void namedQuery(){
		List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
				.setParameter("username", "userA")
				.getResultList();

		//method 위에 @Query로 바로 쿼리를 작성할 수 있음. spring data jpa의 query 참고
	}

	@Test
	void 벌크연산(){
		//벌크연산은 영속성 컨텍스트를 무시하고 데이터베이스에 직접 실행
		//따라서 벌크 연산 먼저 실행 or 벌크 연산 수행 후 영속성 컨텍스트 초기화 
		//벌크 연산 수행 이후에는 em.clear()로 초기화 후 다시 조회
		//update Member m set m.age = 20 -> .executeUpdate() -> 모든 회원의 나이를 20으로 수정
	}
}
