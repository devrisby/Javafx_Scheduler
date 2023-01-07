package org.devrisby.c195.services;

import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.CustomerRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Customer;

import java.util.List;

public class CustomerService {
    private CustomerRepository customerRepository;
    private AppointmentRepository appointmentRepository;

    public CustomerService() {
        this.customerRepository = new CustomerRepository(DB.getConnection());
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
    }

    public List<Appointment> findAppointments(Customer customer) {
        return this.appointmentRepository
                .findAllByCustomerId(customer.getCustomerID());
    }

    public List<Appointment> findAppointments(int customerId) {
        return this.appointmentRepository
                .findAllByCustomerId(customerId);
    }

    public void delete(Customer customer) {
        // Apply Foreign Key Constraint: Delete all customer appointments before deleting customer
        findAppointments(customer)
                .forEach(a -> this.appointmentRepository.deleteById(a.getAppointmentID()));

        this.customerRepository.deleteById(customer.getCustomerID());
    }
}
