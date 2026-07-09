public class Transaction {

    private String details;

    public Transaction(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return details;
    }
}
