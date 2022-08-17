package sn.devkiller.ebankingbackend.Web;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.devkiller.ebankingbackend.DTOs.CustomerDTO;
import sn.devkiller.ebankingbackend.Exceptions.CustomerNotFoundException;
import sn.devkiller.ebankingbackend.Services.BankAccountService;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
@Slf4j
public class CustomerRestController {
  private BankAccountService bankAccountService;

  @GetMapping("/list")
  public List<CustomerDTO> customers() {
    log.info("get list of customers through DTOs");
    return bankAccountService.listCustomers();
  }

  @GetMapping("/{id}")
  public CustomerDTO getCustomer(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {
    log.info("Get customer with id: "+customerId);
    return bankAccountService.getCustomer(customerId);
  }

  @PostMapping("")
  public CustomerDTO saveCustomer(@RequestBody CustomerDTO custDto) {
    log.info("Save new customer");
    return bankAccountService.saveCustomer(custDto);
  }

  @PutMapping("{id}")
  public CustomerDTO updCustomer(@PathVariable Long id, @RequestBody CustomerDTO custDto) throws CustomerNotFoundException {
    custDto.setId(id);
    return bankAccountService.updateCustomer(custDto);
  }

  @DeleteMapping("{id}")
  public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
    bankAccountService.deleteCustomer(id);
  }
}
