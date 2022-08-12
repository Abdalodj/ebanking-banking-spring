package sn.devkiller.ebankingbackend.Config;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sn.devkiller.ebankingbackend.Entities.AccountOperation;
import sn.devkiller.ebankingbackend.Entities.CurrentAccount;
import sn.devkiller.ebankingbackend.Entities.Customer;
import sn.devkiller.ebankingbackend.Entities.SavingAccount;
import sn.devkiller.ebankingbackend.Enums.AccountStatus;
import sn.devkiller.ebankingbackend.Enums.OperationType;
import sn.devkiller.ebankingbackend.Repositories.AccountOperationRepository;
import sn.devkiller.ebankingbackend.Repositories.BankAccountRepository;
import sn.devkiller.ebankingbackend.Repositories.CustomerReposittory;

@Configuration
public class Seeder {
  /* @Bean */
	CommandLineRunner start(CustomerReposittory cReposittory, BankAccountRepository bRepository,
			AccountOperationRepository aOperationRepository) {
		return args -> {
			Stream.of("Hassane", "Yacine", "Aicha").forEach(
					name -> {
						Customer customer = new Customer();
						customer.setName(name);
						customer.setEmail(name + "@gmail.com");
						cReposittory.save(customer);
					});
			cReposittory.findAll().forEach(
					cust -> {
						CurrentAccount cAccount = new CurrentAccount();
						cAccount.setId(UUID.randomUUID().toString());
						cAccount.setBalance(Math.random() * 90000);
						cAccount.setCreatedAt(new Date());
						cAccount.setStatus(AccountStatus.CREATED);
						cAccount.setCustomer(cust);
						cAccount.setOverDraft(9000);
						bRepository.save(cAccount);

						SavingAccount sAccount = new SavingAccount();
						sAccount.setId(UUID.randomUUID().toString());
						sAccount.setBalance(Math.random() * 90000);
						sAccount.setCreatedAt(new Date());
						sAccount.setStatus(AccountStatus.CREATED);
						sAccount.setCustomer(cust);
						sAccount.setInterestRate(5.5);
						bRepository.save(sAccount);
					});
			bRepository.findAll().forEach(
					acc -> {
						for (int i = 0; i < 10; i++) {
							AccountOperation aOperation = new AccountOperation();
							aOperation.setOperationDate(new Date());
							aOperation.setAmount(Math.random() * 120000);
							aOperation.setType(Math.random() > 0.5 ? OperationType.CREDIT : OperationType.DEBIT);
							aOperation.setBankAccount(acc);
							aOperationRepository.save(aOperation);
						}
					});
		};
	}

}