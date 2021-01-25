package com.udacity.jdnd.course3.critter.user.repository;

import com.udacity.jdnd.course3.critter.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPetsId(Long petId);
}
