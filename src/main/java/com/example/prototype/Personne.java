package com.example.prototype;
import jakarta.persistence.*;
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    private String prenom;
    private String nom;
    private String numerotel;
    private String email;
    private String motDePasse;
    private String image;
    private String cin;
    private String ville;
    private String userType;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "depid")
    private Department department;
    public Personne(String prenom, String nom, String numerotel, String email, String motDePasse, String image, String cin, String ville, String userType) {
        this.prenom = prenom;
        this.nom = nom;
        this.numerotel = numerotel;
        this.email = email;
        this.motDePasse = motDePasse;
        this.image = image;
        this.cin = cin;
        this.ville = ville;
        this.userType = userType;
    }

    public Personne() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNumerotel() {
        return numerotel;
    }

    public void setNumerotel(String numerotel) {
        this.numerotel = numerotel;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}