package lu.uni.fstc.tests;

import lu.uni.fstc.proactivity.db.Persistence;
import lu.uni.fstc.proactivity.utils.Global;
import lu.uni.fstc.proactivity.utils.StringUtils;

import lu.uni.fstc.proactivity.rules.*;
import org.hibernate.Transaction;
import org.hibernate.Session;

/**
 * @author Sandro Reis
 *
 */
@SuppressWarnings("unused")
public class TestHibernateNativeInsert {
	/**
	 * @param args
	 */
	public static void main(String args[]){
		Session session = null;
		try {
/*/
			session = Persistence.currentSession();

			Transaction tx = session.beginTransaction();

			// needs rule.setActivated to be set to public for this test
			Rule_PrintOnScreen rule = new Rule_PrintOnScreen("Test Hibernate Native Insert 1-upd");
//			rule.setId(100); // only possible if it already exists
			rule.setActivated(true);
			session.saveOrUpdate(rule);

			Rule_PrintOnScreen rule1 = new Rule_PrintOnScreen("Test Hibernate Native Insert 2-ins");
			rule1.setActivated(true);
			session.saveOrUpdate(rule1);

			Rule_Welcome rule2 = new Rule_Welcome(8, 8);
			rule2.setActivated(true);
			session.saveOrUpdate(rule2);

			Rule_Welcome rule3 = new Rule_Welcome(100, 100);
			rule3.setActivated(true);
			session.saveOrUpdate(rule3);

			tx.commit();
			Global.logger.info("Successfully data inserted in Rule_PrintOnScreen");
/**/		
/**/
			Persistence.showClassData(StringUtils.stringAfter(AbstractRule.class.toString(), "."));

			Persistence.executeQuery("DELETE AbstractRule WHERE rule_id = 13", true);
//			Global.logger.info("" + Utils.stringAfter(Persistence.class.toString(), "."));
//			Global.logger.info("" + Utils.stringAfter(AbstractRule.class.toString(), "."));
		}
		catch(Exception e) {
			Global.logger.info("" + e.getMessage());
		}
		finally{
//			Persistence.checkData("select * from PAS.Rule_PrintOnScreen");
//			Persistence.checkData("select * from Rules");
//			session.close();
//			Persistence.SessionClose();
		}

	}
}
