package org.devrisby.c195.services;

import org.devrisby.c195.data.AppointmentRepository;
import org.devrisby.c195.data.DB;
import org.devrisby.c195.models.Appointment;
import org.devrisby.c195.models.Customer;
import org.devrisby.c195.models.User;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeService timeService;

    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository(DB.getConnection());
        this.timeService = new TimeService();
    }

    public Appointment save(Appointment appointment) {
        validateNewAppointmentTime(appointment);
        return this.appointmentRepository.save(appointment);
    }

    private void validateNewAppointmentTime(Appointment appointment) {
        List<Appointment> existingAppointments = this.findAllByCustomerId(appointment.getCustomer().getCustomerID());

        existingAppointments.forEach(a -> {
            Instant newAppointmentStart = appointment.getStart();
            Instant newAppointmentEnd = appointment.getEnd();
            Instant existingStart = a.getStart();
            Instant existingEnd = a.getEnd();

            boolean equalsExistingAppointment =
                    timeService.equalsStartOrEnd(newAppointmentStart, existingStart, existingEnd) ||
                            timeService.equalsStartOrEnd(newAppointmentEnd, existingStart, existingEnd);

            boolean withinExistingAppointment = timeService.inbetweenStartAndEnd(
                    newAppointmentStart,
                    newAppointmentStart,
                    existingStart,
                    existingEnd
            );

            if(equalsExistingAppointment || withinExistingAppointment ) {
                throw new IllegalArgumentException("Appointment times conflict with an existing appointment!");
            }
        });
    }

    public List<Appointment> findAllByCustomerId(int id) {
        return this.appointmentRepository.findAllByCustomerId(id);
    }

    public List<Appointment> appointmentsInMinutes(User user, long minutes) {
        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeInMinutes = timeNow.plusMinutes(minutes);

        Predicate<Appointment> appointmentsInMinutes = a -> {
            LocalDateTime appointment = LocalDateTime.ofInstant(a.getStart(), ZoneId.systemDefault());

            return appointment.isEqual(timeNow) ||
                    appointment.isEqual(timeInMinutes) ||
                    (appointment.isAfter(timeNow) && appointment.isBefore(timeInMinutes));
        };

        return this.appointmentRepository
                .findAllByUserId(user.getUserID())
                .stream()
                .filter(appointmentsInMinutes)
                .collect(Collectors.toList());
    }

    public List<Appointment> customerAppointments(int customerId) {
        return this.appointmentRepository
                .findAllByCustomerId(customerId);
    }

    public List<Appointment> customerAppointments(Customer customer) {
        return this.appointmentRepository
                .findAllByCustomerId(customer.getCustomerID());
    }

    public void deleteCustomerAppointments(Customer customer) {
        customerAppointments(customer)
                .forEach(a -> this.appointmentRepository.deleteById(a.getAppointmentID()));
    }

    public void deleteCustomerAppointments(int customerId) {
        customerAppointments(customerId)
                .forEach(a -> this.appointmentRepository.deleteById(a.getAppointmentID()));
    }

    public int numOfAppointmentsByMonthType(String month, String type) {
        if(month == null || type == null || month.isEmpty() || month.isBlank() || type.isEmpty() || type.isBlank()) {
            return 0;
        }

        List<Appointment> appointments = this.appointmentRepository.findAll();

        return appointments
                .stream()
                .filter(a -> LocalDate.ofInstant(a.getStart(), ZoneId.systemDefault()).getMonth().name().equals(month))
                .filter(a -> a.getType().equals(type))
                .mapToInt(a -> 1)
                .sum();
    }
}
