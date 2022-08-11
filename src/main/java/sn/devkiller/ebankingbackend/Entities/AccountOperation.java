package sn.devkiller.ebankingbackend.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import sn.devkiller.ebankingbackend.Enums.OperationType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Date operationDate;
  private double amount;
  @Enumerated(EnumType.STRING)
  private OperationType type;
  @ManyToOne
  private BankAccount bankAccount;
}
