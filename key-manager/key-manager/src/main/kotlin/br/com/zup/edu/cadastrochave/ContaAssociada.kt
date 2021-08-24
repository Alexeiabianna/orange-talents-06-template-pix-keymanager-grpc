package br.com.zup.edu.cadastrochave

import javax.persistence.Embeddable

@Embeddable
data class ContaAssociada(
    val instituicao: String,
    val nomeTitular: String,
    val agencia: String,
    val numeroDaConta: String,
    val cpfTitular: String
) {

}
