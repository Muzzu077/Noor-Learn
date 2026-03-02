package com.noorlearn.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class ProphetRepositoryImpl_Factory implements Factory<ProphetRepositoryImpl> {
  @Override
  public ProphetRepositoryImpl get() {
    return newInstance();
  }

  public static ProphetRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static ProphetRepositoryImpl newInstance() {
    return new ProphetRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final ProphetRepositoryImpl_Factory INSTANCE = new ProphetRepositoryImpl_Factory();
  }
}
