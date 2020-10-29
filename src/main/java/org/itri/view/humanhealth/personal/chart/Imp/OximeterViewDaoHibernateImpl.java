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
import org.itri.view.humanhealth.hibernate.OximeterRecord;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.util.HibernateUtil;

public class OximeterViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public List<RtOximeterRecord> getRtOximeterRecordList(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<RtOximeterRecord> oximeterRecordList = new ArrayList<RtOximeterRecord>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(RtOximeterRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));
			oximeterRecordList = criteria.list();

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return oximeterRecordList;
	}

	public List<OximeterRecord> getOximeterRecordList(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<OximeterRecord> oximeterRecordList = new ArrayList<OximeterRecord>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(OximeterRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));

			criteria.addOrder(Order.asc("timeCreated"));

			oximeterRecordList = criteria.list();

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return oximeterRecordList;
	}

	public List<OximeterRecord> getOximeterRecordByDateList(long patientId, Calendar calendar) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<OximeterRecord> oximeterRecordList = new ArrayList<OximeterRecord>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(OximeterRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));
			oximeterRecordList = criteria.list();
			
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return oximeterRecordList;
	}
}
