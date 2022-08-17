package sn.devkiller.ebankingbackend.Config;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Exceptions.BalanceNotSufficientException;
import sn.devkiller.ebankingbackend.Exceptions.BankAccountNotFoundException;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;
import sn.devkiller.ebankingbackend.Services.BankAccountService;

@Configuration
public class BankAccountSeeder {

  @Bean
  CommandLineRunner start(BankAccountService bAccountService) {
    return args -> {
      Stream.of("Hassane", "Yacine", "Aicha").forEach(
          name -> {
            CustomerDTO cu = new CustomerDTO();
            cu.setName(name);
            cu.setEmail(name + "@gmail.com");
            bAccountService.saveCustomer(cu);
          });
      bAccountService.listCustomers().forEach(
          cu -> {
            try {
              bAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, cu.getId());
              bAccountService.saveSavingBankAccount(Math.random() * 90000, 5.5, cu.getId());
              List<BankAccount> bankAccounts = bAccountService.bankAccounts();
              for (BankAccount acc : bankAccounts) {
                for (int i = 0; i < 10; i++) {
                  bAccountService.credit(acc.getId(), 10000 + Math.random() * 120000, "Credit");
                  bAccountService.debit(acc.getId(), 1000 + Math.random() * 9000, "Debit");
                }
              }
            } catch (CustomerNotFoundException | BankAccountNotFoundException | BalanceNotSufficientException e) {
              e.printStackTrace();
            }
          });
    };
  }

}
