package br.com.zup.edu.cadastrochave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChaveRepository: JpaRepository<ChavePix, UUID> {
    fun existsByChaveValue(idCliente: String): Boolean
}