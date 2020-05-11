package model;

import static java.lang.Math.abs;

public class DUP {
    private double x = 0.01; // начальное отклонение угла
    private double dx = 0; // начальная угловая скорость
    private double w = 0 ; // начальная угловая скорость маховика
    private double v = 0; // начальная линейная скорость маховика
    private double dw;
    private double M_f;
    private double dt = 1;
    private double fi;
    private double f;
    private double prevX = 0;
    private double prevDx = 0;
    private double discreteX = 0;
    private double discreteDx = 0;
    private String errorType = "";
    private double p_err_t = 0;
    private Satellite satellite;

    public DUP (Satellite satellite) {
        this.satellite = satellite;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public void setDiscreteX() {
        this.discreteX = this.x;
    }

    public void setDiscreteDx() {
        this.discreteDx = this.dx;
    }

    public void setP_err_t(double p_err_t) {
        this.p_err_t = p_err_t;
    }

    public void accelerationFlywheel() { //функция участка разгона
        // угловое ускорение маховика равно ... или 0, если достигнута максимальная скрость
        if (w <= satellite.getW_max()) {
            dw = satellite.getA0() * this.discreteX + satellite.getA1() * this.discreteDx;
        } else {
            dw = 0; // по формуле (4)
        }
        this.w += dw * dt; //вычисление угловой скорости маховика по угловому ускорению
        this.v = this.w * satellite.getR(); // вычисление линейной скорости маховика
        M_f = satellite.getJ_m() * dw ; // момент маховика
    }

    public void rotationSatelliteOnAcceleration(double t) {
        prevX = this.x;
        prevDx = this.dx;
        this.x += dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() - (M_f / satellite.getJ_z()));
        if (t == p_err_t)
            switchToErValues();
    }

    public void brakingFlywheel() { // функция участка торможения
        this.w -= satellite.getW_1_b() * dt ; // вычисление угловой скорости маховикапо угловому ускорению торможения
        this.v = this.w * satellite.getR() ; // вычисление линейной скорости маховика
        fi = satellite.getK0() * this.discreteX + satellite.getK1() * this.discreteDx ; // вычисление фи по формуле (7)
        // релейная функция, если фи по модулю > a, то 1/-1, в зависимости от знака фи, иначе 0
        if (abs(fi) > satellite.getA()) {
            f = Math.copySign(1, fi);
        } else {
            f = 0;
        }
    }

    public void rotationSatelliteOnBracking(double t) {
        prevX = this.x;
        prevDx = this.dx;
        this.x += this.dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() + satellite.getM_b_() - satellite.getM_rc_() * f);
        if (t == p_err_t)
            switchToErValues();
    }


    private void switchToErValues() {
        switch (errorType) {
            case "dupFailure": {
                this.x = (1 - (Math.random()/10000 + 0.9999));
                break;
            }
            case "dupStick": {
                this.x = prevX;
                break;
            }
            case "dupPomeh": {
                this.x = prevX*(Math.random() + 1.4);
                this.errorType = "pomehIsSet";
                break;
            }
            default: {
                break;
            }
        }
    }


    public double getX() {
        return x;
    }

    public double getDx() {
        return dx;
    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevDx() {
        return prevDx;
    }
}
