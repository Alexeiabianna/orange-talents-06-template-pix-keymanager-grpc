package br.com.zup.edu

import br.com.zup.edu.cadastrochave.model.ChavePix
import br.com.zup.edu.cadastrochave.model.ContaAssociada
import br.com.zup.edu.cadastrochave.model.TipoChave
import br.com.zup.edu.cadastrochave.model.TipoConta
import br.com.zup.edu.cadastrochave.repository.ChaveRepository
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

@MicronautTest(transactional = false)
internal class DeletaChavePixTest(
    val repository: ChaveRepository,
    val grpcClientDelete: KeyManagerServiceDeleteGrpc.KeyManagerServiceDeleteBlockingStub
) {

    companion object {
        val ID_CLIENTE = UUID.randomUUID()
        val VALOR_CHAVE = UUID.randomUUID()
    }

    @BeforeEach
    fun init() {
        repository.deleteAll()
    }

    @Test
    fun `deve remover uma chave pix`() {
        val chavePix = ChavePix(
            idCliente = ID_CLIENTE,
            tipoChave = TipoChave.CHAVE_ALEATORIA,
            chaveValue = VALOR_CHAVE.toString(),
            tipoConta = TipoConta.CONTA_CORRENTE,
            conta = contaAssociada()
        )
        repository.save(chavePix)
        val chaveCriada = repository.findById(chavePix.id!!)

        val deletaResponse = grpcClientDelete.deleta(KeyManagerDeleteRequest.newBuilder()
            .setIdClient(ID_CLIENTE.toString())
            .setPixId(chavePix.id.toString())
            .build())
        val count = repository.count()

        with(deletaResponse) {
            Assertions.assertEquals(chaveCriada.get().id.toString(), deletaResponse.pixId.toString())
            Assertions.assertEquals(chaveCriada.get().idCliente.toString(), deletaResponse.idClient.toString())
            Assertions.assertEquals(0, count)
        }

    }

    private fun contaAssociada(): ContaAssociada {
        return ContaAssociada(
            instituicao = "ITAÚ UNIBANCO S.A.",
            nomeTitular = "Rafael M C Ponte",
            agencia = "0001",
            numeroDaConta = "291900",
            cpfTitular = "02467781054",
        )
    }

    @Factory
    class Clients {
        @Bean
        fun blockinStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                KeyManagerServiceDeleteGrpc.KeyManagerServiceDeleteBlockingStub {
            return KeyManagerServiceDeleteGrpc.newBlockingStub(channel)
        }
    }
}