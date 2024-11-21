package io.review360.assessor.storage

import io.ktor.server.auth.UserPasswordCredential

/*
 * In-memory container for Basic Auth secrets
 */
data object SecretsVault {
    private lateinit var credentials: UserPasswordCredential

    fun init(credentials: UserPasswordCredential): Boolean =
        if (!this::credentials.isInitialized) {
            this.credentials = credentials
            true
        } else false

    fun validate(credentials: UserPasswordCredential): Boolean =
        (this::credentials.isInitialized)
                && (this.credentials == credentials)
}