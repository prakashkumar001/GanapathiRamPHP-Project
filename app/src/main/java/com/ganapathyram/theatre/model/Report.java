package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 11/29/2017.
 */

public class Report {
   public String txnDate;
    public String txnCount;
    public String amount;
    public String header;

    public Report(String txnDate, String txnCount, String amount, String dateStr, String header) {
        this.txnDate = txnDate;
        this.txnCount = txnCount;
        this.amount = amount;
        this.dateStr = dateStr;
        this.header=header;
    }

    public String dateStr;

}
