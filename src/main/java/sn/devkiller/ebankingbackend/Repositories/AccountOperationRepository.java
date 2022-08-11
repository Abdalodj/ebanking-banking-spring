package sn.devkiller.ebankingbackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.devkiller.ebankingbackend.Entities.AccountOperation;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long>{
  
}
