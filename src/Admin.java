import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

public class Admin {

    private String url = "jdbc:mysql://localhost:3306/employeeData";
    private String user = "root";
    private String password = "Yessica6446!";
    private HashMap<Integer, Employee> employees;
    private HashMap<Integer, Job> jobs;
    private HashMap<Integer, Division> divisions;
    // private HashMap<Integer, Payroll> payrolls;
    private Scanner scanner;
    
    public Admin() {
        init();
    }

    private void init() {
        // Map empID to employee object
        employees = new HashMap<>();
        jobs = new HashMap<>();
        divisions = new HashMap<>();
        // payrolls = new HashMap<>();
        scanner = new Scanner(System.in);

        // Initialize jobs locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM job_titles";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Job job = new Job();
                job.setJobID(rs.getInt("job_title_id"));
                job.setTitle(rs.getString("job_title"));

                jobs.put(job.getJobID(), job);
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize divisions locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM division";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Division division = new Division();
                division.setDivisionID(rs.getInt("ID"));
                division.setName(rs.getString("Name"));
                division.setCity(rs.getString("city"));
                division.setAddressLine1(rs.getString("addressLine1"));
                division.setAddressLine2(rs.getString("addressLine2"));
                division.setState(rs.getString("state"));
                division.setCountry(rs.getString("country"));
                division.setPostalCode(rs.getString("postalCode"));

                divisions.put(division.getDivisionID(), division);
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize payrolls locally
        // No unique ID to store in HashMap... retrieve from DB instead
        // try (Connection myConn = DriverManager.getConnection(url, user, password)) {
        //     Statement myStmt = myConn.createStatement();
        //     String query = "SELECT * FROM payroll";
        //     ResultSet rs = myStmt.executeQuery(query);

        //     while (rs.next()) {
        //         Payroll payroll = new Payroll();
        //         payroll.setPayID(rs.getInt("payID"));
        //         payroll.setPayDate(rs.getDate("pay_date"));
        //         payroll.setEarnings(rs.getDouble("earnings"));
        //         payroll.setFedTax(rs.getDouble("fed_tax"));
        //         payroll.setFedMed(rs.getDouble("fed_med"));
        //         payroll.setFedSS(rs.getDouble("fed_SS"));
        //         payroll.setStateTax(rs.getDouble("state_tax"));
        //         payroll.setRetire401k(rs.getDouble("retire_401k"));
        //         payroll.setHealthCare(rs.getDouble("health_care"));
        //         payroll.setEmpID(rs.getInt("empid"));

        //         payrolls.put(payroll.getPayID(), payroll);
        //     }
        // } catch (Exception e) {
        //     System.out.println("ERROR " + e.getLocalizedMessage());
        // }
        // queryTester();
                
        // Initial DB connection to store all information locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT e.*, ejt.job_title_id, ed.div_ID, p.*, a.* FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN employee_division ed ON e.empid = ed.empid LEFT JOIN payroll p ON e.empid = p.empid LEFT JOIN address a ON e.empid = a.empid";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpID(rs.getInt("empid"));
                employee.setFirstName(rs.getString("Fname"));
                employee.setLastName(rs.getString("Lname"));
                employee.setEmail(rs.getString("email"));
                employee.setHireDate(rs.getDate("HireDate"));
                employee.setSalary(rs.getBigDecimal("Salary").doubleValue());
                employee.setSsn(rs.getString("ssn"));
                employee.setJobID(rs.getInt("job_title_id"));
                employee.setDivID(rs.getInt("div_ID"));

                Payroll payroll = new Payroll();
                payroll.setPayID(rs.getInt("payID"));
                payroll.setPayDate(rs.getDate("pay_date"));
                payroll.setEarnings(rs.getInt("earnings"));
                payroll.setFedTax(rs.getInt("fed_tax"));
                payroll.setFedMed(rs.getInt("fed_med"));
                payroll.setFedSS(rs.getInt("fed_SS"));
                payroll.setStateTax(rs.getInt("state_tax"));
                payroll.setRetire401k(rs.getInt("retire_401k"));
                payroll.setHealthCare(rs.getInt("health_care"));

                Address address = new Address();
                address.setEmpID(rs.getInt("empid"));
                address.setGender(rs.getString("gender"));
                address.setPronouns(rs.getString("pronouns"));
                address.setIdentifiedRace(rs.getString("identified_race"));
                address.setDob(rs.getDate("dob"));
                address.setPhone(rs.getString("mobile_phone"));
                address.setCityID(rs.getInt("city_id"));
                address.setStateID(rs.getInt("state_id"));

                employee.setPayroll(payroll);
                employee.setAddress(address);
                employees.put(employee.getEmpID(), employee);
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    public HashMap<Integer, Employee> getEmployees() {
        return employees;
    }

    public HashMap<Integer, Division> getDivisions() {
        return divisions;
    }

    public HashMap<Integer, Job> getJobs() {
        return jobs;
    }

    public void addEmployee() {
        Employee newEmployee = createEmployee();
        // newEmployee.setPayroll(createPayroll());
        // newEmployee.setAddress(createAddress());
        int empID;

        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "INSERT INTO employees (Fname, Lname, email, HireDate, Salary, ssn) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newEmployee.getFirstName());
            ps.setString(2, newEmployee.getLastName());
            ps.setString(3, newEmployee.getEmail());
            ps.setDate(4, newEmployee.getHireDate());
            ps.setDouble(5, newEmployee.getSalary());
            ps.setString(6, newEmployee.getSsn());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            System.out.println("Getting ResultSet");
            empID = rs.getInt(1);

            sqlCommand = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            ps = myConn.prepareStatement(sqlCommand);
            ps.setInt(1, empID);
            ps.setInt(2, newEmployee.getJobID());
            ps.executeUpdate();

            sqlCommand = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";
            ps = myConn.prepareStatement(sqlCommand);
            ps.setInt(1, empID);
            ps.setInt(2, newEmployee.getDivID());
            ps.executeUpdate();
            // sqlCommand = "INSERT INTO division (ID, Name, city, addressLine1, addressLine2, state, country, postalCode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            // ps = myConn.prepareStatement(sqlCommand);
            // ps.setInt(1, newEmployee.getDivision().getDivisionID());
            // ps.setString(2, newEmployee.getDivision().getName());
            // ps.setString(3, newEmployee.getDivision().getCity());
            // ps.setString(4, newEmployee.getDivision().getAddressLine1());
            // ps.setString(5, newEmployee.getDivision().getAddressLine2());
            // ps.setString(6, newEmployee.getDivision().getState());
            // ps.setString(7, newEmployee.getDivision().getCountry());
            // ps.setString(8, newEmployee.getDivision().getPostalCode());
            // ps.executeUpdate();
            // System.out.println("Executed Division update");

            // sqlCommand = "INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care, empID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            // ps = myConn.prepareStatement(sqlCommand);
            // ps.setInt(1, newEmployee.getPayroll().getPayID());
            // ps.setDate(2, newEmployee.getPayroll().getPayDate());
            // ps.setDouble(3, newEmployee.getPayroll().getEarnings());
            // ps.setDouble(4, newEmployee.getPayroll().getFedTax());
            // ps.setDouble(5, newEmployee.getPayroll().getFedMed());
            // ps.setDouble(6, newEmployee.getPayroll().getFedSS());
            // ps.setDouble(7, newEmployee.getPayroll().getStateTax());
            // ps.setDouble(8, newEmployee.getPayroll().getRetire401k());
            // ps.setDouble(9, newEmployee.getPayroll().getHealthCare());
            // ps.setInt(10, empID);
            // ps.executeUpdate();
            // System.out.println("Executed Payroll update");

            // sqlCommand = "INSERT INTO address (empid, gender, pronouns, identified_race, dob, mobile_phone, city_id, state_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            // ps = myConn.prepareStatement(sqlCommand);
            // ps.setInt(1, empID);
            // ps.setString(2, newEmployee.getAddress().getGender());
            // ps.setString(3, newEmployee.getAddress().getPronouns());
            // ps.setString(4, newEmployee.getAddress().getIdentifiedRace());
            // ps.setDate(5, newEmployee.getAddress().getDob());
            // ps.setString(6, newEmployee.getAddress().getPhone());
            // ps.setInt(7, newEmployee.getAddress().getCityID());
            // ps.setInt(8, newEmployee.getAddress().getStateID());
            // ps.executeUpdate();
            // System.out.println("Executed Address update");

            employees.put(empID, newEmployee);
            myConn.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    private Employee createEmployee() {
        Employee newEmployee = new Employee();
        Calendar calendar = Calendar.getInstance();
        displayJobSelection();
        System.out.println();
        System.out.print("What is the Job Title? (Enter ID) ");
        newEmployee.setJobID(validateIntegerInput());
        System.out.println();
        scanner.nextLine();
        displayDivisionSelection();
        System.out.println();
        System.out.print("What is the Division Name? (Enter ID) ");
        newEmployee.setDivID(validateIntegerInput());
        System.out.println();
        scanner.nextLine();
        System.out.print("What is the First Name? ");
        newEmployee.setFirstName(validateStringInput());
        System.out.print("What is the Last Name? ");
        newEmployee.setLastName(validateStringInput());
        System.out.print("What is the Email? ");
        newEmployee.setEmail(validateStringInput());
        System.out.print("What is the Hire Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.print("What is the Hire Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("What is the Hire Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        newEmployee.setHireDate(date);
        System.out.print("What is the Salary? ");
        newEmployee.setSalary(validateDoubleInput());
        System.out.print("What is the SSN (no dashes)? ");
        // first validate SSN is numerical, then convert to String
        newEmployee.setSsn(String.valueOf(validateLongInput()));
        
        // for testing purposes, need to read nextLine() to wait for input before print
        // scanner.nextLine();
        // System.out.println(date);
        return newEmployee;
    }

    private void displayJobSelection() {
        for (Job job : jobs.values()) {
            System.out.println("Job ID: " + job.getJobID() + ": " + job.getTitle());
        }
    }

    private void displayDivisionSelection() {
        for (Division div : divisions.values()) {
            System.out.println("Division ID: " + div.getDivisionID() + ": " + div.getName());
        }
    }

    // private Division createDivision() {
    //     // Ask user to select appropriate division and retrieve ID
    //     // HashMap<Integer, String> map = new HashMap<>();
    //     // map.put(1, "Technology Engineering");
    //     // map.put(2, "Marketing");
    //     // map.put(3, "Human Resources");
    //     // map.put(4, "HQ");
    //     Division division = new Division();
    //     System.out.println("Select a Division (1 - 4)");
    //     System.out.println(division.getDivisionsMap());
    //     division.setDivisionID(validateDivisionInput());
    //     // automatically set division name according to division ID
    //     // division.setName(map.get(input));
    //     System.out.println("What is the Divison City? ");
    //     scanner.nextLine();
    //     division.setCity(validateStringInput());
    //     System.out.println("What is the Divison Address 1? ");
    //     division.setAddressLine1(validateStringInput());
    //     System.out.println("What is the Divison Address 2? ");
    //     division.setAddressLine2(validateStringInput());
    //     System.out.println("What is the Divison State? ");
    //     division.setState(validateStringInput());
    //     System.out.println("What is the Divison Country? ");
    //     division.setCountry(validateStringInput());
    //     System.out.println("What is the Divison Postal Code? ");
    //     division.setPostalCode(validateStringInput());
    //     return division;
    // }

    public void addPayroll() {
        Payroll payroll = createPayroll();

        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care, empid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payroll.getPayID());
            ps.setDate(2, payroll.getPayDate());
            ps.setDouble(3, payroll.getEarnings());
            ps.setDouble(4, payroll.getFedTax());
            ps.setDouble(5, payroll.getFedMed());
            ps.setDouble(6, payroll.getFedSS());
            ps.setDouble(7, payroll.getStateTax());
            ps.setDouble(8, payroll.getRetire401k());
            ps.setDouble(9, payroll.getHealthCare());
            ps.setInt(10, payroll.getEmpID());
            ps.executeUpdate();
            System.out.println("Executed adding payroll");

            // employees.put(empID, newEmployee);
            myConn.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    private Payroll createPayroll() {
        Payroll payroll = new Payroll();
        Calendar calendar = Calendar.getInstance();
        // payID is auto incremented based on current value tied with empID
        System.out.print("What is the Pay ID? ");
        payroll.setPayID(validateIntegerInput());
        System.out.print("What is the Pay Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.print("What is the Pay Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("What is the Pay Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        
        payroll.setPayDate(date);
        System.out.print("What is the Earnings? ");
        payroll.setEarnings(validateDoubleInput());
        System.out.print("What is the Federal Tax? ");
        payroll.setFedTax(validateDoubleInput());
        System.out.print("What is the Federal Med? ");
        payroll.setFedMed(validateDoubleInput());
        System.out.print("What is the Federal SS? ");
        payroll.setFedSS(validateDoubleInput());
        System.out.print("What is the State Tax? ");
        payroll.setStateTax(validateDoubleInput());
        System.out.print("What is the Retire 401k? ");
        payroll.setRetire401k(validateDoubleInput());
        System.out.print("What is the Health Care? ");
        payroll.setHealthCare(validateDoubleInput());
        System.out.print("What is the Employee ID? ");
        payroll.setEmpID(validateIntegerInput());
        // set payroll.setEmpID
        return payroll;
    }

    private Address createAddress() {
        Address address = new Address();
        Calendar calendar = Calendar.getInstance();
        // set address.setEmpID
        System.out.println("What is the Gender (M or F)? ");
        scanner.nextLine();
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
        Date date = new Date(calendar.getTimeInMillis());
        
        address.setDob(date);
        System.out.println("What is the Mobile Phone Number (no dashes)? ");
        address.setPhone(String.valueOf(validateLongInput()));
        // figure out what city and state ID are...
        System.out.println("What is the City ID? ");
        address.setCityID(validateIntegerInput());
        System.out.println("What is the State ID? ");
        address.setStateID(validateIntegerInput());
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

    private long validateLongInput() {
        long input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextLong();
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

    private void queryTester() {
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            // String query = "SELECT e.*, ejt.job_title_id FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid";
            String query = "SELECT e.*, ejt.job_title_id, ed.div_ID, p.* FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN employee_division ed ON e.empid = ed.empid LEFT JOIN payroll p ON e.empid = p.empid";
            // String query = "SELECT * FROM division";
            ResultSet rs = myStmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= colNum; i++) {
                    if (i > 1) System.out.print(", ");
                    String colVal = rs.getString(i);
                    System.out.print(rsmd.getColumnName(i) + ": " + colVal);
                }
                System.out.println();
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }
}
