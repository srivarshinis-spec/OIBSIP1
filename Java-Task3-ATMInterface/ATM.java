import java.util.*;

class Transaction {
    String type;
    double amount;
    String details;

    Transaction(String type, double amount, String details) {
        this.type = type;
        this.amount = amount;
        this.details = details;
    }

    public String toString() {
        return type + " | Amount: " + amount + " | " + details;
    }
}

class Account {
    private String userId;
    private String pin;
    private double balance;
    private ArrayList<Transaction> history;

    Account(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        history = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void addTransaction(Transaction t) {
        history.add(t);
    }

    public ArrayList<Transaction> getHistory() {
        return history;
    }
}

class Bank {
    private HashMap<String, Account> accounts;

    Bank() {
        accounts = new HashMap<>();

        accounts.put("1001", new Account("1001", "1234", 5000));
        accounts.put("1002", new Account("1002", "1111", 3000));
        accounts.put("1003", new Account("1003", "2222", 7000));
    }

    public Account login(String id, String pin) {
        Account acc = accounts.get(id);

        if (acc != null && acc.getPin().equals(pin))
            return acc;

        return null;
    }

    public Account getAccount(String id) {
        return accounts.get(id);
    }
}

class ATM {
    private Bank bank;
    private Scanner sc;

    ATM(Bank bank) {
        this.bank = bank;
        sc = new Scanner(System.in);
    }

    public void start() {

        int attempts = 0;
        Account current = null;

        while (attempts < 3) {

            System.out.print("Enter User ID: ");
            String id = sc.next();

            System.out.print("Enter PIN: ");
            String pin = sc.next();

            current = bank.login(id, pin);

            if (current != null) {
                System.out.println("\nLogin Successful!");
                menu(current);
                return;
            } else {
                attempts++;
                System.out.println("Invalid Credentials");
            }
        }

        System.out.println("Access Denied. Too many attempts.");
    }

    private void menu(Account acc) {

        while (true) {

            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");

            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    history(acc);
                    break;

                case 2:
                    withdraw(acc);
                    break;

                case 3:
                    deposit(acc);
                    break;

                case 4:
                    transfer(acc);
                    break;

                case 5:
                    System.out.println("Thank You for Using ATM!");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void history(Account acc) {

        System.out.println("\n----- Transaction History -----");

        if (acc.getHistory().isEmpty()) {
            System.out.println("No Transactions Yet.");
            return;
        }

        for (Transaction t : acc.getHistory()) {
            System.out.println(t);
        }
    }

    private void deposit(Account acc) {

        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        acc.deposit(amt);

        acc.addTransaction(new Transaction("Deposit", amt, "Cash Deposit"));

        System.out.println("Deposit Successful");
        System.out.println("Balance = " + acc.getBalance());
    }

    private void withdraw(Account acc) {

        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        if (acc.withdraw(amt)) {

            acc.addTransaction(new Transaction("Withdraw", amt, "Cash Withdrawal"));

            System.out.println("Withdrawal Successful");
            System.out.println("Balance = " + acc.getBalance());

        } else {
            System.out.println("Insufficient Funds");
        }
    }

    private void transfer(Account acc) {

        System.out.print("Enter Receiver ID: ");
        String id = sc.next();

        Account receiver = bank.getAccount(id);

        if (receiver == null) {
            System.out.println("Receiver Account Not Found");
            return;
        }

        System.out.print("Enter Amount: ");
        double amt = sc.nextDouble();

        if (amt <= 0) {
            System.out.println("Invalid Amount");
            return;
        }

        if (acc.withdraw(amt)) {

            receiver.deposit(amt);

            acc.addTransaction(new Transaction("Transfer", amt,
                    "Transferred to " + id));

            receiver.addTransaction(new Transaction("Received", amt,
                    "Received from " + acc.getUserId()));

            System.out.println("Transfer Successful");
            System.out.println("Balance = " + acc.getBalance());

        } else {
            System.out.println("Insufficient Funds");
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Bank bank = new Bank();
        ATM atm = new ATM(bank);
        atm.start();

    }
}
