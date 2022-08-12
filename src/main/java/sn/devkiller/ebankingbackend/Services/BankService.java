package sn.devkiller.ebankingbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sn.devkiller.ebankingbackend.Entities.BankAccount;
import sn.devkiller.ebankingbackend.Entities.CurrentAccount;
import sn.devkiller.ebankingbackend.Entities.SavingAccount;
import sn.devkiller.ebankingbackend.Repositories.BankAccountRepository;

@Service
@Transactional
public class BankService {
  @Autowired
  private BankAccountRepository bRepository;
  
  public void consulter() {
    BankAccount bAcc = bRepository.findById("0181a0b2-6bfe-4d17-adb3-32d675809a7e").orElse(null);
						System.out.println("*******************************************************\n");
						if (bAcc != null) {
						System.out.println(bAcc.getId());
						System.out.println(bAcc.getBalance());
						System.out.println(bAcc.getStatus());
						System.out.println(bAcc.getCreatedAt());
						System.out.println(bAcc.getCustomer().getName());
						System.out.println(bAcc.getClass().getSimpleName());
						if (bAcc instanceof CurrentAccount) {
							System.out.println("Over Draft -> " + ((CurrentAccount) bAcc).getOverDraft());
						} else if (bAcc instanceof SavingAccount) {
							System.out.println("Rate -> " + ((SavingAccount) bAcc).getInterestRate());
						}
						bAcc.getAccountOperation().forEach(
								op -> {
									System.out
											.println(op.getId() 
											+ "\t" + op.getType() 
											+ "\t" + op.getAmount() 
											+ "\t" + op.getOperationDate());
								});
						}
						System.out.println("\n*******************************************************");
  }
}
