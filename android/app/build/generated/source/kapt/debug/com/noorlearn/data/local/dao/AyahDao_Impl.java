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
import com.noorlearn.data.local.entity.AyahEntity;
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
public final class AyahDao_Impl implements AyahDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AyahEntity> __insertionAdapterOfAyahEntity;

  public AyahDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAyahEntity = new EntityInsertionAdapter<AyahEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `ayahs` (`id`,`surahId`,`ayahNumber`,`textArabic`,`textTranslation`,`audioUrl`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AyahEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSurahId());
        statement.bindLong(3, entity.getAyahNumber());
        if (entity.getTextArabic() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTextArabic());
        }
        if (entity.getTextTranslation() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getTextTranslation());
        }
        if (entity.getAudioUrl() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getAudioUrl());
        }
      }
    };
  }

  @Override
  public Object insertAyahs(final List<AyahEntity> ayahs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAyahEntity.insert(ayahs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAyahsBySurah(final int surahId,
      final Continuation<? super List<AyahEntity>> $completion) {
    final String _sql = "SELECT * FROM ayahs WHERE surahId = ? ORDER BY ayahNumber ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, surahId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AyahEntity>>() {
      @Override
      @NonNull
      public List<AyahEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSurahId = CursorUtil.getColumnIndexOrThrow(_cursor, "surahId");
          final int _cursorIndexOfAyahNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "ayahNumber");
          final int _cursorIndexOfTextArabic = CursorUtil.getColumnIndexOrThrow(_cursor, "textArabic");
          final int _cursorIndexOfTextTranslation = CursorUtil.getColumnIndexOrThrow(_cursor, "textTranslation");
          final int _cursorIndexOfAudioUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "audioUrl");
          final List<AyahEntity> _result = new ArrayList<AyahEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AyahEntity _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpSurahId;
            _tmpSurahId = _cursor.getInt(_cursorIndexOfSurahId);
            final int _tmpAyahNumber;
            _tmpAyahNumber = _cursor.getInt(_cursorIndexOfAyahNumber);
            final String _tmpTextArabic;
            if (_cursor.isNull(_cursorIndexOfTextArabic)) {
              _tmpTextArabic = null;
            } else {
              _tmpTextArabic = _cursor.getString(_cursorIndexOfTextArabic);
            }
            final String _tmpTextTranslation;
            if (_cursor.isNull(_cursorIndexOfTextTranslation)) {
              _tmpTextTranslation = null;
            } else {
              _tmpTextTranslation = _cursor.getString(_cursorIndexOfTextTranslation);
            }
            final String _tmpAudioUrl;
            if (_cursor.isNull(_cursorIndexOfAudioUrl)) {
              _tmpAudioUrl = null;
            } else {
              _tmpAudioUrl = _cursor.getString(_cursorIndexOfAudioUrl);
            }
            _item = new AyahEntity(_tmpId,_tmpSurahId,_tmpAyahNumber,_tmpTextArabic,_tmpTextTranslation,_tmpAudioUrl);
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
