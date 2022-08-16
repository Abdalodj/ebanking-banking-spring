package sn.devkiller.ebankingbackend.Services;

import java.util.List;

import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Entities.Customer;
import sn.devkiller.ebankingbackend.Exceptions.BalanceNotSufficientException;
import sn.devkiller.ebankingbackend.Exceptions.BankAccountNotFoundException;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;

public interface IBankAccountService {
  Customer saveCustomer(Customer customer);
  BankAccount saveCurrentBankAccount(double initialBalance, double overDraft, String type, Long custId) throws CustomerNotFoundException;
  BankAccount saveSavingBankAccount(double initialBalance, double interestRate, String type, Long custId) throws CustomerNotFoundException;
  List<Customer> listCustomers();
  BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;
  void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;
  void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;
  void tranfert(String accountIdSource, String accountIdDestinationn, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;
}
