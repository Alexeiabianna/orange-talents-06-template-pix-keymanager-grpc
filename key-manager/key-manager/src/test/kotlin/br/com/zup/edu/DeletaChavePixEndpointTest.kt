package br.com.zup.edu

import br.com.zup.edu.cadastrochave.model.ChavePix
import br.com.zup.edu.cadastrochave.model.ContaAssociada
import br.com.zup.edu.cadastrochave.model.TipoChave
import br.com.zup.edu.cadastrochave.model.TipoConta
import br.com.zup.edu.cadastrochave.repository.ChaveRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

@MicronautTest(transactional = false)
internal class DeletaChavePixTest(
    val repository: ChaveRepository,
    val grpcClientDelete: KeyManagerServiceDeleteGrpc.KeyManagerServiceDeleteBlockingStub
) {

    companion object {
        val ID_CLIENTE_A = UUID.randomUUID()
        val ID_CLIENTE_B = UUID.randomUUID()

        val VALOR_CHAVE = UUID.randomUUID()
    }

    @BeforeEach
    fun init() {
        repository.deleteAll()
    }

    @Test
    fun `deve remover uma chave pix`() {
        val chavePix = getChavePix(ID_CLIENTE_A)
        repository.save(chavePix)

        val chaveCriada = repository.findById(chavePix.id!!).get()

        val response = deleteResponse(chaveCriada, chaveCriada.idCliente)
        val count = repository.count()

        with(response) {
            assertEquals(chaveCriada.id.toString(), response!!.pixId.toString())
            assertEquals(chaveCriada.idCliente.toString(), response.idClient.toString())
            assertEquals(0, count)
        }
    }

    @Test
    fun `nao deve remover uma chave pix com id cliente diferente`() {
        val chavePixClienteA = getChavePix(ID_CLIENTE_A)
        val chavePixClienteB = getChavePix(ID_CLIENTE_B)

        repository.save(chavePixClienteA)
        repository.save(chavePixClienteB)

        val count = repository.count()

        val response = assertThrows<StatusRuntimeException> {
            deleteResponse(chavePixClienteA, chavePixClienteB.idCliente)
        }

        with(response) {
            assertEquals(Status.NOT_FOUND.code, status.code)
            assertEquals("Chave não existe no sistema", status.description)
        }

    }

    @Test
    fun `nao deve remover chave pix quando cliente inexistente`() {
        val idCliente = UUID.randomUUID()
        val chavePix = getChavePix(ID_CLIENTE_A)

        val response = assertThrows<StatusRuntimeException> {
            deleteResponse(chavePix, idCliente)
        }

        with(response) {
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
        }
    }

    private fun deleteResponse(chavePix: ChavePix, idCliente: UUID): KeyManagerDeleteResponse? {
        return grpcClientDelete.deleta(
            KeyManagerDeleteRequest.newBuilder()
                .setIdClient(idCliente.toString())
                .setPixId(chavePix.id.toString())
                .build()
        )
    }

    private fun getChavePix(idCliente: UUID): ChavePix {
        return ChavePix(
            idCliente = idCliente,
            tipoChave = TipoChave.CHAVE_ALEATORIA,
            chaveValue = VALOR_CHAVE.toString(),
            tipoConta = TipoConta.CONTA_CORRENTE,
            conta = contaAssociada()
        )
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
