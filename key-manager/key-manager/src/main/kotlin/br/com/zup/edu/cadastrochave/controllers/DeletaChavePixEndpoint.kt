package br.com.zup.edu.cadastrochave.controllers

import br.com.zup.edu.KeyManagerDeleteRequest
import br.com.zup.edu.KeyManagerDeleteResponse
import br.com.zup.edu.KeyManagerServiceDeleteGrpc
import br.com.zup.edu.cadastrochave.repository.ChaveRepository
import br.com.zup.edu.grpc.ErrorHandler
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
open class DeletaChavePixEndpoint(val chaveRepository: ChaveRepository): KeyManagerServiceDeleteGrpc.KeyManagerServiceDeleteImplBase() {

    private val logger = LoggerFactory.getLogger(CadastroChavePix::class.java)

    @ErrorHandler
    override fun deleta(request: KeyManagerDeleteRequest?, responseObserver: StreamObserver<KeyManagerDeleteResponse>?) {

        val deletaChavePix = DeleteChavePixRequest(request)

        val id = UUID.fromString(deletaChavePix.pixId)
        val idCliente = UUID.fromString(deletaChavePix.idCliente)

        val chave = chaveRepository.findByIdAndIdCliente(id, idCliente)

        if (chave.isEmpty) {
            throw ChaveNaoExisteException()
        }

        logger.info("Deletando chave: ${deletaChavePix.pixId} Cliente Id: ${deletaChavePix.idCliente}")

        chaveRepository.delete(chave.get())

        val response = KeyManagerDeleteResponse.newBuilder()
            .setPixId(deletaChavePix.pixId)
            .setIdClient(deletaChavePix.idCliente)
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

}