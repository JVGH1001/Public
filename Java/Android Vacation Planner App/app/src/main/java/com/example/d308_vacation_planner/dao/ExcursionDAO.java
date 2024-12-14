package com.example.d308_vacation_planner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_vacation_planner.entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Query("SELECT * FROM Excursion WHERE vacationID = :vacationID")
    List<Excursion> getExcursions(long vacationID);

    @Query("SELECT * FROM Excursion WHERE id = :id")
    Excursion getExcursion(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);

    @Delete
    void deleteExcursion(Excursion excursion);
}
