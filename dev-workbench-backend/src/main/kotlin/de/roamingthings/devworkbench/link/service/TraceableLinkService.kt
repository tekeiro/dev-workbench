package de.roamingthings.devworkbench.link.service

import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.TraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto

interface TraceableLinkService {
    fun retrieveTraceableLinkById(id: Long): TraceableLinkDto?

    fun retrieveByCode(code: String): TraceableLinkDto?

    fun retrieveTraceableLinks(): List<TraceableLinkDto>

    fun addTraceableLinkUniqueByCode(createRequest: CreateTraceableLinkDto): TraceableLinkDto

    fun addAndPromoteTraceableLinkUniqueByCode(createRequest: CreateTraceableLinkDto): TraceableLinkDto

    fun updateTraceableLink(id: Long, updateRequest: UpdateTraceableLinkDto): TraceableLinkDto?

    fun retrieveListByRelevanceWithLimit(limit: Int = 5): List<TraceableLinkDto>

    fun deleteById(id: Long)

    fun recordLinkAccess(id: Long): TraceableLinkDto

    fun promoteTraceableLinkByCode(code: String)

    fun traceableLinkForCodeExists(code: String): Boolean
}