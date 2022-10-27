package com.gnrc.telehealth.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RoomDao {
    @Query("SELECT * FROM task")
    List<Roomdata> getAll();

    @Insert
    void insert(Roomdata recipe);
}
