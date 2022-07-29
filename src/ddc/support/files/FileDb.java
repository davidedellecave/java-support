package ddc.support.files;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class FileDb {

	public static void main(String[] args) {
		// Open a database connection
		// (create a new database if it doesn't exist yet):
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("db/p2.odb");
		EntityManager em = emf.createEntityManager();

		// Store 1000 Point objects in the database:
		em.getTransaction().begin();
		for (int i = 0; i < 1000; i++) {
			FileRecord p = new FileRecord();
			p.setBytes(10);
			p.setExtension("avi");
			em.persist(p);
		}
		em.getTransaction().commit();

		// Find the number of Point objects in the database:
		Query q1 = em.createQuery("SELECT COUNT(p) FROM FileRecord p");
		System.out.println("Total Records: " + q1.getSingleResult());

		// Find the average X value:
		Query q2 = em.createQuery("SELECT AVG(p.bytes) FROM FileRecord p");
		System.out.println("Average X: " + q2.getSingleResult());
		//
		Query q3 = em.createQuery("SELECT * FROM FileRecord p");
		System.out.println("Select *: " + q3.getResultList());
		// TypedQuery<FileRecord> query = em.createQuery("SELECT p FROM FileRecord p",
		// FileRecord.class);
		// List<FileRecord> results = query.getResultList();
		// for (FileRecord p : results) {
		// System.out.println(p);
		// }
		//
//		TypedQuery<FileRecord> query2 = em.createQuery("SELECT p.extension FROM FileRecord p GROUP BY p.extension", FileRecord.class);
//		List<FileRecord> results2 = query2.getResultList();
//		for (FileRecord p : results2) {
//			System.out.println(p);
//		}

//		CriteriaQuery<FileRecord> q = em..createQuery(FileRecord.class);
//		Root<Country> c = q.from(Country.class);
//		q.multiselect(c.get("currency"), cb.sum(c.get("population")));
//		q.where(cb.isMember("Europe", c.get("continents")));
//		q.groupBy(c.get("currency"));
//		g.having(cb.gt(cb.count(c), 1));

		// Close the database connection:
		em.close();
		emf.close();
	}
}