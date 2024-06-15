package io.modulotech.workmanager.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import io.modulotech.workmanager.db.entity.WorkerEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface WorkerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(worker: WorkerEntity)

    @Upsert
    suspend fun upsert(worker: WorkerEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(worker: WorkerEntity)

    @Delete
    suspend fun delete(worker: WorkerEntity)

    @Query("SELECT * FROM worker WHERE id = (:id)")
    suspend fun getWorker(id: String): WorkerEntity?

    @Query("SELECT * FROM worker")
    suspend fun getWorkers(): List<WorkerEntity>

    @Query("SELECT * FROM worker WHERE id = (:id)")
    fun getWorkerAsFlow(id: String): Flow<WorkerEntity?>

    @Query("SELECT * FROM worker")
    fun getWorkersAsFlow(): Flow<List<WorkerEntity>>

}