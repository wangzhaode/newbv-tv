package dev.frost819.newbv.biliapi.http

import okhttp3.Dns
import java.net.Inet4Address
import java.net.InetAddress
import java.net.UnknownHostException

/** DNS policy used by the application's OkHttp clients. Set the mode before starting requests. */
object ApiDns {
    /** Selects IPv4 for new clients when enabled. */
    @Volatile
    var ipv4Only: Boolean = false

    private val ipv4 =
        object : Dns {
            override fun lookup(hostname: String): List<InetAddress> =
                Dns.SYSTEM.lookup(hostname).filterIsInstance<Inet4Address>().ifEmpty {
                    throw UnknownHostException("No IPv4 address for $hostname")
                }
        }

    /** Returns the DNS implementation for the selected mode. */
    fun current(): Dns = if (ipv4Only) ipv4 else Dns.SYSTEM
}
