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
public final class HadithRepositoryImpl_Factory implements Factory<HadithRepositoryImpl> {
  @Override
  public HadithRepositoryImpl get() {
    return newInstance();
  }

  public static HadithRepositoryImpl_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HadithRepositoryImpl newInstance() {
    return new HadithRepositoryImpl();
  }

  private static final class InstanceHolder {
    private static final HadithRepositoryImpl_Factory INSTANCE = new HadithRepositoryImpl_Factory();
  }
}
