package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import hellojpa.domain.Member;
import hellojpa.domain.Team;
import hellojpa.domain.cascade.Child;
import hellojpa.domain.cascade.Parent;
import hellojpa.domain.item.Movie;
import hellojpa.domain.type.Address;
import hellojpa.domain.type.Period;

public class JpaMain {

	public static void main(String[] args) {

		// 엔티티 매니저 팩토리는 하나만 생성해서 애플리케이션 전체에서 공유
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

		// 엔티티 매니저는 쓰레드간에 공유하면 안 된다.
		EntityManager em = emf.createEntityManager();

		// JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
		EntityTransaction tx = em.getTransaction();

		tx.begin();

		try {
			Address homeAddress = new Address("city", "street", "zipcode");

			Member member = new Member();
			member.setHomeAddress(homeAddress);
			member.setWorkPeriod(new Period());

			em.persist(member);

			em.flush();
			em.clear();

			// 불변 객체의 값을 수정할 수 없으므로 새 객체를 생성하여 값을 수정한다.
			Member findMember = em.find(Member.class, member.getId());
			Address newHomeAddress = new Address("new City", homeAddress.getStreet(), homeAddress.getZipcode());
			findMember.setHomeAddress(newHomeAddress);

			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();
		}

		emf.close();
	}
}
