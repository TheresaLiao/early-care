package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.itri.view.humanhealth.hibernate.RtTempPadRecord;
import org.itri.view.humanhealth.hibernate.TempPadRecord;
import org.itri.view.humanhealth.personal.chart.Imp.TemperatureViewDaoHibernateImpl;
import org.zkoss.chart.Charts;
import org.zkoss.chart.Options;
import org.zkoss.chart.PlotLine;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;

public class TemperatureView extends SelectorComposer<Component> {

	private long patientId = 0;
	private double specHigh = 0;
	private double specLow = 0;

	private String GREEN_HASH = "#5CE498";
	private String GRAY_HASH = "#808080";
	private String BLACK_HASH = "#000000";

	@Wire
	private Charts chart;

	@Wire("#textboxId")
	private Textbox textboxId;

	@Wire("#textboxSpecH")
	private Textbox textboxSpecH;

	@Wire("#textboxSpecL")
	private Textbox textboxSpecL;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// Component Setting
		super.doAfterCompose(comp);

		// set PatientId
		setPatientId(textboxId.getValue());

		// Get Spec
		setSpecHigh(textboxSpecH.getValue());
		setSpecLow(textboxSpecL.getValue());

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
		Series series = chart.getSeries(0);
		series.setName("Temperature data");

		// spec. Critical High series
		Series seriesH = chart.getSeries(1);
		seriesH.setName("High");
		seriesH.setColor(GRAY_HASH);

		// spec. Critical Low series
		Series seriesL = chart.getSeries(2);
		seriesL.setName("Low");
		seriesL.setColor(GRAY_HASH);

		chart.setColors(GREEN_HASH);
		chart.getXAxis().setLineColor(BLACK_HASH);

		// init point
		List<Point> histData = getTempPadRecordList(getPatientId());
		for (Point p : histData) {
			series.addPoint(p);

//			Point hPoint = getHighPoint(p.getX());
//			seriesH.addPoint(hPoint);
//			Point lPoint = getLowPoint(p.getX());
//			seriesL.addPoint(lPoint);
		}
		if (histData.size() == 0) {
			System.out.println("no history data in temp");
			for (int i = -19; i <= 0; i++) {
				Point nowPoint = getRtTempPadRecordList(getPatientId());
				nowPoint.setX(new Date().getTime() + i * 1000);
				nowPoint.setColor(GREEN_HASH);
				series.addPoint(nowPoint);

//				Point hPoint = getHighPoint(nowPoint.getX());
//				seriesH.addPoint(hPoint);
//				Point lPoint = getLowPoint(nowPoint.getX());
//				seriesL.addPoint(lPoint);
			}
		}
	}

	@Listen("onTimer = #timer")
	public void updateData() {
		setPatientId(textboxId.getValue());
		Point nowPoint = getRtTempPadRecordList(getPatientId());
		chart.getSeries(0).addPoint(nowPoint, true, true, true);

//		Point hPoint = getHighPoint(nowPoint.getX());
//		chart.getSeries(1).addPoint(hPoint, true, true, true);
//		Point lPoint = getLowPoint(nowPoint.getX());
//		chart.getSeries(2).addPoint(lPoint, true, true, true);
	}

	// Get history data
	private List<Point> getTempPadRecordList(long patientId) {
		TemperatureViewDaoHibernateImpl hqe = new TemperatureViewDaoHibernateImpl();
		List<TempPadRecord> tempPadRecordList = hqe.getTempPadRecordList(patientId);

		int i = tempPadRecordList.size() * (-1);
		List<Point> resp = new ArrayList<Point>();
		for (TempPadRecord item : tempPadRecordList) {
			resp.add(new Point(item.getTimeCreated().getTime(), Double.valueOf(item.getBodyTempData())));
		}
		return resp;
	}

	// Get real time data
	private Point getRtTempPadRecordList(long patientId) {

		TemperatureViewDaoHibernateImpl hqe = new TemperatureViewDaoHibernateImpl();
		List<RtTempPadRecord> rtTempPadRecordList = hqe.getRtTempPadRecordList(patientId);

		for (RtTempPadRecord tt : rtTempPadRecordList) {
			String data = tt.getBodyTempData();
			Date time = tt.getLastUpdated();
			return new Point(time.getTime(), Double.valueOf(data));

		}
		return new Point(new Date().getTime(), 0);
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientIdStr) {
		patientId = Long.parseLong(patientIdStr);
		this.patientId = patientId;
	}

	private Point getHighPoint(Number xValue) {
		Point hPoint = new Point(new Date().getTime(), getSpecHigh());
		hPoint.setX(xValue);
		hPoint.setColor(GRAY_HASH);
		return hPoint;
	}

	private Point getLowPoint(Number xValue) {
		Point lPoint = new Point(new Date().getTime(), getSpecLow());
		lPoint.setX(xValue);
		lPoint.setColor(GRAY_HASH);
		return lPoint;
	}

	public double getSpecHigh() {
		return specHigh;
	}

	public void setSpecHigh(String specHighStr) {
		specHigh = Double.valueOf(specHighStr);
		this.specHigh = specHigh;
	}

	public double getSpecLow() {
		return specLow;
	}

	public void setSpecLow(String specLowStr) {
		specLow = Double.valueOf(specLowStr);
		this.specLow = specLow;
	}

}
