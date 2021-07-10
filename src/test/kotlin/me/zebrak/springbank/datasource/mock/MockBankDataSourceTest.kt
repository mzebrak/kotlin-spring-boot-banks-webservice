package me.zebrak.springbank.datasource.mock

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test


internal class MockBankDataSourceTest {
    private val mockDataSource = MockBankDataSource()

    @Test
    fun `should provide a collection of banks`() {
        // when
        val banks = mockDataSource.retrieveBanks()

        // then
        assertThat(banks.count()).isGreaterThanOrEqualTo(2)
    }

    @Test
    fun `should provide some mock data`() {
        // when
        val banks = mockDataSource.retrieveBanks()

        // then
        assertThat(banks).allMatch { it.accountNumber.isNotBlank() }
        assertThat(banks).allMatch { it.trust != 0.0 }
        assertThat(banks).allMatch { it.transactionFee != 0 }
    }

    @Test
    fun `should provide unique account numbers`() {
        // when
        val banks = mockDataSource.retrieveBanks()

        // then
        assertThat(banks.count()).isEqualTo(HashSet(banks.map { it.accountNumber }).count())
    }
}