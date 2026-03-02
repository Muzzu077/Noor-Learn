package com.noorlearn.domain.usecase;

import com.noorlearn.domain.repository.ChatRepository;
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
public final class AskChatbotUseCase_Factory implements Factory<AskChatbotUseCase> {
  private final Provider<ChatRepository> chatRepositoryProvider;

  public AskChatbotUseCase_Factory(Provider<ChatRepository> chatRepositoryProvider) {
    this.chatRepositoryProvider = chatRepositoryProvider;
  }

  @Override
  public AskChatbotUseCase get() {
    return newInstance(chatRepositoryProvider.get());
  }

  public static AskChatbotUseCase_Factory create(Provider<ChatRepository> chatRepositoryProvider) {
    return new AskChatbotUseCase_Factory(chatRepositoryProvider);
  }

  public static AskChatbotUseCase newInstance(ChatRepository chatRepository) {
    return new AskChatbotUseCase(chatRepository);
  }
}
