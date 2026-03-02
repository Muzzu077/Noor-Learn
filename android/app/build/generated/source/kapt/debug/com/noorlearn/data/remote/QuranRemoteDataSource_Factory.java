package com.noorlearn.data.remote;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.github.jan.supabase.SupabaseClient;
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
public final class QuranRemoteDataSource_Factory implements Factory<QuranRemoteDataSource> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public QuranRemoteDataSource_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public QuranRemoteDataSource get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static QuranRemoteDataSource_Factory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new QuranRemoteDataSource_Factory(supabaseClientProvider);
  }

  public static QuranRemoteDataSource newInstance(SupabaseClient supabaseClient) {
    return new QuranRemoteDataSource(supabaseClient);
  }
}
