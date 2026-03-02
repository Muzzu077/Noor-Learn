package com.noorlearn.data.repository;

import com.noorlearn.data.remote.EdgeFunctionService;
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
public final class ChatRepositoryImpl_Factory implements Factory<ChatRepositoryImpl> {
  private final Provider<EdgeFunctionService> edgeFunctionServiceProvider;

  public ChatRepositoryImpl_Factory(Provider<EdgeFunctionService> edgeFunctionServiceProvider) {
    this.edgeFunctionServiceProvider = edgeFunctionServiceProvider;
  }

  @Override
  public ChatRepositoryImpl get() {
    return newInstance(edgeFunctionServiceProvider.get());
  }

  public static ChatRepositoryImpl_Factory create(
      Provider<EdgeFunctionService> edgeFunctionServiceProvider) {
    return new ChatRepositoryImpl_Factory(edgeFunctionServiceProvider);
  }

  public static ChatRepositoryImpl newInstance(EdgeFunctionService edgeFunctionService) {
    return new ChatRepositoryImpl(edgeFunctionService);
  }
}
