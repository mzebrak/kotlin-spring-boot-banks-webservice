package me.zebrak.springbank.controller

import com.fasterxml.jackson.databind.ObjectMapper
import me.zebrak.springbank.model.Bank
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.*

@SpringBootTest
@AutoConfigureMockMvc
internal class BankControllerTest @Autowired constructor(val mockMvc: MockMvc,
                                                         val objectMapper: ObjectMapper) {

    val baseUrl = "/api/banks"
    val invalidAccountNumber = "does_not_exist"

    @Nested
    @DisplayName("GET /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBanks {
        @Test
        fun `should return all banks`() {
            // when / then
            mockMvc.get(baseUrl)
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    jsonPath("$[0].account_number") { value("1234") }
                    jsonPath("$[1].account_number") { value("1010") }
                }
        }
    }

    @Nested
    @DisplayName("GET /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class GetBank {
        @Test
        fun `should return the bank with the given account number`() {
            // given
            val accountNumber = "1234"

            // when
            mockMvc.get("$baseUrl/$accountNumber")

                // then
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        jsonPath("$.account_number") { value("1234") }
                    }
                }
        }

        @Test
        fun `should return NOT FOUND if the account number does not exist`() {
            // when
            mockMvc.get("$baseUrl/$invalidAccountNumber")
                .andDo { print() }
                // then
                .andExpect { status { isNotFound() } }
        }
    }

    @Nested
    @DisplayName("POST /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PostNewBank {
        @Test
        fun `should add the new bank`() {
            // given
            val newBank = Bank("123456", 32.0, 3)

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(newBank)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect {
                    status { isCreated() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        //json(objectMapper.writeValueAsString(newBank))
                    }
                    jsonPath("$.account_number") { value("123456") }
                    jsonPath("$.trust") { value("32.0") }
                    jsonPath("$.default_transaction_fee") { value("3") }
                }

            mockMvc.get("$baseUrl/${newBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(newBank)) } }
        }

        @Test
        fun `should return BAD REQUEST if bank with given account number already exists`() {
            // given
            val invalidBank = Bank("1010", 10.0, 1)

            // when
            val performPost = mockMvc.post(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // then
            performPost
                .andDo { print() }
                .andExpect { status { isBadRequest() } }

        }
    }

    @Nested
    @DisplayName("PATCH /api/banks")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class PatchExistingBank {

        @Test
        fun `should UPDATE an existing bank`() {
            // given
            val updatedBank = Bank("1010", 20.0, 10)

            // when
            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(updatedBank)
            }

            // then
            performPatch
                .andDo { print() }
                .andExpect {
                    status { isOk() }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        json(objectMapper.writeValueAsString(updatedBank))
                    }
                }

            mockMvc.get("$baseUrl/${updatedBank.accountNumber}")
                .andExpect { content { json(objectMapper.writeValueAsString(updatedBank)) } }
        }

        @Test
        fun `should return BAD REQUEST if no bank with given account number exists`() {
            // given
            val invalidBank = Bank(invalidAccountNumber, 20.0, 10)

            // when
            val performPatch = mockMvc.patch(baseUrl) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(invalidBank)
            }

            // then
            performPatch
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }
    }

    @Nested
    @DisplayName("DELETE /api/banks/{accountNumber}")
    @TestInstance(Lifecycle.PER_CLASS)
    inner class DeleteExistingBank {
        @Test
        @DirtiesContext
        fun `should DELETE the bank with the given account number`() {
            // given
            val accountNumber = "1234"

            // when / then
            mockMvc.delete("$baseUrl/$accountNumber")
                .andDo { print() }
                .andExpect { status { isNoContent() } }

            mockMvc.get("$baseUrl/$accountNumber")
                .andExpect { status { isNotFound() } }
        }

        @Test
        fun `should return NOT FOUND if no bank with given account number exists`() {
            // when
            mockMvc.delete("$baseUrl/$invalidAccountNumber")

                //then
                .andDo { print() }
                .andExpect { status { isNotFound() } }

        }
    }
}