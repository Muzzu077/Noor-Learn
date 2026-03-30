-- SQL Migration for NoorLearn (Supabase)

-- User Profiles table
create table public.user_profiles (
  id uuid references auth.users(id) on delete cascade not null primary key,
  name text,
  role_mode text,
  learning_level text,
  primary_goal text,
  daily_commitment text,
  current_streak integer default 0,
  longest_streak integer default 0,
  last_activity_date text,
  created_at timestamptz default now()
);

-- Enable RLS for user_profiles
alter table public.user_profiles enable row level security;
create policy "Users can view own profile" on public.user_profiles for select using (auth.uid() = id);
create policy "Users can insert own profile" on public.user_profiles for insert with check (auth.uid() = id);
create policy "Users can update own profile" on public.user_profiles for update using (auth.uid() = id);

-- Surahs table
create table public.surahs (
  id integer primary key,
  name_arabic text not null,
  name_english_translation text not null,
  revelation_type text not null,
  number_of_ayahs integer not null
);
alter table public.surahs enable row level security;
create policy "Surahs are public" on public.surahs for select using (true);

-- Ayahs table
create table public.ayahs (
  id integer primary key,
  surah_id integer references public.surahs(id) on delete cascade,
  ayah_number integer not null,
  text_arabic text not null,
  text_translation text not null,
  audio_url text
);
alter table public.ayahs enable row level security;
create policy "Ayahs are public" on public.ayahs for select using (true);

-- Hadiths table
create table public.hadiths (
  id text primary key,
  collection_id text not null,
  hadith_number text not null,
  text_arabic text not null,
  text_translation text not null,
  is_daily boolean default false
);
alter table public.hadiths enable row level security;
create policy "Hadiths are public" on public.hadiths for select using (true);

-- Prophets table
create table public.prophets (
  id text primary key,
  name_arabic text not null,
  name_english text not null,
  short_summary text not null,
  full_story text not null,
  timeline_era text not null
);
alter table public.prophets enable row level security;
create policy "Prophets are public" on public.prophets for select using (true);

-- Bookmarks table
create table public.bookmarks (
  id text primary key,
  user_id uuid references auth.users(id) on delete cascade,
  type text not null,
  reference_id text not null,
  timestamp bigint not null
);
alter table public.bookmarks enable row level security;
create policy "Users manage own bookmarks" on public.bookmarks for select using (auth.uid() = user_id);
create policy "Users can insert bookmarks" on public.bookmarks for insert with check (auth.uid() = user_id);
create policy "Users can delete own bookmarks" on public.bookmarks for delete using (auth.uid() = user_id);

-- Recitation Logs table
create table public.recitation_logs (
  id text primary key,
  user_id uuid references auth.users(id) on delete cascade,
  surah_id integer,
  ayah_id integer,
  transcribed_text text not null,
  accuracy_score float not null,
  tip_ai text,
  timestamp bigint not null
);
alter table public.recitation_logs enable row level security;
create policy "Users view own recitation logs" on public.recitation_logs for select using (auth.uid() = user_id);
create policy "Users insert own recitation logs" on public.recitation_logs for insert with check (auth.uid() = user_id);
create policy "Users delete own recitation logs" on public.recitation_logs for delete using (auth.uid() = user_id);
