package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.database.VacationDatabase;
import com.example.d308_vacation_planner.entities.Excursion;
import com.example.d308_vacation_planner.entities.Vacation;

public class ViewExcursionActivity extends AppCompatActivity {
    VacationDatabase database;
    long vacationID = -1;
    long excursionID = -1;
    boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_excursion);
        getSupportActionBar().setTitle("View Excursion");

        vacationID = getIntent().getLongExtra("vacationID", -1);
        excursionID = getIntent().getLongExtra("excursionID", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDAO().getVacation(vacationID);
        Excursion excursion = database.excursionDAO().getExcursion(excursionID);
        ((TextView)findViewById(R.id.excursionTitleText)).setText(excursion.excursionTitle());
        ((TextView)findViewById(R.id.excursionDateText)).setText(AddVacationActivity.dateToString(excursion.excursionDate()));
        ((TextView)findViewById(R.id.excursionVacationTitleText)).setText(vacation.vacationTitle());
        ((CheckBox)findViewById(R.id.excursionAlertBox)).setChecked(excursion.getExcursionAlert());
    }

    public void AlertClick(View view){
        Excursion excursion = database.excursionDAO().getExcursion(excursionID);
        boolean alert = ((CheckBox)findViewById(R.id.excursionAlertBox)).isChecked();
        excursion.setExcursionAlert(alert);
        database.excursionDAO().updateExcursion(excursion);

        if(alert){
            ViewVacationActivity.scheduleNotification(excursion.excursionDate(), "Excursion Time!", excursion.excursionTitle(), (int)excursion.getExcursionID() + (Integer.MAX_VALUE/2), getApplicationContext());
        }
        else{
            ViewVacationActivity.CancelNotification((int)excursion.getExcursionID() + (Integer.MAX_VALUE/2), getApplicationContext());
        }
    }

    public void BackClick(View view){
        Intent intent = new Intent(this, ViewVacationActivity.class);
        intent.putExtra("id", vacationID);
        startActivity(intent);
    }

    public void ClickEdit(View view){
        Intent intent = new Intent(this, EditExcursionActivity.class);
        intent.putExtra("vacationID", vacationID);
        intent.putExtra("excursionID", excursionID);
        startActivity(intent);
    }

    public void ClickDelete(View view){
        database.excursionDAO().deleteExcursion(database.excursionDAO().getExcursion(excursionID));
        BackClick(view);
    }

    @Override
    public void onBackPressed() {
        if (bool==false) {
            Intent intent = new Intent(this, ViewVacationActivity.class);
            intent.putExtra("id", vacationID);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }
    }
}
