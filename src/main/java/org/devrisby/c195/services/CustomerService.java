package org.devrisby.c195.services;

import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Country;
import org.devrisby.c195.models.Customer;

import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AppointmentService appointmentService;

    public CustomerService() {
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.appointmentService = new AppointmentService();
    }

    public List<Customer> findAll() {
        return this.customerRepository.findAll();
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
