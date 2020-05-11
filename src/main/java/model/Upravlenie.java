//package model;
//
//import javafx.event.ActionEvent;
//
//import java.util.ArrayList;
//
//public class Upravlenie {
//    private Satellite satellite;
//    private DUP dup;
//    private DUS dus;
//    private ArrayList<Double> x_plot = new ArrayList<>(); // значения для OX
//    private ArrayList<Double> yv_plot = new ArrayList<>(); // значения для линейной скорости
//    private ArrayList<Double> yx_plot = new ArrayList<>(); // значения угла
//    private ArrayList<Double> ydx_plot = new ArrayList<>(); // значения угловой скорости
//    private ArrayList<Double> yxm_plot = new ArrayList<>(); // значения угла с имитацией дискретности
//    private ArrayList<Double> ydxm_plot = new ArrayList<>(); // значения угловой скорости с имитацией дискретности
//    private ArrayList<Double> ex_plot = new ArrayList<>(); // значения угла по Эйлеру
//    private ArrayList<Double> edx_plot = new ArrayList<>(); // значение угл. скорости по Эйлеру
//    private ArrayList<Double> rx_plot = new ArrayList<>(); // значения угла по Эйлеру
//    private ArrayList<Double> rdx_plot = new ArrayList<>();
//    private AngularMotion am; // объект углового движения
//    private double T = 1; // период дискретизации
//    private double check_T = 0; // время съема для экстраполятора нулевого порядка
//    private boolean p_err = false; // индикатор ошибки ДУП
//    private double p_err_t = -1; // время ошибки ДУП
//    private boolean s_err = false; // индикатор ошибки ДУС
//    private double s_err_t = -1; // время ошибки ДУС
//    private boolean isStick = false; // флаг залипания
//    private double p_err_value = 0; //значения ошибки
//    private double s_err_value = 0; //значения ошибки
//    private double ex = 0; // угол вычисленный по методу Эйлера
//    private double edx = 0; // угловая скорость вычисленная по методу Эйлера
//    private double i = 0; // счётчик
//    private double t = 0; // время
//    private double dt = 1;
//    private double x_next;
//    private double dx_next;
//    private double xm = 0; //угол с датчика
//    private double dxm = 0; //угловая скорость с датчика
//    private double tWhenHmax = 0;
//
//    private boolean dusFailure = false;
//    private boolean dusStick = false;
//    private boolean dusPomeh = false;
//    private boolean dupFailure = false;
//    private boolean dupStick = false;
//    private boolean dupPomeh = false;
//
//    private boolean tmpdusFailure = false;
//    private boolean tmpdusStick = false;
//    private boolean tmpdusPomeh = false;
//    private boolean tmpdupFailure = false;
//    private boolean tmpdupStick = false;
//    private boolean tmpdupPomeh = false;
//
//    private double dupFailureValue = 0;
//    private double dupStickValue = 0;
//    private double dupPomehValue = 0;
//    private double dusFailureValue = 0;
//    private double dusStickValue = 0;
//    private double dusPomehValue = 0;
//    private double deltaX = 0;
//    private boolean isSetDUSStickValue = false;
//    private boolean isSetDUPStickValue = false;
//    int countDupStick = 0;
//    int countDusStick = 0;
//    double curX = 0;
//    double curDx = 0;
//
//    public void setError(String errorType) {
//        switch (errorType) {
//            case "dusFailure": {
//                dusFailure = true;
//                break;
//            }
//            case "dusStick": {
//                dusStick = true;
//                break;
//            }
//            case "dusPomeh": {
//                dusPomeh = true;
//                break;
//            }
//            case "dupFailure": {
//                dupFailure = true;
//                break;
//            }
//            case "dupStick": {
//                dupStick = true;
//                break;
//            }
//            case "dupPomeh": {
//                dupPomeh = true;
//                break;
//            }
//            case "no": {
//                dupFailure = false;
//                dupStick = false;
//                dupPomeh = false;
//                dusFailure = false;
//                dusStick = false;
//                dusPomeh = false;
//                break;
//            }
//        }
//    }
//
//
//    private void calculate() {
////        characteristicsIsChanged = false;
////        clearAllValues();
////        saveNeshtatAction(new ActionEvent());
//
//        while (am.getW() * satellite.getJ_m() < satellite.getH_max() && t <= 1500 && Math.abs(am.getDx()) < 1.5) {
//
////            errorFilteringUnit();
//            if
//
//
//
//            ex = x_next;  // сохраняем значение угла, вычисленного методом Эйлера
//            edx = dx_next; // сохраняем значение угловой скорости, вычисленной по методу Эйлера
//            ex = am.getNormalX();
//            edx = am.getNormalDx();
//            if (t == check_T) { // если пришло время съёма значений
//                valuePickupUnit(curX, curDx);
//                check_T += T; // задаем следующее время съёма значений
//            }
//
//            recordingValuesUnit(curX, curDx);
//
//            accelerationImpactUnit(curX);
//
//            t += dt; // следующий шаг времени
//            i += 1; // следующий цикл
//            if (t == p_err_t) {
//                System.out.println("as");
//            }
//        }
//        countDupStick = 0;
//        countDusStick = 0;
//        while (am.getW() > 0 && t <= 1500 && Math.abs(am.getDx()) < 1.5) { // # пока скорость маховика > 0
//
//            errorFilteringUnit();
//
//            ex = x_next;// сохраняем значение угла, вычисленного методомЭйлера
//            edx = dx_next;// сохраняем значение угловой скорости,вычисленной по методу Эйлера
//            ex = am.getNormalX();
//            edx = am.getNormalDx();
//            if (t == check_T) { // если пришло время съёма значений
//                valuePickupUnit(curX, curDx);
//                check_T += T; //задаем следующее время съёма значений
//            }
//
//            recordingValuesUnit(curX, curDx);
//
//            brackingImpactUnit(curX, curDx);
//
//            t += dt; // следующий шаг времени
//            i += 1; // следующий цикл
//            if (t == p_err_t) {
//                System.out.println("as");
//            }
//        }
//
//    }
//}
