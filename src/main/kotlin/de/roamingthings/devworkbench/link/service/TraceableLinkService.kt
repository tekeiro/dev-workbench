package de.roamingthings.devworkbench.link.service

import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.TraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto

interface TraceableLinkService {
    fun retrieveTraceableLinkById(id: Long): TraceableLinkDto?

    fun retrieveByCode(code: String): TraceableLinkDto?

    fun retrieveTraceableLinks(): List<TraceableLinkDto>

    fun addTraceableLinkUniqueById(createRequest: CreateTraceableLinkDto): TraceableLinkDto

    fun updateTraceableLink(id: Long, updateRequest: UpdateTraceableLinkDto): TraceableLinkDto?

    fun retrieveListByRelevance(): List<TraceableLinkDto>

    fun deleteById(id: Long)

    fun recordLinkAccess(id: Long): TraceableLinkDto

    fun promoteTraceableLinkByCode(code: String, uri: String)

    fun traceableLinkForCodeExists(code: String): Boolean
}