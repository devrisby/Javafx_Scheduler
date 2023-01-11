package org.devrisby.c195.modules.appointment;

import org.devrisby.c195.data.SQLiteDataSource;
import org.devrisby.c195.modules.customer.Customer;
import org.devrisby.c195.modules.user.User;
import org.devrisby.c195.utils.TimeUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/** Service class for Appointments, used for business logic */
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final TimeUtils timeUtils;

    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository(SQLiteDataSource.getInstance().getConnection());
        this.timeUtils = new TimeUtils();
    }

    /** Validate and save appointment to database */
    public Appointment save(Appointment appointment) {
        validateNewAppointmentTime(appointment);
        return this.appointmentRepository.save(appointment);
    }

    /** Validate and save appointment changes to database  */
    public Appointment update(Appointment appointment) {
        validateUpdatedAppointmentTime(appointment);
        return this.appointmentRepository.save(appointment);
    }

    /** Delete appointment from database */
    public void delete(Appointment appointment){
        this.appointmentRepository.delete(appointment);
    }

    /** Find all appointments from database */
    public List<Appointment> findAll() {
        return this.appointmentRepository.findAll();
    }

    /** Retrieve all appointments occurring in the current month */
    public List<Appointment> findByCurrentMonth() {
        return this.appointmentRepository.findByCurrentMonth();
    }

    /** Retrieve all appointments occurring in the current week */
    public List<Appointment> findByCurrentWeek() {
        return this.appointmentRepository.findByCurrentWeek();
    }

    public List<Appointment> findAllByCustomerId(int id) {
        return this.appointmentRepository.findAllByCustomerId(id);
    }

    private void validateNewAppointmentTime(Appointment appointment) {
        List<Appointment> existingAppointments = this.findAllByCustomerId(appointment.getCustomer().getCustomerID());

        existingAppointments.forEach(a -> {
            Instant newAppointmentStart = appointment.getStart();
            Instant newAppointmentEnd = appointment.getEnd();
            Instant existingStart = a.getStart();
            Instant existingEnd = a.getEnd();

            boolean equalsExistingAppointment =
                    timeUtils.equalsStartOrEnd(newAppointmentStart, existingStart, existingEnd) ||
                            timeUtils.equalsStartOrEnd(newAppointmentEnd, existingStart, existingEnd);

            boolean withinExistingAppointment = timeUtils.betweenStartAndEnd(
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

    private void validateUpdatedAppointmentTime(Appointment appointment) {
        List<Appointment> existingAppointments = this.findAllByCustomerId(appointment.getCustomer().getCustomerID())
                .stream()
                .filter(a -> a.getAppointmentID() != appointment.getAppointmentID())
                .collect(Collectors.toList());

        existingAppointments.forEach(a -> {
            Instant newAppointmentStart = appointment.getStart();
            Instant newAppointmentEnd = appointment.getEnd();
            Instant existingStart = a.getStart();
            Instant existingEnd = a.getEnd();

            boolean equalsExistingAppointment =
                    timeUtils.equalsStartOrEnd(newAppointmentStart, existingStart, existingEnd) ||
                            timeUtils.equalsStartOrEnd(newAppointmentEnd, existingStart, existingEnd);

            boolean withinExistingAppointment = timeUtils.betweenStartAndEnd(
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
