package com.srest.framework.inject

import com.srest.framework.annotation.web.ChildComponent
import com.srest.framework.annotation.web.WebController
import com.srest.framework.bean.InternalPageFilesService
import com.srest.framework.bean.InternalPageService
import com.srest.framework.main.*
import com.srest.framework.request.HttpMethod
import com.srest.framework.response.ContentType
import com.srest.framework.util.MethodManager
import java.io.File
import kotlin.reflect.full.safeCast

internal object WebPageLoader {

    private val PAGE_FILES_EXTENSIONS = listOf("js", "css", "map")

    private val PAGE_SERVICE_BEAN = InternalPageService::class.qualifiedName ?: throw RuntimeException("Cannot resolve InternalPageService beanName!")
    private val PAGE_FILES_SERVICE_BEAN = InternalPageFilesService::class.qualifiedName ?: throw RuntimeException("Cannot resolve InternalPageFilesService beanName!")

    private val GET_PAGE_METHOD = MethodManager.getMethodByName(InternalPageService::class.java, "getPageContent") ?: throw RuntimeException("InternalPageService METHOD not found!")
    private val GET_COMPONENT_METHOD = MethodManager.getMethodByName(InternalPageService::class.java, "getComponentContent") ?: throw RuntimeException("InternalPageService METHOD not found!")
    private val GET_FILE_METHOD = MethodManager.getMethodByName(InternalPageFilesService::class.java, "getPageFileData") ?: throw RuntimeException("InternalPageFilesService METHOD not found!")
    private val GET_FRAMEWORK_METHOD = MethodManager.getMethodByName(InternalPageFilesService::class.java, "getFrameworkData") ?: throw RuntimeException("InternalPageFilesService METHOD not found!")

    fun loadPages(beansBuffer: MutableMap<String, Any>, beansMappers: MutableList<MethodEntry>, annotatedClasses: List<AnnotatedClass>) {
        val webControllers = annotatedClasses.filter { WebController::class in it.annotations }
        webControllers.forEach {
            val webController = it.classObject.getAnnotation(WebController::class.java)
            // create PAGE
            val pageServiceBean: InternalPageService = InternalPageService::class.safeCast(beansBuffer[PAGE_SERVICE_BEAN])
                    ?: throw RuntimeException("InternalPageService BEAN not exists!")
            pageServiceBean.addWebPage(webController)
            // map page/index
            beansMappers.add(MethodEntry.buildPageEntry(PAGE_SERVICE_BEAN, GET_PAGE_METHOD, webController.url))
            // map page/components
            getControllerEndpoints(webController.url, webController.childComponents)
                    .forEach {
                        beansMappers.add(MethodEntry.buildPageEntry(PAGE_SERVICE_BEAN, GET_PAGE_METHOD, it.endpoint))
                        beansMappers.add(MethodEntry.buildComponentEntry(PAGE_SERVICE_BEAN, GET_COMPONENT_METHOD, it.endpoint))
                    }
            // map included files
            loadPageFiles(beansMappers, webController.dir)
            loadPageFiles(beansMappers, webController.dir + "/static")
        }
        // map framework
        if (webControllers.isNotEmpty())
            beansMappers.add(MethodEntry(PAGE_FILES_SERVICE_BEAN, GET_FRAMEWORK_METHOD, "/framework.js", HttpMethod.GET, ContentType.APP_JS))
    }

    private fun loadPageFiles(beansMappers: MutableList<MethodEntry>, path: String) {
        val workingDir = File("application/$path")
        if (!workingDir.isDirectory) return
        workingDir.listFiles()
                .filter { it.isFile && it.extension in PAGE_FILES_EXTENSIONS }
                .forEach { beansMappers.add(MethodEntry.buildFileEntry(
                        PAGE_FILES_SERVICE_BEAN, GET_FILE_METHOD,"/$path/${it.name}", it.extension)) }
    }

    fun getControllerEndpoints(baseUrl: String, pageComponents: Array<ChildComponent>): List<ComponentEntry> {
        val components = mutableListOf<ComponentEntry>() // ComponentEntry(endpoint, "", "")
        fun getChildComponents(childComponents: Array<ChildComponent>, url: String){
            childComponents.forEach {
                val endpoint = url + it.mapping
                components.add(ComponentEntry.build(endpoint, it))
                getChildComponents(it.childComponents, endpoint)
            }
        }
        getChildComponents(pageComponents, baseUrl.replaceFirst("/", ""))
        return components
    }

}