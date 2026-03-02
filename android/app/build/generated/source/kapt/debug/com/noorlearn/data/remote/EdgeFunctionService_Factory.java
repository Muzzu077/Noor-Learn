package com.noorlearn.data.remote;

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
public final class EdgeFunctionService_Factory implements Factory<EdgeFunctionService> {
  @Override
  public EdgeFunctionService get() {
    return newInstance();
  }

  public static EdgeFunctionService_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static EdgeFunctionService newInstance() {
    return new EdgeFunctionService();
  }

  private static final class InstanceHolder {
    private static final EdgeFunctionService_Factory INSTANCE = new EdgeFunctionService_Factory();
  }
}
