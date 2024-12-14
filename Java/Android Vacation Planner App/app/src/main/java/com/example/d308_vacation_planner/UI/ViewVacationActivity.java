package com.example.d308_vacation_planner.UI;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.database.VacationDatabase;
import com.example.d308_vacation_planner.entities.Excursion;
import com.example.d308_vacation_planner.entities.Vacation;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewVacationActivity extends AppCompatActivity {
    VacationDatabase database;
    long id = -1;
    int numExcursions = 0;
    boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_vacation);
        getSupportActionBar().setTitle("View Vacation");

        id = getIntent().getLongExtra("id", -1);
        database = VacationDatabase.getDatabase(getApplicationContext());

        Vacation vacation = database.vacationDAO().getVacation(id);
        ((TextView)findViewById(R.id.vacationTitleText)).setText(vacation.vacationTitle());
        ((TextView)findViewById(R.id.vacationAccommodationText)).setText(vacation.vacationHotel());
        ((TextView)findViewById(R.id.vacationStartDateText)).setText(com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(vacation.vacationStart()));
        ((TextView)findViewById(R.id.vacationEndDateText)).setText(com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(vacation.vacationEnd()));
        ((CheckBox)findViewById(R.id.vacationAlertBox)).setChecked(vacation.getVacationAlert());

        List<Excursion> excursions = database.excursionDAO().getExcursions(id);
        numExcursions = excursions.size();
        if(!excursions.isEmpty()) {
            for (Excursion e : excursions) {
                AddExcursion(e.getExcursionID());
            }
        }
        else{
            noExcursions();
        }
    }

    public void ClickHome(View view){
        Intent intent = new Intent(this, com.example.d308_vacation_planner.UI.MainActivity.class);
        startActivity(intent);
    }

    public void ClickEdit(View view){
        Intent intent = new Intent(this, com.example.d308_vacation_planner.UI.AddVacationActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void ClickDelete(View view){
        if(!database.excursionDAO().getExcursions(id).isEmpty()){
            com.example.d308_vacation_planner.UI.AddVacationActivity.DisplayPopup(this, "Cannot delete vacation with excursions");
            return;
        }

        database.vacationDAO().deleteVacation(database.vacationDAO().getVacation(id));
        ClickHome(view);
    }

    public void ClickAlert(View view){
        Vacation vacation = database.vacationDAO().getVacation(id);
        boolean checked = ((CheckBox)findViewById(R.id.vacationAlertBox)).isChecked();
        vacation.setVacationAlert(checked);
        database.vacationDAO().updateVacation(vacation);

        if(checked) {
            scheduleNotification(vacation.vacationStart(), "Vacation Time!", vacation.vacationTitle(), (int)vacation.getVacationID(), getApplicationContext());
            scheduleNotification(vacation.vacationEnd(), "Vacation time is over :'( ", vacation.vacationTitle(), (((int)vacation.getVacationID()) * -1) - 1, getApplicationContext());
        }
        else{
            CancelNotification((int)vacation.getVacationID(), getApplicationContext());
            CancelNotification((((int)vacation.getVacationID()) * -1) - 1, getApplicationContext());
        }
    }

    public static void scheduleNotification(long date, String title, String text, int id, Context context){
        Intent intent = new Intent(context, com.example.d308_vacation_planner.UI.NotificationBroadcast.class);
        intent.putExtra("notificationTitle", title);
        intent.putExtra("notificationText", text);
        intent.putExtra("notificationId", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ((AlarmManager) context.getSystemService((ALARM_SERVICE))).setAlarmClock(new AlarmManager.AlarmClockInfo(unixToWallTime(date), null), pendingIntent);
    }

    public static void CancelNotification(int id, Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);
        ((AlarmManager) context.getSystemService((ALARM_SERVICE))).cancel(PendingIntent.getBroadcast(context, id, new Intent(context, com.example.d308_vacation_planner.UI.NotificationBroadcast.class), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    static long unixToWallTime(long date){
        long nowWallTime = System.currentTimeMillis();
        long nowUnixTime = new Date().getTime();
        return date - nowUnixTime + nowWallTime;
    }

    public void ClickShare(View view){
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        Vacation v = database.vacationDAO().getVacation(id);
        share.putExtra(Intent.EXTRA_TEXT, "Lets go on Vacation!\nTitle: " + v.vacationTitle() + "\nAccommodation: " + v.vacationHotel() + "\nStart Date: " + com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(v.vacationStart()) + "\nEnd Date: " + com.example.d308_vacation_planner.UI.AddVacationActivity.dateToString(v.vacationEnd()) + "\nImport code: " + com.example.d308_vacation_planner.UI.MainActivity.ExportVacation(v));
        share.setType("text/plain");
        startActivity(share);
    }

    public void ClickAddExcursion(View v){
        Intent intent = new Intent(this, com.example.d308_vacation_planner.UI.EditExcursionActivity.class);
        intent.putExtra("vacationID", id);
        startActivity(intent);
    }

    void AddExcursion(long eId){
        Locale locale = new Locale.Builder().setLanguage("en").setRegion("US").build();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        Excursion e = database.excursionDAO().getExcursion(eId);

        final long excursionID = eId;
        ViewVacationActivity activity = this;

        View excursion = com.example.d308_vacation_planner.UI.MainActivity.LI.inflate(R.layout.excursion_entry, null);
        ((TextView)excursion.findViewById(R.id.excursionEntryTitle)).setText(e.excursionTitle() + " - " + dateFormat.format(new Date(e.excursionDate())));

        //view
        excursion.findViewById(R.id.viewExcursionEntryButton).setOnClickListener(view -> {
            Intent intent = new Intent(activity, com.example.d308_vacation_planner.UI.ViewExcursionActivity.class);
            intent.putExtra("vacationID", id);
            intent.putExtra("excursionID", excursionID);
            startActivity(intent);
        });

        //edit
        excursion.findViewById(R.id.editExcursionEntryButton).setOnClickListener(view -> {
            Intent intent = new Intent(activity, com.example.d308_vacation_planner.UI.EditExcursionActivity.class);
            intent.putExtra("vacationID", id);
            intent.putExtra("excursionID", excursionID);
            startActivity(intent);
        });

        //delete
        excursion.findViewById(R.id.deleteExcursionEntryButton).setOnClickListener(view -> {
            database.excursionDAO().deleteExcursion(database.excursionDAO().getExcursion(excursionID));
            ((ViewGroup)findViewById(R.id.viewVacationExcursions)).removeView(excursion);

            numExcursions--;
            if(numExcursions == 0)
                noExcursions();
        });

        ((ViewGroup)findViewById(R.id.viewVacationExcursions)).addView(excursion);
    }

    void noExcursions(){
        View noExcursions = com.example.d308_vacation_planner.UI.MainActivity.LI.inflate(R.layout.no_excursions, null);
        ((ViewGroup)findViewById(R.id.viewVacationExcursions)).addView(noExcursions);
    }

    @Override
    public void onBackPressed() {
        if (bool==false) {
            Intent intent = new Intent(this, com.example.d308_vacation_planner.UI.MainActivity.class);
            startActivity(intent);
        }
        else {
            super.onBackPressed();
        }
    }
}
