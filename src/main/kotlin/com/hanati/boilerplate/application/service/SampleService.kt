package com.hanati.boilerplate.application.service

import com.hanati.boilerplate.application.port.incoming.CreateSampleCommand
import com.hanati.boilerplate.application.port.incoming.CreateSampleUseCase
import com.hanati.boilerplate.application.port.incoming.DeleteSampleUseCase
import com.hanati.boilerplate.application.port.incoming.GetSampleUseCase
import com.hanati.boilerplate.application.port.incoming.UpdateSampleCommand
import com.hanati.boilerplate.application.port.incoming.UpdateSampleUseCase
import com.hanati.boilerplate.application.port.outgoing.SamplePersistencePort
import com.hanati.boilerplate.domain.Sample
import com.hanati.boilerplate.domain.SampleNotFoundException
import org.springframework.stereotype.Service

/**
 * 인바운드 포트 4개를 한 클래스가 구현한다 - 유스케이스가 늘어나면 클래스를 쪼개면 된다.
 * 아웃바운드 포트(SamplePersistencePort)만 의존하고, JPA/Kafka 같은 구체 기술은 전혀 모른다.
 */
@Service
class SampleService(
	private val samplePersistencePort: SamplePersistencePort,
) : CreateSampleUseCase, GetSampleUseCase, UpdateSampleUseCase, DeleteSampleUseCase {

	override fun create(command: CreateSampleCommand): Sample =
		samplePersistencePort.save(Sample(name = command.name))

	override fun getById(id: Long): Sample =
		samplePersistencePort.findById(id) ?: throw SampleNotFoundException(id)

	override fun getAll(): List<Sample> = samplePersistencePort.findAll()

	override fun update(id: Long, command: UpdateSampleCommand): Sample {
		val sample = getById(id)
		return samplePersistencePort.save(sample.rename(command.name))
	}

	override fun delete(id: Long) {
		getById(id)
		samplePersistencePort.deleteById(id)
	}
}
