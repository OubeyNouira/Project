package com.example.prototype;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int depid;
    private String NomDep;
    @JsonIgnore
    @OneToMany(mappedBy ="department", cascade = CascadeType.ALL)
    private List<Personne> personne;

    public Department(String nomDep) {
        NomDep = nomDep;
    }

    public Department() {

    }

    public void setDepid(int depid) {
        this.depid = depid;
    }

    public int getDepid() {
        return depid;
    }


    public String getNomDep() {
        return NomDep;
    }

    public void setNomDep(String nomDep) {
        NomDep = nomDep;
    }

    public List<Personne> getPersonne() {
        return personne;
    }

    public void setPersonne(List<Personne> personne) {
        this.personne = personne;
    }
}