package com.example.bankapi.Service.BankAccount;

import com.example.bankapi.Config.GlobalConfig.StaticVar;
import com.example.bankapi.DTO.BankAccount.*;
import com.example.bankapi.Entity.BankAccount.Account;
import com.example.bankapi.Entity.BankAccount.CheckingAccount;
import com.example.bankapi.Entity.BankAccount.SavingAccount;
import com.example.bankapi.Entity.Receipt.Receipt;
import com.example.bankapi.ExceptionHandle.InsufficientBalanceException;
import com.example.bankapi.ExceptionHandle.InvalidAccountTypeException;
import com.example.bankapi.Repositories.BankAccount.AccountRepository;
import com.example.bankapi.Repositories.User;
import com.example.bankapi.Service.BankAccount.Strategy.AccountStrategy;
import com.example.bankapi.Service.BankAccount.Strategy.impl.CheckingAccountStrategy;
import com.example.bankapi.Service.BankAccount.Strategy.impl.SavingAccountStrategy;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class AccountService {
    private final Map<Class<? extends Account>, AccountStrategy> strategies = new HashMap<>();
    private final AccountRepository accountRepository;
    @PersistenceContext
    private final EntityManager manager;
    private final User user;
    private final ModelMapper modelMapper;
    @Autowired
    public AccountService(SavingAccountStrategy savingAccount, CheckingAccountStrategy checkingAccount,ModelMapper modelMapper,User user,AccountRepository accountRepository,EntityManager entityManager){
        this.strategies.put(SavingAccount.class,savingAccount);
        this.strategies.put(CheckingAccount.class,checkingAccount);
        this.modelMapper = modelMapper;
        this.user = user;
        this.accountRepository = accountRepository;
        this.manager = entityManager;
    }
    @Transactional
    public AccountRegistryResponse registry(AccountRegistryRequest request){
            long startTime = System.currentTimeMillis();
            Account account = createInforAccount(request);
            AccountStrategy accountStrategy=  strategies.get(account.getClass());
            if(accountStrategy== null){
                throw new IllegalArgumentException("No strategy found for account type "+ account.getAccountType());
            }
            accountStrategy.save(account);
            long endtime = System.currentTimeMillis();
            log.info("Excution time:{} ms",endtime-startTime);
            return modelMapper.map(account, AccountRegistryResponse.class);
    }

    public Boolean updateAmount(String accountNumber,double amount){
        return manager.createQuery("Update Account a Set a.balance = a.balance+ :amount where a.accountNumber=:accountNumber")
                .setParameter("amount", amount)
                .setParameter("accountNumber", accountNumber)
                .executeUpdate() == 1;

    }
    public Boolean isBalanceGreaterThan(double amount,String accountNumber){
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found for account number: " + accountNumber);
        }
        return account.getBalance() - amount>= 0;
    }
    @Transactional
    public boolean handleBalanceByReceipt(Receipt receipt,String receiveAccountNumber){
        switch (receipt.getReceiptType().toUpperCase()) {
            case StaticVar.DEPOSIT_RECEIPT:{
                boolean isSucceed =  updateAmount(receipt.getAccountNumber(), receipt.getAmount());
                return isSucceed;
            }
            case StaticVar.WITHDRAWAL_RECEIPT:{
                if(receipt.getAccount().getAccountType().equals(StaticVar.CHECKING_ACCOUNT)){
                    if (isBalanceGreaterThan(receipt.getAmount(), receipt.getAccountNumber())) {
                        return updateAmount(receipt.getAccountNumber(), receipt.getAmount() * -1);
                    } else {
                        throw new InsufficientBalanceException(StaticVar.IN_SUFFICIENT_EXCEPTION_MESSAGE);
                    }
                }else{
                    throw new InvalidAccountTypeException(StaticVar.INVALID_ACCOUNT_TYPE_EXCEPTION_MESSAGE);
                }
            }
            case StaticVar.TRANSFER_RECEIPT: {
                if(receipt.getAccount().getAccountType().equals(StaticVar.CHECKING_ACCOUNT)){
                    if (isBalanceGreaterThan(receipt.getAmount(), receipt.getAccountNumber())) {
                        if (updateAmount(receiveAccountNumber, receipt.getAmount())) {
                            return updateAmount(receipt.getAccountNumber(), receipt.getAmount() * -1);
                        }
                    } else {
                        throw new InsufficientBalanceException(StaticVar.IN_SUFFICIENT_EXCEPTION_MESSAGE);
                    }
                }else{
                    throw new InvalidAccountTypeException(StaticVar.INVALID_ACCOUNT_TYPE_EXCEPTION_MESSAGE);
                }
            }
            default : {
                throw new IllegalArgumentException("No strategy found for receipt type " + receipt.getReceiptType());
            }
        }
    }

    public Account createInforAccount(AccountRegistryRequest request){
        var user = this.user.findByEmail(request.getUserEmail());
        var account = Account.builder()
                .accountType(request.getAccountType().toLowerCase())
                .createDate(LocalDateTime.now())
                .balance(StaticVar.INIT_BALANCE)
                .accountNumber(request.getAccountNumber()==null? getAccountNumber(): request.getAccountNumber())
                .user(user)
                .build();
        if(request.getAccountType().equalsIgnoreCase(StaticVar.SAVING_ACCOUNT)){
            return modelMapper.map(account,SavingAccount.class);
        }
        if(request.getAccountType().equalsIgnoreCase(StaticVar.CHECKING_ACCOUNT)){
            return modelMapper.map(account,CheckingAccount.class);
        }
        return null;
    }

    public String getAccountNumber() {
        String accountNumber = generateAccountNumber();
        while(accountRepository.existsByAccountNumber(accountNumber)){
            accountNumber = getAccountNumber();
        }
        return accountNumber;
    }
    private String generateAccountNumber(){
        StringBuilder accountNumber = new StringBuilder();
        Random random = new Random();
        for(int i = 0 ; i<StaticVar.ACCOUNT_LENGTH;i++){
            accountNumber.append(random.nextInt(10));
        }
        return accountNumber.toString();
    }
    @Transactional(readOnly = true)
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }
    public getAccountByAccountNumberResponse findByAccountNumber(getAccountByAccountNumberRequest request){
        TypedQuery<Account> query = manager.createQuery("SELECT new Account(a.accountNumber,a.accountType,a.user)" +
                "FROM Account a WHERE a.accountNumber= :accountNumber", Account.class);
         query.setParameter("accountNumber", request.getAccountNumber());
        try {
            Account account = query.getSingleResult();
            getAccountByAccountNumberResponse response = modelMapper.map(account, getAccountByAccountNumberResponse.class);
            response.setFullName(account.getUser().getFullName());
            return response;
        } catch (NoResultException e) {
            // Handle the case where no account is found for the given number
            log.error("Account with number " + request.getAccountNumber() + " not found", e);
            // You can return a specific error object or throw a custom exception here
            return null; // Or throw a new MyAccountNotFoundException();
        } catch (NonUniqueResultException e) {
            // Handle the case where multiple accounts have the same number (highly unlikely)
            log.error("Multiple accounts found with number " + request.getAccountNumber(), e);
            // You can throw an exception indicating data inconsistency or handle it differently
            throw new IllegalStateException("Multiple accounts found for the same number");
        } catch (PersistenceException e) {
            // Catch other general persistence exceptions (e.g., database connection issues)
            log.error("Error retrieving account by number: " + request.getAccountNumber(), e);
            // You can throw a more specific exception or handle it based on your application logic
            throw new RuntimeException("Error retrieving account", e);
        }

    }
//    public List<SavingAccount> getSavingAccountsByEmail(String userEmail) {
//        return accountRepository.findSavingAccountsByUserEmail(userEmail);
//    }
//
//    public List<CheckingAccount> getCheckingAccountsByEmail(String userEmail) {
//        return accountRepository.findCheckingAccountsByUserEmail(userEmail);
//    }
//
//    public GetBankAccountsResponse findAllAccountByUserEmail(String email) {
//        List<CheckingAccount> checkingAccounts = getCheckingAccountsByEmail(email);
//        List<SavingAccount> savingAccounts = getSavingAccountsByEmail(email);
//        GetBankAccountsResponse response = new GetBankAccountsResponse();
//        for(CheckingAccount account:checkingAccounts){
//            response.getAccounts().add(modelMapper.map(account, GetBankAccountResponse.class));
//        }
//        return response;
//    }
    public List<Account> getAllAccounts(){
        TypedQuery<Account> query = manager.createQuery("SELECT a FROM Account a WHERE a.user.id = :userId", Account.class);
        query.setParameter("userId", 1);
        return query.getResultList();
    }
//   public  GetBankAccountsResponse findAccountsByEmail(String email) {
//            List<BankAccountInfo> accountInfos= accountRepository.findAccountByUserEmail(email);
//            GetBankAccountsResponse response = new GetBankAccountsResponse();
//            for(BankAccountInfo info : accountInfos){
//                response.getAccounts().add(modelMapper.map(info, GetBankAccountResponse.class));
//            }
//            return response;
//    }
       public GetBankAccountsResponse findAccountByEmail(String email) {
           TypedQuery<Account> query = manager.createQuery("SELECT a FROM Account a WHERE a.user.email = :email", Account.class);
           query.setParameter("email", email);
           List<Account> accounts= query.getResultList();
           GetBankAccountsResponse response = new GetBankAccountsResponse();
           for(Account account : accounts){
               response.getAccounts().add(modelMapper.map(account, GetBankAccountResponse.class));
           }
           return response;
        }
    public GetBankAccountsResponse findCheckingAccountByEmail(String email) {
        TypedQuery<Account> query = manager.createQuery("SELECT a FROM Account a WHERE a.user.email = :email and a.accountType = 'checking'", Account.class);
        query.setParameter("email", email);
        List<Account> accounts= query.getResultList();
        GetBankAccountsResponse response = new GetBankAccountsResponse();
        for(Account account : accounts){
            response.getAccounts().add(modelMapper.map(account, GetBankAccountResponse.class));
        }
        return response;
    }
}
