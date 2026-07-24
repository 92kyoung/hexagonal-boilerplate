package com.hanati.boilerplate.adapter.incoming.web.dto

import jakarta.validation.constraints.NotBlank

data class SampleRequest(
	@field:NotBlank
	val name: String,
)
