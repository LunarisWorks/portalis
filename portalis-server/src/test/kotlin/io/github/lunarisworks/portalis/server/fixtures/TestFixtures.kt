package io.github.lunarisworks.portalis.server.fixtures

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import kotlin.reflect.KClass
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

internal val TestFixtureMonkey: FixtureMonkey =
    FixtureMonkey
        .builder()
        .plugin(KotlinPlugin())
        .register(Instant::class.java) { fm ->
            fm
                .giveMeBuilder<Instant>()
                .setLazy("$") { Clock.System.now() - (0L..10).random().days }
        }.build()

internal inline fun <reified T : Any> KClass<T>.giveMeBuilder() = TestFixtureMonkey.giveMeKotlinBuilder<T>()

internal inline fun <reified T : Any> KClass<T>.giveMeOne(): T = giveMeBuilder().sample()
