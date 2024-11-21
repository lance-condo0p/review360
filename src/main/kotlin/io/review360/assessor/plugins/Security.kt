package io.review360.assessor.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.review360.assessor.storage.SecretsVault

fun Application.configureSecurity() {
    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (SecretsVault.validate(credentials)) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}
