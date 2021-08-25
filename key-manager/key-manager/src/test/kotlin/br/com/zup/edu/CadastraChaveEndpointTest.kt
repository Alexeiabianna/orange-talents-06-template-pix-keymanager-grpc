package br.com.zup.edu

import br.com.zup.edu.cadastrochave.clients.ClientContasERP
import br.com.zup.edu.cadastrochave.clients.DadosClienteResponse
import br.com.zup.edu.cadastrochave.clients.InstituicaoResponse
import br.com.zup.edu.cadastrochave.clients.TitularResponse
import br.com.zup.edu.cadastrochave.repository.ChaveRepository
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.http.HttpResponse
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

@MicronautTest(transactional = false)
internal class CadastraChaveEndpointTest(
    val repository: ChaveRepository,
    val grpcClient: KeyManagerServiceGrpc.KeyManagerServiceBlockingStub
) {
    @Inject
    lateinit var erpClient: ClientContasERP

    companion object {
        val ID_CLIENTE = UUID.randomUUID()
    }

    @BeforeEach
    fun init() {
        repository.deleteAll()
    }

    @Test
    fun `deve registrar nova chave pix`() {
        //cenario
        `when`(erpClient.consultaConta(
            idCliente = ID_CLIENTE.toString(),
            tipo = "CONTA_CORRENTE"))
            .thenReturn(HttpResponse.ok(dadosDaContaResponse()))
        //acao
        val response = grpcClient.cadastra(KeyManagerRequest.newBuilder()
            .setIdClient(ID_CLIENTE.toString())
            .setChaveTipo(ChaveTipo.EMAIL)
            .setValorChave("rafaponte@zup.com")
            .setContaTipo(ContaTipo.CONTA_CORRENTE)
            .build())
        //validacao
        with(response) {
            Assertions.assertNotNull(pixId)
        }
    }

    @MockBean(ClientContasERP::class)
    fun erpClient(): ClientContasERP? {
        return Mockito.mock(ClientContasERP::class.java)
    }

    @Factory
    class Clients {
        @Bean
        fun blockinStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): KeyManagerServiceGrpc.KeyManagerServiceBlockingStub {
            return KeyManagerServiceGrpc.newBlockingStub(channel)
        }
    }

    private fun dadosDaContaResponse(): DadosClienteResponse {
        return DadosClienteResponse(
            tipo = "CONTA_CORRENTE",
            instituicao = InstituicaoResponse("UNIBANCO ITAU SA", "60701190"),
            agencia = "1218",
            numero = "291900",
            titular = TitularResponse(
                "5260263c-a3c1-4727-ae32-3bdb2538841b",
                "Rafael Ponte",
                "63657527325")
        )

    }

}