package edu.utfpr.cp.sa.dao;

import edu.utfpr.cp.sa.entity.Country;
import edu.utfpr.cp.sa.entity.Customer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class CustomerDAO {
    
    private final String URL = "";
    private final String USER = "";
    private final String PASSWD = "";

    public boolean create(Customer customer) {

        if (this.read().stream().map(Customer::getName).anyMatch(e -> e.equals(customer.getName())))
            throw new IllegalArgumentException("There already is a customer with this name!");
            
        else {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWD)) {

                PreparedStatement statement = conn.prepareStatement("INSERT INTO Customer (name, phone, age, creditLimit, countryId) VALUES (?, ?, ?, ?, ?)");
                statement.setString(1, customer.getName());
                statement.setString(2, customer.getPhone());
                statement.setInt(3, customer.getAge());
                statement.setDouble(4, customer.getCreditLimit());
                statement.setLong(5, customer.getCountry().getId());

                if (statement.executeUpdate() > 0) {
                    return true;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

        return false;
    }

    public Set<Customer> read() {
        HashSet<Customer> customers = new HashSet<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWD)) {

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Customer");

            Customer currentCustomer = null;

            int count = 0;

            while (result.next()) {
                Long countryId = result.getLong("countryId");
                Country country = new CountryDAO().read().stream().filter(e -> countryId == e.getId()).findFirst().get();

                currentCustomer = new Customer(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("phone"),
                        result.getInt("age"),
                        result.getDouble("creditLimit"),
                        country
                );

                customers.add(currentCustomer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();

        }

        return customers;
    }

    public boolean update(Customer customer) {

        if (this.read().stream().map(Customer::getId).anyMatch(e -> e != customer.getId())) 
            throw new IllegalArgumentException("Current customer has not been found!");
            
        else if (this.read().stream().map(Customer::getName).anyMatch(e -> e.equals(customer.getName())))
            throw new IllegalArgumentException("There already is a customer with this name!");
    
        else {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWD)) {

                PreparedStatement statement = conn.prepareStatement("UPDATE Customer SET name = ?, phone = ?, age = ?, creditLimit = ? , countryId = ? WHERE id = ?");
                statement.setString(1, customer.getName());
                statement.setString(2, customer.getPhone());
                statement.setInt(3, customer.getAge());
                statement.setDouble(4, customer.getCreditLimit());
                statement.setLong(5, customer.getCountry().getId());
                statement.setLong(6, customer.getId());

                if (statement.executeUpdate() > 0) {
                    return true;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }

        return false;
    }

    public boolean delete(Long id) {
        if (this.read().stream().map(Customer::getId).anyMatch(e -> e != id)) {
            throw new IllegalArgumentException("Customer has not been found!");

        } else {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWD)) {

                PreparedStatement statement = conn.prepareStatement("DELETE FROM Customer WHERE id=?");
                statement.setLong(1, id);

                if (statement.executeUpdate() > 0) {
                    return true;
                }

            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }

        return false;
    }
}