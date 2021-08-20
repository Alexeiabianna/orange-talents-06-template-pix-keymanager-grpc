package br.com.zup.edu

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class ChavePix(
    val idCliente: String,
    val tipoChave: TipoChave,
    val chaveValue: String,
    val tipoConta: TipoConta
) {
    @Id
    var id: UUID = UUID.randomUUID()
    val dataCriacao: LocalDateTime = LocalDateTime.now()
}
