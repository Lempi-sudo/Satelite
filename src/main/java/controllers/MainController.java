package controllers;

import FXUtils.Loader;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.AngularMotion;
import model.DUP;
import model.DUS;
import model.Satellite;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import sun.rmi.runtime.Log;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
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
    private DUP dup;
    private DUS dus;
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
    private double i = 0; // счётчик
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

    //returned type of the error from DUP
    private String errorDetectionModuleDUP(double elX, double curX, double curDx, double t) {
        if (t > 0) {
            if (dupFailure) {
//            if (Math.abs(curX) < 0.0001 && Math.abs(elX - curX) > 0.01 && curDx > 0.01){
//                System.out.println("dupFailure");
                return "dupFailure";
            }
            if(dupStick) {
//            if (Math.abs(elX - curX) < 0.0001 && curDx > 0.01){
//                System.out.println("dupStick");
                return "dupStick";
            }
            if (dupPomeh) {
//            if (Math.abs(elX - curX) > 0.02 && curDx < 0.001){
//                System.out.println("dupPomeh");
                return "dupPomeh";
            }
        }
        return "";
    }

    //returned type of the error from DUS
    private String errorDetectionModuleDUS(double elDx, double curDx, double prevX, double curX,  double t) {
        if (t > 0) {
//            if (Math.abs(curDx) < 0.00001 && Math.abs(prevX - curX) > 0.01) {
            if (dusFailure) {
//                System.out.println("dusFailure");
                return "dusFailure";
            }
//            if (Math.abs(prevDx - curDx) < 0.0001 && (Math.abs(prevX - curX) - deltaX) < 0.001) {
            if (dusStick) {
//                System.out.println("dusStick: " + t);
                deltaX = Math.abs(prevX-curX);
                return "dusStick";
            }
//            if (Math.abs(prevDx - curDx) > 0.2 ) {
            if (dusPomeh) {
//                System.out.println("dusPomeh");
                return "dusPomeh";
            }
        }
        return "";
    }

    private void calculate() {
        characteristicsIsChanged = false;
        clearAllValues();
        saveNeshtatAction(new ActionEvent());

        while (am.getW() * satellite.getJ_m() < satellite.getH_max() && t <= 4000 && Math.abs(am.getDx()) < 1.5) {
            am.setT(t);
            errorFilteringUnit();


            ex = x_next;  // сохраняем значение угла, вычисленного методом Эйлера
            edx = dx_next; // сохраняем значение угловой скорости, вычисленной по методу Эйлера
//            ex = am.getNormalX();
//            edx = am.getNormalDx();
            if (t == check_T) { // если пришло время съёма значений
                valuePickupUnit(curX, curDx);
                check_T += T; // задаем следующее время съёма значений
            }

            recordingValuesUnit(curX, curDx);

            accelerationImpactUnit(curX);
            if (t >= 73 && t < 77) {
                M_f_plot.add(am.getrM_f());
            }
            t += dt; // следующий шаг времени
            i += 1; // следующий цикл
            if (t == p_err_t) {
                System.out.println("as");
            }
        }
        countDupStick = 0;
        countDusStick = 0;
        while (am.getW() > 0 && t <= 1500 && Math.abs(am.getDx()) < 1.5) { // # пока скорость маховика > 0

            errorFilteringUnit();

            ex = x_next;// сохраняем значение угла, вычисленного методомЭйлера
            edx = dx_next;// сохраняем значение угловой скорости,вычисленной по методу Эйлера
//            ex = am.getNormalX();
//            edx = am.getNormalDx();
            if (t == check_T) { // если пришло время съёма значений
                valuePickupUnit(curX, curDx);
                check_T += T; //задаем следующее время съёма значений
            }

            recordingValuesUnit(curX, curDx);

            brackingImpactUnit(curX, curDx);

            t += dt; // следующий шаг времени
            i += 1; // следующий цикл
            if (t == p_err_t) {
                System.out.println("as");
            }
        }
        for (int i = 0; i < M_f_plot.size(); i++) {
            System.out.println(M_f_plot.get(i));
        }
    }

    public void recordingValuesUnit(double curX, double curDx) {
        x_plot.add(i); // добавили значение для OX
        yv_plot.add(am.getV()); // добавили значение для линейнойскорости маховика

        if (p_err) {
            yx_plot.add(am.getDup());
            ydx_plot.add(am.getNormalDx());
        } else if (s_err) {
            yx_plot.add(am.getNormalX());
            ydx_plot.add(am.getDus());
        } else {
            yx_plot.add(am.getDup());
            ydx_plot.add(am.getDus());
        }


        yxm_plot.add(xm);
        ydxm_plot.add(dxm);

        if (!isFilterEnabled.isSelected()) {
            rx_plot.add(am.getDisXbc());
            rdx_plot.add(am.getDisDXbc());
        } else {
            if (dupStick && countDupStick < 3) {
                rx_plot.add(am.getDisXbc());
            } else {
                rx_plot.add(am.getDiscreteX());
            }
            if (dusStick && countDusStick < 3) {
                rdx_plot.add(am.getDisDXbc());
            } else {
                rdx_plot.add(am.getDiscreteDx());
            }
        }

        ex_plot.add(ex); // добавили значение угла, рассчитанное по Эйлеру
        edx_plot.add(edx); // добавили значение угл. скорости, рассчитанное по Эйлеру
    }

    public void brackingImpactUnit(double curX, double curDx) {
        if (!p_err && p_err_t >= 0 && p_err_t <= 1500 && t >= p_err_t) { // если нет ошибки ДУП и пришло время ошибки
            p_err = true; //ошибка ДУП
            if (!isSetDUPStickValue && selectedError().equals("dupStick")) {
                saveDUPStickValue(curX);
                isSetDUPStickValue = true;
            }
            am.setError(selectedError(), dupStickValue);
            am.rotationSatelliteOnBracking(satellite);

            if (isFilterEnabled.isSelected()) {
                if (countDupStick < 3)
                    am.rRotationSatelliteOnBracking(satellite, false);
                else
                    am.rRotationSatelliteOnBracking(satellite, true);
            } else {
                am.rRotationSatelliteOnBracking(satellite, false);
            }

        } else if (!s_err && s_err_t >= 0 && s_err_t <= 4000 && t >= s_err_t) { // если нет ошибки ДУС и пришло время ошибки
            s_err = true; // ошибка ДУС
            if (!isSetDUSStickValue && selectedError().equals("dusStick")) {
                saveDUSStickValue(curDx);
                isSetDUSStickValue = true;
            }
            am.setError(selectedError(), dusStickValue);
            am.rotationSatelliteOnBracking(satellite);

            if (isFilterEnabled.isSelected()) {
                if (countDupStick < 3)
                    am.rRotationSatelliteOnBracking(satellite, false);
                else
                    am.rRotationSatelliteOnBracking(satellite, true);
            } else {
                am.rRotationSatelliteOnBracking(satellite, false);
            }

        } else {
            am.rotationSatelliteOnBracking(satellite);
            am.rRotationSatelliteOnBracking(satellite, false);
        }

        am.brakingFlywheel(satellite); // производим торможение маховика
        if (isFilterEnabled.isSelected()) {
            if (countDupStick < 3 && countDusStick < 3)
                am.rBrakingFlywheel(satellite, false);
            else
                am.rBrakingFlywheel(satellite, true);
        } else {
            am.rBrakingFlywheel(satellite, false);
        }

    }

    public void accelerationImpactUnit(double curX) {
        if (!p_err && p_err_t >= 0 && p_err_t <= 1500 && t >= p_err_t) {
            p_err = true;
            if (!isSetDUPStickValue && selectedError().equals("dupStick")) {
                dupStickValue = curX;
                isSetDUPStickValue = true;
            }
            am.setError(selectedError(), dupStickValue);
            am.rotationSatelliteOnAcceleration(satellite); // производим вращение спутника
            if (!isFilterEnabled.isSelected()) {
                am.rRotationSatelliteOnAcceleration(satellite, false);
            } else if (countDupStick < 3)
                am.rRotationSatelliteOnAcceleration(satellite, false); // производим вращение спутника
            else am.rRotationSatelliteOnAcceleration(satellite, true);
        } else if (!s_err && s_err_t >= 0 && s_err_t <= 1500 && t >= s_err_t) { //если нет ошибки ДУС и пришло время ошибки
            s_err = true;
            if (!isSetDUSStickValue && selectedError().equals("dusStick")) {
                isSetDUSStickValue = true;
                dusStickValue = am.getDx();
            }
            am.setError(selectedError(), dusStickValue);
            am.rotationSatelliteOnAcceleration(satellite); // производим вращение спутника
            if (!isFilterEnabled.isSelected()) {
                am.rRotationSatelliteOnAcceleration(satellite, false);
            } else if (countDusStick < 3)
                am.rRotationSatelliteOnAcceleration(satellite, false); // производим вращение спутника
            else am.rRotationSatelliteOnAcceleration(satellite, true);
        } else {
            am.rotationSatelliteOnAcceleration(satellite); // производим вращение спутника
            am.rRotationSatelliteOnAcceleration(satellite, false); // производим вращение спутника
        }

        am.accelerationFlywheel(satellite); // производим ускорение маховика

        if (isFilterEnabled.isSelected()) {
            if (countDupStick < 3 && countDusStick < 3)
                am.rAccelerationFlywheel(satellite, false);
            else
                am.rAccelerationFlywheel(satellite, true);
        } else {
            am.rAccelerationFlywheel(satellite, false);
        }

    }

    public void errorFilteringUnit() {
        curX = am.getNormalX();
        curDx = am.getNormalDx();
        String errorType = errorDetectionModuleDUP(am.getPrevX(), curX, curDx, t);
//        if (!errorType.equals("")) {
            switch (errorType) {
                case "dupFailure": {
                    x_next = ex + dt * edx;
                    break;
                }
                case "dupStick": {
                    x_next = ex + dt * edx;
//                        if (countTDUPError <= 2) {
//                    x_next = curX;
//                        } else {
//                            x_next = ex + dt * edx;
//                        }
                    p_err_value = curX;
                    break;
                }
                case "dupPomeh": {
//                    x_next = am.getPrevX();
                    x_next = ex + dt * edx;
                    break;
                }
                default: {
                        x_next = curX;
                    break;
                }
            }
//        } else {
            errorType = errorDetectionModuleDUS(am.getPrevDx(), curDx, am.getPrevX(), curX, t);
//            if (!errorType.equals("")) {
                switch (errorType) {
                    case "dusFailure": {
                        dx_next = (x_next - ex) / dt;
                        break;
                    }
                    case "dusStick": {
//                            if (countTDUSError <= 2) {
//                        dx_next = curDx;
//                            } else {
                                dx_next = (x_next - ex) / dt;
//                            }
                        s_err_value = curDx;
                        break;
                    }
                    case "dusPomeh": {
//                        dx_next = am.getPrevDx();
                        dx_next = (x_next - ex) / dt;

                        break;
                    }
                    default: {
                            dx_next = curDx;
                        break;
                    }
                }
//            }
//            else {
//                x_next = curX;
//                dx_next = curDx;
//            }
//        }
    }

    public void valuePickupUnit(double curX, double curDx) {
        if (!p_err && !s_err) { //сохраняем угол с датчика, если нет ошибки, иначе берём "Эйлеровский"
//                    prevXm = xm;
            xm = curX;
        } else {
            if (!isFilterEnabled.isSelected()) {
                xm = am.getRx();
            } else {
                if (dupStick && countDupStick < 3) {
                    countDupStick++;
//                        xm = prevXm;
                } else if (dupStick && countDupStick == 3) {
//                        prevXm = xm;
                    xm = am.getNormalX();
                } else {
//                        prevXm = xm;
                    xm = am.getNormalX();
                }
            }
        }
        if (!s_err && !p_err) { //сохраняем угловую скорость с датчика, если нет ошибки, иначе берем"Эйлеровскую"
//                    prevDxm = dxm;
            dxm = curDx;
        } else {
            if (!isFilterEnabled.isSelected()) {
                dxm = am.getRdx();
            } else {
                if (dusStick && countDusStick < 3) {
                    countDusStick++;
//                        dxm = prevDxm;
                } else if (dusStick && countDusStick == 3) {
//                        prevDxm = dxm;
                    dxm = am.getNormalDx();
                } else {
//                        prevDxm = dxm;
                    dxm = am.getNormalDx();
                }
            }
        }
        am.setDiscreteRx();
        am.setDiscreteRdx();
        am.setDiscreteX();
        am.setDiscreteDx();
        am.setDisXbc();
        am.setDisDXbc();
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
        rx_plot.clear();
        rdx_plot.clear();
        countDupStick = 0;
        countDusStick = 0;
    }

    public void saveNeshtatAction(ActionEvent actionEvent) {
        p_err_t = Double.valueOf(tf_DUP_time.getText());
        s_err_t = Double.valueOf(tf_DUS_time.getText());
        if (s_err_t > 0) {
            if (rb_DUS_Failure.isSelected()) {
                dusFailure = true;
                dusFailureValue = 1 - (Math.random() / 10000 + 0.9999);
            } else if (rb_DUS_sticking.isSelected()) dusStick = true;
            else if (rb_DUS_pomeh.isSelected()) dusPomeh = true;
        }
        if (p_err_t > 0) {
            if (rb_DUP_Failure.isSelected()) {
                dupFailure = true;
                dupFailureValue = 1 - (Math.random() / 10000 + 0.9999);
            } else if (rb_DUP_sticking.isSelected()) dupStick = true;
            else if (rb_DUP_pomeh.isSelected()) dupPomeh = true;
        }
        characteristicsIsChanged = true;
        am.setP_err_t(p_err_t);
        am.setS_err_t(s_err_t);
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
        System.out.println("wtf: dup = " + dupFailure + ";");
        if (chartsStage.getOwner() == null) {
            chartsStage.initOwner(primaryStage);
        }
        if (!cmpEr)
            chartsController.setGraphsData(x_plot, yv_plot, yx_plot, ydx_plot, yxm_plot, ydxm_plot, minX, maxX, ex_plot, edx_plot, rx_plot, rdx_plot);
        else
            chartsController.setCompareErData(minX, maxX, x_plot, yx_plot, rx_plot, ydx_plot, rdx_plot, yxm_plot, ydxm_plot, ex_plot, edx_plot);
        chartsController.start();
    }

    private String selectedError() {
        if (rb_DUS_Failure.isSelected()) return "dusFailure";
        else if (rb_DUS_sticking.isSelected()) return "dusStick";
        else if (rb_DUS_pomeh.isSelected()) return "dusPomeh";
        if (rb_DUP_Failure.isSelected()) return "dupFailure";
        else if (rb_DUP_sticking.isSelected()) return "dupStick";
        else if (rb_DUP_pomeh.isSelected()) return "dupPomeh";
        return "no";
    }

    public void showActionGraph(ActionEvent actionEvent) {
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

    private void saveDUSStickValue(double errorValue) {
        dusStickValue = errorValue;
    }

    private void saveDUPStickValue(double errorValue) {
        dupStickValue = errorValue;
    }


    public void showErSection(ActionEvent actionEvent) {
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
