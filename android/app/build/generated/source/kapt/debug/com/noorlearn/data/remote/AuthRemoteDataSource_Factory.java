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
public final class AuthRemoteDataSource_Factory implements Factory<AuthRemoteDataSource> {
  private final Provider<SupabaseClient> supabaseClientProvider;

  public AuthRemoteDataSource_Factory(Provider<SupabaseClient> supabaseClientProvider) {
    this.supabaseClientProvider = supabaseClientProvider;
  }

  @Override
  public AuthRemoteDataSource get() {
    return newInstance(supabaseClientProvider.get());
  }

  public static AuthRemoteDataSource_Factory create(
      Provider<SupabaseClient> supabaseClientProvider) {
    return new AuthRemoteDataSource_Factory(supabaseClientProvider);
  }

  public static AuthRemoteDataSource newInstance(SupabaseClient supabaseClient) {
    return new AuthRemoteDataSource(supabaseClient);
  }
}
