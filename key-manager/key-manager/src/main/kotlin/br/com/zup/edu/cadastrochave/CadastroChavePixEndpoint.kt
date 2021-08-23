package br.com.zup.edu.cadastrochave

import br.com.zup.edu.KeyManagerRequest
import br.com.zup.edu.KeyManagerResponse
import br.com.zup.edu.KeyManagerServiceGrpc
import br.com.zup.edu.grpc.ErrorHandler
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
open class CadastroChavePix(val chaveRepository: ChaveRepository): KeyManagerServiceGrpc.KeyManagerServiceImplBase() {

    private val logger = LoggerFactory.getLogger(CadastroChavePix::class.java)

    @ErrorHandler
    override fun cadastra(request: KeyManagerRequest?,
                          responseObserver: StreamObserver<KeyManagerResponse>?)
    {
        logger.info("Cadastrando chave do usuario: ")

        val chavePixRequest = ChavePixRequest(request)

        logger.info("Chave recebida: ${chavePixRequest.chaveValue}")

        if (chaveRepository.existsByChaveValue(chavePixRequest.chaveValue)) {
            throw ChaveExistenteException("Registro de chave existente")
        }

        val chavePix = chavePixRequest.toModel()
        chaveRepository.save(chavePix)

        val response = KeyManagerResponse.newBuilder()
            .setPixId(chavePix.id.toString())
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }



}