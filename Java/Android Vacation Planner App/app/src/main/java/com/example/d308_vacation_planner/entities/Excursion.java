package com.example.d308_vacation_planner.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = @ForeignKey(entity = Vacation.class, parentColumns = "id", childColumns = "vacationID", onDelete = ForeignKey.RESTRICT), indices = {@Index(value = "vacationID")})
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long excursionID;

    @ColumnInfo(name = "excursionTitle")
    private String excursionTitle;

    @ColumnInfo(name = "excursionDate")
    private long excursionDate;

    @ColumnInfo(name = "vacationID")
    private long VacationID;

    @ColumnInfo(name = "excursionAlert")
    private boolean excursionAlert = false;

    public long getExcursionID(){
        return excursionID;
    }

    public void setExcursionID(long excursionID) {
        this.excursionID = excursionID;
    }

    public String excursionTitle(){
        return excursionTitle;
    }

    public long excursionDate(){
        return excursionDate;
    }

    public long VacationID(){
        return VacationID;
    }

    public boolean getExcursionAlert(){return excursionAlert;}

    public void setExcursionAlert(boolean excursionAlert){this.excursionAlert = excursionAlert;}

    @Ignore
    public Excursion(long id, String excursionTitle, long excursionDate, long VacationID){
        this(excursionTitle, excursionDate, VacationID);
        excursionID = id;
    }

    public Excursion(String excursionTitle, long excursionDate, long VacationID){
        this.excursionTitle = excursionTitle;
        this.excursionDate = excursionDate;
        this.VacationID = VacationID;
    }
}
