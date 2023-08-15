package cf.mindaugas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.cfg.Configuration;

@Getter
@Setter
class User {
    private Long id;
    private String name;
}

public class _00_XmlConfigurationAndEntityModeling {
    public static void main(String[] args) {
        try(var sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory()){
            var session = sessionFactory.openSession();
            var transaction = session.beginTransaction();

            // ... action happens

            transaction.commit();
            session.close();
        } catch (Exception e) {
            System.out.println("[ERROR] Error: " + e);
        }
    }
}