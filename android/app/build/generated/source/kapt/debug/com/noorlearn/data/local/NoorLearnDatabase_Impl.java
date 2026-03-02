package com.noorlearn.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.noorlearn.data.local.dao.AyahDao;
import com.noorlearn.data.local.dao.AyahDao_Impl;
import com.noorlearn.data.local.dao.SurahDao;
import com.noorlearn.data.local.dao.SurahDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoorLearnDatabase_Impl extends NoorLearnDatabase {
  private volatile SurahDao _surahDao;

  private volatile AyahDao _ayahDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `surahs` (`id` INTEGER NOT NULL, `nameArabic` TEXT NOT NULL, `nameEnglishTranslation` TEXT NOT NULL, `revelationType` TEXT NOT NULL, `numberOfAyahs` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ayahs` (`id` INTEGER NOT NULL, `surahId` INTEGER NOT NULL, `ayahNumber` INTEGER NOT NULL, `textArabic` TEXT NOT NULL, `textTranslation` TEXT NOT NULL, `audioUrl` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2399d049df91f27c74ac823c97ac5393')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `surahs`");
        db.execSQL("DROP TABLE IF EXISTS `ayahs`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSurahs = new HashMap<String, TableInfo.Column>(5);
        _columnsSurahs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurahs.put("nameArabic", new TableInfo.Column("nameArabic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurahs.put("nameEnglishTranslation", new TableInfo.Column("nameEnglishTranslation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurahs.put("revelationType", new TableInfo.Column("revelationType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSurahs.put("numberOfAyahs", new TableInfo.Column("numberOfAyahs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSurahs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSurahs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSurahs = new TableInfo("surahs", _columnsSurahs, _foreignKeysSurahs, _indicesSurahs);
        final TableInfo _existingSurahs = TableInfo.read(db, "surahs");
        if (!_infoSurahs.equals(_existingSurahs)) {
          return new RoomOpenHelper.ValidationResult(false, "surahs(com.noorlearn.data.local.entity.SurahEntity).\n"
                  + " Expected:\n" + _infoSurahs + "\n"
                  + " Found:\n" + _existingSurahs);
        }
        final HashMap<String, TableInfo.Column> _columnsAyahs = new HashMap<String, TableInfo.Column>(6);
        _columnsAyahs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAyahs.put("surahId", new TableInfo.Column("surahId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAyahs.put("ayahNumber", new TableInfo.Column("ayahNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAyahs.put("textArabic", new TableInfo.Column("textArabic", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAyahs.put("textTranslation", new TableInfo.Column("textTranslation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAyahs.put("audioUrl", new TableInfo.Column("audioUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAyahs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAyahs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAyahs = new TableInfo("ayahs", _columnsAyahs, _foreignKeysAyahs, _indicesAyahs);
        final TableInfo _existingAyahs = TableInfo.read(db, "ayahs");
        if (!_infoAyahs.equals(_existingAyahs)) {
          return new RoomOpenHelper.ValidationResult(false, "ayahs(com.noorlearn.data.local.entity.AyahEntity).\n"
                  + " Expected:\n" + _infoAyahs + "\n"
                  + " Found:\n" + _existingAyahs);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2399d049df91f27c74ac823c97ac5393", "4aa7d43f86adcb2b73d78a4465bf5649");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "surahs","ayahs");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `surahs`");
      _db.execSQL("DELETE FROM `ayahs`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SurahDao.class, SurahDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AyahDao.class, AyahDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SurahDao getSurahDao() {
    if (_surahDao != null) {
      return _surahDao;
    } else {
      synchronized(this) {
        if(_surahDao == null) {
          _surahDao = new SurahDao_Impl(this);
        }
        return _surahDao;
      }
    }
  }

  @Override
  public AyahDao getAyahDao() {
    if (_ayahDao != null) {
      return _ayahDao;
    } else {
      synchronized(this) {
        if(_ayahDao == null) {
          _ayahDao = new AyahDao_Impl(this);
        }
        return _ayahDao;
      }
    }
  }
}
