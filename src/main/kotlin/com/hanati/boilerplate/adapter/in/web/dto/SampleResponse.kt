package com.hanati.boilerplate.adapter.incoming.web.dto

import com.hanati.boilerplate.domain.Sample

data class SampleResponse(
	val id: Long,
	val name: String,
) {
	companion object {
		fun from(sample: Sample): SampleResponse =
			SampleResponse(id = requireNotNull(sample.id), name = sample.name)
	}
}
