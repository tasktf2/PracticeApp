package com.setjy.practiceapp.domain.base

interface UseCase<in Params, out T> where T : Any {

    fun execute(params: Params): T
}