import java.sql.*;
import java.util.HashMap;

public class Admin {

    private String url = "jdbc:mysql://localhost:3306/employeeData";
    private String user = "root";
    private String password = "Yessica6446!";
    private HashMap<Integer, Employee> employees;
    
    public Admin() {
        init();
    }

    private void init() {
        // Map empID to employee object
        employees = new HashMap<>();
                
        // Initial DB connection to store all information locally
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            Statement myStmt = myConn.createStatement();
            ResultSet myRS = myStmt.executeQuery("SELECT * FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN job_titles jt ON jt.job_title_id = ejt.job_title_id LEFT JOIN payroll p ON p.empid = e.empid LEFT JOIN employee_division ed ON ed.empid = e.empid LEFT JOIN division d ON d.ID = ed.div_ID LEFT JOIN address a ON a.empid = e.empid");

            while (myRS.next()) {
                Employee employee = new Employee();
                employee.setJobID(myRS.getInt("job_title_id"));
                employee.setTitle(myRS.getString("job_title"));
                employee.setEmpID(myRS.getInt("empid"));
                employee.setFirstName(myRS.getString("Fname"));
                employee.setLastName(myRS.getString("Lname"));
                employee.setEmail(myRS.getString("email"));
                employee.setHireDate(myRS.getDate("HireDate"));
                employee.setSalary(myRS.getInt("Salary"));
                employee.setSsn(myRS.getString("ssn"));

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
    }

    public HashMap<Integer, Employee> getEmployees() {
        return employees;
    }

    // Test case 'b': search for employee by empID
    public Employee searchEmployee(int empID) {
        // O(1)
        Employee employee = employees.get(empID);
        return employee;
        // PASS: must handle null edge case
    }

    // Test case 'b': search for employee by ssn
    
    public Employee searchEmployee(String ssn) {
        // O(n)
        for (Employee e : employees.values()) {
            if (e.getSsn().equals(ssn)) return e;
        }
        return null;
        // PASS: although testing was limited, must handle edge cases
    }

    // Test case 'b': search for employee by name
    public Employee searchEmployee(String name) {
        
        // Case 1: first name only (possible duplicates)
        // Case 2: last name only (possibly duplicates)
        // Case 3: first and last name
        // Handle each edge case
        return null;
        // NOT TESTED
    }

    // Test case 'a': update employee data
    public void updateEmployeeSSN(int empid, String ssn) {
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            // Prepared statement to avoid SQL injection
            String query = "UPDATE employees SET ssn = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(query);
            // Note: ? index starts at 1 and must be in order
            ps.setString(1, ssn);
            ps.setInt(2, empid);
            ps.executeUpdate();
            myConn.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
        // PASS: must handle edge cases
        // Implement updating of data for other fields
        // Also need to update local variable if update query is successful
    }

    // Test case 'c': update salary for all employees less than constraint
    public void updateAllSalariesLessThan(int newAmount, int constraint) {
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            // O(n)
            for (Employee e : employees.values()) {
                // Check if salary is less than constraint
                // Update salaries with newAmount
            }
            // Prepared statement to avoid SQL injection
            String query = "UPDATE employees SET ssn = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(query);
            // Note: ? index starts at 1 and must be in order
            // ps.setString(1, ssn);
            // ps.setInt(2, empid);
            ps.executeUpdate();
            myConn.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }
}