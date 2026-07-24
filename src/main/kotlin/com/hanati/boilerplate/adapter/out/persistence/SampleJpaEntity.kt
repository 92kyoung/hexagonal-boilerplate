package com.hanati.boilerplate.adapter.outgoing.persistence

import com.hanati.boilerplate.domain.Sample
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sample")
class SampleJpaEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	val id: Long? = null,

	@Column(name = "name", nullable = false, length = 255)
	val name: String,
) {
	fun toDomain(): Sample = Sample(id = id, name = name)

	companion object {
		fun fromDomain(sample: Sample): SampleJpaEntity =
			SampleJpaEntity(id = sample.id, name = sample.name)
	}
}
