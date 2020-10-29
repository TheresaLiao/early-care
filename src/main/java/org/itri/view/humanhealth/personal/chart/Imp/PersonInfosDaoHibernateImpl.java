package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientThreshold;
import org.itri.view.util.HibernateUtil;

public class PersonInfosDaoHibernateImpl {

	private Integer totalNewsScoreSpec = 7;

	public List<Patient> getPatientList() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Patient> tempPatientList = new ArrayList<Patient>();
		List<Patient> patientList = new ArrayList<Patient>();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Patient.class);
			criteria.add(Restrictions.eq("isDeleted", false));
			criteria.createAlias("room", "r");
			criteria.addOrder(Order.asc("r.roomNum"));
			tempPatientList = criteria.list();

			for (Patient p : tempPatientList) {
				Hibernate.initialize(p.getRoom());
				Hibernate.initialize(p.getPatientInfos());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtOximeterRecords());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtTempPadRecords());

				patientList.add(p);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return patientList;
	}

	public List<Patient> getPatientTotalNewsScoreFourList() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Patient> tempPatientList = new ArrayList<Patient>();
		List<Patient> patientList = new ArrayList<Patient>();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Patient.class);
			criteria.add(Restrictions.eq("isDeleted", false));
			criteria.add(Restrictions.ge("totalNewsScore", totalNewsScoreSpec));
			criteria.addOrder(Order.desc("totalNewsScore"));
			criteria.setMaxResults(5);
			tempPatientList = criteria.list();

			for (Patient p : tempPatientList) {
				Hibernate.initialize(p.getRoom());
				Hibernate.initialize(p.getPatientInfos());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtOximeterRecords());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtTempPadRecords());
				patientList.add(p);
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return patientList;
	}

	public Patient getPatientById(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<Patient> tempPatientList = new ArrayList<Patient>();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Patient.class);
			criteria.add(Restrictions.eq("isDeleted", false));
			criteria.add(Restrictions.eq("patientId", patientId));
			tempPatientList = criteria.list();

			for (Patient p : tempPatientList) {
				Hibernate.initialize(p.getRoom());
				Hibernate.initialize(p.getPatientInfos());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtOximeterRecords());
				Hibernate.initialize(p.getRtHeartRhythmRecords());
				Hibernate.initialize(p.getRtTempPadRecords());
				return p;
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public List<PatientThreshold> getPatientThresholdByPatientId(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PatientThreshold> patientThresholdList = new ArrayList<PatientThreshold>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PatientThreshold.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));
			patientThresholdList = criteria.list();

			for (PatientThreshold item : patientThresholdList) {
				Hibernate.initialize(item.getSensor());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return patientThresholdList;
	}
}
