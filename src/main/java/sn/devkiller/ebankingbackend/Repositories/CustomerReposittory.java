package sn.devkiller.ebankingbackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.devkiller.ebankingbackend.Entities.Customer;

public interface CustomerReposittory extends JpaRepository<Customer, Long>{
  
}
