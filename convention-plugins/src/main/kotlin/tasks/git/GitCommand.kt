/*
 * Copyright (c) 2025, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package tasks.git

/**
 * Represents a Git command as a parameterless function returning its output as a [String].
 *
 * This interface allows encapsulating Git commands as callable objects, enabling deferred or dynamic execution as
 * needed.
 */
interface GitCommand : () -> String
