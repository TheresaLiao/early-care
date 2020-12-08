package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.util.HibernateUtil;

public class CommonViewDaoHibernateImpl {

	public Sensor getSensorBySensorId(long sensortId) {
		Sensor resp = new Sensor();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(Sensor.class);
			criteria.add(Restrictions.eq("sensorId", sensortId));
			List<Sensor> temp = criteria.list();
			if (temp.size() == 0) {
				tx.commit();
				return null;
			}
			resp = temp.get(0);
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
