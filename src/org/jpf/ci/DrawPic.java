package org.jpf.ci;

import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartUtilities;

public class DrawPic
{
	private static final Logger logger = LogManager.getLogger(DrawPic.class);

	public DrawPic()
	{
		try
		{
			XYDataset xydataset = createDataset();
			JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("��Ʒһ��Υ��仯ͼ", "����", "Υ����", xydataset, true, true,
					true);
			XYPlot xyplot = (XYPlot) jfreechart.getPlot();
			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
			// dateaxis.setDateFormatOverride(new
			// SimpleDateFormat("MMM-yyyy-dd"));
			dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));
			dateaxis.setLabelFont(new Font("����", Font.BOLD, 14)); // ˮƽ�ײ�����
			dateaxis.setTickLabelFont(new Font("����", Font.BOLD, 12)); // ��ֱ����
			ValueAxis rangeAxis = xyplot.getRangeAxis();// ��ȡ��״
			rangeAxis.setLabelFont(new Font("����", Font.BOLD, 15));
			jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD, 15));
			jfreechart.getTitle().setFont(new Font("����", Font.BOLD, 20));// ���ñ�������
			String fileName = "c:\\pics\\a.jpg";// ����Ϊλ��

			File f = new File("c:\\pics");
			if (!f.exists())
			{
				f.mkdir();
			}
			ChartUtilities.saveChartAsJPEG(new File(fileName), 1, jfreechart, 1000, 600);// ��1000����600
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.print("game over");
	}

	private static XYDataset createDataset()
	{
		Connection conn = null;
		TimeSeriesCollection timeseriescollection=null;
		try
		{
			// ������ݼ��е�࣬�����������
			

			String strSql = "select * from  hz_rpt order by build_date2,prj_name";
			logger.debug("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
			TimeSeries timeseries = new TimeSeries("���Υ��",
					org.jfree.data.time.Day.class);
			while (rs.next())
			{
				timeseries.add(new Day(1, 5, 2013), 181.80000000000001D);
				timeseries.add(new Day(3, 5, 2013), 167.30000000000001D);
				timeseries.add(new Day(4, 5, 2013), 153.80000000000001D);
				timeseries.add(new Day(5, 5, 2013), 167.59999999999999D);
				timeseries.add(new Day(6, 5, 2013), 158.80000000000001D);
				timeseries.add(new Day(7, 5, 2013), 148.30000000000001D);
				timeseries.add(new Day(8, 5, 2013), 153.90000000000001D);
				timeseries.add(new Day(9, 5, 2013), 142.69999999999999D);
				timeseries.add(new Day(10, 5, 2013), 123.2D);
				timeseries.add(new Day(11, 5, 2013), 131.80000000000001D);
				timeseries.add(new Day(12, 5, 2013), 139.59999999999999D);
	
			}

			
			TimeSeries timeseries1 = new TimeSeries("����Υ��",
					org.jfree.data.time.Day.class);
			timeseries1.add(new Day(1, 5, 2013), 129.59999999999999D);
			timeseries1.add(new Day(2, 5, 2013), 123.2D);
			timeseries1.add(new Day(3, 5, 2013), 117.2D);
			timeseries1.add(new Day(4, 5, 2013), 124.09999999999999D);
			timeseries1.add(new Day(5, 5, 2013), 122.59999999999999D);
			timeseries1.add(new Day(7, 5, 2013), 119.2D);
			timeseries1.add(new Day(8, 5, 2013), 116.5D);
			timeseries1.add(new Day(9, 5, 2013), 112.7D);
			timeseries1.add(new Day(10, 5, 2013), 101.5D);
			timeseries1.add(new Day(11, 5, 2013), 106.09999999999999D);
			timeseries1.add(new Day(12, 5, 2013), 110.3D);

			TimeSeries timeseries2 = new TimeSeries("��ҪΥ��",
					org.jfree.data.time.Day.class);
			timeseries2.add(new Day(1, 5, 2013), 119.59999999999999D);
			timeseries2.add(new Day(2, 5, 2013), 123.2D);
			timeseries2.add(new Day(3, 5, 2013), 127.2D);
			timeseries2.add(new Day(4, 5, 2013), 124.09999999999999D);
			timeseries2.add(new Day(5, 5, 2013), 142.59999999999999D);
			timeseries2.add(new Day(7, 5, 2013), 119.2D);
			timeseries2.add(new Day(8, 5, 2013), 126.5D);
			timeseries2.add(new Day(9, 5, 2013), 112.7D);
			timeseries2.add(new Day(10, 5, 2013), 101.5D);
			timeseries2.add(new Day(11, 5, 2013), 116.09999999999999D);
			timeseries2.add(new Day(12, 5, 2013), 120.3D);

			timeseriescollection = new TimeSeriesCollection();
			timeseriescollection.addSeries(timeseries);
			timeseriescollection.addSeries(timeseries1);
			timeseriescollection.addSeries(timeseries2);
			
		} catch (Exception ex)
		{
			logger.error(ex);
			ex.printStackTrace();
		} finally
		{
			AppConn.DoClear(conn);
		}
		return timeseriescollection;
	}

	public static void main(String args[])
	{
		new DrawPic();
	}
}