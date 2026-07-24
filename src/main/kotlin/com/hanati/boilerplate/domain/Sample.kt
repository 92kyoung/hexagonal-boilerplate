package com.hanati.boilerplate.domain

/**
 * 순수 도메인 모델 예시. Spring, JPA 어노테이션에 의존하지 않는다.
 * 실제 프로젝트에서는 이 클래스를 지우고 각자의 도메인 모델로 교체한다.
 */
data class Sample(
	val id: Long? = null,
	val name: String,
) {
	init {
		require(name.isNotBlank()) { "name은 비어있을 수 없습니다." }
	}

	fun rename(newName: String): Sample = copy(name = newName)
}
