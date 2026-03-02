package com.noorlearn.domain.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00a6@\u00a2\u0006\u0002\u0010\u0005J\u0016\u0010\u0006\u001a\u00020\u00042\u0006\u0010\u0007\u001a\u00020\bH\u00a6@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/noorlearn/domain/repository/ProphetRepository;", "", "getProphetStories", "", "Lcom/noorlearn/domain/model/Prophet;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getStory", "prophetId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface ProphetRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getProphetStories(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.noorlearn.domain.model.Prophet>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getStory(@org.jetbrains.annotations.NotNull()
    java.lang.String prophetId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.noorlearn.domain.model.Prophet> $completion);
}