import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class Admin {

    private String url = "jdbc:mysql://localhost:3306/employeeData";
    private String user = "root";
    private String password = "Yessica6446!";
    private HashMap<Integer, Employee> employees;
    private Scanner scanner;
    
    public Admin() {
        init();
    }

    private void init() {
        // Map empID to employee object
        employees = new HashMap<>();
        scanner = new Scanner(System.in);
                
        // Initial DB connection to store all information locally
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            Statement myStmt = myConn.createStatement();
            String sqlCommand = "SELECT * FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN job_titles jt ON jt.job_title_id = ejt.job_title_id LEFT JOIN payroll p ON p.empid = e.empid LEFT JOIN employee_division ed ON ed.empid = e.empid LEFT JOIN division d ON d.ID = ed.div_ID LEFT JOIN address a ON a.empid = e.empid";
            ResultSet myRS = myStmt.executeQuery(sqlCommand);

            while (myRS.next()) {
                Employee employee = new Employee();
                employee.setJobID(myRS.getInt("job_title_id"));
                employee.setTitle(myRS.getString("job_title"));
                employee.setEmpID(myRS.getInt("empid"));
                employee.setFirstName(myRS.getString("Fname"));
                employee.setLastName(myRS.getString("Lname"));
                employee.setEmail(myRS.getString("email"));
                employee.setHireDate(myRS.getDate("HireDate"));
                employee.setSalary(myRS.getBigDecimal("Salary").doubleValue());
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

    public void addEmployee() {
        Employee newEmployee = createEmployee();
        newEmployee.setDivision(createDivision());
        newEmployee.setPayroll(createPayroll());
        newEmployee.setAddress(createAddress());

        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            Statement myStmt = myConn.createStatement();
            String sqlCommand = String.format(
                                "INSERT INTO employees (Fname, Lname, email, HireDate, Salary, ssn)"
                              , newEmployee.getFirstName(), newEmployee.getLastName(), newEmployee.getEmail()
                              , newEmployee.getHireDate(), newEmployee.getSalary(), newEmployee.getSsn());
            // INSERT INTO employees (Fname, Lname, email, HireDate, Salary, ssn) VALUES (" + newEmployee.getFirstName() + ", " + newEmployee.getLastName() + ", " + newEmployee.getEmail() + ", " + newEmployee.getHireDate() + ", " + newEmployee.getSalary() + ", " + newEmployee.getSsn() + ")";
            myStmt.executeUpdate(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = myStmt.getGeneratedKeys();
            employees.put(rs.getInt(1), newEmployee);
            myConn.close();
        } 
        catch (Exception e) 
        {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    private Employee createEmployee() {
        Employee newEmployee = new Employee();
        Calendar calendar = Calendar.getInstance();
        System.out.println("What is the Job Title? ");
        newEmployee.setTitle(validateStringInput());
        System.out.println("What is the First Name? ");
        newEmployee.setFirstName(validateStringInput());
        System.out.println("What is the Last Name? ");
        newEmployee.setLastName(validateStringInput());
        System.out.println("What is the Email? ");
        newEmployee.setEmail(validateStringInput());
        System.out.println("What is the Hire Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.println("What is the Hire Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.println("What is the Hire Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date((calendar.getTimeInMillis()));
        newEmployee.setHireDate(date);
        System.out.println("What is the Salary? ");
        newEmployee.setSalary(validateDoubleInput());
        System.out.println("What is the SSN (no dashes)? ");
        // first validate SSN is numerical, then convert to String
        newEmployee.setSsn(String.valueOf(validateIntegerInput()));
        // for testing purposes, need to read nextLine() to wait for input before print
        // scanner.nextLine();
        // System.out.println(date);
        return newEmployee;
    }

    private Division createDivision() {
        Division division = new Division();
        System.out.println("What is the Divison Name? ");
        division.setName(validateStringInput());
        System.out.println("What is the Divison City? ");
        division.setCity(validateStringInput());
        System.out.println("What is the Divison Address 1? ");
        division.setAddressLine1(validateStringInput());
        System.out.println("What is the Divison Address 2? ");
        division.setAddressLine2(validateStringInput());
        System.out.println("What is the Divison State? ");
        division.setState(validateStringInput());
        System.out.println("What is the Divison Country? ");
        division.setCountry(validateStringInput());
        System.out.println("What is the Divison Postal Code? ");
        division.setPostalCode(validateStringInput());
        return division;
    }

    private Payroll createPayroll() {
        Payroll payroll = new Payroll();
        Calendar calendar = Calendar.getInstance();
        System.out.println("What is the Pay Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.println("What is the Pay Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.println("What is the Pay Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date((calendar.getTimeInMillis()));
        
        payroll.setPayDate(date);
        System.out.println("What is the Earnings? ");
        payroll.setEarnings(validateIntegerInput());
        System.out.println("What is the Federal Tax? ");
        payroll.setFedTax(validateIntegerInput());
        System.out.println("What is the Federal Med? ");
        payroll.setFedMed(validateIntegerInput());
        System.out.println("What is the Federal SS? ");
        payroll.setFedSS(validateIntegerInput());
        System.out.println("What is the State Tax? ");
        payroll.setStateTax(validateIntegerInput());
        System.out.println("What is the Retire 401k? ");
        payroll.setRetire401k(validateIntegerInput());
        System.out.println("What is the Health Care? ");
        payroll.setHealthCare(validateIntegerInput());
        // payroll.setEmpID
        return payroll;
    }

    private Address createAddress() {
        Address address = new Address();
        Calendar calendar = Calendar.getInstance();
        System.out.println("What is the Gender? ");
        address.setGender(validateStringInput());
        System.out.println("What are the pronouns? ");
        address.setPronouns(validateStringInput());
        System.out.println("What is the Identified Race? ");
        address.setIdentifiedRace(validateStringInput());
        System.out.println("What is the Date of Birth? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.println("What is the Date of Birth? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.println("What is the Date of Birth? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date((calendar.getTimeInMillis()));
        
        address.setDob(date);
        System.out.println("What is the Mobile Phone Number (no dashes)? ");
        address.setPhone(String.valueOf(validateIntegerInput()));
        // cityID
        // stateID
        return address;
    }

    private String validateStringInput() {
        String input = "";
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextLine();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private int validateIntegerInput() {
        int input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextInt();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private double validateDoubleInput() {
        double input = 0.0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextDouble();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    // Test case 'b': search for employee by empID
    public Employee searchEmployee(int empID) {
        // O(1)
        Employee employee = employees.get(empID);
        return employee;
        // PASS: must handle null edge case
    }

    // Test case 'b': search for employee by ssn
    
    // public Employee searchEmployee(String ssn) {
    //     // O(n)
    //     for (Employee e : employees.values()) {
    //         if (e.getSsn().equals(ssn)) return e;
    //     }
    //     return null;
    //     // PASS: although testing was limited, must handle edge cases
    // }

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
