package com.noorlearn.ui.screens;

import com.noorlearn.domain.repository.ProphetRepository;
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
public final class ProphetStoriesViewModel_Factory implements Factory<ProphetStoriesViewModel> {
  private final Provider<ProphetRepository> prophetRepositoryProvider;

  public ProphetStoriesViewModel_Factory(Provider<ProphetRepository> prophetRepositoryProvider) {
    this.prophetRepositoryProvider = prophetRepositoryProvider;
  }

  @Override
  public ProphetStoriesViewModel get() {
    return newInstance(prophetRepositoryProvider.get());
  }

  public static ProphetStoriesViewModel_Factory create(
      Provider<ProphetRepository> prophetRepositoryProvider) {
    return new ProphetStoriesViewModel_Factory(prophetRepositoryProvider);
  }

  public static ProphetStoriesViewModel newInstance(ProphetRepository prophetRepository) {
    return new ProphetStoriesViewModel(prophetRepository);
  }
}
