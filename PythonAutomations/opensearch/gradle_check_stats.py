from jenkinsapi.jenkins import Jenkins
from datetime import datetime, timedelta, timezone

# Initialize Jenkins server
server = Jenkins('https://build.ci.opensearch.org/', timeout=30)

# Specify the name of the specific job
job_name = 'gradle-check'

# Get the specific job object
job = server.get_job(job_name)

# Variables to store counts of different build results
success_count = 0
failure_count =  0
unstable_count = 0
#print(list(job.get_build_ids()))
thirty_days_ago = datetime.now(timezone.utc) - timedelta(days=30)
# Loop through builds of the specific job
#for build_number in job.get_build_ids():
for build_number in job.get_build_ids():
    build_info = job.get_build(build_number)
    build_timestamp = build_info.get_timestamp()
    build_date = build_timestamp.replace(tzinfo=timezone.utc)
    if build_date >= thirty_days_ago:
        print(f"Build ID: {build_number}, Build Date: {build_date}")
        # Check if build_info is not None before accessing its status
        if build_info is not None and build_info.get_status() is not None:
            if "SUCCESS" in build_info.get_status():
                success_count += 1
            elif "FAILURE" in build_info.get_status():
                failure_count += 1
            elif "UNSTABLE" in build_info.get_status():
                unstable_count += 1
            print("success_count: ",  success_count)
            print("failure_count: ", failure_count)
            print("unstable_count: ", unstable_count)

# Calculate average values for passed, failed, and unstable builds for the specific job
total_builds = success_count + failure_count + unstable_count
print(total_builds)

average_passed_builds = success_count / total_builds if total_builds > 0 else 0
average_failed_builds = failure_count / total_builds if total_builds > 0 else 0
average_unstable_builds = unstable_count / total_builds if total_builds > 0 else 0

print("Average Builds Passed for", job_name + ":", average_passed_builds)
print("Average Builds Failed for", job_name + ":", average_failed_builds)
print("Average Builds Unstable for", job_name + ":", average_unstable_builds)