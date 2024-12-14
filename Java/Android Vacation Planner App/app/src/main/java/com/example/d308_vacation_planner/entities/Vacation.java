package com.example.d308_vacation_planner.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "id")})
public class Vacation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long vacationID;

    @ColumnInfo(name = "vacationTitle")
    private String vacationTitle;

    @ColumnInfo(name = "vacationHotel")
    private String vacationHotel;

    @ColumnInfo(name = "vacationStart")
    private long vacationStart;

    @ColumnInfo(name = "vacationEnd")
    private long vacationEnd;

    @ColumnInfo(name = "vacationAlert")
    private boolean vacationAlert = false;

    public String vacationTitle(){
        return vacationTitle;
    }

    public String vacationHotel(){
        return vacationHotel;
    }

    public long vacationStart(){
        return vacationStart;
    }

    public long vacationEnd(){
        return vacationEnd;
    }

    public void setVacationID(long vacationID) {this.vacationID = vacationID;}

    public void setVacationAlert(boolean vacationAlert){this.vacationAlert = vacationAlert;}

    public long getVacationID(){
        return vacationID;
    }
    public boolean getVacationAlert(){return vacationAlert;}

    @Ignore
    public Vacation(long Id, String vacationTitle, String vacationHotel, long vacationStart, long vacationEnd)  {
        this(vacationTitle, vacationHotel, vacationStart, vacationEnd);
        this.vacationID = Id;
    }

    public Vacation(String vacationTitle, String vacationHotel, long vacationStart, long vacationEnd) {
        this.vacationTitle = vacationTitle;
        this.vacationHotel = vacationHotel;
        this.vacationStart = vacationStart;
        this.vacationEnd = vacationEnd;
    }
}

