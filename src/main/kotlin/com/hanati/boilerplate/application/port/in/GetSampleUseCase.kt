package com.hanati.boilerplate.application.port.incoming

import com.hanati.boilerplate.domain.Sample

interface GetSampleUseCase {
	fun getById(id: Long): Sample
	fun getAll(): List<Sample>
}
