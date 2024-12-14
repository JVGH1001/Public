package com.example.d308_vacation_planner.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_vacation_planner.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {
    @Query("SELECT * FROM Vacation ORDER BY vacationStart")
    List<Vacation> getVacations();

    @Query("SELECT * FROM Vacation WHERE id = :id")
    Vacation getVacation(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addVacation(Vacation vacation);

    @Update
    void updateVacation(Vacation vacation);

    @Delete
    void deleteVacation(Vacation vacation);
}
