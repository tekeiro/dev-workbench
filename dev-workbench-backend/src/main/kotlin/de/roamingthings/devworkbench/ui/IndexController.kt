package de.roamingthings.devworkbench.ui

import de.roamingthings.devworkbench.link.service.TraceableLinkService
import org.slf4j.Logger
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@Controller
class IndexController(val traceableLinkService: TraceableLinkService, val log: Logger) {

    @GetMapping("/", "/home")
    fun index(model: Model): String {
        val allLinks = traceableLinkService.retrieveListByRelevanceWithLimit(5)
        model.addAttribute("links", allLinks)

        return "index"
    }
}