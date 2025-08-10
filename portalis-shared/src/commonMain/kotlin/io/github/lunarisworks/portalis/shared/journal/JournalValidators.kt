package io.github.lunarisworks.portalis.shared.journal

import io.konform.validation.Validation
import io.konform.validation.constraints.notBlank

val validateMoodLevel =
    Validation {
        constrain("Mood level must be between 1 and 5") {
            it in 1..5
        }
    }

val validateCreateJournalRequest =
    Validation {
        CreateJournalRequest::content {
            notBlank()
        }
        CreateJournalRequest::title ifPresent {
            notBlank()
        }
        CreateJournalRequest::moodLevel ifPresent {
            run(validateMoodLevel)
        }
    }

val validatePatchJournalRequest =
    Validation {
        PatchJournalRequest::title ifPresent {
            notBlank()
        }
        PatchJournalRequest::content ifPresent {
            notBlank()
        }
        PatchJournalRequest::moodLevel ifPresent {
            run(validateMoodLevel)
        }
    }
