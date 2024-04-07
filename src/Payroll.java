import java.util.Date;

public class Payroll {
    private int payID;
    private Date payDate;
    private int earnings;
    private int fedTax;
    private int fedMed;
    private int fedSS;
    private int stateTax;
    private int retire401k;
    private int healthCare;

    public Payroll() {

    }

    public int getPayID() {
        return payID;
    }

    public void setPayID(int payID) {
        this.payID = payID;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public int getEarnings() {
        return earnings;
    }

    public void setEarnings(int earnings) {
        this.earnings = earnings;
    }

    public int getFedTax() {
        return fedTax;
    }

    public void setFedTax(int fedTax) {
        this.fedTax = fedTax;
    }

    public int getFedMed() {
        return fedMed;
    }

    public void setFedMed(int fedMed) {
        this.fedMed = fedMed;
    }

    public int getFedSS() {
        return fedSS;
    }

    public void setFedSS(int fedSS) {
        this.fedSS = fedSS;
    }

    public int getStateTax() {
        return stateTax;
    }

    public void setStateTax(int stateTax) {
        this.stateTax = stateTax;
    }

    public int getRetire401k() {
        return retire401k;
    }

    public void setRetire401k(int retire401k) {
        this.retire401k = retire401k;
    }

    public int getHealthCare() {
        return healthCare;
    }

    public void setHealthCare(int healthCare) {
        this.healthCare = healthCare;
    }
}
