package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.database.VacationDatabase;
import com.example.d308_vacation_planner.entities.Vacation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** @noinspection deprecation, SuspiciousIndentAfterControlStatement */
public class AddVacationActivity extends AppCompatActivity {
    VacationDatabase database;
    long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vacation);

        id = getIntent().getLongExtra("id", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        if(id != -1){//edit vacation
            getSupportActionBar().setTitle("Edit Vacation");
            Vacation vacation = database.vacationDAO().getVacation(id);
            ((TextView)findViewById(R.id.editVacationHeader)).setText("Edit vacation");

            ((TextView)findViewById(R.id.editVacationTitle)).setText(vacation.vacationTitle());
            ((TextView)findViewById(R.id.editVacationHotel)).setText(vacation.vacationHotel());
            ((TextView)findViewById(R.id.editVacationStartDate)).setText(dateToString(new Date(vacation.vacationStart())));
            ((TextView)findViewById(R.id.editVacationEndDate)).setText(dateToString(new Date(vacation.vacationEnd())));

            MaterialButton dateButton = findViewById(R.id.dateSelect);
            dateButton.setOnClickListener(view -> {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date dateOffset = new Date(selection.first);
                    dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                    String date1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                    dateOffset = new Date(selection.second);
                    dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                    String date2 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                    ((TextView)findViewById(R.id.editVacationStartDate)).setText(date1);
                    ((TextView)findViewById(R.id.editVacationEndDate)).setText(date2);
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            });
        }
        else //new vacation
            getSupportActionBar().setTitle("Add Vacation");
            MaterialButton dateButton = findViewById(R.id.dateSelect);
            dateButton.setOnClickListener(view -> {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                Date dateOffset = new Date(selection.first);
                dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                String date1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                dateOffset = new Date(selection.second);
                dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                String date2 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                ((TextView)findViewById(R.id.editVacationStartDate)).setText(date1);
                ((TextView)findViewById(R.id.editVacationEndDate)).setText(date2);
            });
            materialDatePicker.show(getSupportFragmentManager(), "tag");
        });
    }


    public void CreateVacationOnClick(View view){
        String title = ((EditText)findViewById(R.id.editVacationTitle)).getText().toString();
        String vacationHotel = ((EditText)findViewById(R.id.editVacationHotel)).getText().toString();
        String startDate = ((EditText)findViewById(R.id.editVacationStartDate)).getText().toString();
        String endDate = ((EditText)findViewById(R.id.editVacationEndDate)).getText().toString();

        if(title.isEmpty() || vacationHotel.isEmpty() || startDate.isEmpty() || endDate.isEmpty()){
            DisplayPopup(this, "One or more fields empty");
            return;
        }
        else if(!isValidDate(startDate)){
            DisplayPopup(this, "Invalid Start Date");
            return;
        }
        else if(!isValidDate(endDate)){
            DisplayPopup(this, "Invalid End Date");
            return;
        }
        else if(!(new Date(startDate).before(new Date(endDate)))){
            DisplayPopup(this, "Start Date must be before End Date");
            return;
        }

        if(id == -1) {
            Vacation v = new Vacation(title, vacationHotel, new Date(startDate).getTime(), new Date(endDate).getTime());
            id = database.vacationDAO().addVacation(v);
        }
        else{
            Vacation v = new Vacation(id, title, vacationHotel, new Date(startDate).getTime(), new Date(endDate).getTime());
            v.setVacationAlert(database.vacationDAO().getVacation(id).getVacationAlert());
            database.vacationDAO().updateVacation(v);
        }

        Intent intent = new Intent(this, ViewVacationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public static String dateToString(long date){
        return dateToString(new Date(date));
    }
    public static String dateToString(Date date){
        return (date.getMonth()+1)+"/"+date.getDate()+"/"+(date.getYear()+1900);
    }

    public static boolean isValidDate(String date){
        try{
            Date d = new Date(date);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public static void DisplayPopup(AppCompatActivity activity, String text){
        View popup = MainActivity.LI.inflate(R.layout.popup, null);
        ((TextView)popup.findViewById(R.id.popupText)).setText(text);
        popup.findViewById(R.id.popupButton).setOnClickListener(view ->
                ((ViewGroup)activity.getWindow().getDecorView().getRootView()).removeView(popup));
        ((ViewGroup)activity.getWindow().getDecorView().getRootView()).addView(popup);
    }

    @Override
    public void onBackPressed() {
        if (id!=-1) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }
    }
}
