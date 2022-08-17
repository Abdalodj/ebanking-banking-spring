package sn.devkiller.ebankingbackend.Services;

import java.util.List;

import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Exceptions.BalanceNotSufficientException;
import sn.devkiller.ebankingbackend.Exceptions.BankAccountNotFoundException;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;

public interface IBankAccountService {
  CustomerDTO saveCustomer(CustomerDTO custDto);
  CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;
  CustomerDTO updateCustomer(CustomerDTO custDto) throws CustomerNotFoundException;
  void deleteCustomer(Long customerId) throws CustomerNotFoundException;
  List<CustomerDTO> listCustomers();
  List<BankAccount> bankAccounts();
  BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long custId) throws CustomerNotFoundException;
  BankAccount saveSavingBankAccount(double initialBalance, double interestRate, Long custId) throws CustomerNotFoundException;
  BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
  void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
  void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
  void tranfert(String accountIdSource, String accountIdDestinationn, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
