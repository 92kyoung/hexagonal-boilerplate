package com.hanati.boilerplate.adapter.outgoing.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface SampleJpaRepository : JpaRepository<SampleJpaEntity, Long>
