package org.itri.view.humanhealth.personal.chart.Imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.HeartRhythmRecord;
import org.itri.view.humanhealth.hibernate.OximeterRecord;
import org.itri.view.humanhealth.hibernate.RtHeartRhythmRecord;
import org.itri.view.humanhealth.personal.chart.dao.SensorIdTimeDao;
import org.itri.view.util.HibernateUtil;

public class BreathRateViewDaoHibernateImpl {

	private int minusThreeMinit = -3;

	public RtHeartRhythmRecord getRtHeartRhythmRecord(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<RtHeartRhythmRecord> resp = new ArrayList<RtHeartRhythmRecord>();
		RtHeartRhythmRecord item = new RtHeartRhythmRecord();
		try {
			tx = session.beginTransaction();

			Criteria criteria = session.createCriteria(RtHeartRhythmRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			resp = criteria.list();
			item = resp.get(0);
			tx.commit();
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return null;
	}

	public List<HeartRhythmRecord> getHeartRhythmThreeMinRecordList(long sensorId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<HeartRhythmRecord> heartRhythmRecordList = new ArrayList<HeartRhythmRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(HeartRhythmRecord.class);
			criteria.add(Restrictions.eq("sensor.sensorId", sensorId));

			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(now);
			calendar.add(Calendar.MINUTE, minusThreeMinit);
			criteria.add(Restrictions.ge("timeCreated", calendar.getTime()));
			criteria.addOrder(Order.asc("timeCreated"));

			heartRhythmRecordList = criteria.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return heartRhythmRecordList;
	}

	public List<HeartRhythmRecord> getHeartRhythmBySensorListRecordList(List<SensorIdTimeDao> sensorTimeList) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<HeartRhythmRecord> heartRhythmRecordList = new ArrayList<HeartRhythmRecord>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(OximeterRecord.class);
			StringBuffer sql = new StringBuffer();

			sql.append("SELECT  sensor_id,");
			sql.append("		time_created,");
			sql.append("		breath_data");
			sql.append(" FROM heart_rhythm_record ");
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
				HeartRhythmRecord item = new HeartRhythmRecord();
//				 rawItem[0]
				item.setTimeCreated((Date) rawItem[1]);
				item.setBreathData(rawItem[2].toString());
				heartRhythmRecordList.add(item);
			}
			tx.commit();
		} catch (

		Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			session.close();
		}
		return heartRhythmRecordList;
	}

}
