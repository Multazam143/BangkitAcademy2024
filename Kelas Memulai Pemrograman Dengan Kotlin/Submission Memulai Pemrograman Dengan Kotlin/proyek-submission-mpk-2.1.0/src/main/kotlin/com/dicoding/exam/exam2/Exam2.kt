package com.dicoding.exam.exam2

// TODO 1
fun calculate(valueA: Int, valueB: Int, valueC: Int?): Int {
    // Gunakan nilai default 50 jika valueC bernilai null
    val effectiveValueC = valueC ?: 50
    return valueA + (valueB - effectiveValueC)
}

// TODO 2
fun result(result: Int): String {
    return "Result is $result"
}
