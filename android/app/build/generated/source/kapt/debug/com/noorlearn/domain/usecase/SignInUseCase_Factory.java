package com.noorlearn.domain.usecase;

import com.noorlearn.domain.repository.AuthRepository;
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
public final class SignInUseCase_Factory implements Factory<SignInUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public SignInUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SignInUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static SignInUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new SignInUseCase_Factory(authRepositoryProvider);
  }

  public static SignInUseCase newInstance(AuthRepository authRepository) {
    return new SignInUseCase(authRepository);
  }
}
