package me.zebrak.springbank.service

import me.zebrak.springbank.datasource.BankDataSource
import me.zebrak.springbank.model.Bank
import org.springframework.stereotype.Service

@Service
class BankService(private val dataSource: BankDataSource) {

    fun getBanks(): Collection<Bank> = dataSource.retrieveBanks()

    fun getBankByAccountNumber(accountNumber: String): Bank = dataSource.retrieveBankByAccountNumber(accountNumber)

    fun addBank(bank: Bank): Bank = dataSource.createBank(bank)
}