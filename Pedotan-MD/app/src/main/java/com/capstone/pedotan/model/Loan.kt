package com.capstone.pedotan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Loan(
    val id: Int,
    val loan: Int,
    val investor: String,
    val company: String,
    val due: String,
) : Parcelable

object LoanData {
    val loans = listOf(
        Loan(
            1,
            10_000_000,
            "Pak Joko",
            "ABC Corporation",
            "31-DES-2023"
        ),
        Loan(
            2,
            2_500_000,
            "Bu Rina",
            "XYZ Industries",
            "25-SEP-2023"
        ),
        Loan(
            3,
            1_200_000,
            "Pak Budi",
            "DEF Company",
            "01-DES-2023"
        ),
    )
}
