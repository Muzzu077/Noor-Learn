package com.noorlearn.data.repository;

import com.noorlearn.data.local.dao.AyahDao;
import com.noorlearn.data.local.dao.SurahDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class QuranRepositoryImpl_Factory implements Factory<QuranRepositoryImpl> {
  private final Provider<SurahDao> surahDaoProvider;

  private final Provider<AyahDao> ayahDaoProvider;

  public QuranRepositoryImpl_Factory(Provider<SurahDao> surahDaoProvider,
      Provider<AyahDao> ayahDaoProvider) {
    this.surahDaoProvider = surahDaoProvider;
    this.ayahDaoProvider = ayahDaoProvider;
  }

  @Override
  public QuranRepositoryImpl get() {
    return newInstance(surahDaoProvider.get(), ayahDaoProvider.get());
  }

  public static QuranRepositoryImpl_Factory create(Provider<SurahDao> surahDaoProvider,
      Provider<AyahDao> ayahDaoProvider) {
    return new QuranRepositoryImpl_Factory(surahDaoProvider, ayahDaoProvider);
  }

  public static QuranRepositoryImpl newInstance(SurahDao surahDao, AyahDao ayahDao) {
    return new QuranRepositoryImpl(surahDao, ayahDao);
  }
}
