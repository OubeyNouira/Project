    package com.example.prototype;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface PersonneRepository extends JpaRepository<Personne, Integer> {
        List<ChefPole> findChefPolesByUserType(String userType);
        List<Employee> findEmployeesByUserType(String userType);
        List<SuperAdmin> findSuperAdminsByUserType(String userType);

        List<Employee> findEmployeesByDepartment(Department department);

        Employee findEmployeeByEmailAndMotDePasse(String email, String motDePasse);

        ChefPole findChefPoleByEmailAndMotDePasse(String email, String motDePasse);

        SuperAdmin findSuperAdminByEmailAndMotDePasse(String email, String motDePasse);

        Personne findEmployeeByEmail(String email);
    }


