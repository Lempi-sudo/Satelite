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
    private double T= 3;
    private double fi;
    private double f;

    private double rx = 0.01; // начальное отклонение угла
    private double rdx = 0; // начальная угловая скорость
    private double rw = 0 ; // начальная угловая скорость маховика
    private double rv = 0; // начальная линейная скорость маховика
    private double rdw;
    private double rM_f;
    private double rdt = 1;
    private double rfi;
    private double rf;

    private double prevX = 0;
    private double prevDx = 0;
    private double prevRX = 0;
    private double prevRDx = 0;
    private boolean dusFailure = false;
    private boolean dusStick = false;
    private boolean dusPomeh = false;
    private boolean dupFailure = false;
    private boolean dupStick = false;
    private boolean dupPomeh = false;
    private double dupStickValue = 0;
    private double dusStickValue = 0;
    private double discreteX;
    private double discreteDx;
    private double discreteRX;
    private double discreteRDx;
    private boolean isSetDupPomeh = false;
    private boolean isSetDusPomeh = false;
    private double dus = 0;
    private double dup = 0.01;
    private double disXbc = 0;
    private double disDXbc = 0;
    private double t = 0;
    private double p_err_t = 0;
    private double s_err_t = 0;
    boolean flag = false;
    private boolean flag2 = false;

    public double getDus() {
        setErrorValue(false);
        return dus;
    }

    public double getDup() {
        setErrorValue(true);
        return dup;
    }

    public double getDiscreteX() {
        return discreteX;
    }

    public double getDiscreteDx() {
        return discreteDx;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setP_err_t(double p_err_t) {
        this.p_err_t = p_err_t;
    }

    public void setS_err_t(double s_err_t) {
        this.s_err_t = s_err_t;
    }

    public double getDisXbc() {
        return disXbc;
    }

    public double getDisDXbc() {
        return disDXbc;
    }

    public void setDiscreteX() {
        this.discreteX = this.x;
    }

    public void setDiscreteDx() {
        this.discreteDx = this.dx;
    }

    public void setDisXbc() { this.disXbc = this.rx; }
    public void setDisDXbc() { this.disDXbc = this.rdx; }


    public void clearAllValues() {
        discreteX = 0;
        discreteDx = 0;
        prevRX = 0;
        prevRDx = 0;
        prevX = 0;
        prevDx = 0;
        M_f = 0;
        fi = 0;
        f = 0;
        dw = 0;
        w = 0;
        v = 0;
        x = 0.01;
        dx = 0;
        rx = 0.01;
        rdx = 0;
        rw = 0;
        rv = 0;
        rdw = 0;
        rM_f = 0;
        rf = 0;
        rfi = 0;
        dusFailure = false;
        dusStick = false;
        dusPomeh = false;
        dupFailure = false;
        dupStick = false;
        dupPomeh = false;
        isSetDupPomeh = false;
        isSetDusPomeh = false;
    }

    public AngularMotion() {
    }



    public void setError(String error, double errorValue) {
        switch (error) {
            case "dusFailure": {
                dusFailure = true;
                break;
            }
            case "dusStick": {
                dusStickValue = errorValue;
                dusStick = true;
                break;
            }
            case "dusPomeh": {
                dusPomeh = true;
                break;
            }
            case "dupFailure": {
                dupFailure = true;
                break;
            }
            case "dupStick": {
                dupStickValue = errorValue;
                dupStick = true;
                break;
            }
            case "dupPomeh": {
                dupPomeh = true;
                break;
            }
            case "no": {
                dupFailure = false;
                dupStick = false;
                dupPomeh = false;
                dusFailure = false;
                dusStick = false;
                dusPomeh = false;
                break;
            }
        }
    }

    //Вычсиление нормальных значений
    //--------------------------------------------------------------------------------------------
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
    }

    public void rotationSatelliteOnAcceleration(Satellite satellite) {
        prevX = this.x;
        prevDx = this.dx;
        this.x += dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() - (M_f / satellite.getJ_z()));
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
    }

    public void rotationSatelliteOnBracking(Satellite satellite) {
        prevX = this.x;
        prevDx = this.dx;
        this.x += this.dx * dt; // по "жёлтой" формуле X1(n+1)
        this.dx += dt * (satellite.getM_p_() + satellite.getM_b_() - satellite.getM_rc_() * f);
    }

    //--------------------------------------------------------------------------------------------

    public void setDus(double dus) {
        this.dus = dus;
    }

    public void setDup(double dup) {
        this.dup = dup;
    }

    public void setDiscreteRx() {
        setErrorValue(true);
        this.discreteRX = this.dup;
    }

    public void setDiscreteRdx() {
        setErrorValue(false);
        this.discreteRDx = this.dus;
    }

    //Вычисление значений с ошибкой
    //--------------------------------------------------------------------------------------------

    public void rAccelerationFlywheel(Satellite satellite, boolean isSwitch) { //функция участка разгона
        if (!isSwitch) {
            // угловое ускорение маховика равно ... или 0, если достигнута максимальная скрость
            if (rw <= satellite.getW_max()) {
                rdw = satellite.getA0() * this.discreteRX + satellite.getA1() * this.discreteRDx;
            } else {
                rdw = 0; // по формуле (4)
            }
            this.rw += rdw * rdt; //вычисление угловой скорости маховика по угловому ускорению
            this.rv = this.rw * satellite.getR(); // вычисление линейной скорости маховика
            rM_f = satellite.getJ_m() * rdw; // момент маховика
        } else {
            rw = w;
            rdw = dw;
            rv = v;
            rM_f = M_f;
        }
    }

    public void rBrakingFlywheel(Satellite satellite, boolean isSwitch) { // функция участка торможения
        if (!isSwitch) {
            this.rw -= satellite.getW_1_b() * rdt; // вычисление угловой скорости маховикапо угловому ускорению торможения
            this.rv = this.rw * satellite.getR(); // вычисление линейной скорости маховика
            rfi = satellite.getK0() * this.discreteRX + satellite.getK1() * this.discreteRDx; // вычисление фи по формуле (7)
            // релейная функция, если фи по модулю > a, то 1/-1, в зависимости от знака фи, иначе 0
            if (abs(rfi) > satellite.getA()) {
                rf = Math.copySign(1, rfi);
            } else {
                rf = 0;
            }
        } else {
            rw = w;
            rv = v;
            rfi = fi;
            rf = f;
        }
    }

    public void rRotationSatelliteOnAcceleration(Satellite satellite, boolean isSwitch) {
        if (!isSwitch) {
            this.rx += rdx * rdt; // по "жёлтой" формуле X1(n+1)
            this.rdx += rdt * (satellite.getM_p_() - (rM_f / satellite.getJ_z()));
//            setErrorValue();
        } else {
            rx = x;
            rdx = dx;
        }
        prevRX = this.rx;
        prevRDx = this.rdx;
    }

    public void rRotationSatelliteOnBracking(Satellite satellite, boolean isSwitch) {
        if (!isSwitch) {
            this.rx += this.rdx * rdt; // по "жёлтой" формуле X1(n+1)
            this.rdx += rdt * (satellite.getM_p_() + satellite.getM_b_() - satellite.getM_rc_() * rf);
//            setErrorValue();
        }
        else {
            this.rx = x;
            this.rdx = dx;
        }
        prevRX = this.rx;
        prevRDx = this.rdx;
    }

//--------------------------------------------------------------------------------------------



    public double getrM_f() {
        return rM_f;
    }

    public void setErrorValue(boolean dup) {
        this.dup = rx;
        this.dus = rdx;
        if (dup) {
            if (dupFailure) this.dup = (1 - (Math.random() / 10000 + 0.9999));
            if (dupStick) this.dup = dupStickValue;
//            if (t == p_err_t) {
//                this.dup = rx * 3;
//                flag = true;
//            }
//            if (t > p_err_t && flag) this.dup = x;

        }
        else {
            if (dusFailure) this.dus = (1 - (Math.random() / 10000 + 0.9999));
            else if (dusStick) this.dus = dusStickValue;
//            if (t == s_err_t) {
//                this.dus = rdx * 3;
//                flag2 = true;
//            }
//            if (t > s_err_t && flag2) this.dus = dx;
        }
    }

    public double getRx() {
//        setErrorValue(true);
        return rx;
    }

    public double getRdx() {
//        setErrorValue(false);
        return rdx;
    }

    public double getPrevX() {
        return prevX;
    }

    public double getPrevDx() {
        return prevDx;
    }

    public double getX() {
////        if (dupFailure) return (1 - (Math.random()/10000 + 0.9999));
////        else if (dupStick) return dupStickValue;
////        else if (dupPomeh) {
////            dupPomeh = false;
////            return prevX*(Math.random() + 1.3);
////        }
//        else
            return this.x;
    }



    public double getDx() {
//        if (dusFailure) return (1 - (Math.random()/10000 + 0.9999));
//        else if (dusStick) return dusStickValue;
//        else if (dusPomeh) {
//            dusPomeh = false;
//            return prevDx*(Math.random() + 3);
//        }
//        else
            return this.dx;
    }

    public double getNormalX() {
        return this.x;
    }

    public double getNormalDx() {
        return this.dx;
    }



    public double getW() {
        return w;
    }


    public double getV() {
        return v;
    }


}
