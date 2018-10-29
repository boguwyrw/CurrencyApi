package pl.home;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String currencyCode;
        String dateFrom;
        String dateTo;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj kod waluty");
        currencyCode = scanner.next();
        System.out.println("Podaj date poczatkowa");
        dateFrom = scanner.next();
        System.out.println("Podaj date koncowa");
        dateTo = scanner.next();

        ApiRestCall call = new ApiRestCall();
        String jsonResponse = call.getNBPData(currencyCode, dateFrom, dateTo);

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonResponse);
        JsonObject jsonObject = element.getAsJsonObject();
        JsonArray rates =jsonObject.getAsJsonArray("rates");

        List<NbpRate> ratesList = new ArrayList<NbpRate>();
        BigDecimal bidSum = new BigDecimal(0.0);
        BigDecimal askSum = new BigDecimal(0.0);

        for (int i = 0; i < rates.size(); i++) {
            JsonElement singleRate = rates.get(i);
            JsonObject singleRateObject = singleRate.getAsJsonObject();

            String no = singleRateObject.get("no").getAsString();
            String effectiveDate = singleRateObject.get("effectiveDate").getAsString();
            BigDecimal bid = singleRateObject.get("bid").getAsBigDecimal();
            BigDecimal ask = singleRateObject.get("ask").getAsBigDecimal();

            ratesList.add(new NbpRate(no, effectiveDate, bid, ask));
        }

        for(int j = 0; j < ratesList.size(); j++){
            bidSum = bidSum.add(ratesList.get(j).getBid());
            askSum = askSum.add(ratesList.get(j).getAsk());
        }
        BigDecimal divisor = new BigDecimal(ratesList.size());
        BigDecimal bidMean = bidSum.divide(divisor, 5);
        System.out.println("Sredni kurs kupna: " + bidMean);

        BigDecimal askMean = askSum.divide(divisor, 5);
        BigDecimal standardDeviation;
        BigDecimal askValue;
        BigDecimal askPow;
        BigDecimal askSumAll = new BigDecimal(0.0);
        BigDecimal askDeviation;
        for(int k = 0; k < ratesList.size(); k++){
            askValue = ratesList.get(k).getAsk();
            askValue = askValue.subtract(askMean);
            askPow = askValue.pow(2);
            askSumAll = askSumAll.add(askPow);
        }

        askDeviation = askSumAll.divide(divisor,5);
        standardDeviation = askDeviation;
        double standardDev = standardDeviation.doubleValue();
        double standDeviation = Math.sqrt(standardDev);
        BigDecimal finalStandDev = new BigDecimal(standDeviation);
        System.out.println("Odchylenie standardowe kursow sprzedazy: " + finalStandDev.setScale(4, BigDecimal.ROUND_HALF_DOWN));

    }
}
