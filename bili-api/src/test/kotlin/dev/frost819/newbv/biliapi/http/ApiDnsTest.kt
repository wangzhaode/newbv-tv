package dev.frost819.newbv.biliapi.http

import okhttp3.Dns
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.UnknownHostException
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertSame

class ApiDnsTest {
    @AfterEach
    fun resetMode() {
        ApiDns.ipv4Only = false
    }

    @Test
    fun `IPv4 mode accepts IPv4 addresses and rejects an IPv6-only result`() {
        ApiDns.ipv4Only = true
        val dns = ApiDns.current()

        assertIs<Inet4Address>(dns.lookup("192.0.2.1").single())
        assertFailsWith<UnknownHostException> { dns.lookup("2001:db8::1") }
    }

    @Test
    fun `system mode restores the system resolver`() {
        ApiDns.ipv4Only = false

        assertSame(Dns.SYSTEM, ApiDns.current())
        assertIs<Inet6Address>(ApiDns.current().lookup("2001:db8::1").single())
    }
}
