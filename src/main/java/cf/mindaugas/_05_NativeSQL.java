package cf.mindaugas;

import org.hibernate.type.StandardBasicTypes;

public class _05_NativeSQL {
    public static void main(String[] args) {
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            // https://docs.jboss.org/hibernate/orm/6.2/userguide/html_single/Hibernate_User_Guide.html#sql
            var res = session
                    .createNativeQuery("select id, name from Employee", Object[].class)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .list();

            for (var r: res)
                System.out.println(r[0] + " : " + r[1]);

        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }
}
