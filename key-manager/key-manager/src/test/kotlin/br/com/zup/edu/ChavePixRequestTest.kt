package br.com.zup.edu

import br.com.zup.edu.cadastrochave.controllers.ChavePixRequest
import br.com.zup.edu.cadastrochave.model.TipoChave
import br.com.zup.edu.cadastrochave.model.TipoConta
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.*

import java.util.*

@MicronautTest
internal class ChavePixRequestTest {

    lateinit var chavePixRequest: ChavePixRequest

    companion object {
        val ID_CLIENTE = UUID.randomUUID()
        val VALOR_CHAVE = UUID.randomUUID()
        val ALEATORIA = ChaveTipo.CHAVE_ALEATORIA
        val CPF = ChaveTipo.CPF
        val EMAIL = ChaveTipo.EMAIL
        val CELULAR = ChaveTipo.CELULAR
    }

    @Test
    fun `deve selecionar o tipo de chave ALEATORIA corretamente`() {
        chavePixRequest = getRequest(ALEATORIA)
        assertEquals(TipoChave.CHAVE_ALEATORIA.toString(), chavePixRequest.tipoChave.toString())
    }

    @Test
    fun `deve selecionar o tipo de chave CPF corretamente`() {
        chavePixRequest = getRequest(CPF)
        assertEquals(TipoChave.CPF.toString(), chavePixRequest.tipoChave.toString())
    }

    @Test
    fun `deve selecionar o tipo de chave EMAIL corretamente`() {
        chavePixRequest = getRequest(EMAIL)
        assertEquals(TipoChave.EMAIL.toString(), chavePixRequest.tipoChave.toString())
    }

    @Test
    fun `deve selecionar o tipo de chave CELULAR corretamente`() {
        chavePixRequest = getRequest(CELULAR)
        assertEquals(TipoChave.CELULAR.toString(), chavePixRequest.tipoChave.toString())
    }

    fun getRequest(tipoChave: ChaveTipo): ChavePixRequest {
        val request = KeyManagerRequest.newBuilder()
            .setIdClient(ID_CLIENTE.toString())
            .setChaveTipo(tipoChave)
            .setValorChave(VALOR_CHAVE.toString())
            .setContaTipo(ContaTipo.CONTA_CORRENTE)
            .build()
        return ChavePixRequest(request)
    }

    @Factory
    class Clients {
        @Bean
        fun blockinStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }

}