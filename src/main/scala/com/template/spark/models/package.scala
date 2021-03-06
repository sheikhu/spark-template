package com.template.spark

package object models {

  case class Employee(empNo: Long, birthDate: String, firstName: String, lastName: String,
                       gender: String, hireDate: String)

  case class Salary(empNo: Long, salary: Double, fromDate: String, toDate: String)

  case class Title(empNo: Long, title: String, fromDate: String, toDate: String)

  case class Department(id: String, name: String)

  case class JobParameters(input: String = "",
                           inputFormat: String = "csv",
                           outputFormat: String = "csv",
                           output: String = "", verbose: Boolean = false, debug: Boolean = false)

}
