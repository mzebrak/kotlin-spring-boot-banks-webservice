package me.zebrak.springbank.datasource.network

import me.zebrak.springbank.datasource.BankDataSource
import me.zebrak.springbank.datasource.network.dto.BankList
import me.zebrak.springbank.model.Bank
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import java.io.IOException


@Repository("network")
class NetworkDataSource(@Autowired private val restTemplate: RestTemplate) : BankDataSource {
    override fun retrieveBanks(): Collection<Bank> {
        val response: ResponseEntity<BankList> =
            restTemplate.getForEntity<BankList>("http://54.219.183.128/banks", BankList::class.java)

        return response.body?.results
            ?: throw IOException("Could not fetch banks from the network")
    }

    override fun retrieveBankByAccountNumber(accountNumber: String): Bank {
        TODO("Not yet implemented")
    }

    override fun createBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun updateBank(bank: Bank): Bank {
        TODO("Not yet implemented")
    }

    override fun deleteBank(accountNumber: String) {
        TODO("Not yet implemented")
    }
}