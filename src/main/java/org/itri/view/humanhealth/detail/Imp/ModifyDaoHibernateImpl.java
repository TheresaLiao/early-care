package org.itri.view.humanhealth.detail.Imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.NewsMathOperator;
import org.itri.view.humanhealth.hibernate.NewsWarningCondition;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.util.HibernateUtil;

public class ModifyDaoHibernateImpl {

	public List<NewsMathOperator> getNewsMathOperatorList() {
		System.out.println("getNewsMathOperatorList");
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<NewsMathOperator> resp = new ArrayList<NewsMathOperator>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(NewsMathOperator.class);
			resp = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return resp;
	}

	public void updateCombination(Combination item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			item.setEndTime(new Date());
			session.update(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public void createCombination(Combination item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
	}

	public List<Combination> getCombinationByRoomId(Long roomId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		List<Combination> combinationList = new ArrayList<Combination>();
		List<Combination> resp = new ArrayList<Combination>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Combination.class);
			criteria.add(Restrictions.isNull("endTime"));

			combinationList = criteria.list();
			for (Combination item : combinationList) {
				Hibernate.initialize(item.getRoom());
				Hibernate.initialize(item.getSensor());
				Hibernate.initialize(item.getSensor().getSensor2healthTypes());
				if (item.getRoom().getRoomId() == roomId) {
					resp.add(item);
				}
			}
			tx.commit();
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public List<PatientInfo> getPatientList() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PatientInfo> resp = new ArrayList<PatientInfo>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PatientInfo.class);
			criteria.addOrder(Order.asc("name"));

			resp = criteria.list();

			for (PatientInfo item : resp) {
				Hibernate.initialize(item.getPatient());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return resp;
	}

	public List<Sensor> getSensorListBySensorTypeId() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Sensor> resp = new ArrayList<Sensor>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Sensor.class);
			criteria.addOrder(Order.asc("sensorName"));
			resp = criteria.list();

			for (Sensor item : resp) {
				Hibernate.initialize(item.getSensorType());
				Hibernate.initialize(item.getSensor2healthTypes());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return resp;
	}

	public List<NewsWarningCondition> getNewsWarningConditionList(Long patientId) {
		System.out.println("getNewsWarningConditionList");
		List<NewsWarningCondition> resp = new ArrayList<NewsWarningCondition>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(NewsWarningCondition.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));
			resp = criteria.list();
			for (NewsWarningCondition item : resp) {
				Hibernate.initialize(item.getNewsMathOperator());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return resp;

	}
}
