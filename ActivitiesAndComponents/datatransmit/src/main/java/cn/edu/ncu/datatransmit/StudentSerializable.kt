package cn.edu.ncu.datatransmit

import java.io.Serializable
import java.time.LocalDate

data class StudentSerializable(val name: String, val birthday: LocalDate) : Serializable