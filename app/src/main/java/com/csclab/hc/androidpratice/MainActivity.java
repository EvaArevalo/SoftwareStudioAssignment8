package com.csclab.hc.androidpratice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /** Init Variable for IP page **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";

    /** Init Variable for Calculator **/
    EditText inputNumTxt1;
    EditText inputNumTxt2;
    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;

    /** Init Variable for Result**/
    TextView textResult;
    Button return_button;

    /** Init Variable **/
    String oper = "";

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jumpToIPLayout();
    }

    //Function for IP Page Setup
    public void jumpToIPLayout(){
        setContentView(R.layout.ip_page);
        inputIP = (EditText)findViewById(R.id.edIP);
        ipSend = (Button)findViewById(R.id.ipButton);

        ipSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAdd = inputIP.getText().toString();
                jumpToCalculatorLayout();
            }
        });
    }

    //Function for Calculator Page Setup
    public void jumpToCalculatorLayout() {
        setContentView(R.layout.activity_main);

        inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
    }

    /** Function for onclick() implement */
    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        // read EditText and fill variables with numbers
        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        // caculate result
        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            default:
                break;
        }
        // Using log.d to check
        Log.d("debug","ANS "+result);
        Log.d("Client","Client Send");
        //Pass the result String to jumpToResultLayout() and show the result at Result view
        jumpToResultLayout(new String(num1 + " " + oper + " " + num2 + " = " + result));
        //Send result to Server
        Thread t = new thread();
        t.start();
    }

    public void jumpToResultLayout(String resultStr){
        setContentView(R.layout.result_page);

        //Bind return_button and textResult form result view
        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null) {
            //Set the result text
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            // prepare button listener for return button
            return_button.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    jumpToCalculatorLayout();
                }

            });
        }
    }

    //inner Thread class
    class thread extends Thread{
        public void run(){
            try{
                System.out.println("Client: Waiting to connect...");
                int serverPort = 2000;

                // Create socket connect server
                Socket socket = new Socket(ipAdd, serverPort);
                System.out.println("Connected!");

                // Create stream communicate with server
                OutputStream out = socket.getOutputStream();
                String strToSend = "Hi I'm client";

                byte[] sendStrByte = new byte[1024];
                System.arraycopy(strToSend.getBytes(), 0, sendStrByte, 0, strToSend.length());
                out.write(sendStrByte);

            }catch (Exception e){
                System.out.println("Error" + e.getMessage());
            }
        }
    }
}
