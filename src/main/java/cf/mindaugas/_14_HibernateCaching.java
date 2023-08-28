package cf.mindaugas;

public class _14_HibernateCaching {
    public static void _00_1stLevelCacheProof(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
            var session = sessionFactory.openSession();
        ){
            for (int i = 0; i < 3; i++) {
                // var transaction = session.beginTransaction();
                var res = session.find(Employee.class, 1L);
                System.out.println(res);
                // transaction.commit();
            }

            for (int i = 0; i < 3; i++) {
                // var transaction = session.beginTransaction();
                var res = session.find(Employee.class, 1L);
                System.out.println(res);
                // transaction.commit();
            }

        } catch (Exception e){
            System.out.println("[ERROR] Exception: " + e);
        }
    }

    public static void _01_1stLevelCacheProofOfSessionScope(){
        try(
            var hibernateUtil = new HibernateUtilAutoClose();
            var sessionFactory = hibernateUtil.getSessionFactory();
        ){
            for (int i = 0; i < 3; i++) {
                try (var session = sessionFactory.openSession()) {
                    var res = session.find(Employee.class, 1L);
                    System.out.println(res);
                } catch (Exception e) {
                    System.out.println("[ERROR] Exception: " + e);
                }
            }
        }
    }


    public static void main(String[] args) {
        // https://www.youtube.com/watch?v=sbi3Hm_-UbA
        // https://www.youtube.com/watch?v=0lZnBTTbmRQ
        // https://www.youtube.com/watch?v=ISi_OkmuQbs
        // https://www.youtube.com/watch?v=TMZZg-HEjiE


        // _00_1stLevelCacheProof();
        _01_1stLevelCacheProofOfSessionScope();

    }
}