package pl.training.bank.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import pl.training.bank.entity.Account;
import pl.training.bank.operation.Operation;
import pl.training.bank.service.repository.AccountNotFoundException;
import pl.training.bank.service.repository.AccountsRepository;
import pl.training.bank.service.repository.ResultPage;

@Transactional
public class AccountsService {

    private AccountsRepository accountsRepository;
    private AccountNumberGenerator accountNumberGenerator;

    public AccountsService(AccountsRepository accountsRepository, AccountNumberGenerator accountNumberGenerator) {
        this.accountsRepository = accountsRepository;
        this.accountNumberGenerator = accountNumberGenerator;
    }

    public Account createAccount() {
        Account account = new Account(accountNumberGenerator.getNext());
        accountsRepository.save(account);
        return account;
    }

    public void process(Operation operation) {
        operation.setAccountsRepository(accountsRepository);
        operation.execute();
    }

    public Account getAccount(Long id) {
        Account account = accountsRepository.findOne(id);
        if (account == null) {
            throw new AccountNotFoundException();
        }
        return account;
    }

    public void deleteAccount(Long id) {
        accountsRepository.delete(id);
    }

    public ResultPage<Account> getAccounts(int pageNumber, int pageSize) {
        Page<Account> accountsPage = accountsRepository.findAll(new PageRequest(pageNumber, pageSize));
        return new ResultPage<>(accountsPage.getContent(), accountsPage.getNumber(), accountsPage.getTotalPages());
    }

    public void init() {
        System.out.println("AccountsService init...");
    }

    public void destroy() {
        System.out.println("AccountsService destroy...");
    }

}
