package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChartsController implements Initializable {


    @FXML
    public LineChart<Double,Double> chart_razgon;
    private Stage stage;
    XYChart.Series series1;
    XYChart.Series series2;
    XYChart.Series series3;
    XYChart.Series series4;
    XYChart.Series series5;
    XYChart.Series series6;
    XYChart.Series series7;
    XYChart.Series series8;
    XYChart.Series series9;



    public void start() throws IOException {

        stage.showAndWait();
    }

    public void setGraphsData(ArrayList<Double> x_plot, ArrayList<Double> yv_plot, ArrayList<Double> yx_plot, ArrayList<Double> ydx_plot,
                              ArrayList<Double> yxm_plot, ArrayList<Double> ydxm_plot, double minX, double maxX, ArrayList<Double> ex_plot,
                              ArrayList<Double> edx_plot, ArrayList<Double> rx_plot, ArrayList<Double> rdx_plot) {
        series1 = new XYChart.Series();
        series1.setName("Скорость маховика");
        series2 = new XYChart.Series();
        series2.setName("ДСДУПБЦВМ");
        series3 = new XYChart.Series();
        series3.setName("Интеграл Эйлера по углу");
        series4 = new XYChart.Series();
        series4.setName("ДСДУСБЦВМ");
        series5 = new XYChart.Series();
        series5.setName("Интеграл Эйлера по угловой скорости");
        series6 = new XYChart.Series();
        series6.setName("Угол отклонения спутника");
        series7 = new XYChart.Series();
        series7.setName("Угловая скорость спутника");
        series8 = new XYChart.Series();
        series8.setName("Ошибочное знач угла");
        series9 = new XYChart.Series();
        series9.setName("Ошибочное знач угл скорости");

        for (int i = (int) minX; i < maxX; ++i) {
            series1.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yv_plot.get(i)));
            series2.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yxm_plot.get(i)));
//            series3.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ex_plot.get(i)));
            series4.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ydxm_plot.get(i)));
//            series5.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), edx_plot.get(i)));
            series6.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yx_plot.get(i)));
            series7.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ydx_plot.get(i)));
            series8.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), rx_plot.get(i)));
            series9.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), rdx_plot.get(i)));

        }
        chart_razgon.getData().clear();
        chart_razgon.getData().addAll(series1,  series2,  series4, series6, series7);

        chart_razgon.setHorizontalGridLinesVisible(false);
        chart_razgon.setVerticalGridLinesVisible(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chart_razgon.setTitle("Graph");
        chart_razgon.setCreateSymbols(false);
    }

    public void setCompareErData(double minX, double maxX, ArrayList<Double> x_plot, ArrayList<Double> yx_plot,
                                  ArrayList<Double> rx_plot, ArrayList<Double> ydx_plot, ArrayList<Double> rdx_plot,
                                  ArrayList<Double> yxm_plot, ArrayList<Double> ydxm_plot, ArrayList<Double> ex_plot, ArrayList<Double> edx_plot) {
        series2 = new XYChart.Series();
        series2.setName("ДУП");
        series3 = new XYChart.Series();
        series3.setName("Интеграл ДУС по Эйлеру");
        series4 = new XYChart.Series();
        series4.setName("ДУС");
        series5 = new XYChart.Series();
        series5.setName("Производная ДУП по Эйлеру");
        series6 = new XYChart.Series();
        series6.setName("Угол отклонения спутника");
        series7 = new XYChart.Series();
        series7.setName("Угловая скорость спутника");
        series8 = new XYChart.Series();
        series8.setName("Угол эйлера");
        series9 = new XYChart.Series();
        series9.setName("Угл скорость эйлера");
        for (int i = (int) minX; i < maxX; ++i) {
            series2.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yx_plot.get(i)));
            series3.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ex_plot.get(i)));
            series4.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ydx_plot.get(i)));
            series5.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), edx_plot.get(i)));
            series6.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), rx_plot.get(i)));
            series7.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), rdx_plot.get(i)));
            series8.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ex_plot.get(i)));
            series9.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), edx_plot.get(i)));
        }
        chart_razgon.getData().clear();
        chart_razgon.getData().addAll(series2, series3, series4, series5, series6, series7);

        chart_razgon.setHorizontalGridLinesVisible(false);
        chart_razgon.setVerticalGridLinesVisible(false);
    }
}
