package uk.ac.newcastle.enterprisemiddleware.coursework.customer;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 客户数据的服务类
 */

@Singleton
public class CustomerService {
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerRepository customerRepository;

    public List<Customer> findAllCustomers() {
        return customerRepository.findAllCustomers();
    }

    public Customer findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.createCustomer(customer);
    }

    public Customer deleteCustomer(Customer customer) {
        return customerRepository.deleteCustomer(customer);
    }
}
