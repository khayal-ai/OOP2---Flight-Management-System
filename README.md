# OOP2---Flight-Management-System
Java OOP Flight Management System with GUI (Swing) – Flights, Employees, Bookings, Payments java, oop, swing, airport-system, flight-booking
# AirGo Flight Management System ✈️

A **Java OOP-based Airport Management System** with GUI (Swing) for managing flights, employees, passengers, bookings, and payments.  
This system applies strict validation rules to ensure data integrity and provides a user-friendly interface for both employees and passengers.

---

## 📝 Project Summary

The AirGo Flight Management System was developed collaboratively with organized team contributions.  
Each employee worked on a unique module, using **structured superclasses and inherited specialization** to guarantee modularization.  
The system aims to enhance convenience for employees and passengers during flight booking and management processes.  
Dedicated GUI panels are provided for:  
- Managing employees  
- Adding or canceling flights  
- Booking seats  
- Updating user profiles  

The architecture is simple and intuitive, allowing efficient airport operations. Future versions can integrate online availability, payment processing, admin analytics, and more.

---

## ✅ Features & Validations

### Login
- Username must be **at least 3 characters**.  
- Password must be **at least 8 characters**.  
- Role selection (Employee/Passenger) is required.  
- "Remember me" option stores credentials for future sessions.

### Employee Management
- All fields (Name, Employee ID, Password) are required.  
- Validations:  
  - Name ≥ 3 characters  
  - Employee ID = 10 digits  
  - Password ≥ 8 characters  
- Duplicate Employee ID not allowed.

### Flight Management
- Source and destination airports cannot be the same.  
- Adding a duplicate flight triggers a warning.  
- Canceling a non-existing flight shows an error.

### Customer Booking
- Future travel date is required.  
- Passport ID format: **2 uppercase letters + 6 digits**.  
- Duplicate bookings per passport ID are prevented.

### Payment
- Cardholder Name: letters and spaces only.  
- Card Number: exactly 16 digits.  
- Expiry Date: MM/YY format, future date.  
- CVV: exactly 3 digits.  

---

## 🖥️ Application Interface Design

### Start Page
- Full-screen travel banner with **Start** button → opens Login Page.

### Login Page
- Enter credentials, select role, optionally check "Remember me", then **Log in**.  
- Sign up opens the registration page.

### Employee Home Page
- Buttons:  
  - **Manage Flights** → Flight Management Page  
  - **Manage Employee Information** → Employees Management Page  
  - **Logout** → returns to Login Page

### Employees Management Page
- Add or view employees  
- Fields: Name, Employee ID, Password  
- Buttons: Add Employee, View Employees, Back

### Flight Management Page
- Add or cancel flights  
- Fields: Flight Type, Class, Source, Destination, Date/Time  
- Buttons: Add Flight, Cancel Flight, Back

### Customer Home Page
- Buttons: Book Flights, My Profile, Logout

### Book Flights Page
- Select: Local/International + First Class/Economy  
- Enter date, source, destination  
- Search → validate inputs, display available flights  
- Book Now → validate passport, prevent duplicates, open Payment Page

### Payment Page
- Enter payment details and confirm/cancel  
- Confirm → processes payment, shows confirmation dialog  
- Cancel → aborts transaction and returns to Book Flights Page

---

## 🗄️ Database Tables

- **APP.USERS**: stores login credentials  
  - Fields: `username`, `password`, `user_type`, `last_login`

- **APP.EMPLOYEES**: stores employee info  
  - Fields: Name, Employee ID, Password

- **APP.FLIGHT**: stores flight info  
  - Fields: Flight ID, Type, Class, Source, Destination, Date/Time

- **APP.BOOKINGS**: stores bookings  
  - Fields: Booking ID, Passport ID, Flight ID, Payment details

---

## ⚙️ Installation & Run Instructions

1. Clone the repository:  
```bash
git clone https://github.com/USERNAME/oop-airport.git
cd oop-airport
