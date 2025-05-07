# Git Command Usage Standard

To ensure **consistency, maintainability, and portability**, prefer using Git commands defined as **PowerShell** or **Bash** wrapper scripts/functions instead of invoking `git` directly.

This approach allows centralized updates and custom behavior (e.g., logging, validations, environment config).

---

## ✅ Examples

### Bash

```bash
# ❌ Avoid this:
git status

# ✅ Prefer this:
./scripts/git/bash/git-status.sh
```

### PowerShell

```powershell
# ❌ Avoid this:
git status

# ✅ Prefer this:
./scripts/git/ps1/GitStatus.ps1
```

---

## 💡 Tip

If you're authoring new scripts, place them in the appropriate directory:
- `./scripts/git/bash/` for Bash-based workflows.
- `./scripts/git/ps1/` for PowerShell-based workflows.

Make sure the wrappers follow the naming convention `git-<command>.sh` or `Git<Command>.ps1` for discoverability.
