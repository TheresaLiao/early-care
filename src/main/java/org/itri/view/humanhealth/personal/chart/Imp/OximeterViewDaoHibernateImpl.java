package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.OximeterRecord;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.personal.chart.dao.SensorIdTimeDao;
import org.itri.view.util.HibernateUtil;

public class OximeterViewDaoHibernateImpl extends CommonViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public RtOximeterRecord getRtOximeterRecord(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;

		RtOximeterRecord item = new RtOximeterRecord();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(RtOximeterRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			List<RtOximeterRecord> temp = criteria.list();
			if (temp.size() == 0) {
				tx.commit();
				return null;
			}
			item = temp.get(0);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public List<OximeterRecord> getOximeterThreeMinRecordList(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<OximeterRecord> oximeterRecordList = new ArrayList<OximeterRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(OximeterRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));

			oximeterRecordList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return oximeterRecordList;
	}

	public List<OximeterRecord> getOximeterBySensorListRecordList(List<SensorIdTimeDao> sensorTimeList) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<OximeterRecord> oximeterRecordList = new ArrayList<OximeterRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(OximeterRecord.class);
			StringBuffer sql = new StringBuffer();

			sql.append("SELECT  sensor_id,");
			sql.append("		time_created,");
			sql.append("		heart_rate_data,");
			sql.append("		oximeter_data ");
			sql.append(" FROM oximeter_record ");
			sql.append(" WHERE ");
			for (int count = 0; count < sensorTimeList.size(); count++) {
				Date endDate = sensorTimeList.get(count).getEndTime();
				if (endDate == null && count == 0) {
					sql.append(" ( sensor_id = :sensorId" + count);
					sql.append(" 	  AND time_created < :strTime" + count);
					sql.append(" )");

				} else if (endDate != null && count == 0) {
					sql.append(" ( sensor_id = :sensorId" + count);
					sql.append(" 	  AND time_created BETWEEN :strTime" + count);
					sql.append(" 	                       AND :endTime" + count);
					sql.append(" )");

				} else if (endDate == null) {
					sql.append(" OR ( sensor_id = :sensorId" + count);
					sql.append(" 	  AND time_created < :strTime" + count);
					sql.append(" )");

				} else if (endDate != null) {
					sql.append(" OR ( sensor_id = :sensorId" + count);
					sql.append(" 	  AND time_created BETWEEN :strTime" + count);
					sql.append(" 	                       AND :endTime" + count);
					sql.append(" )");
				}
			}
			sql.append(" ORDER BY time_created");

			SQLQuery query = session.createSQLQuery(sql.toString());
			for (int count = 0; count < sensorTimeList.size(); count++) {
				Date endDate = sensorTimeList.get(count).getEndTime();
				query.setLong("sensorId" + count, sensorTimeList.get(count).getSensorId());
				query.setTimestamp("strTime" + count, sensorTimeList.get(count).getStrTime());
				if (endDate != null) {
					query.setTimestamp("endTime" + count, sensorTimeList.get(count).getEndTime());
				}
			}

			List<Object[]> rows = query.list();
			System.out.println(rows.size());

			for (Object[] rawItem : rows) {
				OximeterRecord item = new OximeterRecord();
//				 rawItem[0]
				item.setTimeCreated((Date) rawItem[1]);
				item.setHeartRateData(rawItem[2].toString());
				item.setOximeterData(rawItem[3].toString());
				oximeterRecordList.add(item);
			}
			tx.commit();
		} catch (

		Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return oximeterRecordList;
	}
}
