package br.com.zup.edu.cadastrochave

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:9091/api/v1/clientes/")
interface ClientContasERP {

    @Get("{idCliente}/contas{?tipo}")
    fun consultaConta(@PathVariable idCliente: String, @QueryValue tipo: String): HttpResponse<DadosClienteResponse>
}