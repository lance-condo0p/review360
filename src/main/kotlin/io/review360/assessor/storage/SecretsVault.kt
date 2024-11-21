package io.review360.assessor.storage

data class Credentials(
    val login: String,
    val password: String,
)

/*
 * In-memory container for Basic Auth secrets
 */
object SecretsVault {
    private lateinit var login: String
    private lateinit var password: String

    fun init(login: String, password: String): Boolean =
        if ((!this::login.isInitialized) && (!this::password.isInitialized)) {
            this.login = login
            this.password = password
            true
        } else false

    fun validate(login: String, password: String): Boolean =
        (this::login.isInitialized)
                && (this::password.isInitialized)
                && (this.login == login)
                && (this.password == password)
}