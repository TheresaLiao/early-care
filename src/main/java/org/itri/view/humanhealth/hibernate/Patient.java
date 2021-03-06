package org.itri.view.humanhealth.hibernate;

// Generated 2020/10/29 �U�� 04:58:12 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;
import javax.persistence.SequenceGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Patient generated by hbm2java
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "patient")
public class Patient implements java.io.Serializable {

	private long patientId;
	private String heartRateStatus;
	private Integer heartRateNewsScore;
	private String oximeterStatus;
	private Integer oximeterNewsScore;
	private String bodyTempStatus;
	private Integer bodyTempNewsScore;
	private String breathStatus;
	private Integer breathNewsScore;
	private String pmFiveStatus;
	private String mattressStatus;
	private int totalNewsScore;
	private int newsWarningCounter;
	private String newsStatus;
	private Date lastUpdated;
	private Date timeCreated;
	private boolean isDeleted;
	private Set<NewsWarningCondition> newsWarningConditions = new HashSet<NewsWarningCondition>(0);
	private Set<PatientInfo> patientInfos = new HashSet<PatientInfo>(0);
	private Set<Combination> combinations = new HashSet<Combination>(0);
	private Set<NewsRecord> newsRecords = new HashSet<NewsRecord>(0);

	public Patient() {
	}

	public Patient(String heartRateStatus, Integer heartRateNewsScore, String oximeterStatus, Integer oximeterNewsScore,
			String bodyTempStatus, Integer bodyTempNewsScore, String breathStatus, Integer breathNewsScore,
			String pmFiveStatus, String mattressStatus, int totalNewsScore, int newsWarningCounter, String newsStatus,
			Date lastUpdated, Date timeCreated, boolean isDeleted) {
		this.heartRateStatus = heartRateStatus;
		this.heartRateNewsScore = heartRateNewsScore;
		this.oximeterStatus = oximeterStatus;
		this.oximeterNewsScore = oximeterNewsScore;
		this.bodyTempStatus = bodyTempStatus;
		this.bodyTempNewsScore = bodyTempNewsScore;
		this.breathStatus = breathStatus;
		this.breathNewsScore = breathNewsScore;
		this.pmFiveStatus = pmFiveStatus;
		this.mattressStatus = mattressStatus;
		this.totalNewsScore = totalNewsScore;
		this.newsWarningCounter = newsWarningCounter;
		this.newsStatus = newsStatus;
		this.lastUpdated = lastUpdated;
		this.timeCreated = timeCreated;
		this.isDeleted = isDeleted;
	}

	public Patient(long patientId, String heartRateStatus, String oximeterStatus, String bodyTempStatus,
			String breathStatus, String pmFiveStatus, String mattressStatus, int totalNewsScore, int newsWarningCounter,
			String newsStatus, Date timeCreated, boolean isDeleted) {
		this.patientId = patientId;
		this.heartRateStatus = heartRateStatus;
		this.oximeterStatus = oximeterStatus;
		this.bodyTempStatus = bodyTempStatus;
		this.breathStatus = breathStatus;
		this.pmFiveStatus = pmFiveStatus;
		this.mattressStatus = mattressStatus;
		this.totalNewsScore = totalNewsScore;
		this.newsWarningCounter = newsWarningCounter;
		this.newsStatus = newsStatus;
		this.timeCreated = timeCreated;
		this.isDeleted = isDeleted;
	}

	public Patient(long patientId, String heartRateStatus, Integer heartRateNewsScore, String oximeterStatus,
			Integer oximeterNewsScore, String bodyTempStatus, Integer bodyTempNewsScore, String breathStatus,
			Integer breathNewsScore, String pmFiveStatus, String mattressStatus, int totalNewsScore,
			int newsWarningCounter, String newsStatus, Date lastUpdated, Date timeCreated, boolean isDeleted,
			Set<NewsWarningCondition> newsWarningConditions, Set<PatientInfo> patientInfos,
			Set<Combination> combinations, Set<NewsRecord> newsRecords) {
		this.patientId = patientId;
		this.heartRateStatus = heartRateStatus;
		this.heartRateNewsScore = heartRateNewsScore;
		this.oximeterStatus = oximeterStatus;
		this.oximeterNewsScore = oximeterNewsScore;
		this.bodyTempStatus = bodyTempStatus;
		this.bodyTempNewsScore = bodyTempNewsScore;
		this.breathStatus = breathStatus;
		this.breathNewsScore = breathNewsScore;
		this.pmFiveStatus = pmFiveStatus;
		this.mattressStatus = mattressStatus;
		this.totalNewsScore = totalNewsScore;
		this.newsWarningCounter = newsWarningCounter;
		this.newsStatus = newsStatus;
		this.lastUpdated = lastUpdated;
		this.timeCreated = timeCreated;
		this.isDeleted = isDeleted;
		this.newsWarningConditions = newsWarningConditions;
		this.patientInfos = patientInfos;
		this.combinations = combinations;
		this.newsRecords = newsRecords;
	}

	@SequenceGenerator(name = "patient_seq", sequenceName = "patient_patient_id_seq")
	@GeneratedValue(generator = "patient_seq")
	@Id

	@Column(name = "patient_id", unique = true, nullable = false)
	public long getPatientId() {
		return this.patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	@Column(name = "heart_rate_status", nullable = false, length = 32)
	public String getHeartRateStatus() {
		return this.heartRateStatus;
	}

	public void setHeartRateStatus(String heartRateStatus) {
		this.heartRateStatus = heartRateStatus;
	}

	@Column(name = "heart_rate_news_score")
	public Integer getHeartRateNewsScore() {
		return this.heartRateNewsScore;
	}

	public void setHeartRateNewsScore(Integer heartRateNewsScore) {
		this.heartRateNewsScore = heartRateNewsScore;
	}

	@Column(name = "oximeter_status", nullable = false, length = 32)
	public String getOximeterStatus() {
		return this.oximeterStatus;
	}

	public void setOximeterStatus(String oximeterStatus) {
		this.oximeterStatus = oximeterStatus;
	}

	@Column(name = "oximeter_news_score")
	public Integer getOximeterNewsScore() {
		return this.oximeterNewsScore;
	}

	public void setOximeterNewsScore(Integer oximeterNewsScore) {
		this.oximeterNewsScore = oximeterNewsScore;
	}

	@Column(name = "body_temp_status", nullable = false, length = 32)
	public String getBodyTempStatus() {
		return this.bodyTempStatus;
	}

	public void setBodyTempStatus(String bodyTempStatus) {
		this.bodyTempStatus = bodyTempStatus;
	}

	@Column(name = "body_temp_news_score")
	public Integer getBodyTempNewsScore() {
		return this.bodyTempNewsScore;
	}

	public void setBodyTempNewsScore(Integer bodyTempNewsScore) {
		this.bodyTempNewsScore = bodyTempNewsScore;
	}

	@Column(name = "breath_status", nullable = false, length = 32)
	public String getBreathStatus() {
		return this.breathStatus;
	}

	public void setBreathStatus(String breathStatus) {
		this.breathStatus = breathStatus;
	}

	@Column(name = "breath_news_score")
	public Integer getBreathNewsScore() {
		return this.breathNewsScore;
	}

	public void setBreathNewsScore(Integer breathNewsScore) {
		this.breathNewsScore = breathNewsScore;
	}

	@Column(name = "pm_five_status", nullable = false, length = 32)
	public String getPmFiveStatus() {
		return this.pmFiveStatus;
	}

	public void setPmFiveStatus(String pmFiveStatus) {
		this.pmFiveStatus = pmFiveStatus;
	}

	@Column(name = "mattress_status", nullable = false, length = 32)
	public String getMattressStatus() {
		return this.mattressStatus;
	}

	public void setMattressStatus(String mattressStatus) {
		this.mattressStatus = mattressStatus;
	}

	@Column(name = "total_news_score", nullable = false)
	public int getTotalNewsScore() {
		return this.totalNewsScore;
	}

	public void setTotalNewsScore(int totalNewsScore) {
		this.totalNewsScore = totalNewsScore;
	}

	@Column(name = "news_warning_counter", nullable = false)
	public int getNewsWarningCounter() {
		return this.newsWarningCounter;
	}

	public void setNewsWarningCounter(int newsWarningCounter) {
		this.newsWarningCounter = newsWarningCounter;
	}

	@Column(name = "news_status", nullable = false, length = 32)
	public String getNewsStatus() {
		return this.newsStatus;
	}

	public void setNewsStatus(String newsStatus) {
		this.newsStatus = newsStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", length = 29)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_created", nullable = false, length = 29)
	public Date getTimeCreated() {
		return this.timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	@Column(name = "is_deleted", nullable = false)
	public boolean isIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
	public Set<NewsWarningCondition> getNewsWarningConditions() {
		return this.newsWarningConditions;
	}

	public void setNewsWarningConditions(Set<NewsWarningCondition> newsWarningConditions) {
		this.newsWarningConditions = newsWarningConditions;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
	public Set<PatientInfo> getPatientInfos() {
		return this.patientInfos;
	}

	public void setPatientInfos(Set<PatientInfo> patientInfos) {
		this.patientInfos = patientInfos;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
	public Set<Combination> getCombinations() {
		return this.combinations;
	}

	public void setCombinations(Set<Combination> combinations) {
		this.combinations = combinations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
	public Set<NewsRecord> getNewsRecords() {
		return this.newsRecords;
	}

	public void setNewsRecords(Set<NewsRecord> newsRecords) {
		this.newsRecords = newsRecords;
	}

}
