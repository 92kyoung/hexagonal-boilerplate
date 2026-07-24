package com.hanati.boilerplate.application.service

import com.hanati.boilerplate.application.port.incoming.CreateSampleCommand
import com.hanati.boilerplate.application.port.incoming.UpdateSampleCommand
import com.hanati.boilerplate.application.port.outgoing.SamplePersistencePort
import com.hanati.boilerplate.domain.Sample
import com.hanati.boilerplate.domain.SampleNotFoundException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * 헥사고날 아키텍처의 핵심 이점을 보여주는 테스트: SampleService(코어)는 Spring 컨텍스트도,
 * DB도, JPA도 필요 없다. SamplePersistencePort를 간단한 인메모리 가짜 구현으로 대체하면
 * 순수 Kotlin 객체 생성만으로 테스트가 끝난다 - @SpringBootTest 없이 밀리초 단위로 실행됨.
 */
class SampleServiceTest {

	private class FakeSamplePersistencePort : SamplePersistencePort {
		private val store = mutableMapOf<Long, Sample>()
		private var nextId = 1L

		override fun save(sample: Sample): Sample {
			val saved = if (sample.id == null) sample.copy(id = nextId++) else sample
			store[saved.id!!] = saved
			return saved
		}

		override fun findById(id: Long): Sample? = store[id]

		override fun findAll(): List<Sample> = store.values.toList()

		override fun deleteById(id: Long) {
			store.remove(id)
		}
	}

	private val samplePersistencePort = FakeSamplePersistencePort()
	private val sampleService = SampleService(samplePersistencePort)

	@Test
	fun `생성하면 id가 채번된다`() {
		val created = sampleService.create(CreateSampleCommand(name = "hello"))

		assertEquals("hello", created.name)
		assertEquals(created, sampleService.getById(created.id!!))
	}

	@Test
	fun `없는 id를 조회하면 예외가 발생한다`() {
		assertFailsWith<SampleNotFoundException> {
			sampleService.getById(999L)
		}
	}

	@Test
	fun `수정하면 이름이 바뀐다`() {
		val created = sampleService.create(CreateSampleCommand(name = "before"))

		val updated = sampleService.update(created.id!!, UpdateSampleCommand(name = "after"))

		assertEquals("after", updated.name)
	}

	@Test
	fun `삭제하면 더 이상 조회되지 않는다`() {
		val created = sampleService.create(CreateSampleCommand(name = "temp"))

		sampleService.delete(created.id!!)

		assertFailsWith<SampleNotFoundException> {
			sampleService.getById(created.id!!)
		}
	}
}
