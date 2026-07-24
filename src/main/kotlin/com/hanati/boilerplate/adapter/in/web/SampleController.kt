package com.hanati.boilerplate.adapter.incoming.web

import com.hanati.boilerplate.adapter.incoming.web.dto.SampleRequest
import com.hanati.boilerplate.adapter.incoming.web.dto.SampleResponse
import com.hanati.boilerplate.application.port.incoming.CreateSampleCommand
import com.hanati.boilerplate.application.port.incoming.CreateSampleUseCase
import com.hanati.boilerplate.application.port.incoming.DeleteSampleUseCase
import com.hanati.boilerplate.application.port.incoming.GetSampleUseCase
import com.hanati.boilerplate.application.port.incoming.UpdateSampleCommand
import com.hanati.boilerplate.application.port.incoming.UpdateSampleUseCase
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인바운드 어댑터 (Driving Adapter). HTTP 요청을 유스케이스 호출로 변환하는 역할만 한다.
 * 컨트롤러는 필요한 유스케이스 포트만 딱 4개 주입받는다 (SampleService를 직접 알지 못함).
 */
@RestController
@RequestMapping("/api/samples")
class SampleController(
	private val createSampleUseCase: CreateSampleUseCase,
	private val getSampleUseCase: GetSampleUseCase,
	private val updateSampleUseCase: UpdateSampleUseCase,
	private val deleteSampleUseCase: DeleteSampleUseCase,
) {

	@PostMapping
	fun create(@Valid @RequestBody request: SampleRequest): ResponseEntity<SampleResponse> {
		val sample = createSampleUseCase.create(CreateSampleCommand(name = request.name))
		return ResponseEntity.status(HttpStatus.CREATED).body(SampleResponse.from(sample))
	}

	@GetMapping("/{id}")
	fun getById(@PathVariable id: Long): SampleResponse =
		SampleResponse.from(getSampleUseCase.getById(id))

	@GetMapping
	fun getAll(): List<SampleResponse> =
		getSampleUseCase.getAll().map { SampleResponse.from(it) }

	@PutMapping("/{id}")
	fun update(@PathVariable id: Long, @Valid @RequestBody request: SampleRequest): SampleResponse =
		SampleResponse.from(updateSampleUseCase.update(id, UpdateSampleCommand(name = request.name)))

	@DeleteMapping("/{id}")
	fun delete(@PathVariable id: Long): ResponseEntity<Void> {
		deleteSampleUseCase.delete(id)
		return ResponseEntity.noContent().build()
	}
}
