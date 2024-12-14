package com.example.d308_vacation_planner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308_vacation_planner.dao.ExcursionDAO;
import com.example.d308_vacation_planner.dao.VacationDAO;
import com.example.d308_vacation_planner.entities.Excursion;
import com.example.d308_vacation_planner.entities.Vacation;

//if database scheme is out of date, increase this version number
@Database(entities = {Vacation.class, Excursion.class}, version = 9, exportSchema = false)
public abstract class VacationDatabase extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static VacationDatabase INSTANCE;
    public static VacationDatabase getDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, VacationDatabase.class, "vacation.db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return INSTANCE;
    }
}