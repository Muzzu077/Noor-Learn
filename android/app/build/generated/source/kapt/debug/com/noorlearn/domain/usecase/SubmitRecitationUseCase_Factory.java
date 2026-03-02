package com.noorlearn.domain.usecase;

import com.noorlearn.domain.repository.QuranRepository;
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
public final class SubmitRecitationUseCase_Factory implements Factory<SubmitRecitationUseCase> {
  private final Provider<QuranRepository> quranRepositoryProvider;

  public SubmitRecitationUseCase_Factory(Provider<QuranRepository> quranRepositoryProvider) {
    this.quranRepositoryProvider = quranRepositoryProvider;
  }

  @Override
  public SubmitRecitationUseCase get() {
    return newInstance(quranRepositoryProvider.get());
  }

  public static SubmitRecitationUseCase_Factory create(
      Provider<QuranRepository> quranRepositoryProvider) {
    return new SubmitRecitationUseCase_Factory(quranRepositoryProvider);
  }

  public static SubmitRecitationUseCase newInstance(QuranRepository quranRepository) {
    return new SubmitRecitationUseCase(quranRepository);
  }
}
