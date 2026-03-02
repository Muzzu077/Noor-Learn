# NoorLearn Android App 🕌

Welcome to the NoorLearn Android Project. This is a complete Islamic learning
platform focused on Qur'an reading, AI-powered recitation feedback, Hadith
learning with AI explanations, Prophet stories, Islamic essential tools, and a
personalized learning dashboard.

## 🏗 Architecture

This app follows **Clean Architecture** patterns utilizing **MVVM** in the
presentation layer. The main components include:

- **UI**: Jetpack Compose, Material 3 (Light Theme), Navigation Compose
- **Dependency Injection**: Dagger Hilt
- **Domain**: Pure Kotlin models and UseCases containing business logic
- **Data**: Room Database (Offline-first caching), DataStore (Preferences), and
  Supabase remote sources
- **Backend**: Supabase (PostgreSQL, Auth, Edge Functions)
- **AI Provider**: OpenRouter (`stepfun/step-3.5-flash:free` model via Supabase
  Edge Function)

## 🔐 Security & AI Proxy

The OpenRouter API Key is **never** shipped within the Android app code. It is
securely configured on the Supabase Edge Function
(`supabase/functions/ai-proxy`). The Android app fetches an Auth token from
Supabase and passes it to the Edge Function, which proxies the request to
OpenRouter.

## 🚀 Setup Instructions

### 1. Configure the Backend (Supabase)

Navigate to your Supabase project dashboard:

1. Go to **SQL Editor** and run the contents of the `supabase/schema.sql` file
   located in the root of this project. This will set up your tables and Row
   Level Security (RLS) policies.
2. Under **Settings > API**, copy your `Project URL` and `anon public` key.
3. In your **local** Android development environment, place the Supabase
   credentials in `android/local.properties` (this file is `.gitignore`d):

```properties
SUPABASE_URL=https://[YOUR-PROJECT].supabase.co
SUPABASE_KEY=[YOUR-ANON-KEY]
```

### 2. Deploy the Edge Function

To install Deno and deploy the AI proxy:

1. Ensure the Supabase CLI is installed.
2. Navigate to the `android/supabase/` directory.
3. Set your OpenRouter API key as a secret on Supabase:

```bash
supabase secrets set OPENROUTER_API_KEY=sk-or-...
```

4. Deploy the function:

```bash
supabase functions deploy ai-proxy --no-verify-jwt
```

_(Note: JWT verification is handled manually inside the Edge Function itself if
you want to extend auth)._

### 3. Run the Android App

1. Open the `android` directory in **Android Studio**.
2. Run a Gradle sync.
3. Once synced, you should be able to press the "Run" button and deploy the app
   to an emulator or physical device.

## 🧪 Testing

The architecture has been designed perfectly to allow mocking repository
dependencies. You can run the unit test suite via terminal:

```bash
./gradlew test
```

## 🛠 Features Left to Expand

This setup provides a full production-ready foundation with Clean Architecture
and a wired-up backend. Further enhancements include:

- Completing the Hadith Hub UI Screen.
- Integrating ExoPlayer for verse-by-verse playback in `QuranRemoteDataSource`.
- Plugging `SpeechRecognizer` into the `SubmitRecitationUseCase`.
