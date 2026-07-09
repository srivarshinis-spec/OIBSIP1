import java.util.HashMap;

public class Bank {

    private HashMap<String, Account> accounts;

    public Bank() {

        accounts = new HashMap<>();

        accounts.put("1001", new Account("1001", "1234", 10000));
        accounts.put("1002", new Account("1002", "4321", 5000));
        accounts.put("1003", new Account("1003", "1111", 8000));
    }

    public Account login(String userId, String pin) {

        Account account = accounts.get(userId);

        if (account != null && account.getPin().equals(pin))
            return account;

        return null;
    }

    public Account getAccount(String id) {
        return accounts.get(id);
    }
}
