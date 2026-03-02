package com.noorlearn.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u001c\u0010\b\u001a\u00020\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/noorlearn/data/local/dao/AyahDao;", "", "getAyahsBySurah", "", "Lcom/noorlearn/data/local/entity/AyahEntity;", "surahId", "", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertAyahs", "", "ayahs", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface AyahDao {
    
    @androidx.room.Query(value = "SELECT * FROM ayahs WHERE surahId = :surahId ORDER BY ayahNumber ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAyahsBySurah(int surahId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.data.local.entity.AyahEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAyahs(@org.jetbrains.annotations.NotNull()
    java.util.List<com.noorlearn.data.local.entity.AyahEntity> ayahs, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}