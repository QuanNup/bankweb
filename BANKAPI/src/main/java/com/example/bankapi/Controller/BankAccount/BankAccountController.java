package com.example.bankapi.Controller.BankAccount;

import com.example.bankapi.DTO.BankAccount.*;
import com.example.bankapi.Entity.BankAccount.Account;
import com.example.bankapi.Service.BankAccount.AccountService;
import com.example.bankapi.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name="Bank Account Controller")//gán tên cho swagger
@SecurityRequirement(name="bearerAuth")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class BankAccountController {
    private final AccountService accountService;
    private final UserService userService;
    @PostMapping ("/registry")
    public ResponseEntity<AccountRegistryResponse> accountRegistry(@RequestBody AccountRegistryRequest accountRegistryRequest){
        return ResponseEntity.ok(accountService.registry(accountRegistryRequest));
    }
    @GetMapping("/get-account-number")
    public String getAccountNumber(){
        return accountService.getAccountNumber();
    }

//    @GetMapping("/get-bank-accounts")
//    public GetBankAccountsResponse getBankAccounts(@RequestBody String email){
//        return accountService.findAllAccountByUserEmail(email);
//    }
//    @GetMapping("/get-user")
//    public User getUser(@RequestBody String email){
//        return userService.findUserByEmail(email);
//    }

    @GetMapping("/get-accounts")
    public List<Account> getAccounts(){
        return accountService.getAllAccounts();
    }
    @PostMapping("/get-accounts-by-email")
    public GetBankAccountsResponse getAccountsByEmail(@RequestBody GetBankAccountRequest request){
        return accountService.findAccountByEmail(request.getEmail());
    }
    @PostMapping("/get-checking-accounts-by-email")
    public GetBankAccountsResponse getCheckingAccountsByEmail(@RequestBody GetBankAccountRequest request){
        return accountService.findCheckingAccountByEmail(request.getEmail());
    }
    @PostMapping("/get-account-by-accountNumber")
    public getAccountByAccountNumberResponse getAccountByAccountNumber(@RequestBody getAccountByAccountNumberRequest request){
        return accountService.findByAccountNumber(request);
    }
}
