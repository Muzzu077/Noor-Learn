import os
import time
import subprocess
import shutil

navigation_path = "/home/muzzu/Projects/Noor-Learn/android/app/src/main/kotlin/com/noorlearn/ui/navigation/AppNavigation.kt"
backup_path = navigation_path + ".bak"
artifact_dir = "/home/muzzu/.gemini/antigravity-cli/brain/d6feddcd-f7af-495b-a924-d2037dd5e05c"

routes = [
    ("auth", "auth_screen.png"),
    ("onboarding", "onboarding_screen.png"),
    ("dashboard", "dashboard_screen.png"),
    ("surah_list", "surah_list_screen.png"),
    ("daily_journey", "daily_journey_screen.png"),
    ("chatbot", "chatbot_screen.png"),
    ("tools", "tools_screen.png"),
    ("profile", "profile_screen.png"),
    ("hadith_hub", "hadith_hub_screen.png"),
    ("prophet_stories", "prophet_stories_screen.png"),
    ("ayah_reader/1/Al-Fatiha", "ayah_reader_screen.png"),
    ("qaida", "qaida_screen.png"),
    ("bookmarks", "bookmarks_screen.png"),
    ("reflection_journal", "reflection_journal_screen.png"),
    ("vocabulary_builder", "vocabulary_builder_screen.png"),
    ("para_stories", "para_stories_screen.png")
]

def run_cmd(cmd, cwd=None):
    print(f"Running: {cmd}")
    res = subprocess.run(cmd, shell=True, capture_output=True, text=True, cwd=cwd)
    if res.returncode != 0:
        print(f"Error running {cmd}: {res.stderr}")
    return res.returncode == 0

# Create backup
if not os.path.exists(backup_path):
    shutil.copyfile(navigation_path, backup_path)

try:
    for route, filename in routes:
        print(f"\n=== CAPTURING SCREEN: {route} -> {filename} ===")
        # Read original file content from backup
        with open(backup_path, "r") as f:
            content = f.read()

        # Modify startDestination and bypass loading check
        modified = content.replace(
            'startDestination = if (user != null) Screen.Home.route else "auth",',
            f'startDestination = "{route}",'
        ).replace(
            'if (user == null && authViewModel.isLoading.value) {',
            'if (false && user == null && authViewModel.isLoading.value) {'
        )

        with open(navigation_path, "w") as f:
            f.write(modified)

        # Build & Install
        if not run_cmd("./gradlew installDebug", cwd="/home/muzzu/Projects/Noor-Learn/android"):
            print(f"Build failed for route {route}")
            continue

        # Force stop and start app
        run_cmd("adb shell am force-stop com.noorlearn")
        run_cmd("adb shell monkey -p com.noorlearn -c android.intent.category.LAUNCHER 1")

        # Wait for rendering
        time.sleep(5)

        # Take screenshot
        dest_path = os.path.join(artifact_dir, filename)
        if run_cmd("adb shell screencap -p /sdcard/screen.png"):
            run_cmd(f"adb pull /sdcard/screen.png {dest_path}")
            print(f"Screenshot saved to {dest_path}")
        else:
            print(f"Failed to capture screenshot for route {route}")

finally:
    # Restore original file
    if os.path.exists(backup_path):
        shutil.copyfile(backup_path, navigation_path)
        os.remove(backup_path)
        print("Restored original AppNavigation.kt")
