import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

interface Account {
    int getBalance();
    void deposit(int amount);
    void withdraw(int amount);
}

interface Card {
    String getCardType();
    String getCardPassword();
}

class CreditCard implements Card {
    private String cardPassword;
    private int loanAmount;
    private String repaymentDate;

    CreditCard(String cardPassword) {
        this.cardPassword = cardPassword;
        this.loanAmount = 0;
        this.repaymentDate = "";
    }

    @Override
    public String getCardType() {
        return "Credit Card";
    }

    @Override
    public String getCardPassword() {
        return this.cardPassword;
    }

    public int getLoanAmount() {
        return this.loanAmount;
    }

    public String getRepaymentDate() {
        return this.repaymentDate;
    }

    public void loan(int amount, String date) {
        this.loanAmount += amount;
        this.repaymentDate = date;
    }
}

class CheckCard implements Card {
    private String cardPassword;

    CheckCard(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    @Override
    public String getCardType() {
        return "Check Card";
    }

    @Override
    public String getCardPassword() {
        return this.cardPassword;
    }
}

class BankAccount implements Account {
    private int balance;
    private String password;
    private Card card;
    private String accountNumber;

    BankAccount(String password, String accountNumber) {
        this.balance = 0;
        this.password = password;
        this.accountNumber = accountNumber;
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

    public void issueCard(String cardType, String cardPassword) {
        if (cardType.equals("Credit Card")) {
            this.card = new CreditCard(cardPassword);
        } else if (cardType.equals("Check Card")) {
            this.card = new CheckCard(cardPassword);
        } else {
            System.out.println("Invalid card type");
        }
    }

    public Card getCard() {
        return this.card;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }
}

class Stock {
    String StockName;
    int StockCode;
    int StockPrice;
    private static List<Stock> instances = new ArrayList<>();
    private static Random rand = new Random();

    private Stock(String name, int code, int price) {
        this.StockName = name;
        this.StockCode = code;
        this.StockPrice = price;
        instances.add(this);
    }

    int GetStockPrice() {
        return this.StockPrice;
    }

    int GetStockCode() {
        return this.StockCode;
    }

    String GetStockName() {
        return this.StockName;
    }

    void updatePrice() {
        int change = rand.nextInt((int) (this.StockPrice * 0.3));
        if (rand.nextBoolean()) {
            this.StockPrice += change;
        } else {
            this.StockPrice -= change;
        }
    }

    static class Manager {
        private static HashMap<String, BankAccount> accounts = new HashMap<>();
        private static HashMap<String, String> users = new HashMap<>();
        private static int lastAccountNumber = 0;

        static Stock createStock(String name, int code, int price) {
            Stock stock = new Stock(name, code, price);
            return stock;
        }

        static List<Stock> getStocks() {
            return instances;
        }

        static BankAccount createAccount(String id, String password) {
            if (accounts.containsKey(id)) {
                System.out.println("Account already exists");
                return null;
            }

            String accountNumber = String.format("%03d-%04d-%04d", lastAccountNumber / 1000000, (lastAccountNumber / 10000) % 100, lastAccountNumber % 10000);
            lastAccountNumber++;

            BankAccount account = new BankAccount(password, accountNumber);
            accounts.put(id, account);
            return account;
        }

        static BankAccount getAccount(String id) {
            return accounts.get(id);
        }

        static boolean register(String id, String password, String address, String cardType) {
            if (users.containsKey(id)) {
                System.out.println("User already exists");
                return false;
            }

            users.put(id, password);
            BankAccount account = createAccount(id, password);
            account.issueCard(cardType, password);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true))) {
                writer.write(id + "|" + password + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("account.txt", true))) {
                writer.write(id + "|" + account.getAccountNumber() + "|" + password + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        static boolean unregister(String id, String password) {
            if (!users.containsKey(id)) {
                System.out.println("User does not exist");
                return false;
            }

            if (!users.get(id).equals(password)) {
                System.out.println("Invalid password");
                return false;
            }

            users.remove(id);
            accounts.remove(id);
            return true;
        }
    }

    static class User {
        private static HashMap<String, BankAccount> loggedInAccounts = new HashMap<>();

        static void printAllStocks() {
            for (Stock stock : Manager.getStocks()) {
                System.out.println("Stock Name: " + stock.GetStockName());
                System.out.println("Stock Code: " + stock.GetStockCode());
                System.out.println("Stock Price: " + stock.GetStockPrice());
                System.out.println("-----------------------------");
            }
        }

        static void login(String id, String password) {
            BankAccount account = Manager.getAccount(id);
            if (account != null && account.checkPassword(password)) {
                loggedInAccounts.put(id, account);
            } else {
                System.out.println("Invalid id or password");
            }
        }

        static void printAccountBalance(String id) {
            BankAccount account = loggedInAccounts.get(id);
            if (account != null) {
                System.out.println("Balance: " + account.getBalance());
            } else {
                System.out.println("Not logged in");
            }
        }

        static void printCardInfo(String id) {
            BankAccount account = loggedInAccounts.get(id);
            if (account != null) {
                Card card = account.getCard();
                System.out.println("Card Type: " + card.getCardType());
                if (card instanceof CreditCard) {
                    CreditCard creditCard = (CreditCard) card;
                    System.out.println("Loan Amount: " + creditCard.getLoanAmount());
                    System.out.println("Repayment Date: " + creditCard.getRepaymentDate());
                }
            } else {
                System.out.println("Not logged in");
            }
        }
    }
}

public class app {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String id = "";
        String password = "";
        String address = "";
        String cardType = "";
        int choice = 0;

        // 관리자 계정 생성
        Stock.Manager.register("root", "root", "", "");

        do {
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 종료");
            System.out.print("선택: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("아이디: ");
                    id = scanner.nextLine();
                    System.out.print("비밀번호: ");
                    password = scanner.nextLine();
                    System.out.print("주소: ");
                    address = scanner.nextLine();
                    System.out.print("카드 종류: ");
                    cardType = scanner.nextLine();
                    Stock.Manager.register(id, password, address, cardType);
                    break;
                case 2:
                    System.out.print("아이디: ");
                    id = scanner.nextLine();
                    System.out.print("비밀번호: ");
                    password = scanner.nextLine();
                    Stock.User.login(id, password);
                    if (Stock.User.loggedInAccounts.containsKey(id)) {
                        do {
                            System.out.println("1. 입금");
                            System.out.println("2. 출금");
                            System.out.println("3. 매수");
                            System.out.println("4. 매도");
                            System.out.println("5. 로그아웃");
                            System.out.print("선택: ");
                            choice = scanner.nextInt();
                            scanner.nextLine();  // consume newline

                            switch (choice) {
                                case 1:
                                    System.out.print("입금액: ");
                                    int depositAmount = scanner.nextInt();
                                    scanner.nextLine();  // consume newline
                                    Stock.Manager.getAccount(id).deposit(depositAmount);
                                    break;
                                case 2:
                                    System.out.print("출금액: ");
                                    int withdrawAmount = scanner.nextInt();
                                    scanner.nextLine();  // consume newline
                                    Stock.Manager.getAccount(id).withdraw(withdrawAmount);
                                    break;
                                case 3:
                                    Stock.User.printAllStocks();
                                    break;
                                case 4:
                                    // 매도 코드g
                                    break;
                            }
                        } while (choice != 5);
                    }
                    break;
            }
        } while (choice != 3);

        scanner.close();
    }
}