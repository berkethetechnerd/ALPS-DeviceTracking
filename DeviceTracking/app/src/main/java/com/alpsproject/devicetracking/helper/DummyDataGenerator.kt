package com.alpsproject.devicetracking.helper

import java.util.*
import kotlin.math.round

object DummyDataGenerator {

    fun generateUsageHours(seed: Long): Array<Double> {
        val numberGenerator = Random(seed)
        return arrayOf(
            newData(numberGenerator),
            newData(numberGenerator),
            newData(numberGenerator),
            newData(numberGenerator),
            newData(numberGenerator),
            newData(numberGenerator),
            newData(numberGenerator)
        )
    }

    private fun newData(generator: Random): Double {
        val newDouble = generator.nextDouble() * 6.25
        return newDouble.round(2)
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}