package com.noorlearn.ui.screens;

import com.noorlearn.domain.repository.HadithRepository;
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
public final class HadithHubViewModel_Factory implements Factory<HadithHubViewModel> {
  private final Provider<HadithRepository> hadithRepositoryProvider;

  public HadithHubViewModel_Factory(Provider<HadithRepository> hadithRepositoryProvider) {
    this.hadithRepositoryProvider = hadithRepositoryProvider;
  }

  @Override
  public HadithHubViewModel get() {
    return newInstance(hadithRepositoryProvider.get());
  }

  public static HadithHubViewModel_Factory create(
      Provider<HadithRepository> hadithRepositoryProvider) {
    return new HadithHubViewModel_Factory(hadithRepositoryProvider);
  }

  public static HadithHubViewModel newInstance(HadithRepository hadithRepository) {
    return new HadithHubViewModel(hadithRepository);
  }
}
