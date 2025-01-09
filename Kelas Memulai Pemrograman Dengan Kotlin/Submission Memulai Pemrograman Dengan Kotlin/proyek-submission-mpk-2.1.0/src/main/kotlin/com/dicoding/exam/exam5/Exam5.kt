package com.dicoding.exam.exam5

import kotlinx.coroutines.delay

// TODO 1
suspend fun sum(valueA: Int, valueB: Int): Int {
    delay(3000) // Delay selama 3 detik
    return valueA + valueB // Mengembalikan hasil penjumlahan
}

// TODO 2
suspend fun multiple(valueA: Int, valueB: Int): Int {
    delay(2000) // Delay selama 2 detik
    return valueA * valueB // Mengembalikan hasil perkalian
}
