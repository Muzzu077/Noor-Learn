package com.noorlearn.ui.screens;

import com.noorlearn.domain.usecase.AskChatbotUseCase;
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
public final class ChatbotViewModel_Factory implements Factory<ChatbotViewModel> {
  private final Provider<AskChatbotUseCase> askChatbotUseCaseProvider;

  public ChatbotViewModel_Factory(Provider<AskChatbotUseCase> askChatbotUseCaseProvider) {
    this.askChatbotUseCaseProvider = askChatbotUseCaseProvider;
  }

  @Override
  public ChatbotViewModel get() {
    return newInstance(askChatbotUseCaseProvider.get());
  }

  public static ChatbotViewModel_Factory create(
      Provider<AskChatbotUseCase> askChatbotUseCaseProvider) {
    return new ChatbotViewModel_Factory(askChatbotUseCaseProvider);
  }

  public static ChatbotViewModel newInstance(AskChatbotUseCase askChatbotUseCase) {
    return new ChatbotViewModel(askChatbotUseCase);
  }
}
