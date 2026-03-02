package com.noorlearn.di;

import android.content.Context;
import com.noorlearn.data.local.NoorLearnDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatabaseModule_ProvideRoomDatabaseFactory implements Factory<NoorLearnDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideRoomDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public NoorLearnDatabase get() {
    return provideRoomDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideRoomDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideRoomDatabaseFactory(contextProvider);
  }

  public static NoorLearnDatabase provideRoomDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRoomDatabase(context));
  }
}
