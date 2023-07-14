package com.example.prototype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonneService {
	private final PersonneRepository personneRepository;

	@Autowired
	public PersonneService(PersonneRepository personneRepository) {
		this.personneRepository = personneRepository;
	}

	public List<Personne> getAllPersonnes() {
		return personneRepository.findAll();
	}

	public Personne getPersonneById(int id) {
		return personneRepository.findById(id).orElse(null);
	}

	public Personne savePersonne(Personne personne) {
		return personneRepository.save(personne);
	}

	public void deletePersonne(int id) {
		personneRepository.deleteById(id);
	}

	public Personne updatePersonne(int id, Personne updatedPersonne) {
		Optional<Personne> existingPersonne = personneRepository.findById(id);
		if (existingPersonne.isPresent()) {
			Personne personne = existingPersonne.get();
			personne.setPrenom(updatedPersonne.getPrenom());
			personne.setNom(updatedPersonne.getNom());
			personne.setNumerotel(updatedPersonne.getNumerotel());
			personne.setEmail(updatedPersonne.getEmail());
			personne.setMotDePasse(updatedPersonne.getMotDePasse());
			personne.setImage(updatedPersonne.getImage());
			personne.setCin(updatedPersonne.getCin());
			personne.setVille(updatedPersonne.getVille());
			personne.setUserType(updatedPersonne.getUserType());
			return personneRepository.save(personne);
		} else {
			throw new IllegalArgumentException("Personne not found with id: " + id);
		}
	}
}