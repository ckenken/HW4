package com.example.ckenken.hw4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    final public static int EMPTY_STATE = 1;
    final public static int ACCUMULATE_1_STATE = 2;
    final public static int PENDING_OP_STATE = 3;
    final public static int ACCUMULATE_2_STATE = 4;
    final public static int SHOW_RESULT_STATE = 5;
    final public static int ERROR_STATE = 6;

    Button mButtonZero;
    Button mButtonOne;
    Button mButtonTwo;
    Button mButtonThree;
    Button mButtonFour;
    Button mButtonFive;
    Button mButtonSix;
    Button mButtonSeven;
    Button mButtonEight;
    Button mButtonNine;
    Button mButtonEqual;
    Button mButtonPlus;
    Button mButtonMinus;
    Button mButtonDiv;
    Button mButtonTime;
    Button mButtonC;

    EditText mAnswer;

    public char operator;

    Stack<Double> stack;

    NumberFormat formatter = new DecimalFormat("#0.0000");

    public int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        operator = '_';

        stack = new Stack<Double>();

        mButtonC = (Button) findViewById(R.id.buttonC);
        mButtonPlus = (Button) findViewById(R.id.buttonPlus);
        mButtonMinus = (Button) findViewById(R.id.buttonMinus);
        mButtonTime = (Button) findViewById(R.id.buttonTime);
        mButtonDiv = (Button) findViewById(R.id.buttonDiv);
        mButtonEqual = (Button) findViewById(R.id.buttonEqual);
        mButtonZero = (Button) findViewById(R.id.buttonZero);
        mButtonOne = (Button) findViewById(R.id.buttonOne);
        mButtonTwo = (Button) findViewById(R.id.buttonTwo);
        mButtonThree = (Button) findViewById(R.id.buttonThree);
        mButtonFour = (Button) findViewById(R.id.buttonFour);
        mButtonFive = (Button) findViewById(R.id.buttonFive);
        mButtonSix = (Button) findViewById(R.id.buttonSix);
        mButtonSeven = (Button) findViewById(R.id.buttonSeven);
        mButtonEight = (Button) findViewById(R.id.buttonEight);
        mButtonNine = (Button) findViewById(R.id.buttonNine);

        mAnswer = (EditText) findViewById(R.id.editText);

        mButtonZero.setOnClickListener(new NumberClickListener());
        mButtonOne.setOnClickListener(new NumberClickListener());
        mButtonTwo.setOnClickListener(new NumberClickListener());
        mButtonThree.setOnClickListener(new NumberClickListener());
        mButtonFour.setOnClickListener(new NumberClickListener());
        mButtonFive.setOnClickListener(new NumberClickListener());
        mButtonSix.setOnClickListener(new NumberClickListener());
        mButtonSeven.setOnClickListener(new NumberClickListener());
        mButtonEight.setOnClickListener(new NumberClickListener());
        mButtonNine.setOnClickListener(new NumberClickListener());

        mButtonPlus.setOnClickListener(new OperatorClickListener());
        mButtonMinus.setOnClickListener(new OperatorClickListener());
        mButtonTime.setOnClickListener(new OperatorClickListener());
        mButtonDiv.setOnClickListener(new OperatorClickListener());

        mAnswer.setKeyListener(null);

        state = EMPTY_STATE;

        mButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = EMPTY_STATE;
                mAnswer.setText("0");
                operator = '_';
                stack.clear();
            }
        });

        mButtonEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;

                double now = 0.0;
                double previous = 0.0;
                double answer = 0.0;

                switch (state) {
                    case EMPTY_STATE:
                        break;
                    case ACCUMULATE_1_STATE:
                        break;
                    case PENDING_OP_STATE:
                        state = ACCUMULATE_1_STATE;
                        operator = '_';
                        break;
                    case ACCUMULATE_2_STATE:
                        state = SHOW_RESULT_STATE;
                        now = Double.parseDouble(mAnswer.getText().toString());
                        previous = stack.pop();

                        switch (operator) {
                            case '+':
                                answer = previous + now;
                                mAnswer.setText(formatter.format(answer));
                                break;
                            case '-':
                                answer = previous - now;
                                mAnswer.setText(formatter.format(answer));
                                break;
                            case '*':
                                answer = previous * now;
                                mAnswer.setText(formatter.format(answer));
                                break;
                            case '/':
                                if (now == 0) {
                                    overflow();
                                    Log.e("Divide by zero", "Divide by Zero!");
                                    state = EMPTY_STATE;
                                    mAnswer.setText("0");
                                    operator = '_';
                                    stack.clear();
                                }
                                else {
                                    try {
                                        answer = previous / now;
                                        mAnswer.setText(formatter.format(answer));
                                    } catch (ArithmeticException e) {
                                        Log.e("Divide by zero", "Divide by Zero!");
                                        state = ACCUMULATE_2_STATE;
                                        stack.push(previous);
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                        operator = '_';
                        break;
                    case SHOW_RESULT_STATE:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    class NumberClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            Log.d("State", Integer.toString(state));

            switch (state) {
                case EMPTY_STATE:
                    if (!b.getText().toString().equals("0")) {
                        state = ACCUMULATE_1_STATE;
                        mAnswer.setText(b.getText().toString());
                    }
                    break;
                case PENDING_OP_STATE:
                    state = ACCUMULATE_2_STATE;
                    mAnswer.setText(b.getText().toString());
                    break;
                case ACCUMULATE_1_STATE:
                case ACCUMULATE_2_STATE:
                    if (!(b.getText().toString().equals("0") && mAnswer.getText().toString().equals("0"))) {
                        mAnswer.setText(mAnswer.getText().toString() + b.getText().toString());
                    }
                    break;
                case SHOW_RESULT_STATE:
                    state = ACCUMULATE_1_STATE;
                    mAnswer.setText(b.getText().toString());
                    break;
                default:
                    break;
            }
        }
    };

    class OperatorClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            double now = 0.0;
            double previous = 0.0;
            double answer = 0.0;

            switch (state) {
                case EMPTY_STATE:
                    state = PENDING_OP_STATE;
                    stack.push(0.0);
                    operator = b.getText().toString().charAt(0);
                    break;
                case PENDING_OP_STATE:
                    operator = b.getText().toString().charAt(0);
                    break;
                case ACCUMULATE_1_STATE:
                    state = PENDING_OP_STATE;
                    stack.push(Double.parseDouble(mAnswer.getText().toString()));
                    operator = b.getText().toString().charAt(0);
                    break;
                case ACCUMULATE_2_STATE:
                    state = PENDING_OP_STATE;
                    now = Double.parseDouble(mAnswer.getText().toString());
                    previous = stack.pop();

                    switch (operator) {
                        case '+':
                            answer = previous + now;
                            stack.push(answer);
                            mAnswer.setText(formatter.format(answer));
                            break;
                        case '-':
                            answer = previous - now;
                            stack.push(answer);
                            mAnswer.setText(formatter.format(answer));
                            break;
                        case '*':
                            answer = previous * now;
                            stack.push(answer);
                            mAnswer.setText(formatter.format(answer));
                            break;
                        case '/':

                            if (now == 0) {
                                overflow();
                                Log.e("Divide by zero", "Divide by Zero!");
                                state = EMPTY_STATE;
                                mAnswer.setText("0");
                                operator = '_';
                                stack.clear();
                            }
                            else {
                                try {
                                    answer = previous / now;
                                    stack.push(answer);
                                    mAnswer.setText(formatter.format(answer));
                                } catch (ArithmeticException e) {
                                    Log.e("Divide by zero", "Divide by Zero!");
                                    state = ACCUMULATE_2_STATE;
                                    stack.push(previous);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                    operator = b.getText().toString().charAt(0);
                    break;
                case SHOW_RESULT_STATE:
                    state = PENDING_OP_STATE;
                    stack.push(Double.parseDouble(mAnswer.getText().toString()));
                    operator = b.getText().toString().charAt(0);
                    break;
                default:
                    break;
            }
        }
    }


    private void overflow()
    {
        Log.e("OverFlow:", "Number overflow");

        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle("Oops!");
        ad.setMessage("Number overflow or Divide to Zero!");

        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        ad.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_result:
                Intent intent = new Intent(MainActivity.this, AsusShow.class);
                intent.putExtra("answer", mAnswer.getText().toString());
                startActivity(intent);
                break;
            case R.id.action_about:
                AlertDialog.Builder ad = new AlertDialog.Builder(this);
                ad.setTitle("About this App");
                ad.setMessage("Author: ckenken\nEmail: ChiaHsiang_Kuo@asus.com");

                ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                ad.show();
                break;
            case R.id.action_help:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
                break;
            case R.id.action_exit:
                finish();
                break;
            default:
                break;
        }

//        if (id == R.id.action_setting) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
