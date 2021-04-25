package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChartsController implements Initializable {
// графики строит
    private double T = 1;
    @FXML
    public LineChart<Double,Double> lineChart;
    private Stage stage;
    XYChart.Series series1;
    XYChart.Series series2;
    XYChart.Series series3;
    XYChart.Series series4;
    XYChart.Series series5;
    XYChart.Series series6;
    XYChart.Series series7;

    public void start() throws IOException {

        stage.showAndWait();
    }

    public void setGraphsData(ArrayList<Double> x_plot, ArrayList<Double> yv_plot,
                              ArrayList<Double> yx_plot, ArrayList<Double> ydx_plot,
                              ArrayList<Double> yxm_plot, ArrayList<Double> ydxm_plot,
                              double minX, double maxX, ArrayList<Double> ex_plot,
                              ArrayList<Double> edx_plot) {
        series1 = new XYChart.Series();
        series1.setName("Скорость маховика");
        series2 = new XYChart.Series();
        series2.setName("ДСДУПБЦВМ");
        series3 = new XYChart.Series();
        series3.setName("Производная ДУП");
        series4 = new XYChart.Series();
        series4.setName("ДСДУСБЦВМ");
        series5 = new XYChart.Series();
        series5.setName("Интеграл ДУС");
        series6 = new XYChart.Series();
        series6.setName("Угол отклонения спутника");
        series7 = new XYChart.Series();
        series7.setName("Угловая скорость спутника");

        int check_T = (int) minX;

        int k = (int) minX;
        int count = 1;
        int step = 2;

        int patam=50;

        for (int i = (int) minX; i < maxX; ++i) {
            if (Math.abs(yv_plot.get(i)) < patam)
                series1.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yv_plot.get(i)));
            if (Math.abs(yxm_plot.get(i)) < patam)
                series2.getData().add(new XYChart.Data(String.valueOf(x_plot.get(k)), yxm_plot.get(i)));
            if (i == check_T) {
                if (Math.abs(edx_plot.get((int) (check_T/T))) < patam)
                    series5.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), edx_plot.get((int) (check_T/T))));
                if (Math.abs(ex_plot.get((int) (check_T/T))) < patam)
                    series3.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), 3*ex_plot.get((int) (check_T/T))));
                check_T += T;
            }
            if (Math.abs(ydxm_plot.get(i)) < patam)
                series4.getData().add(new XYChart.Data(String.valueOf(x_plot.get(k)), 3*ydxm_plot.get(i)));
            if (Math.abs(yx_plot.get(i)) < patam)
                series6.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yx_plot.get(i)));
            if (Math.abs(ydx_plot.get(i)) < patam)
                series7.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), 3*ydx_plot.get(i)));

            if (count % 2 != 0 && k < maxX) k += step;
            count++;

        }
        lineChart.getData().clear();
        lineChart.getData().addAll(series1, series2, series3, series4, series5, series6, series7);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lineChart.setTitle("Graph");
        lineChart.setCreateSymbols(false);
    }

    public void setCompareErData(double minX, double maxX, ArrayList<Double> x_plot,
                                 ArrayList<Double> yx_plot, ArrayList<Double> ydx_plot,
                                  ArrayList<Double> yxm_plot, ArrayList<Double> ydxm_plot,
                                 ArrayList<Double> ex_plot, ArrayList<Double> edx_plot) {
        series2 = new XYChart.Series();
        series2.setName("ДСДУПБЦВМ");
        series3 = new XYChart.Series();
        series3.setName("Производная ДУП");
        series4 = new XYChart.Series();
        series4.setName("ДСДУСБЦВМ");
        series5 = new XYChart.Series();
        series5.setName("Интеграл ДУС");
        series6 = new XYChart.Series();
        series6.setName("Угол отклонения спутника");
        series7 = new XYChart.Series();
        series7.setName("Угловая скорость спутника");
        int check_T = (int) minX;
        for (int i = (int) minX; i < maxX - T; ++i) {
            if (Math.abs(yxm_plot.get(i)) < 0.5)
                series2.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yxm_plot.get(i)));
            if (i == check_T) {
                if (edx_plot.get((int) (check_T/T)) < 0.5)
                    series5.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), edx_plot.get((int) (check_T/T))));
                if (Math.abs(ex_plot.get((int) (check_T/T))) < 0.5)
                    series3.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ex_plot.get((int) (check_T/T))));
                check_T += T;
            }
            if (Math.abs(ydxm_plot.get(i)) < 0.5)
                series4.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ydxm_plot.get(i)));
            if (yx_plot.get(i) < 0.5) {
                series6.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), yx_plot.get(i)));
            }
            if (Math.abs(ydx_plot.get(i)) < 0.5)
                series7.getData().add(new XYChart.Data(String.valueOf(x_plot.get(i)), ydx_plot.get(i)));
        }
        lineChart.getData().clear();
        lineChart.getData().addAll(series2, series3, series4, series5, series6, series7);
        lineChart.setHorizontalGridLinesVisible(false);
        lineChart.setVerticalGridLinesVisible(false);
    }

    public void setT(double T) {
        this.T = T;
    }
}
