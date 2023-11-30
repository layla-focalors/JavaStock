import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

interface Account {
    int getBalance();
    void deposit(int amount);
    void withdraw(int amount);
}

class BankAccount implements Account {
    private int balance;
    private String password;

    BankAccount(String password) {
        this.balance = 0;
        this.password = password;
    }

    @Override
    public int getBalance() {
        return this.balance;
    }

    @Override
    public void deposit(int amount) {
        this.balance += amount;
    }

    @Override
    public void withdraw(int amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            System.out.println("Insufficient balance");
        }
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

class Stock {
    String StockName;
    int StockCode;
    int StockPrice;

    private Stock(String name, int code, int price) {
        this.StockName = name;
        this.StockCode = code;
        this.StockPrice = price;
    }

    int GetStockPrice(){
        return this.StockPrice;
    }
    int GetStockCode(){
        return this.StockCode;
    }
    String GetStockName(){
        return this.StockName;
    }

    static class Manager {
        private static List<Stock> stocks = new ArrayList<>();

        static Stock createStock(String name, int code, int price) {
            Stock stock = new Stock(name, code, price);
            stocks.add(stock);
            return stock;
        }

        static List<Stock> getStocks() {
            return stocks;
        }

        public static BankAccount getAccount(String id) {
            return null;
        }
    }

    private static HashMap<String, BankAccount> accounts = new HashMap<>();

    static BankAccount createAccount(String id, String password) {
        if (accounts.containsKey(id)) {
            System.out.println("Account already exists");
            return null;
        }

        BankAccount account = new BankAccount(password);
        accounts.put(id, account);
        return account;
    }

    static BankAccount getAccount(String id) {
        return accounts.get(id);
    }
    static class User {
        static void printAllStocks() {
            for (Stock stock : Manager.getStocks()) {
                System.out.println("Stock Name: " + stock.GetStockName());
                System.out.println("Stock Code: " + stock.GetStockCode());
                System.out.println("Stock Price: " + stock.GetStockPrice());
                System.out.println("-----------------------------");
            }
        }
    }
    private static HashMap<String, BankAccount> loggedInAccounts = new HashMap<>();

    static void login(String id, String password) {
        BankAccount account = Manager.getAccount(id);
        if (account != null && account.checkPassword(password)) {
            loggedInAccounts.put(id, account);
        } else {
            System.out.println("Invalid id or password");
        }
    }
}
public class app {
    public static void main(String[] args){
        Stock.Manager.createStock("Apple", 1, 100);
        Stock.Manager.createStock("Google", 2, 200);
        Stock.User.printAllStocks();
    }
}