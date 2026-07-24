package com.hanati.boilerplate.application.port.outgoing

import com.hanati.boilerplate.domain.Sample

/**
 * 아웃바운드 포트 (Driven Port). 영속성 요구사항만 정의한다 (JPA인지 아닌지 모름).
 */
interface SamplePersistencePort {
	fun save(sample: Sample): Sample
	fun findById(id: Long): Sample?
	fun findAll(): List<Sample>
	fun deleteById(id: Long)
}
