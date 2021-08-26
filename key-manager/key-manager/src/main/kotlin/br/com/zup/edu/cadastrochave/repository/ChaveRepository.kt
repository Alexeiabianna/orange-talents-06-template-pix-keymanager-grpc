package br.com.zup.edu.cadastrochave.repository

import br.com.zup.edu.cadastrochave.model.ChavePix
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository: JpaRepository<ChavePix, UUID> {
    fun existsByChaveValue(idCliente: String): Boolean
    //fun existsById(pix_id: String): Boolean
}