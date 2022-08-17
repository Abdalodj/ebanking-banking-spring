package sn.devkiller.ebankingbackend.Mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Entities.Customer;

@Service
public class BankAccountMapper implements IBankAccountMapper{
  
  public CustomerDTO fromCust(Customer cust) {
    CustomerDTO custDto = new CustomerDTO();
    BeanUtils.copyProperties(cust, custDto);
    /* custDto.setId(cust.getId());
    custDto.setName(cust.getName());
    custDto.setEmail(cust.getEmail()); */
    return custDto;
  }
  
  public Customer fromCustDTO(CustomerDTO custDto) {
    Customer cust = new Customer();
    BeanUtils.copyProperties(custDto, cust);
    return cust;
  }
  
}
