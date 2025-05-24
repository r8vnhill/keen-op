# CI/CD README

## Versioning and Tagging Strategy

### 🔖 Centralized Version Management

All project modules share a single version, defined in:

```kotlin
convention-plugins/src/main/kotlin/Versions.kt
```

To apply the centralized versioning, all subprojects should use the base convention plugin:

```kotlin
plugins {
    id("keen.base")
}
```

For example, the `keen.reproducible` plugin applies versioning via:

```kotlin
plugins {
    id("keen.base")
}
```

---

### 🧭 Semantic Versioning and Git Tagging

This project adheres to [Semantic Versioning](https://semver.org) (`MAJOR.MINOR.PATCH`) to convey meaning and manage compatibility.

- Each release **must** be identified by a **Git tag** (e.g. `v1.2.3`) that matches the version in `Versions.kt`.
- Tags provide **traceability** and can be used to trigger release workflows in CI/CD pipelines.

---

### ✅ Accepted Version Formats

The following suffixes are allowed for pre-release versions:

- `-SNAPSHOT`
- `-ALPHA`
- `-BETA`
- `-RC.n` (e.g. `-RC.1`, `-RC.2`)

#### Examples of valid versions

- `1.0.0-SNAPSHOT`
- `1.2.3-BETA`
- `4.2.0-RC.3`

#### Invalid formats (not allowed)

- `1.0.0-MILESTONE.1`
- `4.2.0-RC-3`

Please ensure all tags follow this format to maintain consistency and avoid release automation issues.

You can extend your `CI_CD.md` file with a section explaining how to configure and use **Release Drafter** for automatic changelog generation. Here’s a complete and polished addition:

---

## 📝 Auto-Generated Release Notes with Release Drafter

To simplify and standardize changelog generation, this repository uses [**Release Drafter**](https://github.com/release-drafter/release-drafter) to automatically draft GitHub release notes as pull requests are merged.

### 🛠️ Setup

The GitHub Actions workflow is defined in:

```
.github/workflows/release-drafter.yml
```

The configuration lives in:

```
.github/release-drafter.yml
```

It groups pull requests into categories (e.g., Features, Fixes, Maintenance) based on their labels, and suggests the next semantic version based on PR labels like `major`, `minor`, or `patch`.

### 📦 How It Works

1. A **draft release** is automatically updated every time a pull request is merged.
2. The content is grouped and formatted based on PR labels.
3. When you're ready to release, just **publish the draft** on GitHub.

### ✅ Labeling Pull Requests

To ensure accurate and useful release notes, label your PRs with one or more of the following:

| Label            | Effect                                     |
|------------------|--------------------------------------------|
| `feature`        | Appears under 🚀 Features                  |
| `bug`            | Appears under 🐛 Bug Fixes                 |
| `chore`          | Appears under 🧰 Maintenance               |
| `major`          | Increments the **major** version           |
| `minor`          | Increments the **minor** version           |
| `patch`          | Increments the **patch** version (default) |
| `skip-changelog` | Excludes the PR from the changelog         |

> 💡 PR labels should be added **before merging** to be included in the draft release.

### 📄 Customizing the Template

The changelog format and version resolution logic can be customized in `.github/release-drafter.yml`. Refer to [Release Drafter Docs](https://github.com/release-drafter/release-drafter#configuration-options) for advanced usage.

#### 📤 Publishing a Release

Once the draft release looks good:

1. Go to the **GitHub Releases** section.
2. Click the latest **Draft release**.
3. Review the notes and publish them.

GitHub will create a tag (e.g., `v1.2.3`) and trigger any release-specific CI workflows.
