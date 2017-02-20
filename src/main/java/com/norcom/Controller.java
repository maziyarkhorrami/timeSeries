package com.norcom;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import net.seninp.jmotif.sax.SAXException;
import net.seninp.jmotif.sax.datastructure.SAXRecord;
import net.seninp.jmotif.sax.datastructure.SAXRecords;

public class Controller implements Initializable {

	@FXML
	private TableView<?> tasksTable;

	@FXML
	private Label numberOfTimeSamplesLbl;

	@FXML
	private TextField numberOfTimeSamplesTxt;

	@FXML
	private Label sizeOfWindowLbl;

	@FXML
	private TextField sizeOfWindowTxt;

	@FXML
	private Label sAXAlphabetLbl;

	@FXML
	private TextField sAXAlphabetTxt;

	@FXML
	private NumberAxis xAxis;

	@FXML
	private NumberAxis yAxis;

	@FXML
	private LineChart<Number, Number> lineChart;

	@FXML
	private BarChart<String, Number> barChart;

	@FXML
	private Button getTSBtn;

	@FXML
	private Button getSAXFreqBtn;

	@FXML
	private Button getPACFeatBtn;

	private static Model model = new Model();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Handle ListView selection changes.
		
		sAXAlphabetTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				numberOfTimeSamplesTxt.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
		numberOfTimeSamplesTxt.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				numberOfTimeSamplesTxt.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		sizeOfWindowTxt.textProperty().addListener((observalble, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				sizeOfWindowTxt.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});

		getTSBtn.setOnAction((event) -> {
			// Model model = new Model();
			// We need to get the data from TextFields and pass it to the
			// LineChat
			SAXRecords sAXrecords = generateDataForPlot();
			// xAxis.setTickUnit((double) 4);
			Integer size = Integer.valueOf(numberOfTimeSamplesTxt.getText());
			ArrayList<Double> timeSampleData = model.generateRandomNumbers(size);
			lineChart.setData(getTimeSeriesData(sAXrecords, timeSampleData));
			//lineChart.getStylesheets().addAll(getClass().getClassLoader().getResource("lineChart.css").toExternalForm());
			lineChart.setTitle("Time Series");
			lineChart.setLegendVisible(false);
			barChart.setTitle("");
			barChart.setAnimated(false);
			
		});

		getSAXFreqBtn.setOnAction((event) -> {
			//barChart.setLegendVisible(false);

			SAXRecords sAXrecords = generateDataForPlot();
			barChart.getStylesheets().addAll(getClass().getClassLoader().getResource("barChart.css").toExternalForm());
			barChart.setData(getSAXFreqData(sAXrecords));
			barChart.setHorizontalGridLinesVisible(false);
			barChart.setVerticalGridLinesVisible(false);
			
			barChart.setAlternativeRowFillVisible(false);
			barChart.setAlternativeColumnFillVisible(false);
			barChart.getXAxis().setVisible(false);
			barChart.getYAxis().setVisible(false);
			//barChart.setCenterShape(true);
			//barChart.getYAxis().setTickLabelsVisible(false);
			//barChart.getYAxis().setTickMarkVisible(false);
			

			barChart.setTitle("SAX Presentation");
			lineChart.setTitle("");
		});

	}

	private ObservableList<Series<String, Number>> getSAXFreqData(SAXRecords sAXrecords) {
		ObservableList<XYChart.Series<String, Number>> answer = FXCollections.observableArrayList();
		//Series<String, Number> sAXValuesLine = new Series<>();
		
		Collection<SAXRecord> sAXRecordsCollection = sAXrecords.getRecords();
		System.out.println(" size of collection of sax Records: "+sAXRecordsCollection.size());
		
		for(SAXRecord itr : sAXRecordsCollection){
			System.out.println(" alphabet : "+  String.valueOf(itr.getPayload()));
			Series<String, Number> serie = new Series<>();
			serie.setName(String.valueOf(itr.getPayload()));
			
			for(Integer idx : sAXrecords.getAllIndices()){
				if((String.valueOf(sAXrecords.getByIndex(idx).getPayload()).equals(String.valueOf(itr.getPayload())))){
					serie.getData().add(new XYChart.Data<String, Number>(String.valueOf(idx), 2));
				}
			}
			answer.add(serie);
		}
		
//		ArrayList<Integer> indices =  sAXrecords.getAllIndices();
//
//		for (Iterator<SAXRecord> i = sAXRecordsCollection.iterator(); i.hasNext(); ){
//			i.next();
//		}
//			
//		Iterator<SAXRecord> itr = sAXRecordsCollection.iterator();
//		
//		for(int i = 0 ; i < sAXRecordsCollection.size() ; i ++){
//			//SAXRecord record = 
//		}
//		for(Integer idx : indices){
//			//System.out.println(" Index Length : "+indices.size()+" ; X : "+idx+" Y: "+timeSampleData.get(idx));
//			System.out.println(idx + ", " + String.valueOf(sAXrecords.getByIndex(idx).getPayload()));
//			sAXValuesLine.getData().add(new XYChart.Data<String, Number>(String.valueOf(sAXrecords.getByIndex(idx).getPayload()), 2));
//		}
//		
//		answer.add(sAXValuesLine);
		return answer;
	}

	public SAXRecords generateDataForPlot() {
		Model model = new Model();
		Integer size = Integer.valueOf(numberOfTimeSamplesTxt.getText());
		ArrayList<Double> timeSampleData = model.generateRandomNumbers(size);
		String alphabet = sAXAlphabetTxt.getText();
		Integer alphabetSAX = Integer.valueOf(alphabet);
		System.out.println(" size of alphabet : " + alphabetSAX);
		Integer windowSize = Integer.valueOf(sizeOfWindowTxt.getText());
		boolean showPlot = true;
		try {
			SAXRecords timeSeriesSax = model.generateTimeSeriesSAX(timeSampleData, windowSize, alphabetSAX, showPlot);
			return timeSeriesSax;
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ObservableList<XYChart.Series<String, Double>> getChartData() {
		double javaValue = 17.56;
		double cValue = 17.06;
		double cppValue = 8.25;
		ObservableList<XYChart.Series<String, Double>> answer = FXCollections.observableArrayList();
		Series<String, Double> java = new Series<>();
		Series<String, Double> c = new Series<>();
		Series<String, Double> cpp = new Series<>();
		java.setName("java");
		c.setName("C");
		cpp.setName("C++");
		for (int i = 2011; i < 2021; i++) {
			java.getData().add(new XYChart.Data(Integer.toString(i), javaValue));
			javaValue = javaValue + 4 * Math.random() - .2;

			c.getData().add(new XYChart.Data(Integer.toString(i), cValue));
			cValue = cValue + 4 * Math.random() - 2;

			cpp.getData().add(new XYChart.Data(Integer.toString(i), cppValue));
			cppValue = cppValue + 4 * Math.random() - 2;
		}
		answer.addAll(java, c, cpp);
		return answer;
	}

	private ObservableList<XYChart.Series<Number, Number>> getTimeSeriesData(SAXRecords sAXrecords,
			ArrayList<Double> timeSampleData) {

		ObservableList<XYChart.Series<Number, Number>> answer = FXCollections.observableArrayList();
		Series<Number, Number> timeSeriesValues = new Series<>();
		//timeSeriesValues.setName("Time Serie");
		timeSeriesValues.setName(null);
		// System.out.println(sAXrecords.getIndexes().size());
		for (int i = 0; i < timeSampleData.size(); i++) {
			timeSeriesValues.getData().add(new XYChart.Data<Number, Number>(i, timeSampleData.get(i)));
		}

		answer.add(timeSeriesValues);
		return answer;

	}
}
