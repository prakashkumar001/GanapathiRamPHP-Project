package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 11/29/2017.
 */

public class Report {
   public String txnDate;
    public String txnCount;
    public String amount;

    public Report(String txnDate, String txnCount, String amount, String dateStr) {
        this.txnDate = txnDate;
        this.txnCount = txnCount;
        this.amount = amount;
        this.dateStr = dateStr;
    }

    public String dateStr;

}
