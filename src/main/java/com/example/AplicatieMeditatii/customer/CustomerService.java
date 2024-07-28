package com.example.AplicatieMeditatii.customer;

import com.example.AplicatieMeditatii.exception.DuplicateResourceException;
import com.example.AplicatieMeditatii.exception.RequestValidationException;
import com.example.AplicatieMeditatii.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFound(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        String email = customerRegistrationRequest.email();
        if(customerDao.existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("email [%s] already exists".formatted(email));
        }
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()));
    }
    public void deleteCustomerById(Long id) {
        if(customerDao.existsPersonWithId(id)) {
            throw new ResourceNotFound(
                    "customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }
    public void updateCustomer(
            Long id,
            CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);
        boolean changes = false;
        if(updateRequest.name() != null || !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if(updateRequest.email() != null || !updateRequest.email().equals(customer.getEmail())) {
            if(customerDao.existsPersonWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("email [%s] already exists".formatted(updateRequest.email()));
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if(updateRequest.age() != null || updateRequest.age() != customer.getAge()) {
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if(!changes) {
            throw new RequestValidationException("no data changes found");
        }
        customerDao.insertCustomer(customer);
    }

}
