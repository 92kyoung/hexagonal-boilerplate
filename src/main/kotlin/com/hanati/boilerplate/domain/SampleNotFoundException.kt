package com.hanati.boilerplate.domain

class SampleNotFoundException(id: Long) : RuntimeException("Sample not found: id=$id")
