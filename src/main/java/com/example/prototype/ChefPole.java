package com.example.prototype;
import jakarta.persistence.Entity;
@Entity
public class ChefPole extends Personne {
    private String pole;

    public ChefPole(String prenom, String nom, String numerotel, String email, String motDePasse, String image, String cin, String ville, String userType, String pole) {
        super(prenom, nom, numerotel, email, motDePasse, image, cin, ville, userType);
        this.pole = pole;
    }

    public ChefPole() {
        super();
    }

    public String getPole() {
        return pole;
    }

    public void setPole(String pole) {
        this.pole = pole;
    }
}