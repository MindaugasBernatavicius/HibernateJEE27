package cf.mindaugas;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;

@MappedSuperclass
abstract class BaseEntity  {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}

@Entity
@AllArgsConstructor
class Movie extends BaseEntity {
    private String title;
    private String description;
    private String type;
}

public class _09_Inheritance {
    public static void main(String[] args) {
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();
            session.persist(new Movie("Groudnhog day", "Very good movie", "Comedy"));

            var res = session.createSelectionQuery("From Movie").list();
            System.out.println(res);
            tx.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }
}
