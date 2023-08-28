package cf.mindaugas;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Entity
class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull private String name;
    @NonNull private Integer salary;
}

public class _03_HQL {
    public static void _00_CreateData(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            session.persist(new Employee("Alfonso", 100));
            session.persist(new Employee("Mindaugas", 200));
            session.persist(new Employee("Peter", 100));

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_NativeQueryNeededForGroupBy(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            var qStr = "SELECT GROUP_CONCAT(E.name), E.salary FROM Employee E GROUP BY E.salary";
            var q = session.createNativeQuery(qStr, Object[].class).list();
            q.forEach(o -> System.out.format("|%-30s|%-10s|\n", o[0].toString(), o[1].toString()));

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // _00_CreateData();

        // NOTE:: because JDBC/HIB do not support group concat (since this is mysql specific (postgres uses string_agg())), you will wither need to
        // 1. Register a function by overriding the Dialect: https://stackoverflow.com/a/60199919/1964707 and https://stackoverflow.com/a/60484151/1964707
        // 2. Abandon HQL for native queries: https://learningprogramming.net/java/hibernate5/group-by-clause-in-native-query-in-hibernate-5/
        // 3. Use Criteria API
        _01_NativeQueryNeededForGroupBy();
    }
}
