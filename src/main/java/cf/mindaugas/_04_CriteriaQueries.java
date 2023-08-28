package cf.mindaugas;

public class _04_CriteriaQueries {
    public static void _00_simpleExample(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            // var transaction = session.beginTransaction();

            var cb = session.getCriteriaBuilder();
            var cr = cb.createQuery(Employee.class);
            var root = cr.from(Employee.class);
            cr.select(root);

            var query = session.createQuery(cr);
            var results = query.getResultList();

            System.out.println(results);

            // transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_expressionCriteriaExample(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            // var transaction = session.beginTransaction();

            var cb = session.getCriteriaBuilder();
            var cr = cb.createQuery(Employee.class);
            var root = cr.from(Employee.class);
            cr.select(root).where(cb.gt(root.get("salary"), 100));
            // more: https://www.baeldung.com/hibernate-criteria-queries#1-using-expressions

            var query = session.createQuery(cr);
            var results = query.getResultList();

            System.out.println(results);

            // transaction.commit();
        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void main(String[] args) {
        _00_simpleExample();
    }
}
