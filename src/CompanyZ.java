import java.sql.*;
import java.util.HashMap;

public class CompanyZ {
    public static void main(String[] args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/employeeData";
        String user = "root";
        String password = "Yessica6446!";
        StringBuilder output = new StringBuilder( "" );

        // Map empID to employee object
        HashMap<Integer, Employee> employees = new HashMap<>();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            Statement myStmt = myConn.createStatement();
            // ResultSet myRS = myStmt.executeQuery("SELECT * FROM employees");
            ResultSet myRS = myStmt.executeQuery("SELECT * FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN job_titles jt ON jt.job_title_id = ejt.job_title_id LEFT JOIN payroll p ON p.empid = e.empid LEFT JOIN employee_division ed ON ed.empid = e.empid LEFT JOIN division d ON d.ID = ed.div_ID LEFT JOIN address a ON a.empid = e.empid");

            // Employee employee = new Employee();
            while (myRS.next()) {
                // Employee employee = new Employee(myRS.getInt("empid"), myRS.getString("Fname"), myRS.getString("Lname"), myRS.getString("email"), myRS.getDate("HireDate"), myRS.getInt("Salary"));
                Employee employee = new Employee();
                employee.setJobID(myRS.getInt("job_title_id"));
                employee.setTitle(myRS.getString("job_title"));
                employee.setEmpID(myRS.getInt("empid"));
                employee.setFirstName(myRS.getString("Fname"));
                employee.setLastName(myRS.getString("Lname"));
                employee.setEmail(myRS.getString("email"));
                employee.setHireDate(myRS.getDate("HireDate"));
                employee.setSalary(myRS.getInt("Salary"));
                // employee.setGender(myRS.getString("gender"));
                Payroll payroll = new Payroll();
                payroll.setPayID(myRS.getInt("payID"));
                payroll.setPayDate(myRS.getDate("pay_date"));
                payroll.setEarnings(myRS.getInt("earnings"));
                payroll.setFedTax(myRS.getInt("fed_tax"));
                payroll.setFedMed(myRS.getInt("fed_med"));
                payroll.setFedSS(myRS.getInt("fed_SS"));
                payroll.setStateTax(myRS.getInt("state_tax"));
                payroll.setRetire401k(myRS.getInt("retire_401k"));
                payroll.setHealthCare(myRS.getInt("health_care"));

                Division division = new Division();
                division.setDivisionID(myRS.getInt("ID"));
                division.setName(myRS.getString("Name"));
                division.setCity(myRS.getString("city"));
                division.setAddressLine1(myRS.getString("addressLine1"));
                division.setAddressLine2(myRS.getString("addressLine2"));
                division.setState(myRS.getString("state"));
                division.setCountry(myRS.getString("country"));
                division.setPostalCode(myRS.getString("postalCode"));

                Address address = new Address();
                address.setEmpID(myRS.getInt("empid"));
                address.setGender(myRS.getString("gender"));
                address.setPronouns(myRS.getString("pronouns"));
                address.setIdentifiedRace(myRS.getString("identified_race"));
                address.setDob(myRS.getDate("dob"));
                address.setPhone(myRS.getString("mobile_phone"));
                address.setCityID(myRS.getInt("city_id"));
                address.setStateID(myRS.getInt("state_id"));

                employee.setPayroll(payroll);
                employee.setDivision(division);
                employee.setAddress(address);
                employees.put(employee.getEmpID(), employee);
            }
            myConn.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        for (Employee e : employees.values()) {
            System.out.println(e.getFirstName() + " " + e.getLastName() + " " + e.getPayroll().getHealthCare());
        }
    }
}
