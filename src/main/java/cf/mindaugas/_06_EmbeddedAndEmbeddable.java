package cf.mindaugas;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString
class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull String name;
    @Embedded @NonNull Contact contact;
}
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@ToString
class Contact {
    String street;
    Integer number;
}

public class _06_EmbeddedAndEmbeddable {
    public static void main(String[] args) {
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();
            session.persist(new Company("Mindaugas Corp.", new Contact("Shepherd st.", 53)));

            var res = session.createSelectionQuery("From Company").list();
            System.out.println(res);
            tx.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }
}
