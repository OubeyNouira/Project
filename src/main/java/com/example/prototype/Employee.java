package com.example.prototype;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Employee extends Personne {
    private double salary;
    private String poste;
    private int matricule;
    private String adresse;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Vacation> vacations;

    public Employee(double salary, String poste, int matricule, String adresse) {
        this.salary = salary;
        this.poste = poste;
        this.matricule = matricule;
        this.adresse = adresse;
        this.vacations = new ArrayList<>();
    }

    public Employee() {
        this.vacations = new ArrayList<>();
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public int getMatricule() {
        return matricule;
    }

    public void setMatricule(int matricule) {
        this.matricule = matricule;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Vacation> getVacations() {
        return vacations;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
    }

    public void addVacation(Vacation vacation) {
        vacations.add(vacation);
        vacation.setEmployee(this);
    }

    public static class LoginRequest {
        private String email;
        private String motDePasse;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMotDePasse() {
            return motDePasse;
        }

        public void setMotDePasse(String motDePasse) {
            this.motDePasse = motDePasse;
        }
    }
    public void removeVacation(Vacation vacation) {
        vacations.remove(vacation);
        vacation.setEmployee(null);
    }
}