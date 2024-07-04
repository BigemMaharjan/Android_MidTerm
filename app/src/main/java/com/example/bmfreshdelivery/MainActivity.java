package com.example.bmfreshdelivery;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bmfreshdelivery.databinding.ActivityMainBinding;
import com.example.bmfreshdelivery.models.SubscriptionClass;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    //to store radio button value
    String subPlan;

    // to store plan type that is selected from the spinner
    String selectedPlanType;

    //to store PlanType price according to subscription
    double planTypePrice = 0;

    //to store the price of the additional
    double milkPrice = 0;

    double lemonadePrice = 0;

    // to store the delivery type and it's price
    String selectedDelivery;

    double deliveryTypePrice = 0;


    //    Creating an instance of the generated binding class
    ActivityMainBinding binding;

    //    DatePicker
    DatePickerDialog datePicker;

    //Creating an instance of the Subscription Class
    SubscriptionClass SClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);

        //        instantiating the binding view
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //Setting for the subscription plan radio button
        binding.rdMonthly.setChecked(false);
        binding.rdYearly.setChecked(false);

        binding.chkUpdates.setChecked(false);
        binding.chkDesigns.setChecked(false);


        //For Date picker
        binding.edtDate.setInputType(InputType.TYPE_NULL);

        //Listeners
        setListeners();


        //        Creating an ArrayAdapter to fetch the items from an array for the Type of Delivery
        ArrayAdapter<CharSequence> adapterSpace = ArrayAdapter.createFromResource(this,
                R.array.deliveryType_array, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Specify the layout to use when the list of choices appears
        adapterSpace.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Bind the adapter to the spinner
        binding.spnDesignTypes.setAdapter(adapterSpace);


//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

//    Setting the listeners
    private void setListeners(){
        binding.chkDesigns.setOnClickListener(this);
        binding.chkUpdates.setOnClickListener(this);
        binding.rgSubsPlan.setOnCheckedChangeListener(this);
        binding.spnPlanType.setOnItemSelectedListener(this);
        binding.spnDesignTypes.setOnItemSelectedListener(this);
        binding.edtDate.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
    }


    //onClick Method
    @Override
    public void onClick(View v) {
            if(v.getId() == R.id.edtDate){
//            If the date EditText is clicked, open a DatePickerDialog
            Calendar cal = Calendar.getInstance();
            int dayOfSales = cal.get(Calendar.DAY_OF_MONTH);
            int monthOfSales = cal.get(Calendar.MONTH);
            int yearOfSales = cal.get(Calendar.YEAR);
            datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day){
// Ensuring date is of past or the current date
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, day);

                    // Normalize selectedDate to the start of the day
                    selectedDate.set(Calendar.HOUR_OF_DAY, 0);
                    selectedDate.set(Calendar.MINUTE, 0);
                    selectedDate.set(Calendar.SECOND, 0);
                    selectedDate.set(Calendar.MILLISECOND, 0);

                    if(selectedDate.after(cal)){
                        binding.edtDate.setError("Sales date cannot be in the future.");
                    }else{
                        binding.edtDate.setError(null);
                        binding.edtDate.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }
            }, yearOfSales, monthOfSales, dayOfSales);

            // Set the max date to today
            datePicker.getDatePicker().setMaxDate(cal.getTimeInMillis());

//            Show the DatePickerDialog
            datePicker.show();
        }
        else if(v.getId() == R.id.btnSubmit){
            //Validating the form, once the submit button is clicked
            if(formValidate()){
                //   If check update  is checked which is 4L Milk
                if(binding.chkUpdates.isChecked()) {
                    milkPrice = binding.rdMonthly.isChecked() ? 5 : binding.rdYearly.isChecked() ? 50 : 0;
                } else {
                    milkPrice = 0;
                }
//            If check design is checked which is 3L Lemonade
                if(binding.chkDesigns.isChecked()) {
                    lemonadePrice = binding.rdMonthly.isChecked() ? 8 : binding.rdYearly.isChecked() ? 80 : 0;
                }else{
                    lemonadePrice = 0;
                }

                //Creating Subscription Class object with the entered details
                SClass = new SubscriptionClass(binding.edtName.getText().toString(), subPlan, planTypePrice, milkPrice, lemonadePrice, deliveryTypePrice);

                // Getting the subscription details as a formatted string
                String result = SClass.getSubscriptionClass();

                //Creating a Snackbar to display the subscription details
                Snackbar snackbar = Snackbar.make(binding.mealDel, result, Snackbar.LENGTH_LONG);
                snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Okay", new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
//                        Dismissing the Snackbar once the 'Okay' button is clicked
                        snackbar.dismiss();
                    }
                });


                // Get the Snackbar's TextView and customize it if necessary
                View snackbarView = snackbar.getView();
                TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setMaxLines(3); // Allow multiple lines if needed

//                Showing the Snackbar
                snackbar.show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //For the Plan Type
        if(adapterView.getId() == binding.spnPlanType.getId()) {

            selectedPlanType = binding.spnPlanType.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), selectedPlanType, Toast.LENGTH_LONG).show();

            //Calling the plan type update function
            updatePlanType(selectedPlanType);
        }

        //For the delivery Type
        else if (adapterView.getId() == binding.spnDesignTypes.getId()){
            selectedDelivery = binding.spnDesignTypes.getItemAtPosition(i).toString();
            Toast.makeText(getApplicationContext(), selectedDelivery, Toast.LENGTH_LONG).show();

            //Calling the delivery type update function
            updateDeliveryType(selectedDelivery);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

//    For Radio button
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        //For updating the plan type according to subscription plan selected
        if(radioGroup.getId() == binding.rgSubsPlan.getId()){
//            Checking whether the monthly plan is selected
            if(checkedId == binding.rdMonthly.getId()){
                subPlan = "Monthly";
                setPlanType(R.array.monthMeal_array);
            }
//            Checking whether the yearly plan is selected
            else if(checkedId == binding.rdYearly.getId()){
                subPlan = "Yearly";
                setPlanType(R.array.yearVeggies_array);
            }
        }
    }

    //Creating planType method
    private void setPlanType(int arrayResId){
        //        Creating an ArrayAdapter to fetch the items from an array
        ArrayAdapter<CharSequence> adapterSpacePlanType = ArrayAdapter.createFromResource(this,
                arrayResId, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Specifying the layout to use when the list of choices appears
        adapterSpacePlanType.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

//        Binding the adapter to the spinner
        binding.spnPlanType.setAdapter(adapterSpacePlanType);
    }

//    Update plan type function
    private void updatePlanType(String upPlTy){
//        Checking whether the monthly plan is selected
        if(binding.rdMonthly.isChecked()){
//            using switch case and storing the price as per the customer selection
            switch(upPlTy){
                case "Cooked Meal":
                    this.planTypePrice = 10;
                    break;
                case "Uncooked Meal":
                    this.planTypePrice = 15;
                    break;
            }
        }
        //        Checking whether the yearly plan is selected
        else if (binding.rdYearly.isChecked()){
//            using switch case and storing the price as per the customer selection
            switch(upPlTy){
                case "Veggies with Recipe":
                    this.planTypePrice = 110;
                    break;
                case "Veggies Only":
                    this.planTypePrice = 160;
            }
        }
    }

    //    Update delivery type function
    private void updateDeliveryType(String upDeTy){
        //        Checking whether the monthly plan is selected
        if(binding.rdMonthly.isChecked()){
            //            using switch case and storing the price as per the customer selection
            switch(upDeTy){
                case "Store pick-up":
                    this.deliveryTypePrice = 0;
                    break;
                case "Community Box":
                    this.deliveryTypePrice = 2;
                    break;
                case "Door Step":
                    this.deliveryTypePrice = 4;
                    break;
            }
        }
        //        Checking whether the yearly plan is selected
        else if (binding.rdYearly.isChecked()){
            //            using switch case and storing the price as per the customer selection
            switch(upDeTy){
                case "Store pick-up":
                    this.deliveryTypePrice = 0;
                    break;
                case "Community Box":
                    this.deliveryTypePrice = 22;
                    break;
                case "Door Step":
                    this.deliveryTypePrice = 42;
                    break;
            }
        }
    }


//    Form Validation
    private boolean formValidate(){
        //Validating Customer Name
        if(binding.edtName.length() == 0){
            binding.edtName.setError("Customer Name is required");
            return false;
        }

        //Validating Email ID
        if(binding.edtEmail.length() == 0){
            binding.edtEmail.setError("Email ID is required");
            return false;
        }  else if(!isValidEmail(binding.edtEmail.getText().toString())) {
            binding.edtEmail.setError("Invalid email address");
            return false;
        }

        //        Validating subscription plan
        if (binding.rgSubsPlan.getCheckedRadioButtonId() == -1) {
            Toast.makeText(MainActivity.this, "No subscription plan is selected", Toast.LENGTH_SHORT).show();
            return false;
        }

        //Validating the delivery type
        if(selectedDelivery.isEmpty()){
            binding.edtName.setError("Type of Delivery must be selected");
            return false;
        }

        // Validating the date
        if (binding.edtDate.getText().toString().isEmpty()) {
            binding.edtDate.setError("Date selection is required");
            return false;
        }

        return true;
    }


    //    Function to check the valid email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}