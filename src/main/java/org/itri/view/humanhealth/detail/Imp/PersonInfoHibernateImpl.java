package org.itri.view.humanhealth.detail.Imp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Room;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.util.HibernateUtil;

public class PersonInfoHibernateImpl {

	private int minusOneHour = -1;

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

	public List<Combination> getCombinationEndTimeNullByRoomId(List<Long> roomIdList) {
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
						Hibernate.initialize(combination.getPatient().getNewsWarningConditions());
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

	public List<Combination> getCombinationByRoomId(Long roomId, long patientId) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<Room> roomList = new ArrayList<Room>();
		List<Combination> combinationList = new ArrayList<Combination>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Room.class);
			criteria.add(Restrictions.eq("roomId", roomId));
			roomList = criteria.list();

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MONTH, minusOneHour);
			System.out.println(dateFormat.format(calendar.getTime()));

			for (Room r : roomList) {
				Hibernate.initialize(r.getCombinations());
				for (Combination combination : r.getCombinations()) {
					if (combination.getPatient().getPatientId() == patientId && (combination.getEndTime() == null
							|| combination.getEndTime().after(calendar.getTime()))) {
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

}
