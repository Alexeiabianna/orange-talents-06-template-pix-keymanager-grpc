package br.com.zup.edu.cadastrochave

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class ChavePix(
    @field:NotBlank
    val idCliente: UUID,

    @Enumerated(value = EnumType.STRING)
    @field:NotNull
    val tipoChave: TipoChave,

    @field:NotBlank
    val chaveValue: String,

    @Enumerated(value = EnumType.STRING)
    @field:NotNull
    val tipoConta: TipoConta
) {
    @Id
    @GeneratedValue
    var id: UUID? = null
    val dataCriacao: LocalDateTime = LocalDateTime.now()

}
