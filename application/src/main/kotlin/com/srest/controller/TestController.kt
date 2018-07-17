package com.srest.controller

import com.srest.framework.annotation.Component
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.util.Controller
import com.srest.framework.util.RequestMapping

@Controller
internal class TestControllerA(
        private val simpleComponent: ParentComponent
) {

    @RequestMapping(HttpMethod.GET, "/aa", ContentType.JSON_TYPE)
    fun getTest(): String {
        return "{ \"${this::class.simpleName}\" : ${simpleComponent.getIndexValue()}}"
    }
}

@Controller
internal class TestControllerB(
        private val simpleComponent: ParentComponent
) {

    @RequestMapping(HttpMethod.GET, "/bb", ContentType.JSON_TYPE)
    fun getTest(): String {
        return "{ \"${this::class.simpleName}\" : ${simpleComponent.getIndexValue()}}"
    }
}

@Component
internal class SimpleComponent {
    var index = 0
    fun getIndexValue() = index++
}

@Component
internal class SimpleComponentB {
    var index = 0
    fun getIndexValue() = index++
}


@Component
internal class ParentComponent(
        val simpleComponent: SimpleComponent,
        val simpleComponentB: SimpleComponentB
) {
    fun getIndexValue() = "\"${simpleComponent.getIndexValue()}_PARENT\""
}