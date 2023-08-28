package cf.mindaugas;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

@Getter @Setter @ToString @RequiredArgsConstructor @NoArgsConstructor(force = true)
@Entity(name="klientas")
@Table(name="el_customero")
class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customer_name", nullable = true)
    @NonNull private String name;
}

class HibernateUtil {
    private static SessionFactory sessionFactory;
    public static SessionFactory start(){
        if(sessionFactory == null){
            try {
                var configuration = new Configuration();
                var settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/users?serverTimezone=UTC");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.HBM2DDL_AUTO, "create");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(Customer.class);

                var serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch(Exception e){
                System.out.println("[ERROR] Error:" + e);
            }
        }
        return sessionFactory;
    }

    public static void terminate(){
        sessionFactory.close();
    }
}

public class _01_JavaConfigurationAndEntityModeling {
    public static void main(String[] args) {
        try {
            var sessionFactory = HibernateUtil.start();
            var session = sessionFactory.openSession();
            var transaction = session.beginTransaction();

            // ... action happens

            transaction.commit();
            session.close();
            sessionFactory.close();
        } catch (Exception e) {
            System.out.println("[ERROR] Error:" + e);
        }
    }
}
