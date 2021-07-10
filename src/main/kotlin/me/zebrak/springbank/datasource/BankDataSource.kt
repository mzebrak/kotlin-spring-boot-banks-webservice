package me.zebrak.springbank.datasource

import me.zebrak.springbank.model.Bank

interface BankDataSource {
    fun retrieveBanks(): Collection<Bank>

    fun retrieveBankByAccountNumber(accountNumber: String): Bank

    fun createBank(bank: Bank): Bank
}