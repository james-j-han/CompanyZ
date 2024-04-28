import java.sql.Date;

public class Employee {
    
    private int empID;
    private String firstName;
    private String lastName;
    private String email;
    private Date hireDate;
    private double salary;
    private String ssn;
    private int jobID;
    private int divID;
    private boolean fullTime;

    public Employee() {
        
    }

    public Employee(boolean fullTime) {
        this.fullTime = fullTime;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getDivID() {
        return divID;
    }

    public void setDivID(int divID) {
        this.divID = divID;
    }

    public boolean getEmployeeType() {
        return fullTime;
    }

    public void setEmployeeType(boolean fullTime) {
        this.fullTime = fullTime;
    }
}
