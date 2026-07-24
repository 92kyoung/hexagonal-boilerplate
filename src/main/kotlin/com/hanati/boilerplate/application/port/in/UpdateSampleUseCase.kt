package com.hanati.boilerplate.application.port.incoming

import com.hanati.boilerplate.domain.Sample

interface UpdateSampleUseCase {
	fun update(id: Long, command: UpdateSampleCommand): Sample
}

data class UpdateSampleCommand(
	val name: String,
)
