package org.itri.view.admin;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.util.HibernateUtil;

public class HibernateQueryExample {

	/*
	 * Provide some examples queries
	 */
	public static void main(String[] args) {
		HibernateQueryExample hqe = new HibernateQueryExample();
		hqe.getPatientNameById();
//		hqe.getPatientOximeterRecord();
    }
	
	public void getPatientNameById() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		
		try{
			tx = session.beginTransaction();
			
			Criteria criteria = session.createCriteria(Patient.class);
			criteria.add(Restrictions.eq("patientId", Long.parseLong("1")));
			Patient p = (Patient) criteria.uniqueResult();
			
			System.out.println("name is: " + p.getPatientInfos().stream().findFirst().get().getName());
			
			tx.commit();
		} 
		catch (Exception e){
			e.printStackTrace();
			tx.rollback();
		}
		finally {
			session.close();
		}
	}
	
//	public void getPatientOximeterRecord() {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction tx = null;
//		
//		try{
//			tx = session.beginTransaction();
//			
//			Criteria criteria = session.createCriteria(Patient.class);
//			criteria.add(Restrictions.eq("patientId", Long.parseLong("1")));
//			Patient p = (Patient) criteria.uniqueResult();
//			
//			String patientName = p.getPatientInfos().stream().findFirst().get().getName();
//			String oximeterData = p.getRtOximeterRecords().stream().findFirst().get().getOximeterData();
//			
//			System.out.println("Patient name: " + patientName + " oximeter data: " + oximeterData);
//			
//			tx.commit();
//		} 
//		catch (Exception e){
//			e.printStackTrace();
//			tx.rollback();
//		}
//		finally {
//			session.close();
//		}
//	}
}
