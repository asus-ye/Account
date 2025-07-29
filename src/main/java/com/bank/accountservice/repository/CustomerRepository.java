package com.bank.accountservice.repository;

import com.bank.accountservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // تحديد هذه الواجهة كمكون Spring Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // يمكننا إضافة طرق مخصصة هنا إذا احتجنا
    Optional<Customer> findByNationalId(String nationalId);
}