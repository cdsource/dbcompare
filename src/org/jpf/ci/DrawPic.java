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
			JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("产品一周违规变化图", "日期", "违规数", xydataset, true, true,
					true);
			XYPlot xyplot = (XYPlot) jfreechart.getPlot();
			DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
			// dateaxis.setDateFormatOverride(new
			// SimpleDateFormat("MMM-yyyy-dd"));
			dateaxis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));
			dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
			dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
			ValueAxis rangeAxis = xyplot.getRangeAxis();// 获取柱状
			rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
			jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
			jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
			String fileName = "c:\\pics\\a.jpg";// 保存为位置

			File f = new File("c:\\pics");
			if (!f.exists())
			{
				f.mkdir();
			}
			ChartUtilities.saveChartAsJPEG(new File(fileName), 1, jfreechart, 1000, 600);// 宽1000，高600
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
			// 这个数据集有点多，但都不难理解
			

			String strSql = "select * from  hz_rpt order by build_date2,prj_name";
			logger.debug("strSql=" + strSql);
			conn = AppConn.GetInstance().GetConn();
			PreparedStatement pStmt = conn.prepareStatement(strSql);
			ResultSet rs = pStmt.executeQuery();
			TimeSeries timeseries = new TimeSeries("阻断违规",
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

			
			TimeSeries timeseries1 = new TimeSeries("严重违规",
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

			TimeSeries timeseries2 = new TimeSeries("主要违规",
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