package sn.devkiller.ebankingbackend.Mappers;

import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Entities.Customer;

public interface IBankAccountMapper {
  public CustomerDTO fromCust(Customer cust);
  public Customer fromCustDTO(CustomerDTO custDto);
}
