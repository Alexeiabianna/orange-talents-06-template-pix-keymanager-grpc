package br.com.zup.edu.cadastrochave.clients

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("\${clients.erp.url}")
interface ClientContasERP {

    @Get("{idCliente}/contas{?tipo }")
    fun consultaConta(@PathVariable idCliente: String, @QueryValue tipo: String): HttpResponse<DadosClienteResponse>
}