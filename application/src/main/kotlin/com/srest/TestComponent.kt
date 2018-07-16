package com.srest

import com.srest.framework.annotation.Component

@Component
internal class TestComponent(
        val testComponentB: TestComponentB
) {

    fun printTest() {
        println("Test OK!")
    }

}

@Component
internal class TestComponentB {

    fun printTest2() {
        println("Test OK!")
    }

}