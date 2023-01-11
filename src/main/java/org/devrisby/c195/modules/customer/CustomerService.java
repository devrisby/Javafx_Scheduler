package org.devrisby.c195.modules.customer;

import org.devrisby.c195.data.SQLiteDataSource;
import org.devrisby.c195.modules.country.Country;
import org.devrisby.c195.modules.appointment.AppointmentService;

import java.util.List;

/** Class for Customer business logic */
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentService appointmentService;

    public CustomerService() {
        this.customerRepository = new CustomerRepository(SQLiteDataSource.getInstance().getConnection());
        this.appointmentService = new AppointmentService();
    }

    public List<Customer> findAll() {
        return this.customerRepository.findAll();
    }

    public Customer save(Customer customer) {
        return this.customerRepository.save(customer);
    }

    public void delete(Customer customer) {
        this.appointmentService.deleteCustomerAppointments(customer);
        this.customerRepository.deleteById(customer.getCustomerID());
    }

    public int countCustomersByCountry(Country country) {
        List<Customer> customers = this.customerRepository.findAll();
        return customers
                .stream()
                .filter(c -> c.getDivision().getCountry().getCountryID() == country.getCountryID())
                .mapToInt(i -> 1)
                .sum();
    }
}
