package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.NewsRecord;
import org.itri.view.util.HibernateUtil;

public class EwsViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public List<NewsRecord> getNewsRecordByDateList(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<NewsRecord> newsRecordList = new ArrayList<NewsRecord>();

		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(NewsRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));

			criteria.addOrder(Order.asc("timeCreated"));

			newsRecordList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return newsRecordList;
	}
}
