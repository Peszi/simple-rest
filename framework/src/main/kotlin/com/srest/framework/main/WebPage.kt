package com.srest.framework.main

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.WebController

internal class WebPage(
        val dir: String,
        val childComponents: Array<ChildComponent>
) {
    companion object {
        fun build(webController: WebController, childComponents: Array<ChildComponent>) =
                WebPage(webController.dir, childComponents)
    }
}