package com.zukron.note

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.zukron.note.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Project name is Note
 * Created by Zukron Alviandy R on 9/5/2020
 * Contact me if any issues on zukronalviandy@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class TestingDatabase {
    @Test
    fun testRepo() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = NoteRepository.getInstance(appContext)
        CoroutineScope(IO).launch {
            println(db?.noteDao()!!.getAll())
        }
    }
}