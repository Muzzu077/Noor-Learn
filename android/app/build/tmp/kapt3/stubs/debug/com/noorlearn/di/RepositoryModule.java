package com.noorlearn.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\'J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\'J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\'\u00a8\u0006\u0013"}, d2 = {"Lcom/noorlearn/di/RepositoryModule;", "", "()V", "bindChatRepository", "Lcom/noorlearn/domain/repository/ChatRepository;", "chatRepositoryImpl", "Lcom/noorlearn/data/repository/ChatRepositoryImpl;", "bindHadithRepository", "Lcom/noorlearn/domain/repository/HadithRepository;", "hadithRepositoryImpl", "Lcom/noorlearn/data/repository/HadithRepositoryImpl;", "bindProphetRepository", "Lcom/noorlearn/domain/repository/ProphetRepository;", "prophetRepositoryImpl", "Lcom/noorlearn/data/repository/ProphetRepositoryImpl;", "bindQuranRepository", "Lcom/noorlearn/domain/repository/QuranRepository;", "quranRepositoryImpl", "Lcom/noorlearn/data/repository/QuranRepositoryImpl;", "app_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract com.noorlearn.domain.repository.QuranRepository bindQuranRepository(@org.jetbrains.annotations.NotNull()
    com.noorlearn.data.repository.QuranRepositoryImpl quranRepositoryImpl);
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract com.noorlearn.domain.repository.ChatRepository bindChatRepository(@org.jetbrains.annotations.NotNull()
    com.noorlearn.data.repository.ChatRepositoryImpl chatRepositoryImpl);
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract com.noorlearn.domain.repository.HadithRepository bindHadithRepository(@org.jetbrains.annotations.NotNull()
    com.noorlearn.data.repository.HadithRepositoryImpl hadithRepositoryImpl);
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract com.noorlearn.domain.repository.ProphetRepository bindProphetRepository(@org.jetbrains.annotations.NotNull()
    com.noorlearn.data.repository.ProphetRepositoryImpl prophetRepositoryImpl);
}