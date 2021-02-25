package com.challenge.omdb

import com.challenge.omdb.utils.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

internal class TestSchedulerProvider : SchedulerProvider {
    override fun ui(): Scheduler = Schedulers.trampoline()
    override fun io(): Scheduler = Schedulers.trampoline()
    override fun computation(): Scheduler = Schedulers.trampoline()
}
