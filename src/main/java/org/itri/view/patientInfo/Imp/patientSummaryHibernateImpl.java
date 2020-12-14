package org.itri.view.patientInfo.Imp;

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
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.util.HibernateUtil;

public class patientSummaryHibernateImpl {

	public void updatePatientInfo(PatientInfo item) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.update(item);
			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
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

	public PatientInfo getPatientInfoByPatientId(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		PatientInfo resp = new PatientInfo();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PatientInfo.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));

			List<PatientInfo> temp = criteria.list();
			if (temp.size() != 0) {
				resp = temp.get(0);
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

	public void createPatient(Patient item) {
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

	public void createPatientInfo(PatientInfo item) {
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

}
