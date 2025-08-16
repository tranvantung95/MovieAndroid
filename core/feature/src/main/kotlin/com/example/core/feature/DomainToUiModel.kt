package com.example.core.feature

import com.example.share.core.domain.DomainModel

interface DomainToUiModel<UI : UiModel, Domain : DomainModel> {
    fun mapToUi(domain: Domain): UI
    fun mapToUIList(domainList: List<Domain>): List<UI> {
        return domainList.map {
            mapToUi(it)
        }
    }
}