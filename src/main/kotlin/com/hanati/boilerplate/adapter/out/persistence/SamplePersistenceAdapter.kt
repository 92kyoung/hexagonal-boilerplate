package com.hanati.boilerplate.adapter.outgoing.persistence

import com.hanati.boilerplate.application.port.outgoing.SamplePersistencePort
import com.hanati.boilerplate.domain.Sample
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

/**
 * SamplePersistencePort의 구현체. 코어(SampleService)는 이 클래스의 존재를 모르고
 * SamplePersistencePort 인터페이스만 의존한다 - JPA 엔티티 <-> 도메인 모델 변환은 여기서만 일어난다.
 */
@Component
class SamplePersistenceAdapter(
	private val sampleJpaRepository: SampleJpaRepository,
) : SamplePersistencePort {

	@Transactional
	override fun save(sample: Sample): Sample =
		sampleJpaRepository.save(SampleJpaEntity.fromDomain(sample)).toDomain()

	@Transactional(readOnly = true)
	override fun findById(id: Long): Sample? =
		sampleJpaRepository.findById(id).map { it.toDomain() }.orElse(null)

	@Transactional(readOnly = true)
	override fun findAll(): List<Sample> =
		sampleJpaRepository.findAll().map { it.toDomain() }

	@Transactional
	override fun deleteById(id: Long) {
		sampleJpaRepository.deleteById(id)
	}
}
