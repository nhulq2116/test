package com.mililu.moneypower;

import java.text.NumberFormat;
import java.util.Calendar;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class StatisticActivity2 extends Activity implements OnItemSelectedListener{
	private 	Button btnPrevious, btnNext, btnBack;
	private DataBaseAdapter dbAdapter;
	private int id_user, mMonth, mYear;
	private long mIncome, mExpenditure;
	private TextView txtDate, txtTotalExpenditure, txtTotalIncome, txtTime, txtType, txtTittle;
	private View mChart;
	private LinearLayout chartContainer;
	private String typeStatistic[]={"Statistic By Month", "Statistic by Year"};
	
	private String[] ListMonth = new String[] {"Jan", "Feb" , "Mar", "Apr", "May", "Jun",
			"Jul", "Aug" , "Sep", "Oct", "Nov", "Dec"};
	
	// Color of each Pie Chart Sections
	private int[] colors = { Color.rgb(37, 126, 255), Color.rgb(255, 37, 126), Color.rgb(126, 255, 37), Color.rgb(255, 185, 15), Color.rgb(191, 62, 255),
			Color.rgb(141, 238, 238), Color.rgb(255, 69, 0), Color.rgb(154, 255, 154), Color.rgb(255, 246, 143), Color.rgb(70, 132, 153), Color.rgb(0, 255, 127),
			Color.rgb(100, 50, 200), Color.rgb(255, 247, 40), Color.rgb(255, 40, 40), };
	private Spinner spnTypeStatistic, spnTypeChart;
	private String[] NameIncome, NameExpense;
	private double[] MoneyIncome, MoneyExpense;
	private double[] income;
	private double[] expense;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistic2);
        
        // Get The Reference Of View
	    txtDate=(TextView)findViewById(R.id.tv_statistic2_date);
	    txtTotalIncome=(TextView)findViewById(R.id.tv_statistic2_totalincome);
	    txtTotalExpenditure=(TextView)findViewById(R.id.tv_statistic2_totalexpenditure);
        btnNext=(Button)findViewById(R.id.btn_statistic2_next);
        btnPrevious=(Button)findViewById(R.id.btn_statistic2_previous);
        btnBack = (Button)findViewById(R.id.btn_statistic2_back);
        spnTypeChart = (Spinner)findViewById(R.id.spn_statistic2_typechart);
        spnTypeStatistic = (Spinner)findViewById(R.id.spn_statistic2_typestatistic);
        chartContainer = (LinearLayout) findViewById(R.id.layout_statistic2_chart);
        txtTime=(TextView)findViewById(R.id.tv_time_rp);
        txtType=(TextView)findViewById(R.id.tv_type_rp);
        txtTittle=(TextView)findViewById(R.id.tv_statistic_title);
		
        // Create a instance of SQLite Database
	    dbAdapter =new DataBaseAdapter(this);
	    dbAdapter = dbAdapter.open();
	    // Spinner click listener
	    spnTypeStatistic.setOnItemSelectedListener(this);
	    spnTypeChart.setOnItemSelectedListener(this);
	    loadSpnTypeStatistic();
	    loadSpnTypeChart();
	    // Set OnClick Listener on button 
	    btnNext.setOnClickListener(new MyEvent());
	    btnPrevious.setOnClickListener(new MyEvent());
	    btnBack.setOnClickListener(new MyEvent());
	    id_user = HomeActivity.id_user;
		GetCurrentDate();
		
		// Khoi tao font
	    Typeface font_bold = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLD.TTF");
	    Typeface font_light = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUELIGHT.TTF");
	    Typeface font_italic = Typeface.createFromAsset(getAssets(),"fonts/HELVETICANEUEBOLDITALIC.TTF");
	    
	    // Set fonts
	    txtDate.setTypeface(font_light);
	    txtTotalIncome.setTypeface(font_light);
	    txtTotalExpenditure.setTypeface(font_light);
	    txtTime.setTypeface(font_light);
	    txtType.setTypeface(font_light);
	    txtTittle.setTypeface(font_light);
	}
	
    private void loadSpnTypeStatistic() {
        // Creating adapter for spinner
        ArrayAdapter<String> dataStatisticAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeStatistic);

        //dataStatisticAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // attaching data adapter to spinner
        spnTypeStatistic.setAdapter(dataStatisticAdapter);
    }
    
    private void loadSpnTypeChart() {
    	// Creating adapter for spinner
    	String typeChart[] = {};
    	if (spnTypeStatistic.getSelectedItemPosition() == 0){
    		typeChart = new String[] {"Income", "Expense"};
    	}
    	else if(spnTypeStatistic.getSelectedItemPosition() == 1){
    		typeChart = new String[] {"Income", "Expense", "Genaral"};
    	}
    	ArrayAdapter<String> dataChartAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeChart);
        //dataChartAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        // attaching data adapter to spinner
        spnTypeChart.setAdapter(dataChartAdapter);
    }
	
	private void getTotalIncome(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mIncome = dbAdapter.getTotalIncome(mMonth, mYear, id_user);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mIncome = dbAdapter.getTotalIncome(mYear, id_user);
		}
		txtTotalIncome.setText("Total Income: " + NumberFormat.getCurrencyInstance().format(mIncome));
		
	}
	private void getTotalExpenditure(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mExpenditure = dbAdapter.getTotalExpenditure(mMonth, mYear, id_user);
		}
		else if(spnTypeStatistic.getSelectedItemPosition() == 1){
			mExpenditure = dbAdapter.getTotalExpenditure(mYear, id_user);
		}
		txtTotalExpenditure.setText("Total Expenditure: " + NumberFormat.getCurrencyInstance().format(mExpenditure));
	}
	
	private void CalcultateIncome(){
			double balance = 0;
			Cursor cursorIncome = null;
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				cursorIncome = dbAdapter.getListIncomeOfMonth(mMonth, mYear, id_user);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				cursorIncome = dbAdapter.getListIncomeOfYear(mYear, id_user);
			}
			if (cursorIncome.getCount()>0){
				// Pie Chart Section Names
				NameIncome = new String[cursorIncome.getCount()];
				// Pie Chart Section Value
				MoneyIncome = new double[cursorIncome.getCount()];
				
				int count = 0;
				
				/// Set up data for chart
				cursorIncome.moveToFirst();
				while(!cursorIncome.isAfterLast()){
					String name = cursorIncome.getString(cursorIncome.getColumnIndexOrThrow("NAME_INCOME"));
					NameIncome[count] = name;
					int id_income = cursorIncome.getInt(cursorIncome.getColumnIndexOrThrow("ID_CATEGORY"));
					if (spnTypeStatistic.getSelectedItemPosition() == 0){
						balance = dbAdapter.CalculateIncomeByMonth(id_income, mMonth, mYear, id_user);
					}
					else if (spnTypeStatistic.getSelectedItemPosition() == 1){
						balance = dbAdapter.CalculateIncomeByYear(id_income, mYear, id_user);
					}
					MoneyIncome[count] = balance;
					cursorIncome.moveToNext();
					count++;
				}
				cursorIncome.close();
				// Draw chart
				DrawPieChart(NameIncome, MoneyIncome, chartContainer, mChart);
			}
			else {
				// remove any views before u paint the chart
				chartContainer.removeAllViews();
				chartContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
			}
	}
	
	private void CalcultateExpense(){
			double balance = 0;
			Cursor cursorExpen = null;
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				cursorExpen = dbAdapter.getListExpenditureOfMonth(mMonth, mYear, id_user);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				cursorExpen = dbAdapter.getListExpenditureOfYear(mYear, id_user);
			}
			if (cursorExpen.getCount()>0){
				// Pie Chart Section Names
				NameExpense = new String[cursorExpen.getCount()];
				// Pie Chart Section Value
				MoneyExpense = new double[cursorExpen.getCount()];
				
				int count = 0;
				
				/// Set up data for chart
				cursorExpen.moveToFirst();
				while(!cursorExpen.isAfterLast()){
					String name = cursorExpen.getString(cursorExpen.getColumnIndexOrThrow("NAME_EXP"));
					NameExpense[count] = name;
					int id_expend = cursorExpen.getInt(cursorExpen.getColumnIndexOrThrow("ID_PARENT_CATEGORY"));
					if (spnTypeStatistic.getSelectedItemPosition() == 0){
						balance = dbAdapter.CalculateExpendByMonth(id_expend,mMonth, mYear, id_user);
					}
					else if (spnTypeStatistic.getSelectedItemPosition() == 1){
						balance = dbAdapter.CalculateExpendByYear(id_expend, mYear, id_user);
					}
					
					MoneyExpense[count] = balance;
					cursorExpen.moveToNext();
					count++;
				}
				cursorExpen.close();
				// Draw chart
				DrawPieChart(NameExpense, MoneyExpense, chartContainer, mChart);
			}
			else {
				// remove any views before u paint the chart
				chartContainer.removeAllViews();
				chartContainer.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
				
			}
	}
	
	public void GetCurrentDate(){
		Calendar cal=Calendar.getInstance();
		// by month 
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			mMonth=(cal.get(Calendar.MONTH)+1);
			mYear=cal.get(Calendar.YEAR);
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear=cal.get(Calendar.YEAR);
			txtDate.setText(String.valueOf(mYear));
		}
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		Spinner spinner = (Spinner) parent;
		if(spinner.getId() == R.id.spn_statistic2_typestatistic)
		{
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				txtDate.setText(mMonth +"/"+mYear);
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				txtDate.setText(String.valueOf(mYear));
			}
			getTotalIncome();
			getTotalExpenditure();
			loadSpnTypeChart();
		}
		if(spinner.getId() == R.id.spn_statistic2_typechart)
		{
				if (spnTypeChart.getSelectedItemPosition() == 0){
					CalcultateIncome();
				}
				else if (spnTypeChart.getSelectedItemPosition() == 1){
					CalcultateExpense();
				}
				else if (spnTypeChart.getSelectedItemPosition() == 2){
					DrawLineChart();
				}

		}
	}
	
	@Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }
	
	private class MyEvent implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.btn_statistic2_back){
				StatisticActivity2.this.finish();
			}
			if(v.getId()==R.id.btn_statistic2_previous){
				setPreviousDate();
				getTotalIncome();
			    getTotalExpenditure();
			    if (spnTypeChart.getSelectedItemPosition() == 0){
			    	CalcultateIncome();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 1){
			    	CalcultateExpense();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 2){
			    	DrawLineChart();
			    }
			}
			if (v.getId()==R.id.btn_statistic2_next){
				setNextDate();
				getTotalIncome();
			    getTotalExpenditure();
			    if (spnTypeChart.getSelectedItemPosition() == 0){
			    	CalcultateIncome();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 1){
			    	CalcultateExpense();
			    }
			    else if (spnTypeChart.getSelectedItemPosition() == 2){
			    	DrawLineChart();
			    }
			}
		}
	}
	
	private void setPreviousDate(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			if ((mMonth < 13) && (mMonth > 1)){
				mMonth -= 1;
			}
			else if (mMonth == 1) {
				mMonth = 12;
				mYear --;
			}
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear--;
			txtDate.setText(String.valueOf(mYear));
		}
	}
	private void setNextDate(){
		if (spnTypeStatistic.getSelectedItemPosition() == 0){
			if ((mMonth < 12) && (mMonth > 0)){
				mMonth += 1;
			}
			else if (mMonth == 12){
				mMonth = 1;
				mYear ++;
			}
			txtDate.setText((mMonth)+"-"+mYear);
		}
		else if (spnTypeStatistic.getSelectedItemPosition() == 1){
			mYear ++;
			txtDate.setText(String.valueOf(mYear));
		}
	}
	private void DrawPieChart(String[] name, double[] value, LinearLayout chartContainer, View mChart) {
		// Instantiating CategorySeries to plot Pie Chart
		CategorySeries distributionSeries = new CategorySeries(
				" Android version distribution as on October 1, 2012");
		for (int i = 0; i < value.length; i++) {
			// Adding a slice with its values and name to the Pie Chart
			distributionSeries.add(name[i], value[i]);
		}
		
		// Instantiating a renderer for the Pie Chart
		DefaultRenderer defaultRenderer = new DefaultRenderer();
		for (int i = 0; i < value.length; i++) {
			SimpleSeriesRenderer seriesRenderer = new SimpleSeriesRenderer();
			seriesRenderer.setColor(colors[i]);
			//seriesRenderer.setDisplayBoundingPoints(true); //.setDisplayChartValues(true);
			//Adding colors to the chart
			defaultRenderer.setBackgroundColor(Color.TRANSPARENT);
			defaultRenderer.setApplyBackgroundColor(true);
			// Adding a renderer for a slice
			defaultRenderer.addSeriesRenderer(seriesRenderer);
		}
		if (spnTypeChart.getSelectedItemPosition() == 0){
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				defaultRenderer.setChartTitle("Income (" + mMonth + "/" + mYear + ")");
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				defaultRenderer.setChartTitle("Income (" + mYear + ")");
			} 
		}
		else if	(spnTypeChart.getSelectedItemPosition() == 1){
			if (spnTypeStatistic.getSelectedItemPosition() == 0){
				defaultRenderer.setChartTitle("Expenditure (" + mMonth + "/" + mYear + ")");
			}
			else if (spnTypeStatistic.getSelectedItemPosition() == 1){
				defaultRenderer.setChartTitle("Expenditure (" + mYear + ")");
			} 
		}
		defaultRenderer.setChartTitleTextSize(23);
		defaultRenderer.setLabelsTextSize(18);
		defaultRenderer.setZoomButtonsVisible(false);
		defaultRenderer.setDisplayValues(true);
		defaultRenderer.setLegendTextSize(18);
		defaultRenderer.setPanEnabled(false);
		defaultRenderer.setLabelsColor(Color.BLACK);
		
		
		// this part is used to display graph on the xml
		// Creating an intent to plot bar chart using dataset and
		// multipleRenderer
		// Intent intent = ChartFactory.getPieChartIntent(getBaseContext(),
		// distributionSeries , defaultRenderer, "AChartEnginePieChartDemo");
		
		// Start Activity
		// startActivity(intent);
		
		// remove any views before u paint the chart
		chartContainer.removeAllViews();
		chartContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		// drawing pie chart
		mChart = ChartFactory.getPieChartView(getBaseContext(),
				distributionSeries, defaultRenderer);
		// adding the view to the linearlayout
		chartContainer.addView(mChart);
		
	}
	
	private void DrawLineChart() {
		income = new double[12];
		expense = new double[12];
		
		for (int i = 0; i < 12; i ++){
			income[i] = dbAdapter.getTotalIncome((i+1),mYear, id_user);
			expense[i] = dbAdapter.getTotalExpenditure((i+1), mYear, id_user);
		}
		
		double YaxixMax = 0;
		for (int i = 0; i < ListMonth.length; i++){
			if (YaxixMax < income[i]){
				YaxixMax = income[i];
			}
			if (YaxixMax < expense[i]){
				YaxixMax = expense[i];
			}
		}
		// Creating an XYSeries for Income
		XYSeries incomeSeries = new XYSeries("Income");
		// Creating an XYSeries for Expense
		XYSeries expenseSeries = new XYSeries("Expense");
		// Adding data to Income and Expense Series
		for(int i=0;i<ListMonth.length;i++){
			incomeSeries.add(i,income[i]);
			expenseSeries.add(i,expense[i]);
		}
				 
		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Income Series to the dataset
		dataset.addSeries(incomeSeries);
		// Adding Expense Series to dataset
		dataset.addSeries(expenseSeries);
		
		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer incomeRenderer = new XYSeriesRenderer();
		incomeRenderer.setColor(Color.BLUE); //color of the graph set to cyan
		incomeRenderer.setFillPoints(true);
		incomeRenderer.setLineWidth(2f);
		incomeRenderer.setDisplayChartValues(true);
		//setting chart value distance
		incomeRenderer.setDisplayChartValuesDistance(10);
		//setting line graph point style to circle
		incomeRenderer.setPointStyle(PointStyle.CIRCLE);
		//setting stroke of the line chart to solid
		incomeRenderer.setStroke(BasicStroke.SOLID);
		incomeRenderer.setChartValuesTextSize(15);
		
		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer expenseRenderer = new XYSeriesRenderer();
		expenseRenderer.setColor(Color.RED);
		expenseRenderer.setFillPoints(true);
		expenseRenderer.setLineWidth(2f);
		expenseRenderer.setDisplayChartValues(true);
		//setting line graph point style to circle
		expenseRenderer.setPointStyle(PointStyle.SQUARE);
		//setting stroke of the line chart to solid
		expenseRenderer.setStroke(BasicStroke.SOLID);
		expenseRenderer.setChartValuesTextSize(15);
		
		
		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("Income vs Expense " + "(" + mYear + ")");
		multiRenderer.setYTitle("Amount");
		/***
		 * Customizing graphs
		 */
		//setting text size of the title
		multiRenderer.setChartTitleTextSize(28);
		//setting text size of the axis title
		multiRenderer.setAxisTitleTextSize(24);
		//setting text size of the graph lable
		multiRenderer.setLabelsTextSize(18);
		//setting zoom buttons visiblity
		multiRenderer.setZoomButtonsVisible(false);
		//setting pan enablity which uses graph to move on both axis
		multiRenderer.setPanEnabled(false, false);
		//setting click false on graph
		multiRenderer.setClickEnabled(false);
		//setting zoom to false on both axis
		multiRenderer.setZoomEnabled(true, true);
		//setting lines to display on y axis
		multiRenderer.setShowGridY(false);
		//setting lines to display on x axis
		multiRenderer.setShowGridX(true);
		//setting legend to fit the screen size
		multiRenderer.setFitLegend(true);
		//setting displaying line on grid
		//multiRenderer.setShowGrid(true);
		//setting zoom to false
		//multiRenderer.setZoomEnabled(true);
		//setting external zoom functions to false
		//multiRenderer.setExternalZoomEnabled(true);
		//setting displaying lines on graph to be formatted(like using graphics)
		//multiRenderer.setAntialiasing(true);
		//setting to in scroll to false
		//multiRenderer.setInScroll(true);
		//setting to set legend height of the graph
		multiRenderer.setLegendHeight(30);
		multiRenderer.setLegendTextSize(18);
		//setting x axis label align
		multiRenderer.setXLabelsAlign(Align.CENTER);
		//setting y axis label to align
		multiRenderer.setYLabelsAlign(Align.LEFT);
		//setting text style
		multiRenderer.setTextTypeface("sans_serif", Typeface.NORMAL);
		//setting no of values to display in y axis
		multiRenderer.setYLabels(15);
		// setting y axis max value, Since i'm using static values inside the graph so i'm setting y max value to 4000.
		// if you use dynamic values then get the max y value and set here
		multiRenderer.setYAxisMax(YaxixMax*1.1);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMin(-1);
		//setting used to move the graph on xaxiz to .5 to the right
		multiRenderer.setXAxisMax(ListMonth.length);
		//setting bar size or space between two bars
		//multiRenderer.setBarSpacing(0.5);
		//Setting background color of the graph to transparent
		//multiRenderer.setBackgroundColor(Color.WHITE);
		//Setting margin color of the graph to transparent
		multiRenderer.setMarginsColor(Color.WHITE);
		//multiRenderer.setApplyBackgroundColor(true);
		//multiRenderer.setScale(2f);
		//setting x axis point size
		//multiRenderer.setPointSize(4f);
		
		//setting the margin size for the graph in the order top, left, bottom, right
		multiRenderer.setMargins(new int[]{50, 30, 20, 20});
		multiRenderer.setLabelsColor(Color.BLACK);
		multiRenderer.setXLabelsColor(Color.BLACK);
		for(int i=0; i< ListMonth.length;i++){
			multiRenderer.addXTextLabel(i, ListMonth[i]);
		}
		
		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(incomeRenderer);
		multiRenderer.addSeriesRenderer(expenseRenderer);
		
		//remove any views before u paint the chart
		chartContainer.removeAllViews();
		chartContainer.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//drawing bar chart
		mChart = ChartFactory.getLineChartView(StatisticActivity2.this, dataset, multiRenderer);
		//adding the view to the linearlayout
		chartContainer.addView(mChart);
			
	}
}
