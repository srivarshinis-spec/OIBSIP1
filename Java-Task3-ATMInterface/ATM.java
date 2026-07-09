import java.util.Scanner;

public class ATM {

    private Bank bank;
    private Scanner sc;

    public ATM(Bank bank) {
        this.bank = bank;
        sc = new Scanner(System.in);
    }

    public void start() {

        int attempts = 0;
        Account current = null;

        while (attempts < 3) {

            System.out.print("Enter User ID: ");
            String id = sc.nextLine();

            System.out.print("Enter PIN: ");
            String pin = sc.nextLine();

            current = bank.login(id, pin);

            if (current != null)
                break;

            attempts++;
            System.out.println("Invalid Credentials");
        }

        if (current == null) {
            System.out.println("Access Denied.");
            return;
        }

        menu(current);
    }

    private void menu(Account account) {

        while (true) {

            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");

            System.out.print("Choice: ");
            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    history(account);
                    break;

                case 2:
                    withdraw(account);
                    break;

                case 3:
                    deposit(account);
                    break;

                case 4:
                    transfer(account);
                    break;

                case 5:
                    System.out.println("Thank You!");
                    return;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }

    private void history(Account account) {

        if (account.getTransactions().isEmpty()) {
            System.out.println("No Transactions.");
            return;
        }

        for (Transaction t : account.getTransactions()) {
            System.out.println(t);
        }
    }

    private void withdraw(Account account) {

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        if (account.withdraw(amount)) {

            account.addTransaction(new Transaction("Withdraw : ₹" + amount));

            System.out.println("Withdraw Successful");
            System.out.println("Balance : ₹" + account.getBalance());

        } else {
            System.out.println("Insufficient Funds");
        }
    }

    private void deposit(Account account) {

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        account.deposit(amount);

        account.addTransaction(new Transaction("Deposit : ₹" + amount));

        System.out.println("Balance : ₹" + account.getBalance());
    }

    private void transfer(Account account) {

        sc.nextLine();

        System.out.print("Recipient Account ID: ");
        String id = sc.nextLine();

        Account receiver = bank.getAccount(id);

        if (receiver == null) {
            System.out.println("Invalid Account");
            return;
        }

        System.out.print("Amount: ");
        double amount = sc.nextDouble();

        if (account.withdraw(amount)) {

            receiver.deposit(amount);

            account.addTransaction(
                    new Transaction("Transferred ₹" + amount + " to " + id));

            receiver.addTransaction(
                    new Transaction("Received ₹" + amount + " from " + account.getUserId()));

            System.out.println("Transfer Successful");
            System.out.println("Balance : ₹" + account.getBalance());

        } else {
            System.out.println("Insufficient Funds");
        }
    }
}
