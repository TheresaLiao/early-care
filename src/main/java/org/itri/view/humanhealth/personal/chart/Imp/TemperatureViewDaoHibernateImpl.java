package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.hibernate.RtTempPadRecord;
import org.itri.view.humanhealth.hibernate.TempPadRecord;
import org.itri.view.util.HibernateUtil;

public class TemperatureViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public List<RtTempPadRecord> getRtTempPadRecordList(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<RtTempPadRecord> rtTempPadRecordList = new ArrayList<RtTempPadRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RtTempPadRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));
			rtTempPadRecordList = criteria.list();
			for (RtTempPadRecord item : rtTempPadRecordList) {
				Hibernate.initialize(item.getSensor());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return rtTempPadRecordList;
	}

	public List<TempPadRecord> getTempPadRecordList(long patientId) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<TempPadRecord> tempPadRecordList = new ArrayList<TempPadRecord>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(TempPadRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));
			tempPadRecordList = criteria.list();

			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return tempPadRecordList;
	}

	public List<TempPadRecord> getTempPadRecordByDateList(long patientId, Calendar calendar) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<TempPadRecord> tempPadRecordList = new ArrayList<TempPadRecord>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(TempPadRecord.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));

			tempPadRecordList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return tempPadRecordList;
	}
}