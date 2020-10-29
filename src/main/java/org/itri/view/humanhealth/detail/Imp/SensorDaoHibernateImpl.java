package org.itri.view.humanhealth.detail.Imp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.util.HibernateUtil;

public class SensorDaoHibernateImpl {

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
