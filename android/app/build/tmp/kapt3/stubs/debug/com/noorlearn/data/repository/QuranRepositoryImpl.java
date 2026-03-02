package com.noorlearn.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J,\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u001c\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010\u0013\u001a\u00020\rH\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u0011H\u0096@\u00a2\u0006\u0002\u0010\u0017JD\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\r2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u001a\u001a\u00020\u000b2\u0006\u0010\u001b\u001a\u00020\u001cH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001d\u0010\u001eR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u001f"}, d2 = {"Lcom/noorlearn/data/repository/QuranRepositoryImpl;", "Lcom/noorlearn/domain/repository/QuranRepository;", "surahDao", "Lcom/noorlearn/data/local/dao/SurahDao;", "ayahDao", "Lcom/noorlearn/data/local/dao/AyahDao;", "(Lcom/noorlearn/data/local/dao/SurahDao;Lcom/noorlearn/data/local/dao/AyahDao;)V", "bookmarkAyah", "Lkotlin/Result;", "Lcom/noorlearn/domain/model/Bookmark;", "userId", "", "ayahId", "", "bookmarkAyah-0E7RQCE", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAyahs", "", "Lcom/noorlearn/domain/model/Ayah;", "surahId", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getSurahs", "Lcom/noorlearn/domain/model/Surah;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "submitRecitation", "Lcom/noorlearn/domain/model/RecitationLog;", "transcribedText", "accuracyScore", "", "submitRecitation-hUnOzRk", "(Ljava/lang/String;IILjava/lang/String;FLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class QuranRepositoryImpl implements com.noorlearn.domain.repository.QuranRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.noorlearn.data.local.dao.SurahDao surahDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.noorlearn.data.local.dao.AyahDao ayahDao = null;
    
    @javax.inject.Inject()
    public QuranRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.noorlearn.data.local.dao.SurahDao surahDao, @org.jetbrains.annotations.NotNull()
    com.noorlearn.data.local.dao.AyahDao ayahDao) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getSurahs(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.domain.model.Surah>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getAyahs(int surahId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.domain.model.Ayah>> $completion) {
        return null;
    }
}