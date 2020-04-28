package model;

import static java.lang.Math.abs;

public class AngularMotion {

    private double x = 0.01; // начальное отклонение угла
    private double dx = 0; // начальная угловая скорость
    private double w = 0 ; // начальная угловая скорость маховика
    private double v = 0; // начальная линейная скорость маховика
    private double dw;
    private double M_f;
    private double dt = 1;
    private double fi;
    private double f;

    private double ex = 0.01; // начальное отклонение угла
    private double edx = 0; // начальная угловая скорость
    private double ew = 0 ; // начальная угловая скорость маховика
    private double ev = 0; // начальная линейная скорость маховика
    private double edw;
    private double eM_f;
    private double edt = 1;
    private double efi;
    private double ef;

    private double prevX = 0;
    private double prevDx = 0;
    private boolean dusFailure = false;
    private boolean dusStick = false;
    private boolean dusPomeh = false;
    private boolean dupFailure = false;
    private boolean dupStick = false;
    private boolean dupPomeh = false;
    private double dupFailureValue = 0;
    private double dupStickValue = 0;
    private double dupPomehValue = 0;
    private double dusFailureValue = 0;
    private double dusStickValue = 0;
    private double dusPomehValue = 0;
    private double discreteX;
    private double discreteDx;

    public void setDiscreteX() {
        this.discreteX = this.x;
    }

    public void setDiscreteDx() {
        this.discreteDx = this.dx;
    }

    public double getDiscreteX() {
        return discreteX;
    }

    public double getDiscreteDx() {
        return discreteDx;
    }

    public void clearAllValues() {
        prevX = 0;
        prevDx = 0;
        M_f = 0;
        fi = 0;
        f = 0;
        dw = 0;
        w = 0;
        v = 0;
        x = 0;
        dx = 0;
        dusFailure = false;
        dusStick = false;
        dusPomeh = false;
        dupFailure = false;
        dupStick = false;
        dupPomeh = false;
    }

    public AngularMotion(Satellite satellite) {
//        w = (satellite.getA0() * this.x + satellite.getA1() * this.dx);
//        v = this.w * satellite.getR();
    }

    public void accelerationFlywheel(Satellite satellite) { //функция участка разгона
       // угловое ускорение маховика равно ... или 0, если достигнута максимальная скрость
        if (w <= satellite.getW_max()) {
            dw = satellite.getA0() * this.discreteX + satellite.getA1() * this.discreteDx;
        } else {
            dw = 0; // по формуле (4)
        }
        this.w += dw * dt; //вычисление угловой скорости маховика по угловому ускорению
        this.v = this.w * satellite.getR(); // вычисление линейной скорости маховика
        M_f = satellite.getJ_m() * dw ; // момент маховика
//        this.x += dx * dt; // по "жёлтой" формуле X1(n+1)
//        this.dx += dt * (satellite.getM_p_() - (M_f / satellite.getJ_z())); // по "жёлтой" формуле X2(n+1) для уравнения (3)
    }

    public void rotationSatelliteOnAcceleration(Satellite satellite, String error, double errorValue) {
        //todo В этот метод передать флаг ошибки и if true изменять ошибочно значения согласно ошибке
        switch (error) {
            case "dusFailure": {
//                dusFailureValue = errorValue;
                dusFailure = true;
                break;
            }
            case "dusStick": {
                dusStickValue = errorValue;
                dusStick = true;
                break;
            }
            case "dusPomeh": {
//                dusPomehValue = errorValue;
                dusPomeh = true;
                break;
            }
            case "dupFailure": {
//                dupFailureValue = errorValue;
                dupFailure = true;
                break;
            }
            case "dupStick": {
                dupStickValue = errorValue;
                dupStick = true;
                break;
            }
            case "dupPomeh": {
//                dupPomehValue = errorValue;
                dupPomeh = true;
                break;
            }
            default: {

                break;
            }
        }
        prevX = this.x;
        prevDx = this.dx;
        this.x += dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() - (M_f / satellite.getJ_z()));
    }



    public void eylerRotateOnAcc(Satellite satellite) {

    }

    public void eylerRotateOnBr(Satellite satellite) {

    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevDx() {
        return prevDx;
    }

    public void brakingFlywheel(Satellite satellite) { // функция участка торможения
        this.w -= satellite.getW_1_b() * dt ; // вычисление угловой скорости маховикапо угловому ускорению торможения
        this.v = this.w * satellite.getR() ; // вычисление линейной скорости маховика
        fi = satellite.getK0() * this.discreteX + satellite.getK1() * this.discreteDx ; // вычисление фи по формуле (7)
        // релейная функция, если фи по модулю > a, то 1/-1, в зависимости от знака фи, иначе 0
        if (abs(fi) > satellite.getA()) {
            f = Math.copySign(1, fi);
        } else {
            f = 0;
        }
//        this.x += this.dx * dt ; // по "жёлтой" формуле X1(n+1)
//        this.dx += dt * (satellite.getM_p_() + satellite.getM_b_() - satellite.getM_rc_() * f) ; // по "жёлтой" формуле X2(n+1) для уравнения (5)
    }

    public void rotationSatelliteOnBracking(Satellite satellite, String error, double errorValue) {
        //todo В этот метод передать флаг ошибки и if true изменять ошибочно значения согласно ошибке

        switch (error) {
            case "dusFailure": {
//                dusFailureValue = errorValue;
                dusFailure = true;
                break;
            }
            case "dusStick": {
                dusStickValue = errorValue;
                dusStick = true;
                break;
            }
            case "dusPomeh": {
//                dusPomehValue = errorValue;
                dusPomeh = true;
                break;
            }
            case "dupFailure": {
//                dupFailureValue = errorValue;
                dupFailure = true;
                break;
            }
            case "dupStick": {
                dupStickValue = errorValue;
                dupStick = true;
                break;
            }
            case "dupPomeh": {
//                dupPomehValue = errorValue;
                dupPomeh = true;
                break;
            }
            default: {
                 // по "жёлтой" формуле X2(n+1) для уравнения (5)
                break;
            }
        }
        prevX = this.x;
        prevDx = this.dx;
        this.x += this.dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() + satellite.getM_b_() - satellite.getM_rc_() * f);
    }


    public double getX() {
        if (dupFailure) return (1 - (Math.random()/10000 + 0.9999));
        else if (dupStick) return dupStickValue;
        else if (dupPomeh) {
            dupPomeh = false;
            return prevX*(Math.random() + 1.3);
        }
        else return this.x;
    }


    public void setX(double x) {
        this.x = x;
    }

    public boolean isDusFailure() {
        return dusFailure;
    }

    public boolean isDusStick() {
        return dusStick;
    }

    public boolean isDusPomeh() {
        return dusPomeh;
    }

    public boolean isDupFailure() {
        return dupFailure;
    }

    public boolean isDupStick() {
        return dupStick;
    }

    public boolean isDupPomeh() {
        return dupPomeh;
    }

    public double getDx() {
        if (dusFailure) return (1 - (Math.random()/10000 + 0.9999));
        else if (dusStick) return dusStickValue;
        else if (dusPomeh) {
            dusPomeh = false;
            return prevDx*(Math.random() + 3);
        }
        else return this.dx;
    }

    public double getNormalX() {
        return this.x;
    }

    public double getNormalDx() {
        return this.dx;
    }

    public void setDusFailure(boolean dusFailure) {
        this.dusFailure = dusFailure;
    }

    public void setDusStick(boolean dusStick) {
        this.dusStick = dusStick;
    }

    public void setDusPomeh(boolean dusPomeh) {
        this.dusPomeh = dusPomeh;
    }

    public void setDupFailure(boolean dupFailure) {
        this.dupFailure = dupFailure;
    }

    public void setDupStick(boolean dupStick) {
        this.dupStick = dupStick;
    }

    public void setDupPomeh(boolean dupPomeh) {
        this.dupPomeh = dupPomeh;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    public double getDw() {
        return dw;
    }

    public void setDw(double dw) {
        this.dw = dw;
    }

    public double getM_f() {
        return M_f;
    }

    public void setM_f(double m_f) {
        M_f = m_f;
    }

    public double getDt() {
        return dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public double getFi() {
        return fi;
    }

    public void setFi(double fi) {
        this.fi = fi;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public void setDupErrorValue(double dupStickValue) {
        this.dupStickValue = dupStickValue;
    }
}
