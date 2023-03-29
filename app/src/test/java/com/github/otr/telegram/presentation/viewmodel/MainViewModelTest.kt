package com.github.otr.telegram.presentation.viewmodel

import com.github.otr.telegram.domain.entity.Chat
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

/**
 * https://developer.android.com/training/testing/local-tests#mocking-dependencies
 * https://developer.android.com/codelabs/basic-android-kotlin-compose-test-viewmodel
 */
class MainViewModelTest {
    val viewModel: MainViewModel = MainViewModel()

    @Ignore("Not implemented yet")
    @Test
    fun getChatListLiveData() {
        // GIVEN

        // WHEN
//        val chatListLiveData: LiveData<List<Chat>> = viewModel.chatListLiveData
        val firstItem: Chat = viewModel.chatListLiveData.value?.get(0)
            ?: throw AssertionError("First value of Live Data == null")
        // THEN
        val expected: Boolean = true
        val actual: Boolean = firstItem.favorite
        assertEquals(expected, actual)
    }

    @Ignore("Not implemented yet")
    @Test
    fun changeFavoriteStatus() {

    }

}
