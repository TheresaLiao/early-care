package org.itri.view.humanhealth.personal.chart.Imp;

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
import org.itri.view.humanhealth.hibernate.RtHeartRhythmRecord;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.util.HibernateUtil;

public class PersonInfosDaoHibernateImpl {

	private Integer totalNewsScoreSpec = 7;

	

	public List<SensorThreshold> getPatientThresholdByPatientId(long patientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<SensorThreshold> sensorThresholdList = new ArrayList<SensorThreshold>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(SensorThreshold.class);
			criteria.add(Restrictions.eq("patient.patientId", patientId));
			sensorThresholdList = criteria.list();

			for (SensorThreshold item : sensorThresholdList) {
				Hibernate.initialize(item.getSensor());
			}
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return sensorThresholdList;
	}
}
