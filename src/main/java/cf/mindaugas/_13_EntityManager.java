package cf.mindaugas;

import jakarta.persistence.Persistence;

public class _13_EntityManager {
    public static void _00_CreateData(){
        try (
            var emf = Persistence.createEntityManagerFactory("persistence");
            var em = emf.createEntityManager();
        ){
            em.getTransaction().begin();

            em.persist(new Employee("Alfonso", 100));
            em.persist(new Employee("Mindaugas", 200));
            em.persist(new Employee("Peter", 100));

            em.getTransaction().commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_ReadData(){
        try (
            var emf = Persistence.createEntityManagerFactory("persistence");
            var em = emf.createEntityManager();
        ){
            em.getTransaction().begin();
            System.out.println(em.find(Employee.class, 1L));
            em.getTransaction().commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) {
        // Ref: https://stackoverflow.com/questions/5640778/hibernate-sessionfactory-vs-jpa-entitymanagerfactory
        // Ref: https://www.baeldung.com/hibernate-entitymanager
        // _00_CreateData();
        _01_ReadData();
    }
}
