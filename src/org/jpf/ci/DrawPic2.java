package org.jpf.ci;

import java.awt.Font;
import java.io.File;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Month;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartUtilities;

public class DrawPic2
{

	public DrawPic2()
	{
		XYDataset xydataset = createDataset();
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Legal & General单位信托基金价格", "日期", "价格", xydataset,
				true, true, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		dateaxis.setLabelFont(new Font("黑体", Font.BOLD, 14)); // 水平底部标题
		dateaxis.setTickLabelFont(new Font("宋体", Font.BOLD, 12)); // 垂直标题
		ValueAxis rangeAxis = xyplot.getRangeAxis();// 获取柱状
		rangeAxis.setLabelFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("宋体", Font.BOLD, 20));// 设置标题字体
		String fileName = "c:\\pics\\a.jpg";// 保存为位置
		try
		{
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
	{ // 这个数据集有点多，但都不难理解
		TimeSeries timeseries = new TimeSeries("legal & general欧洲指数信任",
				org.jfree.data.time.Day.class);
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

		TimeSeries timeseries1 = new TimeSeries("legal & general英国指数信任",
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

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(timeseries);
		timeseriescollection.addSeries(timeseries1);
		return timeseriescollection;
	}

	public static void main(String args[])
	{
		System.out.print(new DrawPic());
	}
}