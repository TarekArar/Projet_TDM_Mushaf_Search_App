package com.mazrou.boilerplate.perssistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mazrou.boilerplate.model.database.Ayat
import com.mazrou.boilerplate.model.database.RacineModel
import com.mazrou.boilerplate.model.database.Surah
import com.mazrou.boilerplate.model.database.World
import com.mazrou.boilerplate.model.ui.Racine


@Dao
interface QuranDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyat(ayat: Ayat): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurah(surah: Surah): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorld(world: World): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRacine(racineModel: RacineModel): Long

    @Query("SELECT * FROM racine WHERE racine  LIKE :query || '%' ")
    suspend fun searchByRacine(query: String): List<RacineModel>

    @Query("SELECT id, racine ,letterNumber, count(*) worldNumber FROM (SELECT * FROM racine r JOIN world w ON w.idRacine = r.id AND racine LIKE :query || '%') GROUP BY racine")
    suspend fun searchByRacineUi(query: String): List<Racine>

// SELECT arabicWord , text , surahName FROM racine  r JOIN world  w ON w.idRacine = r.id AND r.id = 553 JOIN ayat  a ON w.ayatID =a.id JOIN surah s on s.id = a.idSurah
}