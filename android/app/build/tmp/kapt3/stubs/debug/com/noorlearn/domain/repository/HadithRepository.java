package com.noorlearn.domain.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J$\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0004H\u00a6@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0006\u0010\u0007J\u000e\u0010\b\u001a\u00020\tH\u00a6@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\t0\f2\u0006\u0010\r\u001a\u00020\u0004H\u00a6@\u00a2\u0006\u0002\u0010\u0007\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u000e"}, d2 = {"Lcom/noorlearn/domain/repository/HadithRepository;", "", "explainHadith", "Lkotlin/Result;", "", "hadithId", "explainHadith-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDailyHadith", "Lcom/noorlearn/domain/model/Hadith;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getHadithsBySource", "", "source", "app_debug"})
public abstract interface HadithRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getDailyHadith(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.noorlearn.domain.model.Hadith> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getHadithsBySource(@org.jetbrains.annotations.NotNull()
    java.lang.String source, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.domain.model.Hadith>> $completion);
}