package sn.devkiller.ebankingbackend.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.devkiller.ebankingbackend.Entities.AccountOperation;
import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Entities.CurrentAccount;
import sn.devkiller.ebankingbackend.Entities.Customer;
import sn.devkiller.ebankingbackend.Entities.SavingAccount;
import sn.devkiller.ebankingbackend.Enums.OperationType;
import sn.devkiller.ebankingbackend.Exceptions.BalanceNotSufficientException;
import sn.devkiller.ebankingbackend.Exceptions.BankAccountNotFoundException;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;
import sn.devkiller.ebankingbackend.Repositories.AccountOperationRepository;
import sn.devkiller.ebankingbackend.Repositories.BankAccountRepository;
import sn.devkiller.ebankingbackend.Repositories.CustomerReposittory;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountService implements IBankAccountService {
  private CustomerReposittory customerReposittory;
  private BankAccountRepository bankAccountRepository;
  private AccountOperationRepository accountOperationRepository;

  @Override
  public Customer saveCustomer(Customer customer) {
    log.info("Saving new Customer");
    Customer savedCustomer = customerReposittory.save(customer);
    return savedCustomer;
  }

  @Override
  public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, String type, Long custId) throws CustomerNotFoundException {
    Customer cu = customerReposittory.findById(custId).orElse(null);
    if (cu == null) {
      throw new CustomerNotFoundException("Customer not found");
    }
    CurrentAccount bAccount = new CurrentAccount();
    bAccount.setId(UUID.randomUUID().toString());
    bAccount.setCreatedAt(new Date());
    bAccount.setBalance(initialBalance);
    bAccount.setOverDraft(overDraft);
    bAccount.setCustomer(cu);
    CurrentAccount savedCAccount = bankAccountRepository.save(bAccount);
    return savedCAccount;
  }

  @Override
  public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, String type, Long custId) throws CustomerNotFoundException {
    Customer cu = customerReposittory.findById(custId).orElse(null);
    if (cu == null) {
      throw new CustomerNotFoundException("Customer not found");
    }
    SavingAccount bAccount = new SavingAccount();
    bAccount.setId(UUID.randomUUID().toString());
    bAccount.setCreatedAt(new Date());
    bAccount.setBalance(initialBalance);
    bAccount.setInterestRate(interestRate);
    bAccount.setCustomer(cu);
    SavingAccount savedAccount = bankAccountRepository.save(bAccount);
    return savedAccount;
  }

  @Override
  public List<Customer> listCustomers() {
    return customerReposittory.findAll();
  }

  @Override
  public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
    BankAccount bAccount = bankAccountRepository.findById(accountId).orElseThrow(
      ()->new BankAccountNotFoundException("Bank account not found.")
    );
    return bAccount;
  }

  @Override
  public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
    BankAccount baAccount = getBankAccount(accountId);
    if (baAccount.getBalance() < amount) {
      throw new BalanceNotSufficientException("Balance not sufficient");
    }    
    AccountOperation aOperation = new AccountOperation();
    aOperation.setType(OperationType.DEBIT);
    aOperation.setAmount(amount);
    aOperation.setDescription(description);
    aOperation.setOperationDate(new Date());
    accountOperationRepository.save(aOperation);
    baAccount.setBalance(baAccount.getBalance() - amount);
    bankAccountRepository.save(baAccount);
  }

  @Override
  public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
    BankAccount baAccount = getBankAccount(accountId);  
    AccountOperation aOperation = new AccountOperation();
    aOperation.setType(OperationType.CREDIT);
    aOperation.setAmount(amount);
    aOperation.setDescription(description);
    aOperation.setOperationDate(new Date());
    accountOperationRepository.save(aOperation);
    baAccount.setBalance(baAccount.getBalance() + amount);
    bankAccountRepository.save(baAccount);    
  }

  @Override
  public void tranfert(String accountIdSource, String accountIdDestinationn, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
    debit(accountIdSource, amount, "Transfert to "+accountIdDestinationn);
    credit(accountIdDestinationn, amount, "Transfert from "+accountIdSource);
  }
}
