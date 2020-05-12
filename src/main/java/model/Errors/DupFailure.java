package model.Errors;


import model.AngularMotion;

public class DupFailure implements SensorError {

    private AngularMotion am;

    public void setAm(AngularMotion am) {
        this.am = am;
    }

    public DupFailure(AngularMotion am, double p_err_t) {
        this.am = am;
        this.p_err_t = p_err_t;
    }

    private double p_err_t;

    public void setP_err_t(double p_err_t) {
        this.p_err_t = p_err_t;
    }

    @Override
    public double getX(double t) {
        if (t >= p_err_t) return 0;
        return am.getX();
    }

    @Override
    public double getDx(double t) {
        return am.getDx();
    }
}
