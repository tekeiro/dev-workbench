package de.roamingthings.devworkbench.ui

import de.roamingthings.devworkbench.config.JiraConfiguration
import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.service.TraceableLinkService
import de.rpr.mycity.web.UI_PATH_TRACEABLE_LINKS
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@Controller
@RequestMapping(UI_PATH_TRACEABLE_LINKS)
class TraceableLinksController(val jiraConfiguration: JiraConfiguration, val traceableLinkService: TraceableLinkService, val log: Logger) {

    @PostMapping
    fun addAndFollowTraceableLink(request: HttpServletRequest,
                                  response: HttpServletResponse,
                                  model: Model): String {
        val code: String? = request.getParameter("code")

        if (code != null && !code.isEmpty()) {
            log.debug("Promoting link with code {}", code)

            val uri = "${jiraConfiguration.baseUri}/${code}"
            val title = null

            traceableLinkService.addTraceableLinkUniqueByCode(CreateTraceableLinkDto(code, uri, title))
            return followTraceableLink(code, response)
        }

        return "redirect:/"
    }

    @RequestMapping(
            value = "code/{code}/trace",
            method = arrayOf(RequestMethod.GET, RequestMethod.POST))
    fun followTraceableLink(@PathVariable("code") code: String, response: HttpServletResponse): String {
        log.debug("Following link with code {}", code)

        val traceableLink = traceableLinkService.retrieveByCode(code)
        if (traceableLink != null) {
            traceableLinkService.promoteTraceableLinkByCode(code)
            return "redirect:" + traceableLink.uri
        }
        return "redirect:/"
    }
}