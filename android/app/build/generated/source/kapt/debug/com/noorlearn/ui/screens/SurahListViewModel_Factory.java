package com.noorlearn.ui.screens;

import com.noorlearn.domain.usecase.GetSurahListUseCase;
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
public final class SurahListViewModel_Factory implements Factory<SurahListViewModel> {
  private final Provider<GetSurahListUseCase> getSurahListUseCaseProvider;

  public SurahListViewModel_Factory(Provider<GetSurahListUseCase> getSurahListUseCaseProvider) {
    this.getSurahListUseCaseProvider = getSurahListUseCaseProvider;
  }

  @Override
  public SurahListViewModel get() {
    return newInstance(getSurahListUseCaseProvider.get());
  }

  public static SurahListViewModel_Factory create(
      Provider<GetSurahListUseCase> getSurahListUseCaseProvider) {
    return new SurahListViewModel_Factory(getSurahListUseCaseProvider);
  }

  public static SurahListViewModel newInstance(GetSurahListUseCase getSurahListUseCase) {
    return new SurahListViewModel(getSurahListUseCase);
  }
}
