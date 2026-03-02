package com.noorlearn.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0007\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0007\u0010\bJ\u000e\u0010\t\u001a\u00020\nH\u0096@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\b\u0012\u0004\u0012\u00020\n0\r2\u0006\u0010\u000e\u001a\u00020\u0005H\u0096@\u00a2\u0006\u0002\u0010\b\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u000f"}, d2 = {"Lcom/noorlearn/data/repository/HadithRepositoryImpl;", "Lcom/noorlearn/domain/repository/HadithRepository;", "()V", "explainHadith", "Lkotlin/Result;", "", "hadithId", "explainHadith-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDailyHadith", "Lcom/noorlearn/domain/model/Hadith;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHadithsBySource", "", "source", "app_debug"})
public final class HadithRepositoryImpl implements com.noorlearn.domain.repository.HadithRepository {
    
    @javax.inject.Inject()
    public HadithRepositoryImpl() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getDailyHadith(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.noorlearn.domain.model.Hadith> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getHadithsBySource(@org.jetbrains.annotations.NotNull()
    java.lang.String source, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.domain.model.Hadith>> $completion) {
        return null;
    }
}