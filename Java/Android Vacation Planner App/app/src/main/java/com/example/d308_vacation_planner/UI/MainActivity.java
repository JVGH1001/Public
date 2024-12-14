package com.example.d308_vacation_planner.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.database.VacationDatabase;
import com.example.d308_vacation_planner.entities.Vacation;

import java.text.DateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static LayoutInflater LI;
    VacationDatabase database;
    int vacationsNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Vacation Planner");

        database = VacationDatabase.getDatabase(getApplicationContext());

        LI = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        List<Vacation> vacations = database.vacationDAO().getVacations();
        if(!vacations.isEmpty()) {
            for (Vacation v : vacations) {
                AddEntry(v);
            }
        }
        else{
            displayNoVacation();
        }
    }

    void AddEntry(Vacation v){
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);

        View vacation = LI.inflate(R.layout.vacation_entry, null);
        ((TextView)vacation.findViewById(R.id.vacationTitleEntry)).setText(v.vacationTitle());
        ((TextView)vacation.findViewById(R.id.vacationAccomodationEntry)).setText("at "+v.vacationHotel());
        ((TextView)vacation.findViewById(R.id.vacationDateEntry)).setText(dateFormat.format(new Date(v.vacationStart())) + " - " + dateFormat.format(new Date(v.vacationEnd())));

        long id = v.getVacationID();
        MainActivity activity = this;

        vacation.findViewById(R.id.deleteVacationButton).setOnClickListener(view -> {
            if(!database.excursionDAO().getExcursions(id).isEmpty()){
                com.example.d308_vacation_planner.UI.AddVacationActivity.DisplayPopup(activity, "You can't delete a vacation while it still has excursions");
                return;
            }

            Vacation v1 = database.vacationDAO().getVacation(id);
            database.vacationDAO().deleteVacation(v1);
            ((ViewGroup)findViewById(R.id.insertPoint)).removeView(vacation);

            vacationsNumber--;
            if(vacationsNumber == 0)
                displayNoVacation();
        });

        vacation.findViewById(R.id.editVacationButton).setOnClickListener(view -> {
            Intent intent = new Intent(activity, com.example.d308_vacation_planner.UI.AddVacationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        vacation.findViewById(R.id.viewVacationButton).setOnClickListener(view -> {
            Intent intent = new Intent(activity, ViewVacationActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

        ((ViewGroup)findViewById(R.id.insertPoint)).addView(vacation);
        vacationsNumber++;
    }

    public void AddVacation(View view){
        Intent intent = new Intent(this, com.example.d308_vacation_planner.UI.AddVacationActivity.class);
        startActivity(intent);
    }

    void displayNoVacation(){
        View noVacation = LI.inflate(R.layout.no_vacation, null);
        ((ViewGroup)findViewById(R.id.insertPoint)).addView(noVacation);
    }

    public static String ExportVacation(Vacation vacation){

        String[] strings = new String[]{vacation.vacationTitle(), vacation.vacationHotel(), com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(vacation.vacationStart()), com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(vacation.vacationEnd())};

        //$ - escape
        //~ - separator
        for (String string : strings) {
            string.replaceAll("\\$", "$$");
            string.replaceAll("~", "$~");
        }

        //strings combine
        String combined = strings[0];
        for(int i = 1; i < strings.length; i++){
            combined += "~" + strings[i];
        }

        //return encoded data
        return Base64.getEncoder().encodeToString(combined.getBytes());
    }
}