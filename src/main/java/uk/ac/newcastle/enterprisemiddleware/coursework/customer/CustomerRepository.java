package uk.ac.newcastle.enterprisemiddleware.coursework.customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Logger;

/**
 * @description 客户数据的持久化类
 */

@Singleton
public class CustomerRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    List<Customer> findAllCustomers() {
        TypedQuery<Customer> namedQuery = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return namedQuery.getResultList();
    }

    Customer findById(Long id) {
        return em.find(Customer.class, id);
    }

    Customer findByEmail(String email) {
        TypedQuery<Customer> namedQuery = em
                .createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class)
                .setParameter("email", email)
                .setMaxResults(1);
        List<Customer> resultList = namedQuery.getResultList();
        return resultList.size() == 0 ? null: resultList.get(0);
    }

    Customer createCustomer(Customer customer) {
        em.persist(customer);
        return customer;
    }

    Customer deleteCustomer(Customer customer) {
        if(customer.getId() != null && customer.getId() > 0) {
            em.remove(customer);
        } else {
            log.info("CustomerRepository.deleteById() - No ID was found so can't Delete.");
        }
        return customer;
    }

}
