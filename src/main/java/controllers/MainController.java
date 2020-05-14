package controllers;

import FXUtils.Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.AngularMotion;
import model.Errors.*;
import model.Satellite;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public RadioButton rb_DUP_Failure;
    public RadioButton rb_DUS_Failure;
    public RadioButton rb_DUS_sticking;
    public RadioButton rb_DUP_sticking;
    public TextField tf_DUS_time;
    public TextField tf_DUP_time;
    public Button btn_Start;
    public TextField tf_T_time;
    public TextField tf_Max_razgon_mahovika;
    public TextField tf_Koef_razgon_A0;
    public TextField tf_Uskor_tormozh_mahovika;
    public TextField tf_Vozmush_uskor;
    public TextField tf_Koef_Rele_A;
    public TextField tf_Moment_inercii_sputnika;
    public TextField tf_Massa_sputnika;
    public TextField tf_Razmer_sputnika;
    public TextField tf_Moment_inercii_mahovika;
    public TextField tf_Max_kinetich_m_mahovika;
    public TextField tf_Tyaga_URD;
    public TextField tf_Plecho_sili_tyagi;
    public TextField tf_Max_uprvl_moment_mahovika;
    public TextField tf_Radius_mahovika;
    public TextField tf_Koef_razgon_A1;
    public TextField tf_Koef_tormozh_A0;
    public TextField tf_Koef_tormozh_A1;
    public Button btn_Set_Default;
    public Button btnSave;
    public RadioButton rb_DUS_pomeh;
    public RadioButton rb_DUP_pomeh;
    public TextField tf_To;
    public TextField tf_From;
    public CheckBox isFilterEnabled;
    Stage primaryStage;

    private FXMLLoader chartsLoader = new FXMLLoader();
    private Parent chartsView;
    private ChartsController chartsController;
    private Stage chartsStage;
    private boolean characteristicsIsChanged;

    private ArrayList<Double> M_f_plot = new ArrayList<>();
    private Satellite satellite;
    private SensorError sensors;

    private ArrayList<Double> x_plot = new ArrayList<>(); // значения для OX
    private ArrayList<Double> yv_plot = new ArrayList<>(); // значения для линейной скорости
    private ArrayList<Double> yx_plot = new ArrayList<>(); // значения угла
    private ArrayList<Double> ydx_plot = new ArrayList<>(); // значения угловой скорости
    private ArrayList<Double> yxm_plot = new ArrayList<>(); // значения угла с имитацией дискретности
    private ArrayList<Double> ydxm_plot = new ArrayList<>(); // значения угловой скорости с имитацией дискретности
    private ArrayList<Double> ex_plot = new ArrayList<>(); // значения угла по Эйлеру
    private ArrayList<Double> edx_plot = new ArrayList<>(); // значение угл. скорости по Эйлеру
    private ArrayList<Double> rx_plot = new ArrayList<>(); // значения угла по Эйлеру
    private ArrayList<Double> rdx_plot = new ArrayList<>();
    private AngularMotion am; // объект углового движения
    private double T = 1; // период дискретизации
    private double check_T = 0; // время съема для экстраполятора нулевого порядка
    private boolean p_err = false; // индикатор ошибки ДУП
    private double p_err_t = -1; // время ошибки ДУП
    private boolean s_err = false; // индикатор ошибки ДУС
    private double s_err_t = -1; // время ошибки ДУС
    private boolean isStick = false; // флаг залипания
    private double p_err_value = 0; //значения ошибки
    private double s_err_value = 0; //значения ошибки
    private double ex = 0; // угол вычисленный по методу Эйлера
    private double edx = 0; // угловая скорость вычисленная по методу Эйлера
    private int i = 0; // счётчик
    private double t = 0; // время
    private double dt = 1;
    private double x_next;
    private double dx_next;
    private double xm = 0; //угол с датчика
    private double dxm = 0; //угловая скорость с датчика
    private double tWhenHmax = 0;

    private boolean dusFailure = false;
    private boolean dusStick = false;
    private boolean dusPomeh = false;
    private boolean dupFailure = false;
    private boolean dupStick = false;
    private boolean dupPomeh = false;

    private boolean tmpdusFailure = false;
    private boolean tmpdusStick = false;
    private boolean tmpdusPomeh = false;
    private boolean tmpdupFailure = false;
    private boolean tmpdupStick = false;
    private boolean tmpdupPomeh = false;

    private double dupFailureValue = 0;
    private double dupStickValue = 0;
    private double dupPomehValue = 0;
    private double dusFailureValue = 0;
    private double dusStickValue = 0;
    private double dusPomehValue = 0;
    private double deltaX = 0;
    private boolean isSetDUSStickValue = false;
    private boolean isSetDUPStickValue = false;
    int countDupStick = 0;
    int countDusStick = 0;
    double curX = 0;
    double curDx = 0;
    double countDupPom = 0;
    double countDusPom = 0;
    int pickUpCount = 0;

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setDefaultAction(ActionEvent actionEvent) {
        satellite = new Satellite();
        characteristicsIsChanged = true;
    }

    public void saveAction(ActionEvent actionEvent) {
        satellite.setJ_z(Double.valueOf(tf_Moment_inercii_sputnika.getText()));
        satellite.setM_z(Double.valueOf(tf_Massa_sputnika.getText()));
        satellite.setA_z(Double.valueOf(tf_Razmer_sputnika.getText()));
        satellite.setJ_m(Double.valueOf(tf_Moment_inercii_mahovika.getText()));
        satellite.setW_max(Double.valueOf(tf_Max_razgon_mahovika.getText()));
        satellite.setP_rc(Double.valueOf(tf_Tyaga_URD.getText()));
        satellite.setL_m(Double.valueOf(tf_Plecho_sili_tyagi.getText()));
        satellite.setW_1_b(Double.valueOf(tf_Uskor_tormozh_mahovika.getText()));
        satellite.setM_p_(Double.valueOf(tf_Vozmush_uskor.getText()));
        satellite.setR(Double.valueOf(tf_Radius_mahovika.getText()));
        satellite.setA0(Double.valueOf(tf_Koef_razgon_A0.getText()));
        satellite.setA1(Double.valueOf(tf_Koef_razgon_A1.getText()));
        satellite.setA(Double.valueOf(tf_Koef_Rele_A.getText()));
        satellite.setK0(Double.valueOf(tf_Koef_tormozh_A0.getText()));
        satellite.setK1(Double.valueOf(tf_Koef_tormozh_A1.getText()));
        T = Double.valueOf(tf_T_time.getText());
        characteristicsIsChanged = true;
    }

    private void defaultInit() {
        tf_Moment_inercii_sputnika.setText(String.valueOf(satellite.getJ_z()));
        tf_Massa_sputnika.setText(String.valueOf(satellite.getM_z()));
        tf_Razmer_sputnika.setText(String.valueOf(satellite.getA_z()));
        tf_Moment_inercii_mahovika.setText(String.valueOf(satellite.getJ_m()));
        tf_Max_razgon_mahovika.setText(String.valueOf(satellite.getW_max()));
        tf_Tyaga_URD.setText(String.valueOf(satellite.getP_rc()));
        tf_Plecho_sili_tyagi.setText(String.valueOf(satellite.getL_m()));
        tf_Vozmush_uskor.setText(String.valueOf(satellite.getM_p_()));
        tf_Radius_mahovika.setText(String.valueOf(satellite.getR()));
        tf_Koef_razgon_A0.setText(String.valueOf(satellite.getA0()));
        tf_Koef_razgon_A1.setText(String.valueOf(satellite.getA1()));
        tf_Koef_Rele_A.setText(String.valueOf(satellite.getA()));
        tf_Koef_tormozh_A0.setText(String.valueOf(satellite.getK0()));
        tf_Koef_tormozh_A1.setText(String.valueOf(satellite.getK1()));
        tf_Uskor_tormozh_mahovika.setText(String.valueOf(satellite.getW_1_b()));
        tf_DUS_time.setText(String.valueOf(s_err_t));
        tf_DUP_time.setText(String.valueOf(p_err_t));
        tf_T_time.setText(String.valueOf(T));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartsLoader.setLocation(getClass().getResource("/ChartsView.fxml"));
        try {
            chartsView = chartsLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        chartsController = chartsLoader.getController();
        chartsStage = new Loader().initStage("Graph", chartsView, true);
        chartsController.setStage(chartsStage);

        satellite = new Satellite();
        am = new AngularMotion();
        defaultInit();
        ToggleGroup rbDusGroup = new ToggleGroup();
        ToggleGroup rbDupGroup = new ToggleGroup();
        rbDusGroup.getToggles().add(rb_DUS_Failure);
        rbDusGroup.getToggles().add(rb_DUS_sticking);
        rbDusGroup.getToggles().add(rb_DUS_pomeh);
        rbDupGroup.getToggles().add(rb_DUP_Failure);
        rbDupGroup.getToggles().add(rb_DUP_sticking);
        rbDupGroup.getToggles().add(rb_DUP_pomeh);
//        calculate();
//        chartsController.setGraphsData(x_plot, yv_plot, yx_plot, ydx_plot, yxm_plot, ydxm_plot, 0, x_plot.size(), ex_plot, edx_plot);
    }



    private void calculate() {
        characteristicsIsChanged = false;
        clearAllValues();
        am.setTdis(T);
        chartsController.setT(T);
        saveSelectedError(new ActionEvent());
        am.setSensors(sensors);
        am.setFilterIsEnabled(isFilterEnabled.isSelected());
        while (am.getW() * satellite.getJ_m() < satellite.getH_max() && t <= 1350) {
            am.setT(t);
                curX = am.getX();
                curDx = am.getDx();

            if (t == check_T) { // если пришло время съёма значений
                valuePickupUnit();
                check_T += T; // задаем следующее время съёма значений
            }

            recordingValuesUnit();

            am.accelerationFlywheel(satellite); // производим ускорение маховика
            am.rotationSatelliteOnAcceleration(satellite); // производим вращение спутника

            t += dt; // следующий шаг времени
            i += 1; // следующий цикл

        }
        countDupStick = 0;
        countDusStick = 0;
        while (am.getW() > 0 && t <= 1350) { // # пока скорость маховика > 0
            am.setT(t);
            if (t == check_T) { // если пришло время съёма значений
                valuePickupUnit();
                check_T += T; //задаем следующее время съёма значений
            }

            recordingValuesUnit();

            am.brakingFlywheel(satellite); // производим торможение маховика
            am.rotationSatelliteOnBracking(satellite);

            t += dt; // следующий шаг времени
            i += 1; // следующий цикл

        }

    }

    public void recordingValuesUnit() {
        x_plot.add( (double) i); // добавили значение для OX
        yv_plot.add(am.getV()); // добавили значение для линейнойскорости маховика

        yxm_plot.add(am.getDupValue());
        ydxm_plot.add(am.getDusValue());

        yx_plot.add(am.getNormalX());
        ydx_plot.add(am.getNormalDx());
        ex_plot.add(ex); // добавили значение угла, рассчитанное по Эйлеру
        edx_plot.add(edx); // добавили значение угл. скорости, рассчитанное по Эйлеру
    }


    public void valuePickupUnit() {
        am.setDiscreteX();
        am.setDiscreteDx();
        am.doDiffDup();
        am.doIntDus();
        if (isFilterEnabled.isSelected()) {
            am.errorFilteringUnit();
        }
        am.addDiffDup(am.getCurDifDup());
        am.addIntDus(am.getCurIntDus());
    }


    private void clearAllValues() {
        am.clearAllValues();
        ex = 0;
        edx = 0;
        i = 0;
        t = 0;
        xm = 0;
        dxm = 0;
        x_next = 0;
        dx_next = 0;
        p_err_value = 0;
        s_err_value = 0;
        check_T = 0;
        dupFailure = false;
        dupStick = false;
        dupPomeh = false;
        dusFailure = false;
        dusStick = false;
        dusPomeh = false;
        dusFailureValue = 0;
        dusStickValue = 0;
        dusPomehValue = 0;
        dupFailureValue = 0;
        dupStickValue = 0;
        isSetDUPStickValue = false;
        isSetDUSStickValue = false;

        dupPomehValue = 0;
        p_err = false;
        s_err = false;
        x_plot.clear();
        yv_plot.clear();
        yx_plot.clear();
        ydx_plot.clear();
        yxm_plot.clear();
        ydxm_plot.clear();
        ex_plot.clear();
        edx_plot.clear();
        countDupStick = 0;
        countDusStick = 0;
    }

    public void showFullGraph(ActionEvent actionEvent) throws IOException {
            am = new AngularMotion();
            calculate();
        showGraph(0, x_plot.size(), false);
    }

    public void showAccelerationSection(ActionEvent actionEvent) {
            am = new AngularMotion();
            calculate();
        try {
            showGraph(0, x_plot.size()*0.07, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showBrakingSection(ActionEvent actionEvent) {
            am = new AngularMotion();
            calculate();
        try {
            showGraph(x_plot.size()*0.92, x_plot.size(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showGraph(double minX, double maxX, boolean cmpEr) throws IOException {
        System.out.println("wtf: dupFailure = " + dupFailure + ";");
        if (chartsStage.getOwner() == null) {
            chartsStage.initOwner(primaryStage);
        }
        if (!cmpEr)
            chartsController.setGraphsData(x_plot, yv_plot, yx_plot, ydx_plot, yxm_plot, ydxm_plot, minX, maxX, am.getDiffDup(), am.getIntDus());
        else
            chartsController.setCompareErData(minX, maxX, x_plot, yx_plot, ydx_plot, yxm_plot, ydxm_plot, am.getDiffDup(), am.getIntDus());
        chartsController.start();
    }

    @FXML
    private void saveSelectedError(ActionEvent actionEvent) {
        p_err_t = Double.valueOf(tf_DUP_time.getText());
        s_err_t = Double.valueOf(tf_DUS_time.getText());
        if (p_err_t > 0 || s_err_t > 0) {
            if (rb_DUS_Failure.isSelected()) sensors = new DusFailure(am, s_err_t);
            else if (rb_DUS_sticking.isSelected()) sensors = new DusStick(am, s_err_t);
            else if (rb_DUS_pomeh.isSelected()) sensors = new DusPomeh(am, s_err_t);
            else if (rb_DUP_Failure.isSelected()) sensors = new DupFailure(am, p_err_t);
            else if (rb_DUP_sticking.isSelected()) sensors = new DupStick(am, p_err_t);
            else if (rb_DUP_pomeh.isSelected()) sensors = new DupPomeh(am, p_err_t);
        } else sensors = new NormalSensor(am);
        characteristicsIsChanged = true;
    }

    public void showActionGraph(ActionEvent actionEvent) {
        if (tf_From.getText().isEmpty() || (tf_To.getText()).isEmpty()) return;
        double from = Double.valueOf(tf_From.getText());
        double to = Double.valueOf((tf_To.getText()));
            am = new AngularMotion();
            calculate();
        try {
            showGraph(from, to, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showErSection(ActionEvent actionEvent) {
        if (tf_From.getText().isEmpty() || (tf_To.getText()).isEmpty()) return;
        double from = Double.valueOf(tf_From.getText());
        double to = Double.valueOf((tf_To.getText()));
        System.out.println(isFilterEnabled.isSelected());
            am = new AngularMotion();
            calculate();
        try {
            showGraph(from, to, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
