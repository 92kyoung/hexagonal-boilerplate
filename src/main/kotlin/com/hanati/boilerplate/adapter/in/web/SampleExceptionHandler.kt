package com.hanati.boilerplate.adapter.incoming.web

import com.hanati.boilerplate.domain.SampleNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class SampleExceptionHandler {

	@ExceptionHandler(SampleNotFoundException::class)
	fun handleNotFound(ex: SampleNotFoundException): ResponseEntity<ErrorResponse> =
		ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(message = ex.message ?: "Not found"))
}

data class ErrorResponse(
	val message: String,
)
