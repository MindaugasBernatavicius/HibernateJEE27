package cf.mindaugas;

import jakarta.persistence.*;
import lombok.*;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Setter
@ToString
class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull private String name;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Address address;
}

@Entity
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Setter
@ToString(exclude = "student")
class Address {
    @Id @Column(name = "student_id")
    private Long id;
    @NonNull private String city;
    @NonNull private String street;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    @MapsId
    private Student student;
}

public class _10_OneToOne {

    public static void _00_simpleExampleOfOneToOneAssociation(){}
    public static void _01_sharedPrimaryKey(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var tx = session.beginTransaction();
            var student = new Student("John");
            var address = new Address("Tallin", "DontKnow");
            // address.setStudent(student); // if this is commented out: IdentifierGenerationException: attempted to assign id from null one-to-one property [cf.mindaugas.Address.student]
            student.setAddress(address); // need to attack the address to student, because this is the entity that will cascade the persistence
            session.persist(student);
            // session.persist(address); // no need if PERSIST action is cascaded
            tx.commit();

            var res = session.createSelectionQuery("From Student", Student.class).list();
            // var res = session.find(Student.class, 1L);
            System.out.println(res);

        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) {
        // Ref (NOT BAD): https://www.developer.com/database/how-to-implement-database-relationship-in-hibernate/
        // _00_simpleExampleOfOneToOneAssociation();
        _01_sharedPrimaryKey();
    }
}
