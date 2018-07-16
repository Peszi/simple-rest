package com.srest.controller

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.ComponentRequest
import com.srest.framework.annotation.WebComponent
import com.srest.framework.annotation.web.WebController

//@WebController("/", "web", [
//    ChildComponent("/user","page/user.html", UserComponent::class,
//            [
//                ChildComponent("/john","page/user/john.html", JohnComponent::class),
//                ChildComponent("/bob","page/user/bob.html", JohnComponent::class),
//                ChildComponent("/dick","page/user/dick.html", JohnComponent::class)
//            ]),
//    ChildComponent("/spring","page/spring.html", SpringComponent::class)
//])
internal class IndexController {

    @ComponentRequest(UserComponent::class) // that is not requestMapping!!! INTERNAL ONLY
    fun getUser(): Map<String, Int> {
        return mapOf("data" to 59)
    }
}

internal class UserComponent

internal class SpringComponent

internal class JohnComponent