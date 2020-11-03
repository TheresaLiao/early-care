package org.itri.view.admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.util.HibernateUtil;

public class LoginControllerHibernateImpl {

	public PatientInfo getPatientInfo(String account, String pwd) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PatientInfo> tempPatientInfo = new ArrayList<PatientInfo>();

		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(PatientInfo.class);
			criteria.add(Restrictions.eq("username", account));
			criteria.add(Restrictions.eq("password", pwd));
			tempPatientInfo = criteria.list();

			PatientInfo p = tempPatientInfo.get(0);
			Hibernate.initialize(p.getPatient());
			Hibernate.initialize(p.getPatient().getGateway());
			tx.commit();
			return p;
		} catch (

		Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

}
