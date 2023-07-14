package com.example.prototype;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Vacation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String prenom;
    private String cin;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    @JsonIgnore
    @ManyToOne
    private Employee employee;

    public Vacation(int id, String nom, String prenom, String cin, LocalDate dateDebut, LocalDate dateFin, Employee employee) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.cin = cin;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employee = employee;
    }



    public Vacation() {
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void demandVacation() {
        // Logic for requesting a vacation
        // You can implement your own business logic here
        System.out.println("Vacation requested from " + dateDebut + " to " + dateFin);
    }

    public void retrieveVacation() {
        // Logic for retrieving vacation information
        // You can implement your own business logic here
        System.out.println("Retrieving vacation information for the period from " + dateDebut + " to " + dateFin);
    }
}