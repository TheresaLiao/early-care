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
import org.itri.view.humanhealth.hibernate.HeartRhythmRecord;
import org.itri.view.humanhealth.hibernate.RtHeartRhythmRecord;
import org.itri.view.util.HibernateUtil;

public class BreathRateViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public RtHeartRhythmRecord getRtHeartRhythmRecord(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<RtHeartRhythmRecord> resp = new ArrayList<RtHeartRhythmRecord>();
		RtHeartRhythmRecord item = new RtHeartRhythmRecord();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(RtHeartRhythmRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			resp = criteria.list();
			item = resp.get(0);
			tx.commit();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public List<HeartRhythmRecord> getHeartRhythmRecordList(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<HeartRhythmRecord> heartRhythmRecordList = new ArrayList<HeartRhythmRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(HeartRhythmRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));

			heartRhythmRecordList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return heartRhythmRecordList;
	}

//	public List<RtHeartRhythmRecord> getRtHeartRhythmRecordList(long patientId) {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = null;
//		List<RtHeartRhythmRecord> rtHeartRhythmRecordList = new ArrayList<RtHeartRhythmRecord>();
//		try {
//			tx = session.beginTransaction();
//			Criteria criteria = session.createCriteria(RtHeartRhythmRecord.class);
//			criteria.add(Restrictions.eq("patient.patientId", patientId));
//			rtHeartRhythmRecordList = criteria.list();
//
//			for (RtHeartRhythmRecord item : rtHeartRhythmRecordList) {
//				Hibernate.initialize(item.getSensor());
//			}
//
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//		} finally {
//			session.close();
//		}
//		return rtHeartRhythmRecordList;
//	}

//	public List<HeartRhythmRecord> getHeartRhythmRecordList(long patientId) {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = null;
//		List<HeartRhythmRecord> heartRhythmRecordList = new ArrayList<HeartRhythmRecord>();
//		try {
//			tx = session.beginTransaction();
//			Criteria criteria = session.createCriteria(HeartRhythmRecord.class);
//			criteria.add(Restrictions.eq("patient.patientId", patientId));
//
//			Date now = new Date();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(now);
//			calendar.add(Calendar.MINUTE, minusThreeMinit);
//			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
//
//			criteria.addOrder(Order.asc("timeCreated"));
//
//			heartRhythmRecordList = criteria.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//		} finally {
//			session.close();
//		}
//		return heartRhythmRecordList;
//	}

//	public List<HeartRhythmRecord> getHeartRhythmRecordByDateList(long patientId, Calendar calendar) {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = null;
//		List<HeartRhythmRecord> heartRhythmRecordList = new ArrayList<HeartRhythmRecord>();
//		try {
//			tx = session.beginTransaction();
//			Criteria criteria = session.createCriteria(HeartRhythmRecord.class);
//			criteria.add(Restrictions.eq("patient.patientId", patientId));
//
//			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
//			criteria.addOrder(Order.asc("timeCreated"));
//
//			heartRhythmRecordList = criteria.list();
//			tx.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			tx.rollback();
//		} finally {
//			session.close();
//		}
//		return heartRhythmRecordList;
//	}
}
