package com.noorlearn.di;

import com.noorlearn.data.local.NoorLearnDatabase;
import com.noorlearn.data.local.dao.AyahDao;
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
public final class DatabaseModule_ProvideAyahDaoFactory implements Factory<AyahDao> {
  private final Provider<NoorLearnDatabase> dbProvider;

  public DatabaseModule_ProvideAyahDaoFactory(Provider<NoorLearnDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AyahDao get() {
    return provideAyahDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAyahDaoFactory create(
      Provider<NoorLearnDatabase> dbProvider) {
    return new DatabaseModule_ProvideAyahDaoFactory(dbProvider);
  }

  public static AyahDao provideAyahDao(NoorLearnDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAyahDao(db));
  }
}
