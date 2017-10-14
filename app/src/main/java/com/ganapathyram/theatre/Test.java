package com.ganapathyram.theatre;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.citizen.sdk.ESCPOSConst;
import com.citizen.sdk.ESCPOSPrinter;

/**
 * Created by Prakash on 10/14/2017.
 */

public class Test extends AppCompatActivity {

	private ESCPOSPrinter posPtr = null;
private EditText editText_Address_WiFi;
private Button button_SamplePrint01_WiFi;
private Button button_SamplePrint02_WiFi;
private EditText editText_Address_Bluetooth;
private Button button_SamplePrint01_Bluetooth;
private Button button_SamplePrint02_Bluetooth;
//	private EditText editText_UsbDevice;
private Button button_SamplePrint01_USB;
private Button button_SamplePrint02_USB;

final String PREFERENCES_NAME = "Citizen_PSample_for_PosPrinterLibraly";
final String PRE_KEY_IP_ADDRESS = "IP_Address";
final String DEFAULT_IP_ADDRESS = "192.168.129.222";
final String PRE_KEY_BLUETOOTH_ADDRESS = "Bluetooth_Address";
final String DEFAULT_BLUETOOTH_ADDRESS = "00:01:90:E8:03:33";


@Override
public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.test );

        //
        editText_Address_WiFi = ( EditText ) findViewById( R.id.editText_Address_WiFi );
        editText_Address_Bluetooth = ( EditText ) findViewById( R.id.editText_Address_Bluetooth );
//		editText_UsbDevice = ( EditText ) findViewById( R.id.editText_UsbDevice );
        //
        editText_Address_WiFi.setText( loadAddress( ESCPOSConst.CMP_PORT_WiFi ) );
        editText_Address_Bluetooth.setText( loadAddress( ESCPOSConst.CMP_PORT_Bluetooth ) );
//		editText_UsbDevice.setText( R.string.hint_usbdevice );

        //
        // Sample Print 1 Button - Wi-Fi
        //
        button_SamplePrint01_WiFi = ( Button ) findViewById( R.id.button_SamplePrint01_WiFi );
        button_SamplePrint01_WiFi.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Get Address
        String addr = editText_Address_WiFi.getText().toString();

        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_WiFi, addr );
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Text
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Print QRcode
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 6, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_RIGHT );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        saveAddress( ESCPOSConst.CMP_PORT_WiFi, addr );
        }
        } );
        //
        // Sample Print 2 Button - Wi-Fi - for 3 inch paper
        //
        button_SamplePrint02_WiFi = ( Button ) findViewById( R.id.button_SamplePrint02_WiFi );
        button_SamplePrint02_WiFi.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Get Address
        String addr = editText_Address_WiFi.getText().toString();

        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_WiFi, addr );
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Max page mode area
        Log.d( "Max. Page area",  "( x,y ) : " + posPtr.getPageModeArea() );

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Normal
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printNormal( "\u001b|2vC\u001b|cA- Sample Print 2 -\n" );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Start Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_PAGE_MODE );

        // Position set
        posPtr.setPageModeVerticalPosition( 0 );
        posPtr.setPageModeHorizontalPosition( 0 );

        // Direction set
        posPtr.setPageModePrintDirection( ESCPOSConst.CMP_PD_TOP_TO_BOTTOM );

        // Print data output
        posPtr.setPageModePrintArea( "500,0,76,800" );
        posPtr.printNormal( "\u001b|4C- Receipt -\n" );
        posPtr.setPageModePrintArea( "260,0,120,800" );
        posPtr.printText( "   $ 299.99-  \n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_UNDERLINE | ESCPOSConst.CMP_FNT_BOLD, ESCPOSConst.CMP_TXT_4WIDTH | ESCPOSConst.CMP_TXT_4HEIGHT );
        posPtr.setPageModePrintArea( "88,0,88,560" );
        posPtr.printText( "CITIZEN SYSTEMS\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_2WIDTH | ESCPOSConst.CMP_TXT_3HEIGHT );
        posPtr.setPageModePrintArea( "0,0,88,480" );
        posPtr.printBarCode( "123456789012", ESCPOSConst.CMP_BCS_UPCA, 64, 4, ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_HRI_TEXT_BELOW );
        posPtr.setPageModePrintArea( "0,600,192,192" );
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 5, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_LEFT );

        // End Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_NORMAL );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        saveAddress( ESCPOSConst.CMP_PORT_WiFi, addr );
        }
        } );


        //
        // Sample Print 1 Button - Bluetooth
        //
        button_SamplePrint01_Bluetooth = ( Button ) findViewById( R.id.button_SamplePrint01_Bluetooth );
        button_SamplePrint01_Bluetooth.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Get Address
        String addr = editText_Address_Bluetooth.getText().toString();

        // Connect
        //int result = posPtr.connect( ESCPOSConst.CMP_PORT_Bluetooth, addr );				// Android 2.3.2 ( API Level 9 ) or before
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_Bluetooth_Insecure, addr );		// Android 2.3.3 ( API Level 10 ) or later
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Text
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Print QRcode
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 6, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_RIGHT );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        saveAddress( ESCPOSConst.CMP_PORT_Bluetooth_Insecure, addr );
        }
        } );
        //
        // Sample Print 2 Button - Bluetooth - for 2 inch paper
        //
        button_SamplePrint02_Bluetooth = ( Button ) findViewById( R.id.button_SamplePrint02_Bluetooth );
        button_SamplePrint02_Bluetooth.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Get Address
        String addr = editText_Address_Bluetooth.getText().toString();

        // Connect
        //int result = posPtr.connect( ESCPOSConst.CMP_PORT_Bluetooth, addr );				// Android 2.3.2 ( API Level 9 ) or before
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_Bluetooth_Insecure, addr );		// Android 2.3.3 ( API Level 10 ) or later
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Max page mode area
        Log.d( "Max. Page area",  "( x,y ) : " + posPtr.getPageModeArea() );

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Normal
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printNormal( "\u001b|2vC\u001b|cA- Sample Print 2 -\n" );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Start Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_PAGE_MODE );

        // Position set
        posPtr.setPageModeVerticalPosition( 0 );
        posPtr.setPageModeHorizontalPosition( 0 );

        // Direction set
        posPtr.setPageModePrintDirection( ESCPOSConst.CMP_PD_TOP_TO_BOTTOM );

        // Print data output
        posPtr.setPageModePrintArea( "308,0,76,800" );
        posPtr.printNormal( "\u001b|4C- Receipt -\n" );
        posPtr.setPageModePrintArea( "184,0,120,800" );
        posPtr.printText( "   $ 299.99-  \n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_UNDERLINE | ESCPOSConst.CMP_FNT_BOLD, ESCPOSConst.CMP_TXT_4WIDTH | ESCPOSConst.CMP_TXT_4HEIGHT );
        posPtr.setPageModePrintArea( "88,0,88,560" );
        posPtr.printText( "CITIZEN SYSTEMS\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_2WIDTH | ESCPOSConst.CMP_TXT_3HEIGHT );
        posPtr.setPageModePrintArea( "0,0,88,480" );
        posPtr.printBarCode( "123456789012", ESCPOSConst.CMP_BCS_UPCA, 64, 4, ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_HRI_TEXT_BELOW );
        posPtr.setPageModePrintArea( "0,600,192,192" );
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 5, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_LEFT );

        // End Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_NORMAL );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        saveAddress( ESCPOSConst.CMP_PORT_Bluetooth_Insecure, addr );
        }
        } );


        //
        // Sample Print 1 Button - USB
        //
        button_SamplePrint01_USB = ( Button ) findViewById( R.id.button_SamplePrint01_USB );
        button_SamplePrint01_USB.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Set context
        posPtr.setContext( Test.this );

        // Get Address
        UsbDevice usbDevice = null;												// null (Automatic detection)
        //
        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_USB, usbDevice );		// Android 3.1 ( API Level 12 ) or later
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Text
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printText( "- Sample Print 1 -\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_2HEIGHT );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Print QRcode
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 6, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_RIGHT );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        } );
        //
        // Sample Print 2 Button - USB - for 2 inch paper
        //
        button_SamplePrint02_USB = ( Button ) findViewById( R.id.button_SamplePrint02_USB );
        button_SamplePrint02_USB.setOnClickListener( new View.OnClickListener()
        {
public void onClick( View v )
        {
        // Constructor
        ESCPOSPrinter posPtr = new ESCPOSPrinter();

        // Set context
        posPtr.setContext( Test.this );

        // Get Address
        UsbDevice usbDevice = null;												// null (Automatic detection)

        // Connect
        int result = posPtr.connect( ESCPOSConst.CMP_PORT_USB, usbDevice );		// Android 3.1 ( API Level 12 ) or later
        if ( ESCPOSConst.CMP_SUCCESS == result )
        {
        // Character set
        posPtr.setEncoding( "ISO-8859-1" );		// Latin-1
        //posPtr.setEncoding( "Shift_JIS" );	// Japanese 日本語を印字する場合は、この行を有効にしてください.

        // Max page mode area
        Log.d( "Max. Page area",  "( x,y ) : " + posPtr.getPageModeArea() );

        // Start Transaction ( Batch )
        posPtr.transactionPrint( ESCPOSConst.CMP_TP_TRANSACTION );

        // Print Normal
        posPtr.printText( getString( R.string.app_name ) + "\n\n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );
        posPtr.printNormal( "\u001b|2vC\u001b|cA- Sample Print 2 -\n" );
        posPtr.printText( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_1WIDTH | ESCPOSConst.CMP_TXT_1HEIGHT );

        // Start Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_PAGE_MODE );

        // Position set
        posPtr.setPageModeVerticalPosition( 0 );
        posPtr.setPageModeHorizontalPosition( 0 );

        // Direction set
        posPtr.setPageModePrintDirection( ESCPOSConst.CMP_PD_TOP_TO_BOTTOM );

        // Print data output
        posPtr.setPageModePrintArea( "308,0,76,800" );
        posPtr.printNormal( "\u001b|4C- Receipt -\n" );
        posPtr.setPageModePrintArea( "184,0,120,800" );
        posPtr.printText( "   $ 299.99-  \n", ESCPOSConst.CMP_ALIGNMENT_CENTER, ESCPOSConst.CMP_FNT_UNDERLINE | ESCPOSConst.CMP_FNT_BOLD, ESCPOSConst.CMP_TXT_4WIDTH | ESCPOSConst.CMP_TXT_4HEIGHT );
        posPtr.setPageModePrintArea( "88,0,88,560" );
        posPtr.printText( "CITIZEN SYSTEMS\n", ESCPOSConst.CMP_ALIGNMENT_RIGHT, ESCPOSConst.CMP_FNT_DEFAULT, ESCPOSConst.CMP_TXT_2WIDTH | ESCPOSConst.CMP_TXT_3HEIGHT );
        posPtr.setPageModePrintArea( "0,0,88,480" );
        posPtr.printBarCode( "123456789012", ESCPOSConst.CMP_BCS_UPCA, 64, 4, ESCPOSConst.CMP_ALIGNMENT_LEFT, ESCPOSConst.CMP_HRI_TEXT_BELOW );
        posPtr.setPageModePrintArea( "0,600,192,192" );
        posPtr.printQRCode( "http://www.citizen-systems.co.jp/", 5, ESCPOSConst.CMP_QRCODE_EC_LEVEL_L, ESCPOSConst.CMP_ALIGNMENT_LEFT );

        // End Page mode
        posPtr.pageModePrint( ESCPOSConst.CMP_PM_NORMAL );

        // Partial Cut with Pre-Feed
        posPtr.cutPaper( ESCPOSConst.CMP_CUT_PARTIAL_PREFEED );

        // End Transaction ( Batch )
        result = posPtr.transactionPrint( ESCPOSConst.CMP_TP_NORMAL );

        // Disconnect
        posPtr.disconnect();

        if ( ESCPOSConst.CMP_SUCCESS != result )
        {
        // Transaction Error
        Toast.makeText( Test.this, "Transaction Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        else
        {
        // Connect Error
        Toast.makeText( Test.this, "Connect or Printer Error : " + Integer.toString( result ), Toast.LENGTH_LONG ).show();
        }
        }
        } );


        }


//
// Save & Load Address
//
private void saveAddress( int portType, String address )
        {
        switch ( portType )
        {
        case ESCPOSConst.CMP_PORT_WiFi:
        savePreferences( PRE_KEY_IP_ADDRESS, address );
        break;
        case ESCPOSConst.CMP_PORT_Bluetooth:
        case ESCPOSConst.CMP_PORT_Bluetooth_Insecure:
        savePreferences( PRE_KEY_BLUETOOTH_ADDRESS, address );
        break;
default:
        break;
        }
        }
private String loadAddress( int portType )
        {
        String address = "";
        switch ( portType )
        {
        case ESCPOSConst.CMP_PORT_WiFi:
        address = loadPreferences( PRE_KEY_IP_ADDRESS, DEFAULT_IP_ADDRESS );
        break;
        case ESCPOSConst.CMP_PORT_Bluetooth:
        case ESCPOSConst.CMP_PORT_Bluetooth_Insecure:
        address = loadPreferences( PRE_KEY_BLUETOOTH_ADDRESS, DEFAULT_BLUETOOTH_ADDRESS );
        break;
default:
        break;
        }
        return address;
        }
private void savePreferences( String sKeyName, String sValue )
        {
        SharedPreferences sp = getSharedPreferences( PREFERENCES_NAME, Activity.MODE_PRIVATE );
        SharedPreferences.Editor e = sp.edit();
        e.putString( sKeyName, sValue );
        e.commit();
        }
private String loadPreferences( String sKeyName, String sDefaultValue )
        {
        SharedPreferences sp = getSharedPreferences( PREFERENCES_NAME, Activity.MODE_PRIVATE );
        return sp.getString( sKeyName, sDefaultValue );
        }

        }
