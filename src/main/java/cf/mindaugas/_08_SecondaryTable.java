package cf.mindaugas;

import jakarta.persistence.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "book_type")
class BookType {
    @Id private int id;
    private String type;
}

@Entity
@Table(name = "Book")
@SecondaryTable(name = "book_type")
@RequiredArgsConstructor
class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull private String title;
    @Column(table = "book_type") @NonNull private String type;
}


public class _08_SecondaryTable {
    public static void main(String[] args) {
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();
            session.persist(new Book("Harry Potter", "Adventure"));

            var res = session.createSelectionQuery("From Book").list();
            System.out.println(res);
            tx.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }
}
