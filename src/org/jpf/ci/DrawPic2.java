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
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Legal & General��λ���л���۸�", "����", "�۸�", xydataset,
				true, true, true);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
		dateaxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));
		dateaxis.setLabelFont(new Font("����", Font.BOLD, 14)); // ˮƽ�ײ�����
		dateaxis.setTickLabelFont(new Font("����", Font.BOLD, 12)); // ��ֱ����
		ValueAxis rangeAxis = xyplot.getRangeAxis();// ��ȡ��״
		rangeAxis.setLabelFont(new Font("����", Font.BOLD, 15));
		jfreechart.getLegend().setItemFont(new Font("����", Font.BOLD, 15));
		jfreechart.getTitle().setFont(new Font("����", Font.BOLD, 20));// ���ñ�������
		String fileName = "c:\\pics\\a.jpg";// ����Ϊλ��
		try
		{
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
	{ // ������ݼ��е�࣬�����������
		TimeSeries timeseries = new TimeSeries("legal & generalŷ��ָ������",
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

		TimeSeries timeseries1 = new TimeSeries("legal & generalӢ��ָ������",
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