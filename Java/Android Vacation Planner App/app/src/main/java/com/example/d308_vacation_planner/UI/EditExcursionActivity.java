package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.database.VacationDatabase;
import com.example.d308_vacation_planner.entities.Excursion;
import com.example.d308_vacation_planner.entities.Vacation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** @noinspection deprecation, SuspiciousIndentAfterControlStatement */
public class EditExcursionActivity extends AppCompatActivity {
    VacationDatabase database;
    long vacationID = -1;
    long excursionID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_excursion);

        vacationID = getIntent().getLongExtra("vacationID", -1);
        excursionID = getIntent().getLongExtra("excursionID", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDAO().getVacation(vacationID);
        ((TextView)findViewById(R.id.editExcursionVacationTitle)).setText(vacation.vacationTitle());

        if(excursionID != -1){ //edit excursion
            getSupportActionBar().setTitle("Edit Excursion");
            Excursion e = database.excursionDAO().getExcursion(excursionID);
            ((TextView)findViewById(R.id.editExcursionVacationTitle)).setText(vacation.vacationTitle());
            ((EditText)findViewById(R.id.editExcursionTitle)).setText(e.excursionTitle());
            ((EditText)findViewById(R.id.editExcursionDate)).setText(AddVacationActivity.dateToString(e.excursionDate()));

            MaterialButton dateButton = findViewById(R.id.dateSelection);
            dateButton.setOnClickListener(view -> {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker().build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    Date dateOffset = new Date(selection);
                    dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                    String date1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                    ((TextView)findViewById(R.id.editExcursionDate)).setText(date1);
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            });
        }
        else //new excursion
            getSupportActionBar().setTitle("Add Excursion");
            MaterialButton dateButton = findViewById(R.id.dateSelection);
            dateButton.setOnClickListener(view -> {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker().build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                Date dateOffset = new Date(selection);
                dateOffset.setMinutes(dateOffset.getMinutes() + dateOffset.getTimezoneOffset());
                String date1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(dateOffset);
                ((TextView) findViewById(R.id.editExcursionDate)).setText(date1);
            });
            materialDatePicker.show(getSupportFragmentManager(), "tag");
        });
    }

    public void SaveClick(View view){
        String title = ((EditText)findViewById(R.id.editExcursionTitle)).getText().toString();
        String date = ((EditText)findViewById(R.id.editExcursionDate)).getText().toString();
        Vacation vacation = database.vacationDAO().getVacation(vacationID);

        if(title.isEmpty() || date.isEmpty()){
            AddVacationActivity.DisplayPopup(this, "One or more fields empty");
            return;
        }
        if(!AddVacationActivity.isValidDate(date)){
            AddVacationActivity.DisplayPopup(this, "Invalid Date");
            return;
        }
        if(new Date(date).before(new Date(vacation.vacationStart())) || new Date(date).after(new Date(vacation.vacationEnd()))){
            AddVacationActivity.DisplayPopup(this, "Excursion date must be during vacation start & end");
            return;
        }

        if(excursionID != -1){
            Excursion excursion = new Excursion(excursionID, title, new Date(date).getTime(), vacationID);
            database.excursionDAO().updateExcursion(excursion);
        }
        else {
            Excursion excursion = new Excursion(title, new Date(date).getTime(), vacationID);
            excursionID = database.excursionDAO().addExcursion(excursion);
        }

        Intent intent = new Intent(this, ViewExcursionActivity.class);
        intent.putExtra("vacationID", vacationID);
        intent.putExtra("excursionID", excursionID);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (excursionID==-1) {
            Intent intent = new Intent(this, ViewVacationActivity.class);
            intent.putExtra("id", vacationID);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }
    }
}
