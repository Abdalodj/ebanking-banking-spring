package sn.devkiller.ebankingbackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.devkiller.ebankingbackend.Entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String>{
  
}
