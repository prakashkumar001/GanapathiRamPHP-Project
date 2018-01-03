package com.ganapathyram.theatre.model;

/**
 * Created by Prakash on 11/29/2017.
 */

public class Report {
    public String Sno;
   public String txnDate;
    public String txnCount;
    public String amount;
    public String header;
    public String reportType;
    public String user;

    public Report(String Sno,String txnDate, String txnCount, String amount, String dateStr, String header,String reportType,String user) {
        this.txnDate = txnDate;
        this.txnCount = txnCount;
        this.amount = amount;
        this.dateStr = dateStr;
        this.header=header;
        this.reportType= reportType;
        this.Sno= Sno;
        this.user=user;
    }

    public String dateStr;

}
