import os

with open("full_crash.txt", "rb") as f:
    data = f.read().decode("utf-8", errors="ignore")

lines = data.split("\n")
traces = []
capture = 0

for line in lines:
    if "FATAL" in line or "com.noorlearn" in line or "AndroidRuntime" in line:
        capture = 15  # Capture 15 lines after a match
        traces.append("--- MATCH START ---")
    if capture > 0:
        traces.append(line)
        capture -= 1

output = "\n".join(traces[-300:])
with open("filtered_logs.txt", "w", encoding="utf-8") as out:
    out.write(output)

print("Filtered logs containing com.noorlearn written to filtered_logs.txt")
