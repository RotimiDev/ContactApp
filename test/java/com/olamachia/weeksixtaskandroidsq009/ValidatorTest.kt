package com.olamachia.weeksixtaskandroidsq009

import com.google.common.truth.Truth
import org.junit.Test

class ValidatorTest {
    @Test
    fun whenPhoneNumberIsValid() {
        val validNumber = "08069271995"
        val validate = Validator.mobileValidate(validNumber)

        Truth.assertThat(validate).isEqualTo(true)
    }
}
