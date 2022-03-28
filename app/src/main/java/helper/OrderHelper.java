package helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import object.Keys;

public class OrderHelper {

    int totalPrice = 0;
    int myPrice = 0;

    public OrderHelper() {
        // default is general for 30.000
        myPrice = 30000;
    }

    public OrderHelper(int workMode) {
        if (workMode == Keys.PROFESI_UMUM) {
            myPrice = 30000;
        } else if (workMode == Keys.PROFESI_PELAJAR) {
            myPrice = 15000;
        }
    }

    ArrayList<String> orderList = new ArrayList<String>();

    public void resetItem(){
        orderList.clear();
    }

    public void addItem(String name) {

        name = name.replace("+", "plus");

        if (!orderList.contains(name)) {
            orderList.add(name);
        }

    }

    public void removeItem(String name) {

        name = name.replace("+", "plus");

        orderList.remove(name);

    }

    public int count(){
        return orderList.size();
    }

    public ArrayList <String> getItems(){
        return orderList;
    }

    public String getTotalPrice() {

        totalPrice = orderList.size() * myPrice;

        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        String textna = kursIndonesia.format(totalPrice);
        return textna.substring(0, textna.length()-3);
    }

}
