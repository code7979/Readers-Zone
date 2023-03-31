package com.code7979.readerszone

import com.code7979.readerszone.util.toRemoteBookDetail
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun json_string_is_not_null() {
        val jsonString = """{
                                "status":"ok",
                                "id":"1503212300",
                                "title":"Invent Your Own Computer Games with Python",
                                "subtitle":"A beginner's guide to computer programming in Python",
                                "description":"Invent Your Own Computer Games with Python teaches you how to program in the Python language...",
                                "authors":"Al Sweigart",
                                "publisher":"CreateSpace",
                                "pages":"367",
                                "year":"2015",
                                "image":"https://www.dbooks.org/img/books/1503212300s.jpg",
                                "url":"https://www.dbooks.org/invent-your-own-computer-games-with-python-1503212300/",
                                "download":"https://www.dbooks.org/d/1503212300-1635507922-39943ccf97e71c6e/"
                              }"""

        val toBookDetails = jsonString.toRemoteBookDetail()
        assertNotNull(toBookDetails)
    }
}