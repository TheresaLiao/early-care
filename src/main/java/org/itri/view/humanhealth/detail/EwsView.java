package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.itri.view.humanhealth.hibernate.NewsRecord;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.personal.chart.Imp.EwsViewDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfosDaoHibernateImpl;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Options;
import org.zkoss.chart.PlotLine;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class EwsView extends SelectorComposer<Window> {

	private long patientId = 0;

	private String GREEN_HASH = "#5CE498";
	private String GRAY_HASH = "#808080";
	private String BLACK_HASH = "#000000";
	private String WHITE_HASH = "#FFFFFF";

	@Wire
	private Charts chart;

	@Wire("#textboxId")
	private Textbox textboxId;

	@Wire("#textboxHisDate")
	private Textbox textboxHisDate;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		// Component Setting
		super.doAfterCompose(comp);

		// set PatientId
		setPatientId(textboxId.getValue());

		// set charts
		Options options = new Options();
		options.getGlobal().setUseUTC(false);
		chart.setOptions(options);
		chart.setAnimation(true);
		chart.setBackgroundColor("black");
		chart.getXAxis().setType("datetime");
		chart.getXAxis().setTickPixelInterval(150);
		chart.getYAxis().setTitle("");
		PlotLine plotLine = new PlotLine();
		plotLine.setValue(0);
		plotLine.setWidth(1);
		plotLine.setColor(GRAY_HASH);
		chart.getYAxis().addPlotLine(plotLine);
		chart.getTooltip().setHeaderFormat("<b>{series.name}</b><br/>");
		chart.getTooltip().setPointFormat("{point.x:%Y-%m-%d %H:%M:%S}<br>{point.y}");
		chart.getLegend().setEnabled(false);
		chart.getExporting().setEnabled(false);

		// value series
		Series series = chart.getSeries();
		series.setName("EWS data");

		chart.setColors(WHITE_HASH);
		chart.getXAxis().setLineColor(BLACK_HASH);

		// init point
		List<Point> histData = getNewsRecordList(getPatientId());
		for (Point p : histData) {
			series.addPoint(p);
		}

		if (histData.size() == 0) {
			System.out.println("no history data in ews");
			for (int i = -19; i <= 0; i++) {
				Point nowPoint = getRtHeartRhythmRecordList(getPatientId());
				nowPoint.setX(new Date().getTime() + i * 1000);
				nowPoint.setColor(WHITE_HASH);
				series.addPoint(nowPoint);
			}
		}
	}

	@Listen("onTimer = #timer")
	public void updateData() {
		setPatientId(textboxId.getValue());

		Point nowPoint = getRtHeartRhythmRecordList(getPatientId());
		chart.getSeries().addPoint(nowPoint, true, true, true);
	}

	// Get history data
	private List<Point> getNewsRecordList(long patientId) {
		EwsViewDaoHibernateImpl hqe = new EwsViewDaoHibernateImpl();
		List<NewsRecord> newsRecordList = hqe.getNewsRecordByDateList(patientId);

		List<Point> resp = new ArrayList<Point>();
		for (NewsRecord item : newsRecordList) {
			resp.add(new Point(item.getTimeCreated().getTime(), Double.valueOf(item.getNewsScore())));
		}
		return resp;
	}

	// Get real time data
	private Point getRtHeartRhythmRecordList(long patientId) {
		PersonInfosDaoHibernateImpl hqe = new PersonInfosDaoHibernateImpl();
		Patient patient = hqe.getPatientById(patientId);
		return new Point(patient.getLastUpdated().getTime(), Double.valueOf(patient.getTotalNewsScore()));
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientIdStr) {
		patientId = Long.parseLong(patientIdStr);
		this.patientId = patientId;
	}
}