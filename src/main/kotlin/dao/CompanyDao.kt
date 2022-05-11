package dao

import Company
import dao.DaoCrud

interface CompanyDao : DaoCrud<Company> {
    fun getByTaxNumber(taxNumber: String): Company?
    fun getByRegistrationNumber(registrationNumber: String) : Company?
}