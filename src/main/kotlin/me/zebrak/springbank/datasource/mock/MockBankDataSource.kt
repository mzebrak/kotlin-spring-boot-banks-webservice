package me.zebrak.springbank.datasource.mock

import me.zebrak.springbank.datasource.BankDataSource
import me.zebrak.springbank.model.Bank
import org.springframework.stereotype.Repository
import java.lang.IllegalArgumentException

@Repository("mock")
class MockBankDataSource : BankDataSource {
    val banks = mutableListOf<Bank>(Bank("1234", 10.0, 1),
                                    Bank("1010", 20.0, 2),
                                    Bank("5678", 30.0, 3))

    override fun retrieveBanks(): Collection<Bank> = banks

    override fun retrieveBankByAccountNumber(accountNumber: String): Bank =
        banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number: $accountNumber")

    override fun createBank(bank: Bank): Bank {
        if (banks.any { it.accountNumber == bank.accountNumber })
            throw IllegalArgumentException("Bank with account number: ${bank.accountNumber} already exists!")

        banks.add(bank)
        return bank
    }

    override fun updateBank(bank: Bank): Bank {
        val currentBank = banks.firstOrNull { it.accountNumber == bank.accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number: ${bank.accountNumber}")

        banks.remove(currentBank)
        banks.add(bank)
        return bank
    }

    override fun deleteBank(accountNumber: String) {
        val currentBank = banks.firstOrNull { it.accountNumber == accountNumber }
            ?: throw NoSuchElementException("Could not find a bank with account number: $accountNumber")

        banks.remove(currentBank)
    }
}