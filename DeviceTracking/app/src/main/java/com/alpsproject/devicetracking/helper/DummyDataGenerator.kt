package com.alpsproject.devicetracking.helper

import java.util.*

class DummyDataGenerator {

    companion object {
        fun generateUsageHours(seed: Long): Array<Double> {
            val numberGenerator = Random(seed)
            return arrayOf(
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 6.25,
                numberGenerator.nextDouble() * 3.25,
            )
        }
    }
}