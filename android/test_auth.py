import urllib.request
import urllib.error
import json

url = "https://zbtweubvxltwtysxfzua.supabase.co/auth/v1/signup"
headers = {
    "apikey": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpidHdldWJ2eGx0d3R5c3hmenVhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzIzNDgzNDYsImV4cCI6MjA4NzkyNDM0Nn0.-MVGbaN9xDcfQwQYdfPMdY4rlzC2P2QcWLWn1RM8YCE",
    "Content-Type": "application/json"
}
data = json.dumps({
    "email": "demo_test_5@noorlearn.app",
    "password": "NoorTest2026!",
    "data": {"name": "Demo User"}
}).encode("utf-8")

req = urllib.request.Request(url, data=data, headers=headers, method="POST")

try:
    with urllib.request.urlopen(req) as response:
        with open("out_py.txt", "w", encoding="utf-8") as f:
            f.write(f"Status: {response.status}\nBody: {response.read().decode('utf-8')}")
except urllib.error.HTTPError as e:
    with open("out_py.txt", "w", encoding="utf-8") as f:
        f.write(f"HTTP Error: {e.code}\nResponse: {e.read().decode('utf-8')}")
except Exception as e:
    with open("out_py.txt", "w", encoding="utf-8") as f:
        f.write(f"Exception: {e}")
