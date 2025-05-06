Prefer using git commands defined as Gradle tasks in the `keen.git` plugin instead of using git commands directly in the terminal. This ensures consistency and leverages the Gradle build system for better integration and automation.

For example:

```bash
# Instead of using:
git status

# Use the Gradle task:
./gradlew gitStatus
```
