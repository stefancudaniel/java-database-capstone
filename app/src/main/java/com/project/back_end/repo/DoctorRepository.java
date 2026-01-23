package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("select distinct d from Doctor d left join fetch d.availableTimes")
    List<Doctor> findAllWithAvailableTimes();

    @Query("select distinct d from Doctor d left join fetch d.availableTimes where lower(d.name) like lower(concat('%', :name, '%'))")
    List<Doctor> findByNameLike(String name);

    @Query("select distinct d from Doctor d left join fetch d.availableTimes where lower(d.name) like lower(concat('%', :name, '%')) and lower(d.specialty) = lower(:specialty)")
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(String name, String specialty);

    @Query("select distinct d from Doctor d left join fetch d.availableTimes where lower(d.specialty) = lower(:specialty)")
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

    /*@Query("SELECT d FROM Doctor d WHERE LOWER(d.specialty) = LOWER(:specialty)")
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);*/

    // keep existing simple findByEmail, findById, etc.
    Doctor findByEmail(String email);

}