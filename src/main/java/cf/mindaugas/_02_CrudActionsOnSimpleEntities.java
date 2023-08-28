package cf.mindaugas;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

class HibernateUtilAutoClose implements AutoCloseable {
    private static SessionFactory sessionFactory;
    public HibernateUtilAutoClose(){
        if(sessionFactory == null){ // or you can check if sessionFactory.isClosed()
            try {
                var configuration = new Configuration();
                var settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/users?serverTimezone=UTC&createDatabaseIfNotExist=true");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                settings.put(Environment.HBM2DDL_AUTO, "validate");
                settings.put(Environment.SHOW_SQL, "true");
                configuration.setProperties(settings);

                // configuration.addAnnotatedClass(Customer.class);
                // configuration.addAnnotatedClass(Employee.class);
                // configuration.addAnnotatedClass(Company.class);
                // configuration.addAnnotatedClass(Contact.class); // no need to configure embeddable
                // configuration.addAnnotatedClass(Book.class);
                // configuration.addAnnotatedClass(BookType.class); // did not need to configure a "secondary entity"
                // configuration.addAnnotatedClass(Movie.class);

                // for example: 10
                configuration.addAnnotatedClass(Student.class);
                configuration.addAnnotatedClass(Address.class);

                // for example: 11
                // configuration.addAnnotatedClass(Person.class);
                // configuration.addAnnotatedClass(PhoneNumber.class);

                var serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch(Exception e){
                System.out.println("[ERROR] Exception: " + e);
            }
        }
    }

    public SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public void close() {
        sessionFactory.close();
        // session factory does not become null
        // ... when it's closed, so if you want
        // ... to reopen it in a loop you must
        // ... nullify it explicitly
        sessionFactory = null;
    }
}

public class _02_CrudActionsOnSimpleEntities {
    public static void _00_Read(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            // // getById()
            // var customer = session.find(Customer.class, 3L);
            // System.out.println(customer);

            // getAll()
            var customers = session.createSelectionQuery("From klientas").list();
            System.out.println(customers);

            // // getBySomeValue()
            // var qStr = "From Customer where name like :name";
            // var q = session.createSelectionQuery(qStr);
            // var customers = q.setParameter("name", "Peter").list();
            // System.out.println(customers);

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_Create(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            session.persist(new Customer("Alfonso"));

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _02_Update(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            var customer = session.find(Customer.class, 3L);
            customer.setName("New name");
            session.persist(customer);

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _03_Delete(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            var customer = session.find(Customer.class, 4L);
            if(customer != null) session.remove(customer);

            // Hibernate: select c1_0.id,c1_0.customer_name from Customer c1_0 where c1_0.id=?
            // Hibernate: delete from Customer where id=?

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _04_DeleteWORetrieval(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            var transaction = session.beginTransaction();

            var res = session
                    .createMutationQuery("Delete from Customer where id = :id")
                    .setParameter("id", 1L)
                    .executeUpdate();
            System.out.println("Update rows: " + res);

            // Hibernate: delete from Customer where id=?
            // Update rows: 1

            transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) {
        _00_Read();
        // _01_Create();
        // _02_Update();
        // _03_Delete();
        // _04_DeleteWORetrieval();
    }
}
