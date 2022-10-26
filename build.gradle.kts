import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	kotlin("plugin.jpa") version "1.6.21"
	kotlin("kapt") version "1.7.10"
	id("jacoco")
}

group = "com.server"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// ** starter ** //
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// ** webflux ** //
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// ** h2 ** //
	runtimeOnly("com.h2database:h2")

	// ** jpa ** //
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// ** querydsl ** //
	val querydslVersion = "5.0.0"
	implementation("com.querydsl:querydsl-jpa:$querydslVersion")
	kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")

	// cache, redis
//	implementation("org.springframework.boot:spring-boot-starter-cache")

	// ** test ** //
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.7"

}

tasks.withType<JacocoReport> {
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.required.set(true)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}
