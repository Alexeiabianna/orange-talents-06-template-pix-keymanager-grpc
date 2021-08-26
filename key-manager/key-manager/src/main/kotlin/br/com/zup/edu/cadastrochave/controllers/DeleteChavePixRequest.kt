package br.com.zup.edu.cadastrochave.controllers

import br.com.zup.edu.KeyManagerDeleteRequest
import br.com.zup.edu.grpc.ErrorHandler
import io.micronaut.validation.Validated
import javax.validation.constraints.NotBlank

@ErrorHandler
@Validated
class DeleteChavePixRequest(request: KeyManagerDeleteRequest?) {

    @field:NotBlank
    @field:ValidUUID
    val pixId: String = request!!.pixId

    @field:NotBlank
    @field:ValidUUID
    val idCliente: String = request!!.idClient
}