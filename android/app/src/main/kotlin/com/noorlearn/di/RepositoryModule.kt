package com.noorlearn.di

import com.noorlearn.data.repository.AuthRepositoryImpl
import com.noorlearn.data.repository.HadithRepositoryImpl
import com.noorlearn.data.repository.ProphetRepositoryImpl
import com.noorlearn.data.repository.QuranRepositoryImpl
import com.noorlearn.data.repository.ChatRepositoryImpl
import com.noorlearn.domain.repository.AuthRepository
import com.noorlearn.domain.repository.HadithRepository
import com.noorlearn.domain.repository.ProphetRepository
import com.noorlearn.domain.repository.QuranRepository
import com.noorlearn.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindQuranRepository(
        quranRepositoryImpl: QuranRepositoryImpl
    ): QuranRepository

    @Binds
    abstract fun bindChatRepository(
        chatRepositoryImpl: ChatRepositoryImpl
    ): ChatRepository

    @Binds
    abstract fun bindHadithRepository(
        hadithRepositoryImpl: HadithRepositoryImpl
    ): HadithRepository

    @Binds
    abstract fun bindProphetRepository(
        prophetRepositoryImpl: ProphetRepositoryImpl
    ): ProphetRepository

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
