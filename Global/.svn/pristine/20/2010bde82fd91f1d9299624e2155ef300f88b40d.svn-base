package lu.uni.fstc.proactivity.db;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import lu.uni.fstc.proactivity.utils.Global;

/**
 * Downloaded and adapted from originals <b>[HibernateUtil]</b> in:<br>
 * <a href="http://www.roseindia.net">Roseindia</a> and 
 * <a href="http://www.java2s.com/Code/Java/Hibernate/HibernateCollectionMappingMap.htm">Java2s.com</a>
 * @author Sandro Reis
 * @version 3.0 - Sandro Reis © 2011-2013
 */
public class Persistence {
	/*
	 * **********************************
	 * **********************************
	 * **** 		SESSIONS 		*****
	 * **********************************
	 * **********************************
	 */

	/**
	 * Hibernate's SessionFactory interface provides instances of the Session class,
	 * which represent connections to the database. 
	 * Instances of SessionFactory are thread-safe and typically shared throughout 
	 * an application. 
	 */
	public static final SessionFactory sessionFactory;
	static {
		try {
			// Create the SessionFactory from hibernate.cfg.xml
			sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch(Throwable e) {
			// Make sure you log the exception, as it might be swallowed
			Global.logger.severe("Initial SessionFactory creation failed.\n" + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	/**
	 * Session instances, on the other hand, aren't thread-safe and should only 
	 * be used for a single transaction or unit of work in an application.
	 */
	private static final ThreadLocal<Session> session = new ThreadLocal<Session>();

	/**
	 * Opens the session, or returns the already opened session
	 * @throws HibernateException
	 */
	public static void sessionOpen() throws HibernateException {
		Session s = session.get();
		// Open a new Session, if this thread has none yet
		if(s == null) {
			s = sessionFactory.openSession();
			// Store it in the ThreadLocal variable
			session.set(s);
		}
	}

	/**
	 * Closes the session
	 * @throws HibernateException
	 */
	public static void sessionClose() throws HibernateException {
		Session s = session.get();
		if (s != null)
			s.close();
		session.set(null); 
	}

	/**
	 * @return the session
	 * @throws HibernateException
	 */
	public static Session currentSession() throws HibernateException {
		sessionOpen();
		return session.get();
	}
	
	/*
	 * **********************************
	 * **********************************
	 * **** 	 TRANSACTIONS		*****
	 * **********************************
	 * **********************************
	 */

	static Transaction tx;

	/**
	 * Starts a transaction in the current session
	 */
	public static void beginTransaction() {
		Session s = currentSession();
		tx = s.beginTransaction();
	}
	
	/**
	 * Ends the open transaction with a rollback
	 * @param closeConnection <b>Flag:</b> the method should also close the connection
	 */
	public static void rollbackTransaction(boolean closeConnection) {
		if (tx != null)
			if (tx.isActive())
				tx.rollback();
		if (closeConnection)
			sessionClose();
	}

	/**
	 * Ends the open transaction with a commit
	 * @param closeConnection <b>Flag:</b> the method should also close the connection
	 */
	public static void commitTransaction(boolean closeConnection) {
		if (tx != null)
			if (tx.isActive())
				tx.commit();
		if (closeConnection)
			sessionClose();
	}

	/*
	 * **********************************
	 * **********************************
	 * **** 	 MANAGE DATA		*****
	 * **********************************
	 * **********************************
	 */

	/**
	 * @param obj the object to be saved to the database
	 * @param isNewRecord <i>true</i> if the saving corresponds to an INSERT, <i>false</i> if it is an UPDATE
	 * @return boolean true if operation was successful
	 */
	public static boolean save(Object obj, boolean isNewRecord) {
		Session s = currentSession();

		if (isNewRecord)
			s.save(obj);
		else
			s.saveOrUpdate(obj);

		Global.logger.finest("object saved!");
		return true;
	}

	/**
	 * @param obj the object to be saved to the database
	 * @return boolean true if successful
	 */
	public static boolean insert(Object obj) {
		Session s = currentSession();
		s.save(obj);
		Global.logger.finest("object inserted!");
		return true;
	}

	/**
	 * @param hql the query in HQL - Hibernate Query Language
	 * @param singleTransaction <b>Flag:</b> this execution is atomic (a unique transaction)
	 */
	public static void executeQuery(String hql, boolean singleTransaction) {
		// TODO Replicate here the same concept as  in MySQLOperantion.execute: Check first if connection is valid. If not, try to reopen it. Then execute query!
		Session s = currentSession();

		if (singleTransaction) 
			beginTransaction();

		Query query = s.createQuery(hql);
		int row = query.executeUpdate();

		if (singleTransaction)
			commitTransaction(true);

		Global.logger.finest("Query affected " + row + " rows");
	}

	/**
	 * @param className the class that is represented in the database
	 */
	public static void delClassData(String className) {
		executeQuery("DELETE " + className, false);
		session.get().flush();
		session.get().clear();
	}

	/**
	 * @param className the class that is represented in the database
	 * @return the list of elements from that class that were read
	 */
	public static List<?> getClassData(String className) {
		Session s = currentSession();
//			Query query = s.createQuery(" from " + className);
		 // order by the first field, which is always the ID
		Query query = s.createQuery(" from " + className + " order by 1");
		List<?> li = query.list();
		Global.logger.finest("list = " + li);
		sessionClose();
		return li;
	}

	/**
	 * @param className the class that is represented in the database
	 */
	public static void showClassData(String className) {
		Session s = currentSession();
		Query query = s.createQuery("from " + className + " order by 1"); // order by the first field (ID)

		List<?> li = query.list();
		Iterator<?> iterator = li.iterator();
		Global.logger.finest("Iterating through the elements of the " + className + " table");
		while (iterator.hasNext()) {
			Global.logger.finest(iterator.next().toString());
		}			
		sessionClose();
	  }
}