package com.example.android.wir_tecrepo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class JustJava extends AppCompatActivity {
    private int qty = 1;
    private double basePrice = 5;
    private double whipCreamPrice = 1;
    private double chocoPrice = 2;
    private boolean isCreamWhipped = false;
    boolean isWithChoco = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.just_java);

        Context context = getApplicationContext();
        CharSequence text = "Welcome!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {

        EditText nameField = (EditText) findViewById(R.id.name_edit_view);
        String name = nameField.getText().toString();

        displayQty(qty);
        displayMessage(createOrderSummary(name));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(name));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the total price of the order based on the current quantity.
     *
     * @param quantity is the number of cups of coffee ordered
     * @return the price
     */
    private double calculatePrice(int quantity) {
        double totalPrice = basePrice;
        if (isCreamWhipped) {
            totalPrice += whipCreamPrice;
        }
        if (isWithChoco) {
            totalPrice += chocoPrice;
        }
        return totalPrice * quantity;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQty(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    private String displayPrice(int number) {
        return "Total: " + NumberFormat.getCurrencyInstance().format(calculatePrice(number));
    }

    /**
     * Create summary of the order.
     *
     * @param name is the the name from the name field
     * @return text summary
     */
    private String createOrderSummary(String name) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText("Order Summary");
        String summary = "Name: " + name + "\n" +
                "Add Whipped Cream? " + isCreamWhipped + "\n" +
                "Add Chocolate? " + isWithChoco + "\n" +
                "Quantity: " + qty + "\n" +
                displayPrice(qty) + "\n" +
                getString(R.string.thank_you);
        return summary;
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String msg) {

        TextView msgTextView = (TextView) findViewById(R.id.msg_text_view);
        msgTextView.setText(msg);
    }

    /**
     * This method increments the given quantity on the screen.
     */
    public void increment(View view) {
        if (qty == 100) {
            Toast.makeText(this, "You cannot have more than 100 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        qty++;
        displayQty(qty);
        displayMessage(displayPrice(qty));
    }

    /**
     * This method decrements the given quantity on the screen.
     */
    public void decrement(View view) {
        if (qty == 1) {
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        qty--;
        displayQty(qty);
        displayMessage(displayPrice(qty));
    }

    /**
     * This method sets the isCreamWhipped to either true or false depending on the state of the checkbox
     */
    public void setCreamWhipped(View view) {
        CheckBox chxWhipCream = (CheckBox) findViewById(R.id.whipped_checkbox);
        isCreamWhipped = chxWhipCream.isChecked();
        displayMessage(displayPrice(qty));
    }

    /**
     * This method sets the isWithChoco to either true or false depending on the state of the checkbox
     */
    public void setWithChoco(View view) {
        CheckBox chxChoco = (CheckBox) findViewById(R.id.choco_checkbox);
        isWithChoco = chxChoco.isChecked();
        displayMessage(displayPrice(qty));
    }
}
