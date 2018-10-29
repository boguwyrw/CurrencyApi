package pl.home;

import java.math.BigDecimal;

public class NbpRate {

    private String no;
    private String effectiveDate;
    private BigDecimal bid;
    private BigDecimal ask;

    public NbpRate() {
    }

    public NbpRate(String no, String effectiveDate, BigDecimal bid, BigDecimal ask) {
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.bid = bid;
        this.ask = ask;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }
}
