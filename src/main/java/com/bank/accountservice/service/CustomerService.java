package com.bank.accountservice.service;

import com.bank.accountservice.model.Customer;
import com.bank.accountservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // تحديد هذه الفئة كمكون Spring Service
public class CustomerService {

    @Autowired // حقن CustomerRepository تلقائياً
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        // يمكن إضافة منطق عمل هنا، مثل التحقق من عدم وجود رقم هوية مكرر
        if (customerRepository.findByNationalId(customer.getNationalId()).isPresent()) {
            throw new IllegalArgumentException("Customer with National ID " + customer.getNationalId() + " already exists.");
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id).map(customer -> {
            customer.setFirstName(customerDetails.getFirstName());
            customer.setLastName(customerDetails.getLastName());
            customer.setEmail(customerDetails.getEmail());
            customer.setPhoneNumber(customerDetails.getPhoneNumber());
            // يمكن تحديث المزيد من الحقول هنا
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("Customer not found with id " + id));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}