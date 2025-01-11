package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    public AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        if (account.getUsername().length() <= 0) {
            return null;
        }

        if (account.getPassword().length() < 4) {
            return null;
        }

        if (accountDAO.getAccountByUsername(account.getUsername()) == null) {
            return accountDAO.insertAccount(account);
        }

        return null;
    }

    public Account login(Account account) {
         Account fetchedAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (fetchedAccount == null) {
            return null;
        }
        

        if (!account.getPassword().equals(fetchedAccount.getPassword())) {
            return null;
        }

        return fetchedAccount;
    }
}
