package com.hanati.boilerplate.application.port.incoming

import com.hanati.boilerplate.domain.Sample

/**
 * 인바운드 포트 (Driving Port). 유스케이스 하나당 인터페이스 하나 - 인터페이스 분리 원칙(ISP)을
 * 지켜서, 어댑터가 필요한 유스케이스만 정확히 의존하게 한다.
 */
interface CreateSampleUseCase {
	fun create(command: CreateSampleCommand): Sample
}

data class CreateSampleCommand(
	val name: String,
)
