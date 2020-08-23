//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.installalogs.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LogDao {
    @Query("SELECT * from log_table ORDER BY createdAt DESC")
    LiveData<List<Log>> all();

    @Query("SELECT * from log_table ORDER BY createdAt DESC")
    List<Log> allPlain();

    @Delete
    void delete(Log log);

    @Query("DELETE from log_table")
    void deleteAll();

    @Query("SELECT * from log_table WHERE id = :id LIMIT 1")
    Log get(int id);

    @Query("SELECT * from log_table WHERE packageName = :packageName ORDER BY createdAt DESC LIMIT 1")
    Log getPackageNewest(String packageName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Log log);

    @Query("SELECT * from log_table WHERE label LIKE '%' || :searchWord || '%' "
            + "OR packageName LIKE '%' || :searchWord || '%' " + "OR versionCode LIKE '%' || :searchWord || '%' "
            + "OR versionName LIKE '%' || :searchWord || '%' " + "OR md5 LIKE '%' || :searchWord || '%' "
            + "OR sha1 LIKE '%' || :searchWord || '%' " + "OR sha256 LIKE '%' || :searchWord || '%' "
            + "OR sha512 LIKE '%' || :searchWord || '%' " + "ORDER BY createdAt DESC")
    LiveData<List<Log>> search(String searchWord);

    @Update
    void update(Log log);
}
