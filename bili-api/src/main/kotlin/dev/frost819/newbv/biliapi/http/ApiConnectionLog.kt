package dev.frost819.newbv.biliapi.http

import dev.frost819.newbv.biliapi.util.BiliLogger
import okhttp3.Call
import okhttp3.EventListener
import okhttp3.Response
import java.io.IOException
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.net.Proxy

/** Logs connection phases for the main API and login hosts without request URLs or credentials. */
internal object ApiConnectionLog : EventListener() {
    override fun connectStart(call: Call, inetSocketAddress: InetSocketAddress, proxy: Proxy) {
        val host = call.request().url.host
        val family = if (inetSocketAddress.address is Inet4Address) "IPv4" else "IPv6"
        BiliLogger.info { "Network probe: $host connect $family" }
    }

    override fun secureConnectStart(call: Call) {
        BiliLogger.info { "Network probe: ${call.request().url.host} TLS start" }
    }

    override fun responseHeadersEnd(call: Call, response: Response) {
        BiliLogger.info { "Network probe: ${call.request().url.host} HTTP ${response.code}" }
    }

    override fun callFailed(call: Call, ioe: IOException) {
        BiliLogger.info { "Network probe: ${call.request().url.host} ${ioe.javaClass.simpleName}" }
    }
}
