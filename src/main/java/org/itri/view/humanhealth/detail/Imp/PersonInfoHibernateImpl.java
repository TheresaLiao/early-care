package org.itri.view.humanhealth.detail.Imp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.util.HibernateUtil;

public class PersonInfoHibernateImpl {

	public List<Combination> getCombination() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<Combination> tempCombinationList = new ArrayList<Combination>();
		List<Combination> combinationList = new ArrayList<Combination>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Combination.class);
//			criteria.add(Restrictions.eq("room.roomNum", "335-01"));
			criteria.add(Restrictions.isNull("endTime"));

			tempCombinationList = criteria.list();

			for (Combination p : tempCombinationList) {
				Hibernate.initialize(p.getRoom());
				Hibernate.initialize(p.getPatient());
				Hibernate.initialize(p.getPatient().getPatientInfos());
				Hibernate.initialize(p.getSensor());

				Hibernate.initialize(p.getSensor().getRtOximeterRecords());
				Hibernate.initialize(p.getSensor().getRtHeartRhythmRecords());
				Hibernate.initialize(p.getSensor().getRtTempPadRecords());

				combinationList.add(p);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return combinationList;
	}

	public List<SensorThreshold> getSensorThresholdByIdList(List<Long> sensorIdList) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<SensorThreshold> sensorList = new ArrayList<SensorThreshold>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SensorThreshold.class);
			criteria.add(Restrictions.in("sensor.sensorId", sensorIdList));
			criteria.addOrder(Order.desc("sensor.sensorId"));

			sensorList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return sensorList;
	}

}
