package cf.mindaugas;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@ToString(exclude = "phones") // only 1 query:select p1_0.id,p1_0.name from Person p1_0 where p1_0.id=?
// @ToString // additional query executed: select p1_0.person_id,p1_0.id,p1_0.number from PhoneNumber p1_0 where p1_0.person_id=?
    // ... note, if you set a breakpoint and then check the object with association, it will be fetched during debugging as well!
class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull String name;
    // w/o mappedBy we would have 1:M w/ junction table
    @OneToMany(
        cascade = { CascadeType.PERSIST, CascadeType.REMOVE },
        mappedBy = "owner",
        fetch = FetchType.LAZY
    )
    @NonNull private Set<PhoneNumber> phones;
}

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
// @ToString(exclude = "owner") // Solving StackOverflow error
@Setter
class PhoneNumber {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NonNull String number;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="person_id")
    Person owner;

    // @Override
    // public String toString() {
    //     return "PhoneNumber{" +
    //             "id=" + id +
    //             ", number='" + number + '\'' +
    //             ", owner=" + owner.name +
    //             '}';
    // }
}

public class _11_OneToManyAndBidirectional {
    public static void _00_persistExample(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();

            // [ERROR] Exception: java.lang.IllegalStateException: org.hibernate.TransientObjectException:
            // ... object references an unsaved transient instance - save the transient instance before flushing: cf.mindaugas.Phone
            var phone1 = new PhoneNumber("+370-1");
            var phone2 = new PhoneNumber("+370-2");
            var person = new Person("John", new HashSet<>(){{
                add(phone1);
                add(phone2);
            }});
            phone1.setOwner(person); // if we do not specify the owner the person_id column, will be null
            phone2.setOwner(person); // if we do not specify the owner the person_id column, will be null
            // session.persist(phone1);
            session.persist(person);

            tx.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_removeExample(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();

            // var res = session.createSelectionQuery("From Person", Person.class).list();
            // System.out.println(res);

            var person = session.find(Person.class, 1L);
            System.out.println(person);
            // session.remove(person);

            tx.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) {
        // _00_persistExample();
        _01_removeExample();
    }
}
