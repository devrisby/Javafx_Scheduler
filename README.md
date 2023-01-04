# JavaFX Scheduler App

## About

- Software II Course Project

### Scenario

- You are contracted to build a GUI-based scheduling desktop application for a global consulting organization

### Features

- _Login form_
  - Provides error message for invalid credentials
  - Determines user's location and displays it 
  - Translates login form based on user's computer language settings (English or French)
  - Logs all user login attempts with timestamps in a separate .txt file
- _Customer Records_
  - Add, update, and delete customer records
  - Display custom message upon customer record deletion
  - Follow customer foreign key constraints and delete all customer appointments when customer is deleted 
- _Scheduling_
  - Add, update, and delete customer appointments
  - Display custom message upon customer appointment deletion
  - Feature options for displaying customer appointments by:
    - Current month
    - Current week
    - All
  - Store appointment times in UTC and display times in user's local timezone
  - Prevent creating appointments when:
    - it is outside of business hours (8:00 am - 10:00pm EST)
    - it overlaps with existing appointments
  - Display appointment alert in user interface when there is an appointment within 15 minutes of the user's login
- _Reports_
  - Display total number of customer appointments by type and month
  - Schedule for each contact in the organization
  - Current number of appointments handled by each contact in the organization for the present month

## Screenshots

<details>
    <summary>Login Screen</summary>

![Login Screen](./docs/loginscreen_en.png)

</details>

<details>
    <summary>Home Screen</summary>

![homeController Screen](./docs/home_no_appointments.png)
</details>

<details>
    <summary>Customers Screen</summary>

![customers Screen](./docs/customers.png)
</details>

<details>
    <summary>Add New Customer Screen</summary>

![add customer Screen](./docs/add_customers.png)
</details>