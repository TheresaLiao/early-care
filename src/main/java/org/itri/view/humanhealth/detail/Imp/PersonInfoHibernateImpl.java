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
import org.itri.view.humanhealth.hibernate.Room;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.util.HibernateUtil;

public class PersonInfoHibernateImpl {

	public List<Combination> getCombinationByRoomId(List<Long> roomIdList) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<Room> roomList = new ArrayList<Room>();
		List<Combination> combinationList = new ArrayList<Combination>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Room.class);
			criteria.add(Restrictions.in("roomId", roomIdList));

			roomList = criteria.list();
			for (Room r : roomList) {
				Hibernate.initialize(r.getCombinations());

				// combinationList.addAll(r.getCombinations());
				for (Combination combination : r.getCombinations()) {
					if (combination.getEndTime() == null) {
						Hibernate.initialize(combination.getPatient());
						Hibernate.initialize(combination.getPatient().getPatientInfos());
						Hibernate.initialize(combination.getSensor());
						Hibernate.initialize(combination.getRoom());
						combinationList.add(combination);
					}
				}
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
