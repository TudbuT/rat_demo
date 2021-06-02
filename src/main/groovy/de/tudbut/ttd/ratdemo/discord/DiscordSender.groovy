package de.tudbut.ttd.ratdemo.discord

import de.tudbut.ttd.ratdemo.Main
import tudbut.net.http.HTTPRequest
import tudbut.net.http.HTTPResponse
import tudbut.net.http.ParsedHTTPValue

class DiscordSender {

    String webhook

    DiscordSender(String webhook) {
        setWebhook(webhook)
    }

    void send(DiscordPayload payload) {
        if(Main.debug)
            println "Sending $payload.class.name..."
        HTTPRequest request = payload.createRequest(new URL(webhook).getPath())
        HTTPResponse response = request.send()
        ParsedHTTPValue res = response.parse()
        if (res.statusCode < 200 || res.statusCode >= 300) {
            println 'Error: ' + String.valueOf(res.statusCodeAsEnum) + ' ' + res.bodyRaw
        }
        if(Main.debug)
            println "Sent $payload.class.name!"
    }
}
