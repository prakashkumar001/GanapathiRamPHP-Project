package com.ganapathyram.theatre.model;

import android.app.Dialog;
import android.app.ProgressDialog;

import com.citizen.sdk.ESCPOSPrinter;

/**
 * Created by Prakash on 1/13/2018.
 */

public class PrintError {
    public ProgressDialog progressdialog;
    public Dialog orderdialog;
    public String orderId;
    public int result;
    public ESCPOSPrinter escposPrinter;

    public PrintError(ProgressDialog progressdialog, Dialog orderdialog, String orderId, String seatno) {
        this.progressdialog = progressdialog;
        this.orderdialog = orderdialog;
        this.orderId = orderId;
        this.seatno = seatno;
    }

    public String seatno;


    public PrintError(ProgressDialog progressdialog, Dialog orderdialog, String orderId, String seatno,ESCPOSPrinter escposPrinter,int result) {
        this.progressdialog = progressdialog;
        this.orderdialog = orderdialog;
        this.orderId = orderId;
        this.seatno = seatno;
        this.escposPrinter = escposPrinter;
        this.result = result;
    }
}
