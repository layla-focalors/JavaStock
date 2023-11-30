import java.util.ArrayList;
import java.util.List;

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
}
public class app {
    public static void main(String[] args){

    }
}