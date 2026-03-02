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
public final class SignUpUseCase_Factory implements Factory<SignUpUseCase> {
  private final Provider<AuthRepository> authRepositoryProvider;

  public SignUpUseCase_Factory(Provider<AuthRepository> authRepositoryProvider) {
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public SignUpUseCase get() {
    return newInstance(authRepositoryProvider.get());
  }

  public static SignUpUseCase_Factory create(Provider<AuthRepository> authRepositoryProvider) {
    return new SignUpUseCase_Factory(authRepositoryProvider);
  }

  public static SignUpUseCase newInstance(AuthRepository authRepository) {
    return new SignUpUseCase(authRepository);
  }
}
