package org.itri.view.patientInfo.Imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Room;
import org.itri.view.humanhealth.hibernate.RoomGroup;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.util.HibernateUtil;

public class roomSummaryHibernateImpl {

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
				if (item.getSensor() != null) {
					Hibernate.initialize(item.getSensor());
					Hibernate.initialize(item.getSensor().getSensor2healthTypes());
				}

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

	public RoomGroup getRoomGroupByRoomId(long RoomId) {
		RoomGroup resp = new RoomGroup();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoomGroup.class);
			criteria.add(Restrictions.eq("room.roomId", RoomId));
			List<RoomGroup> temp = criteria.list();
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

	public Room getRoomByRoomId(long RoomId) {
		Room resp = new Room();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Room.class);
			criteria.add(Restrictions.eq("roomId", RoomId));
			List<Room> temp = criteria.list();
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

	public Room getRoomByRoomNum(String RoomNum) {
		Room resp = new Room();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Room.class);
			criteria.add(Restrictions.eq("roomNum", RoomNum));
			List<Room> temp = criteria.list();
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

	public void createRoomGroup(RoomGroup item) {
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

	public void createRoom(Room item) {
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

	public void updateRoomGroup(RoomGroup item) {
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

	public void updateRoom(Room item) {
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

	public void updateCombinationEndTime(Combination item) {
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

	public List<RoomGroup> getRoomGroupList() {
		List<RoomGroup> resp = new ArrayList<RoomGroup>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(RoomGroup.class);

			resp = criteria.list();
			for (RoomGroup item : resp) {
				Hibernate.initialize(item.getRoom());
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

	public List<Room> getRoomList() {
		List<Room> resp = new ArrayList<Room>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Room.class);
			criteria.addOrder(Order.desc("roomId"));
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

	public List<Combination> getCombinationList(List<Long> roomIdList) {
		List<Combination> resp = new ArrayList<Combination>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Combination.class);
			criteria.add(Restrictions.in("room.roomId", roomIdList));
			criteria.add(Restrictions.isNull("endTime"));

			resp = criteria.list();
			for (Combination item : resp) {
				Hibernate.initialize(item.getPatient());
				Hibernate.initialize(item.getPatient().getPatientInfos());
				Hibernate.initialize(item.getRoom());
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

	public List<PatientInfo> getPatientList(List<Long> usedPatientIdList) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PatientInfo> resp = new ArrayList<PatientInfo>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PatientInfo.class);
			criteria.add(Restrictions.not(Restrictions.in("patient.patientId", usedPatientIdList)));
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

	public List<PatientInfo> getPatientList(List<Long> usedPatientIdList, Long defaultPatientId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		List<PatientInfo> resp = new ArrayList<PatientInfo>();
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(PatientInfo.class);
			Criterion where1 = Restrictions.not(Restrictions.in("patient.patientId", usedPatientIdList));
			Criterion where2 = Restrictions.eq("patient.patientId", defaultPatientId);
			criteria.add(Restrictions.or(where1, where2));
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

	public Set<Long> getUsedPatientIdList() {
		Set<Long> resp = new HashSet<>();

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(Combination.class);
			criteria.add(Restrictions.isNull("endTime"));

			List<Combination> temp = criteria.list();
			for (Combination item : temp) {
				Hibernate.initialize(item.getPatient());
				resp.add(item.getPatient().getPatientId());
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
