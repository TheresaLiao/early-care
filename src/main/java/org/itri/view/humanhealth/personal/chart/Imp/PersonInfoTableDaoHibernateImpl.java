package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.personal.chart.dao.PersonInfoTableDao;
import org.itri.view.util.HibernateUtil;

public class PersonInfoTableDaoHibernateImpl {

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

	public List<PersonInfoTableDao> getPatientByIdAndTimeCreated(long patientId, Date strTime, Date endTime) {
		System.out.println("getPatientByIdAndTimeCreated");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("patientId " + patientId);
		System.out.println("strTime " + sdf.format(strTime));
		System.out.println("endTime " + sdf.format(endTime));

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Session session = sessionFactory.getCurrentSession();

		List<PersonInfoTableDao> resp = new ArrayList<PersonInfoTableDao>();
		Transaction tx = session.beginTransaction();

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT A.heart_rate_data,");
		sql.append("	A.oximeter_data,");
		sql.append("	B.breath_data,");
		sql.append("	C.body_temp_data,");
		sql.append("	D.news_score,");
		sql.append("	A.time_created ");
		sql.append(" FROM");
		sql.append("	oximeter_record AS A,");
		sql.append("	heart_rhythm_record AS B,");
		sql.append("	temp_pad_record AS C,");
		sql.append("	news_record AS D ");
		sql.append(" WHERE");
		sql.append("	A.patient_id = B.patient_id ");
		sql.append("	AND A.patient_id = C.patient_id ");
		sql.append("	AND A.patient_id = D.patient_id ");
		sql.append("	AND A.time_created = B.time_created ");
		sql.append("	AND A.time_created = C.time_created ");
		sql.append("	AND A.time_created = D.time_created ");
		sql.append("	AND A.patient_id = :patientId ");
		sql.append("	AND A.time_created BETWEEN :strTime AND :endTime ");
		sql.append(" ORDER BY time_created DESC ");

		SQLQuery query = session.createSQLQuery(sql.toString());
		query.setLong("patientId", patientId);
		query.setTimestamp("strTime", strTime);
		query.setTimestamp("endTime", endTime);

		List<Object[]> rows = query.list();

		for (Object[] rawItem : rows) {

			PersonInfoTableDao item = new PersonInfoTableDao();
			item.setHeartRateData(Double.valueOf(rawItem[0].toString()));
			item.setOximeterData(Double.valueOf(rawItem[1].toString()));
			item.setBreathData(Double.valueOf(rawItem[2].toString()));
			item.setBodyTempData(Double.valueOf(rawItem[3].toString()));
			item.setNewsScore(Double.valueOf(rawItem[4].toString()));
			item.setTimeCreated((Date) rawItem[5]);
			resp.add(item);
		}
		tx.commit();
		return resp;
	}
}
