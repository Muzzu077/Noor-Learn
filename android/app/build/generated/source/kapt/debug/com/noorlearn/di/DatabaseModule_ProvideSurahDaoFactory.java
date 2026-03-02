package com.noorlearn.di;

import com.noorlearn.data.local.NoorLearnDatabase;
import com.noorlearn.data.local.dao.SurahDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideSurahDaoFactory implements Factory<SurahDao> {
  private final Provider<NoorLearnDatabase> dbProvider;

  public DatabaseModule_ProvideSurahDaoFactory(Provider<NoorLearnDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SurahDao get() {
    return provideSurahDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideSurahDaoFactory create(
      Provider<NoorLearnDatabase> dbProvider) {
    return new DatabaseModule_ProvideSurahDaoFactory(dbProvider);
  }

  public static SurahDao provideSurahDao(NoorLearnDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSurahDao(db));
  }
}
