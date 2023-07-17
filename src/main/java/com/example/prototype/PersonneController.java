package com.example.prototype;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping({"/personnes", "/departments", "/vacations"})
public class PersonneController {
    private final PersonneRepository personneRepository;
    private final DepartmentRepository departmentRepository;
    private final VacationRepository vacationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonneController(PersonneRepository personneRepository, DepartmentRepository departmentRepository, VacationRepository vacationRepository, PasswordEncoder passwordEncoder) {
        this.personneRepository = personneRepository;
        this.departmentRepository=departmentRepository;
        this.vacationRepository = vacationRepository;
        this.passwordEncoder = passwordEncoder;


    }
    //GetMappings
    @GetMapping("/vacations")
    public List<Vacation> getAllVacations() {
        return vacationRepository.findAll();
    }
    @GetMapping("/employees/{depid}")
    public ResponseEntity<List<Employee>> getAllEmployeesByDepartment(@PathVariable int depid) {
        Optional<Department> departmentOptional = departmentRepository.findById(depid);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            List<Employee> employees = personneRepository.findEmployeesByDepartment(department);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }
    @GetMapping("/chefpole")
    public List<ChefPole> getAllChefPoles() {
        return personneRepository.findChefPolesByUserType("chefpole");
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/employee")
    public List<Employee> getAllEmployees() {
        return personneRepository.findEmployeesByUserType("employee");
    }
    @GetMapping("/superadmin")
    public List<SuperAdmin> getAllSuperAdmins() {
        return personneRepository.findSuperAdminsByUserType("superadmin");}
    //PosteMappings

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/employee/{employeeId}/vacation")
    public ResponseEntity<Vacation> createVacationForEmployee(@PathVariable int employeeId, @RequestBody Vacation vacation) {
        Optional<Personne> employeeOptional = personneRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            Personne personne = employeeOptional.get();
            if (personne instanceof Employee) {
                Employee employee = (Employee) personne;
                vacation.setEmployee(employee);
                vacation.setNom(employee.getNom());
                vacation.setPrenom(employee.getPrenom());
                vacation.setCin(employee.getCin());
                Vacation createdVacation = vacationRepository.save(vacation);
                return new ResponseEntity<>(createdVacation, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Employee.LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String motDePasse = loginRequest.getMotDePasse();

        Employee existingEmployee = personneRepository.findEmployeeByEmailAndMotDePasse(email, motDePasse);
        ChefPole existingChefPole = personneRepository.findChefPoleByEmailAndMotDePasse(email, motDePasse);
        SuperAdmin existingSuperAdmin = personneRepository.findSuperAdminByEmailAndMotDePasse(email, motDePasse);

        if (existingEmployee != null) {
            return new ResponseEntity<>("Employee login successful", HttpStatus.OK);
        } else if (existingChefPole != null) {
            return new ResponseEntity<>("ChefPole login successful", HttpStatus.OK);
        } else if (existingSuperAdmin != null) {
            return new ResponseEntity<>("SuperAdmin login successful", HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
    }

    // Existing mappings...

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chefpole/{depid}")
    public ResponseEntity<ChefPole> addChefPoleToDepartment(@RequestBody ChefPole chefPole, @PathVariable int depid) {
        Optional<Department> departmentOptional = departmentRepository.findById(depid);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            chefPole.setDepartment(department);
            ChefPole savedChefPole = personneRepository.save(chefPole);
            return new ResponseEntity<>(savedChefPole, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentRepository.save(department);
        return new ResponseEntity<>(createdDepartment, HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/employee/{depid}")
    public ResponseEntity<Employee> addEmployeeToDepartment(@RequestBody Employee employee, @PathVariable int depid) {
        Optional<Department> departmentOptional = departmentRepository.findById(depid);
        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            employee.setDepartment(department);

            // Encode the employee's password using the passwordEncoder
            String encodedPassword = passwordEncoder.encode(employee.getMotDePasse());
            employee.setMotDePasse(encodedPassword);

            Employee savedEmployee = personneRepository.save(employee);
            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @PostMapping("/employee/{depid}")
//    public ResponseEntity<Employee> addEmployeeToDepartment(@RequestBody Employee employee, @PathVariable int depid) {
//        Optional<Department> departmentOptional = departmentRepository.findById(depid);
//        if (departmentOptional.isPresent()) {
//            Department department = departmentOptional.get();
//            employee.setDepartment(department);
//            Employee savedEmployee = personneRepository.save(employee);
//            return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = personneRepository.save(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);}
    @PreAuthorize("hasRole('SuperADMIN')")
    @PostMapping("/superadmin")
    public ResponseEntity<SuperAdmin> createSuperAdmin(@RequestBody SuperAdmin superAdmin) {
        SuperAdmin createdSuperAdmin = personneRepository.save(superAdmin);
        return new ResponseEntity<>(createdSuperAdmin, HttpStatus.CREATED);}
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/chefpole")
    public ResponseEntity<ChefPole> createChefPole(@RequestBody ChefPole chefPole) {
        ChefPole createdChefPole = personneRepository.save(chefPole);
        return new ResponseEntity<>(createdChefPole, HttpStatus.CREATED);}
    //DeleteMappings
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/departments/{depid}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int depid) {
        Optional<Department> departmentOptional = departmentRepository.findById(depid);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();

            // Delete all related entities (personne) before deleting the department
            personneRepository.deleteAll(department.getPersonne());

            departmentRepository.deleteById(depid);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/chefpole/{id}")
    public ResponseEntity<Void> deleteChefPole(@PathVariable int id) {
        Optional<Personne> chefPoleOptional = personneRepository.findById(id);
        if (chefPoleOptional.isPresent()) {
            personneRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);}}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int id) {
        Optional<Personne> employeeOptional = personneRepository.findById(id);
        if (employeeOptional.isPresent()) {
            personneRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);}}
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/superadmin/{id}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable int id) {
        personneRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);}
    //PatchMappings
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employees/{employeeId}/department/{newDepid}")
    public ResponseEntity<Employee> updateEmployeeDepartment(@PathVariable int employeeId, @PathVariable int newDepid) {
        Optional<Personne> employeeOptional = personneRepository.findById(employeeId);
        Optional<Department> departmentOptional = departmentRepository.findById(newDepid);

        if (employeeOptional.isPresent() && departmentOptional.isPresent()) {
            Personne personne = employeeOptional.get();
            if (personne instanceof Employee) {
                Employee employee = (Employee) personne;
                Department newDepartment = departmentOptional.get();
                employee.setDepartment(newDepartment);
                Employee updatedEmployee = personneRepository.save(employee);

                return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/superadmins/{superAdminId}/department/{newDepid}")
    public ResponseEntity<SuperAdmin> updateSuperAdminDepartment(@PathVariable int superAdminId, @PathVariable int newDepid) {
        Optional<Personne> superAdminOptional = personneRepository.findById(superAdminId);
        Optional<Department> departmentOptional = departmentRepository.findById(newDepid);

        if (superAdminOptional.isPresent() && departmentOptional.isPresent()) {
            Personne personne = superAdminOptional.get();
            if (personne instanceof SuperAdmin) {
                SuperAdmin superAdmin = (SuperAdmin) personne;
                Department newDepartment = departmentOptional.get();
                superAdmin.setDepartment(newDepartment);
                SuperAdmin updatedSuperAdmin = personneRepository.save(superAdmin);

                return new ResponseEntity<>(updatedSuperAdmin, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/departments/{depid}")
    public ResponseEntity<Department> updateDepartment(@PathVariable int depid, @RequestBody Department departmentUpdates) {
        Optional<Department> departmentOptional = departmentRepository.findById(depid);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();

            if (departmentUpdates.getNomDep() != null) {
                department.setNomDep(departmentUpdates.getNomDep());
            }

            Department updatedDepartment = departmentRepository.save(department);
            return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/chefpoles/{chefPoleId}/department/{newDepid}")
    public ResponseEntity<ChefPole> updateChefPoleDepartment(@PathVariable int chefPoleId, @PathVariable int newDepid) {
        Optional<Personne> chefPoleOptional = personneRepository.findById(chefPoleId);
        Optional<Department> departmentOptional = departmentRepository.findById(newDepid);

        if (chefPoleOptional.isPresent() && departmentOptional.isPresent()) {
            Personne personne = chefPoleOptional.get();
            if (personne instanceof ChefPole) {
                ChefPole chefPole = (ChefPole) personne;
                Department newDepartment = departmentOptional.get();
                chefPole.setDepartment(newDepartment);
                ChefPole updatedChefPole = personneRepository.save(chefPole);

                return new ResponseEntity<>(updatedChefPole, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/chefpole/{id}")
    public ChefPole patchChefPole(@PathVariable int id, @RequestBody ChefPole chefPoleUpdates) {
        Optional<Personne> existingPersonneOptional = personneRepository.findById(id);

        if (existingPersonneOptional.isPresent()) {
            Personne existingPersonne = existingPersonneOptional.get();

            if (existingPersonne instanceof ChefPole) {
                ChefPole existingChefPole = (ChefPole) existingPersonne;

                if (chefPoleUpdates.getPrenom() != null) {
                    existingChefPole.setPrenom(chefPoleUpdates.getPrenom());
                }
                if (chefPoleUpdates.getNom() != null) {
                    existingChefPole.setNom(chefPoleUpdates.getNom());
                }
                if (chefPoleUpdates.getNumerotel() != null) {
                    existingChefPole.setNumerotel(chefPoleUpdates.getNumerotel());
                }
                if (chefPoleUpdates.getEmail() != null) {
                    existingChefPole.setEmail(chefPoleUpdates.getEmail());
                }
                if (chefPoleUpdates.getMotDePasse() != null) {
                    existingChefPole.setMotDePasse(chefPoleUpdates.getMotDePasse());
                }
                if (chefPoleUpdates.getImage() != null) {
                    existingChefPole.setImage(chefPoleUpdates.getImage());
                }
                if (chefPoleUpdates.getCin() != null) {
                    existingChefPole.setCin(chefPoleUpdates.getCin());
                }
                if (chefPoleUpdates.getVille() != null) {
                    existingChefPole.setVille(chefPoleUpdates.getVille());
                }
                // Exclude userType update logic

                ChefPole updatedChefPole = personneRepository.save(existingChefPole);
                return updatedChefPole;
            } else {
                throw new IllegalArgumentException("Invalid update attempt for non-chefpole subclass");
            }
        } else {
            throw new EntityNotFoundException("Personne not found with ID: " + id);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/employee/{id}")
    public Employee patchEmployee(@PathVariable int id, @RequestBody Employee employeeUpdates) {
        Optional<Personne> existingPersonneOptional = personneRepository.findById(id);

        if (existingPersonneOptional.isPresent()) {
            Personne existingPersonne = existingPersonneOptional.get();

            if (existingPersonne instanceof Employee) {
                Employee existingEmployee = (Employee) existingPersonne;

                if (employeeUpdates.getPrenom() != null) {
                    existingEmployee.setPrenom(employeeUpdates.getPrenom());
                }
                if (employeeUpdates.getNom() != null) {
                    existingEmployee.setNom(employeeUpdates.getNom());
                }
                if (employeeUpdates.getNumerotel() != null) {
                    existingEmployee.setNumerotel(employeeUpdates.getNumerotel());
                }
                if (employeeUpdates.getEmail() != null) {
                    existingEmployee.setEmail(employeeUpdates.getEmail());
                }
                if (employeeUpdates.getMotDePasse() != null) {
                    existingEmployee.setMotDePasse(employeeUpdates.getMotDePasse());
                }
                if (employeeUpdates.getImage() != null) {
                    existingEmployee.setImage(employeeUpdates.getImage());
                }
                if (employeeUpdates.getCin() != null) {
                    existingEmployee.setCin(employeeUpdates.getCin());
                }
                if (employeeUpdates.getVille() != null) {
                    existingEmployee.setVille(employeeUpdates.getVille());
                }
                // Exclude userType update logic

                Employee updatedEmployee = personneRepository.save(existingEmployee);
                return updatedEmployee;
            } else {
                throw new IllegalArgumentException("Invalid update attempt for non-employee subclass");
            }
        } else {
            throw new EntityNotFoundException("Personne not found with ID: " + id);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/superadmin/{id}")
    public SuperAdmin patchSuperAdmin(@PathVariable int id, @RequestBody SuperAdmin superAdminUpdates) {
        Optional<Personne> existingPersonneOptional = personneRepository.findById(id);

        if (existingPersonneOptional.isPresent()) {
            Personne existingPersonne = existingPersonneOptional.get();

            if (existingPersonne instanceof SuperAdmin) {
                SuperAdmin existingSuperAdmin = (SuperAdmin) existingPersonne;

                if (superAdminUpdates.getPrenom() != null) {
                    existingSuperAdmin.setPrenom(superAdminUpdates.getPrenom());
                }
                if (superAdminUpdates.getNom() != null) {
                    existingSuperAdmin.setNom(superAdminUpdates.getNom());
                }
                if (superAdminUpdates.getNumerotel() != null) {
                    existingSuperAdmin.setNumerotel(superAdminUpdates.getNumerotel());
                }
                if (superAdminUpdates.getEmail() != null) {
                    existingSuperAdmin.setEmail(superAdminUpdates.getEmail());
                }
                if (superAdminUpdates.getMotDePasse() != null) {
                    existingSuperAdmin.setMotDePasse(superAdminUpdates.getMotDePasse());
                }
                if (superAdminUpdates.getImage() != null) {
                    existingSuperAdmin.setImage(superAdminUpdates.getImage());
                }
                if (superAdminUpdates.getCin() != null) {
                    existingSuperAdmin.setCin(superAdminUpdates.getCin());
                }
                if (superAdminUpdates.getVille() != null) {
                    existingSuperAdmin.setVille(superAdminUpdates.getVille());
                }

                SuperAdmin updatedSuperAdmin = personneRepository.save(existingSuperAdmin);
                return updatedSuperAdmin;
            } else {
                throw new IllegalArgumentException("Invalid update attempt for non-superadmin subclass");
            }
        } else {
            throw new EntityNotFoundException("Personne not found with ID: " + id);
        }
    }
}


