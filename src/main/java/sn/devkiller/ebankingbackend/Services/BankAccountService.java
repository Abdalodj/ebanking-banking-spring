package sn.devkiller.ebankingbackend.Services;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Entities.AccountOperation;
import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Entities.CurrentAccount;
import sn.devkiller.ebankingbackend.Entities.Customer;
import sn.devkiller.ebankingbackend.Entities.SavingAccount;
import sn.devkiller.ebankingbackend.Enums.OperationType;
import sn.devkiller.ebankingbackend.Exceptions.BalanceNotSufficientException;
import sn.devkiller.ebankingbackend.Exceptions.BankAccountNotFoundException;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;
import sn.devkiller.ebankingbackend.Mappers.BankAccountMapper;
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
  private BankAccountMapper dtoMapper;

  @Override
  public CustomerDTO saveCustomer(CustomerDTO custDto) {
    log.info("Saving new Customer");
    Customer cu = dtoMapper.fromCustDTO(custDto);
    Customer savedCustomer = customerReposittory.save(cu);
    return dtoMapper.fromCust(savedCustomer);
  }

  @Override
  public CustomerDTO updateCustomer(CustomerDTO custDto) throws CustomerNotFoundException {
    log.info("Updating customer");
    Customer cu = dtoMapper.fromCustDTO(custDto);
    Customer savedCustomer = customerReposittory.save(cu);
    return dtoMapper.fromCust(savedCustomer);
  }

  @Override
  public void deleteCustomer(Long customerId) throws CustomerNotFoundException {
    log.info("Saving new Customer"); 
    customerReposittory.deleteById(customerId);
  }

  @Override
  public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long custId) throws CustomerNotFoundException {
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
  public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long custId) throws CustomerNotFoundException {
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
  public List<CustomerDTO> listCustomers() {
    List<Customer> custs =  customerReposittory.findAll();
    List<CustomerDTO> custsDtos = custs.stream().map(cu -> dtoMapper.fromCust(cu)).collect(Collectors.toList());
    return custsDtos;
  }

  @Override
  public List<BankAccount> bankAccounts() {
    return bankAccountRepository.findAll();
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

  @Override
  public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
    Customer cust = customerReposittory.findById(customerId).orElseThrow(() -> new CustomerNotFoundException("Customer Not Found"));
    return dtoMapper.fromCust(cust);
  }
}
