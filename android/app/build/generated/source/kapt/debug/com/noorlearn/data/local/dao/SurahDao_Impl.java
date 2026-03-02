package com.noorlearn.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.noorlearn.data.local.entity.SurahEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SurahDao_Impl implements SurahDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SurahEntity> __insertionAdapterOfSurahEntity;

  public SurahDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSurahEntity = new EntityInsertionAdapter<SurahEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `surahs` (`id`,`nameArabic`,`nameEnglishTranslation`,`revelationType`,`numberOfAyahs`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SurahEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNameArabic() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNameArabic());
        }
        if (entity.getNameEnglishTranslation() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getNameEnglishTranslation());
        }
        if (entity.getRevelationType() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRevelationType());
        }
        statement.bindLong(5, entity.getNumberOfAyahs());
      }
    };
  }

  @Override
  public Object insertSurahs(final List<SurahEntity> surahs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSurahEntity.insert(surahs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSurahs(final Continuation<? super List<SurahEntity>> $completion) {
    final String _sql = "SELECT * FROM surahs ORDER BY id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SurahEntity>>() {
      @Override
      @NonNull
      public List<SurahEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNameArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "nameArabic");
          final int _cursorIndexOfNameEnglishTranslation = CursorUtil.getColumnIndexOrThrow(_cursor, "nameEnglishTranslation");
          final int _cursorIndexOfRevelationType = CursorUtil.getColumnIndexOrThrow(_cursor, "revelationType");
          final int _cursorIndexOfNumberOfAyahs = CursorUtil.getColumnIndexOrThrow(_cursor, "numberOfAyahs");
          final List<SurahEntity> _result = new ArrayList<SurahEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SurahEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpNameArabic;
            if (_cursor.isNull(_cursorIndexOfNameArabic)) {
              _tmpNameArabic = null;
            } else {
              _tmpNameArabic = _cursor.getString(_cursorIndexOfNameArabic);
            }
            final String _tmpNameEnglishTranslation;
            if (_cursor.isNull(_cursorIndexOfNameEnglishTranslation)) {
              _tmpNameEnglishTranslation = null;
            } else {
              _tmpNameEnglishTranslation = _cursor.getString(_cursorIndexOfNameEnglishTranslation);
            }
            final String _tmpRevelationType;
            if (_cursor.isNull(_cursorIndexOfRevelationType)) {
              _tmpRevelationType = null;
            } else {
              _tmpRevelationType = _cursor.getString(_cursorIndexOfRevelationType);
            }
            final int _tmpNumberOfAyahs;
            _tmpNumberOfAyahs = _cursor.getInt(_cursorIndexOfNumberOfAyahs);
            _item = new SurahEntity(_tmpId,_tmpNameArabic,_tmpNameEnglishTranslation,_tmpRevelationType,_tmpNumberOfAyahs);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
