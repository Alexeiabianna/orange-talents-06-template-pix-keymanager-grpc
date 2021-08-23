package br.com.zup.edu.cadastrochave

import br.com.zup.edu.KeyManagerRequest
import br.com.zup.edu.grpc.ErrorHandler
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@ErrorHandler
class ChavePixRequest(val request: KeyManagerRequest?) {

    @field:NotBlank
    val idCliente: String = request!!.idClient

    @field:NotNull
    var tipoChave: TipoChave = TipoChave.valueOf(request!!.chaveTipo.toString())

    @field:NotBlank
    var chaveValue: String = request!!.valorChave

    @field:NotNull
    var tipoConta: TipoConta = TipoConta.valueOf(request!!.contaTipo.toString())

    fun validatorFormat(chaveValue: String): String {
        if (tipoChave == TipoChave.CHAVE_ALEATORIA) {
            return geraChaveAleatoria()
        }
        if (tipoChave == TipoChave.CPF) {
            if (chaveValue.matches("^[0-9]{11}$".toRegex()))
                return chaveValue
        }
        if (tipoChave == TipoChave.EMAIL) {
            if (chaveValue.matches("^\\S+@\\S+\$".toRegex())) {
                return chaveValue
            }
        }
        if (tipoChave == TipoChave.CELULAR) {
            if (chaveValue.matches("^\\([1-9]{2}\\) (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}\$".toRegex()))
                return chaveValue
        }
        return throw FormatoInvalidoException("Formato ${tipoChave.toString()} inválido")
    }

    fun geraChaveAleatoria(): String {
        this.chaveValue = UUID.randomUUID().toString()
        return chaveValue
    }

    fun converteIdCliente(): UUID {
        return UUID.fromString(this.idCliente)
    }

    fun toModel(): ChavePix {
        return ChavePix(converteIdCliente(), this.tipoChave, validatorFormat(this.chaveValue), this.tipoConta)
    }
}
